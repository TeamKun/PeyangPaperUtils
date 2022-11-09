# PeyangPaperUtils

ぺやんぐがLabで開発してるときに作ったユーティリティとかをまとめたやつ。

## [JavaDoc](https://teamkun.github.io/PeyangPaperUtils/)

## 注意

+ このライブラリを同梱するときは、**必ずパッケージのリロケート**をしてください。

### 使い方

1. あなたのプラグインのonEnable()に、以下を追加します：

  ```java

    PeyangPaperUtils.init(this);

  ```

2. あなたのプラグインのonDisable()に、以下を追加します：

  ```java

    PeyangPaperUtils.dispose();

  ```

## いまあるもの

+ ターミナル
  + プレイヤとコンソールの入出力を統一
  + 標準出力/プログレスバー/通知
  + `Terminal#success`, `Terminal#error`, `Terminal#info`, `Terminal#warn` などつよつよなメソッドいっぱい。
  + ターミナルは `Terminals.of(CommandSender|Audience|Player)` または `Terminals.ofConsole()` で取得できる。
+ 質問システム
  + プレイヤ/コンソールに質問できる
    + Yes/No, Cancel, Apply for ALL とかがある。
+ コマンドシステム
    + つよつよなタブ補完/コマンドハンドラを提供
    + CommandBaseを継承してつかう。
    + `CommandBase#indicateInvalidArgs` メソッドとかで引数がよくない場合に指示とかする。
    + サブコマンドもつくれる。
+ コマンドヘルプシステム
    + コマンドの引数を含んだ、ページ化されたヘルプを自動で構築する。
    + `/<ベースコマンド名> help [サブコマンド名|ページ番号] [ページ番号]` でつかえる。
+ Runner
    + `BukkitRunnable` の引数に `Plugin` を指定しなくていいように。
    + つまり実行したプラグインは自動で推測される！
    + Example
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
