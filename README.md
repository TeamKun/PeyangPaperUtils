# PeyangPaperUtils

ぺやんぐがLabで開発してるときに作ったユーティリティとかをまとめたやつ。

## [JavaDoc](https://teamkun.github.io/PeyangPaperUtils/)

## いまあるもの

+ ターミナル
    + プレイヤとコンソールの入出力を統一
    + 標準出力/プログレスバー/通知
    + `Terminal#success`, `Terminal#error`, `Terminal#info`, `Terminal#warn` などつよつよなメソッドいっぱい。
    + ターミナルは `Terminals.of(CommandSender|Audience|Player)` または `Terminals.ofConsole()` で取得できる。
+ 質問システム
    + プレイヤ/コンソールに質問できる
    + Yes/No, Cancel, Apply for ALL とかがある。
    + 質問にはキューがあり、一つ一つ、回答されたら次の質問にうつる。
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
          if (new Random().nextInt(10) == 0) {
              this.cancel();
          }
      }, 20L);

      // 1秒毎(20チック)に実行 + カウント
      Runner.runTimer((long count) => {
          Terminal.ofConsole().info(count + ": Hello, world!");
          if (count == 10) {
              this.cancel();
          }
      }, 20L);
      
      Runner.run(() => {
          throw new IOException("Hello, exception!");
      }, (Exception exception) => {
          Terminal.ofConsole().error(exception.getMessage());
      });
      ```
