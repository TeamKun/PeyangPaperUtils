# PeyangPaperUtils

ぺやんぐがLabで開発してるときに作ったユーティリティとかをまとめたやつ。

## [JavaDoc](https://teamkun.github.io/PeyangPaperUtils/)

---

## 注意

+ このライブラリを同梱するときは、**必ずパッケージのリロケート**をしてください。

## 使い方

1. あなたのプラグインのonEnable()に、以下を追加します：

  ```java
    PeyangPaperUtils.init(this);

  ```

2. あなたのプラグインのonDisable()に、以下を追加します：

  ```java
    PeyangPaperUtils.dispose();

  ```

---

## いまあるもの

---

### Runner

BukkitRunnableの匿名クラスでタスク作るのが面倒くさい人向けのユーティリティ。  
まあつまり, BukkitRunnableをプラグイン指定無しで使えるってやつ。  
あ, あと, Runner.run系のメソッドのラムダは例外を許容する。例外をキャッチするには, (大体) 第ニ引数に例外をキャッチするラムダをつっこむ。

```java
  Runner.run(() => {
      Terminal.ofConsole().info("Hello, world!");
  });
  
  // 1秒毎(20チック)に実行
  Runner.runTimer(() => {
      Terminal.ofConsole().info("Hello, world!");
  }, 20L);
  
  // 1秒毎(20チック)に実行 + カウント
  Runner.runTimer((long count) => {
      Terminal.ofConsole().info(count + ": Hello, world!");
  }, 20L);
  
  Runner.runTimer(() => {
      throw new IOException("Hello, exception!");
  }, (Exception exception, BukkitTask task) => {
      Terminal.ofConsole().error(exception.getMessage());
      task.cancel();
  }, 20L);
```

### ターミナルシステム

プレイヤとコンソールのI/Oを統一するインタフェースとユーティリティをまとめたやつ。   
↓↓↓ターミナルを初期化↓↓↓

```java
  Player examplePlayer;
  Audience exampleAudience;  // net.kyori.adventure.audience.Audience
  
  Terminal terminal = Terminals.of(examplePlayer);
  // or
  Terminal terminal = Terminals.of(exampleAudience);
  // or
  Terminal terminal = Terminals.of(examplePlayer, exampleAudience);
  // or
  Terminal terminal = Terminals.ofConsole(); 
 ```

#### ログ出力

ログをめちゃくちゃ簡単に吐けるやつ..!!!

```java
  terminal.info("Hello World!");  // 青色で I: Hello World! と出力
  terminal.warn("Hello World!");  // 黄色で W: Hello World! と出力
  terminal.error("Hello World!"); // 赤色で E: Hello World! と出力
  terminal.success("Hello World!"); // 緑色で S: Hello World! と出力
  
  terminal.writeLine("Hello World!"); // Hello World! と出力
  terminal.write(TextComponent.of("Hello World!")); // Hello World! と出力 (Adventure API)
```

#### プログレスバー(いまはプレイヤのみ)

プログレスバーを表示するやつ。文字通り。

```java
  ProgressBar progress = terminal.createProgressbar("example_progressbar");
  progress.setProgressMax(100);
  progress.setPrefix("Progress: ");
  progress.setSuffix(" %");
  progress.show();
  // 別スレッドで実行
  for (int i = 0; i < 100; i++) {
      progress.setProgress(i);
      Thread.sleep(100);
  }
  
  // バリアで待機てきな? ことを? しとく??
  
  progress.hide();
```

### 入力システム

プレイヤ/コンソールに質問を行い, 入力を受け付ける機能。  
質問の返答は

+ プレイヤ: チャット (prefix/suffixなし)
+ コンソール: コンソール(コマンド実行する感覚)
  で行できる。

```java
  Input input = terminal.getInput();
  
  QuestionResult result = input.showYNQuestion("Are you sure?").waitAndGetResult();
  if (result.test(QuestionAttribute.YES))
      terminal.info("Yes, it is.");
  else
      terminal.info("No, it isn't.");
  
  result = input.showYNQuestionCancellable("Are you sure?", QuestionAttribute.APPLY_FOR_ALL).waitAndGetResult();
  if (result.test(QuestionAttribute.YES))
      terminal.info("Yes, it is.");
  else if (result.test(QuestionAttribute.CANCEL))
      terminal.info("Cancelled.");
  else
      terminal.info("No, it isn't.");
  
  if (result.test(QuestionAttribute.APPLY_FOR_ALL))
      terminal.info("Applied for all.");
```

#### 警告

