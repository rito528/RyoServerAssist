# RyoServerAssist

### 開発環境

- [Intellij IDEA](https://www.jetbrains.com/ja-jp/idea/)
- [Scala 2.13.6](https://www.scala-lang.org/download/2.13.6.html)
- [sbt 1.5.5](https://www.scala-sbt.org/1.x/docs/Setup.html)
- [Paper 1.17.1](https://papermc.io/downloads)

### このプラグインを利用する上での注意

- 基本的に更新を行った際は再起動を行うようにお願いします。

### 前提プラグイン

- WorldGuard
- WorldEdit
- Multiverse-Core
- Multiverse-Portals
- dynmap
- Votifier

### このプラグインを新Minecraftバージョンに合わせるときに必ず更新するもの

- plugin.ymlのapiVersionを利用バージョンに変更
- resourcesディレクトリの中にあるja_jp.jsonを利用バージョンに変更する

### 必要な設定

- MySQLでset global wait_timeout=10;を実行してタイムアウト時間を変更(短く)する
- MySQLでset global max_connections = 1000;を実行してコネクション数を変更する