+ waitうんたら系は, 非同期で使ってください。下手したらメインスレッド止めてサーバ死にます。いや。マジで。

### コマンドシステム

コマンドとか簡単に作ったり, 引数のバリデーションを簡単にできるユーティリティ郡。  
コマンドのヘルプを自動で構築する。(`/<コマンド名> help [ページ番号]` で使用)
サブコマンドを使用する場合 `SubCommandable` を継承する。
<details>
<summary>例</summary>

  <details>
    <summary>plugin.yml</summary>

    ```yml
      name: ExamplePlugin
      
      commands:
        examplecommand:
          aliases:
            - ex
      permission:
        examplepermission:
          default: op
    ```

  </details>

  <details>
    <summary>CommandManagerを管理するクラス(onEnableがあるとこ推奨)</summary>

    ```java 
      CommandManager manager;
  
      // onEnable 内
  
      this.manager = new CommandManager(this, "examplecommand", "ExamplePlugin", "examplepermission");
      
      this.manager.registerCommand("dostuff", new CommandDoStuff(), "ds", "do_stuff", "stuff");

    ```

  </details>

  <details>
    <summary>コマンドクラス</summary>

    ```java
      public class CommandDoStuff extends CommandBase {
  
        @Override
        public void onCommand(@NotNull CommandSender sender, @NotNull Terminal terminal, String[] args) {
  
          if (this.indicateArgsLengthInvalid(terminal, args, 1, 2)  // 引数の長さが1~2でない場合はエラーを表示して終了
              || this.indicatePlayer(terminal)  // コンソールから実行された場合はエラーを表示して終了
            return;
  
         String stuffName = args[0];
         Integer repeatCount;
         if (args.length >= 2 && this.parseInteger(terminal, args[1], 1, 100) != null)  
             // 引数が2つ以上で, 2番目の引数が1~100の整数でない場合はエラーを表示して終了
           return;
         else
           repeatCount = 1;
  
        for (int i = 0; i < repeatCount; i++)
          terminal.info("Do stuff: " + stuffName);
  
        }
    
        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Terminal terminal, String[] args) {
            return List.of("stuff1", "stuff2");
        }
    
        @Override
        public @Nullable String getPermission() {
            return "exampleplugin.dostuff";
        }
    
        @Override
        public TextComponent getHelpOneLine() {
            return of("Do stuff!");
        }
    
        @Override
        public String[] getArguments() {
            return new String[] {
              this.required("stuff"),  // 必須引数
              this.optional("repeat")  // 任意引数
            };
        }
      }
    ```

  </details>

</details>

+ DBのトランザクションシステム

```java
  Connection connection;
  class Employee {
      int id;
      String name;
      int age;
      String type;
  }
  
  QueryResult<Employee> result = Transaction.create(connection, "SELECT * FROM example_table WHERE id = ? AND type = ?")
      .set(1, 123456)
      .set(2, "EMPLOYEE")
      .executeQuery();
```

+ DB操作時の自動コミット(すべての実行の最後にコミット)
  ```java
    Transaction.create(connection)
        .doTransaction((Transaction) -> {
            Connection connection = transaction.getConnection();
            // ここでDB操作
        });
  ```
+ DB操作時のtry-catchを省略できるように！(非チェック例外にします)
  => SQLExceptionが発生した場合は自動的にロールバック！！
+ close忘れがなくなる
+ 結果をStream処理できる
  ```java
    result.stream().map((ResultRow row) -> {
        Employee employee = new Employee();
        employee.id = row.getInt("id");
        employee.name = row.getString("name");
        employee.age = row.getInt("age");
        employee.type = row.getString("type");
        return employee;
    }).forEach((Employee employee) -> {
        System.out.println(employee.name);
    });
  ```
+ 結果をオブジェクトに変換して取得(マッパーを予め定義)
  ```java
    result.mapper((ResultRow row) -> {
        Employee employee = new Employee();
        employee.id = row.getInt("id");
        employee.name = row.getString("name");
        employee.age = row.getInt("age");
        employee.type = row.getString("type");
        return employee;
    });
  
    while (result.next()) {
        Employee employee = result.get();
        System.out.println(employee.name);
    }
  ```
+ 結果をListで取得(マッパーを引数に突っ込む)
  ```java
    ArrayList<Employee> employees = result.mapToList((ResultSet resultSet) -> {
        Employee employee = new Employee();
        employee.id = resultSet.getInt("id");
        employee.name = resultSet.getString("name");
        employee.age = resultSet.getInt("age");
        employee.type = resultSet.getString("type");
        return employee;
    });
  ```
