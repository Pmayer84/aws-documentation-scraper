file:///C:/Users/petermayer/OneDrive%20-%20Microsoft/Documents/Desktop/scraping_projects/aws_scraping/aws_scraper_github/aws-documentation-scraper/src/main/scala/io/scalastic/aws/Model.scala
### java.lang.ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.13.6
Classpath:
<WORKSPACE>\.bloop\root\bloop-bsp-clients-classes\classes-Metals-7Gd8IOvcQtWSuaM_8OVrTw== [exists ], <HOME>\AppData\Local\bloop\cache\semanticdb\com.sourcegraph.semanticdb-javac.0.10.3\semanticdb-javac-0.10.3.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.6\scala-library-2.13.6.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-reflect\2.13.6\scala-reflect-2.13.6.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\softwaremill\sttp\client3\core_2.13\3.3.5\core_2.13-3.3.5.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\lihaoyi\upickle_2.13\1.3.15\upickle_2.13-1.3.15.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\json\json\20210307\json-20210307.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\jsoup\jsoup\1.14.3\jsoup-1.14.3.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\softwaremill\sttp\model\core_2.13\1.4.7\core_2.13-1.4.7.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\softwaremill\sttp\shared\core_2.13\1.2.5\core_2.13-1.2.5.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\softwaremill\sttp\shared\ws_2.13\1.2.5\ws_2.13-1.2.5.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\lihaoyi\ujson_2.13\1.3.15\ujson_2.13-1.3.15.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\lihaoyi\upack_2.13\1.3.15\upack_2.13-1.3.15.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\lihaoyi\upickle-implicits_2.13\1.3.15\upickle-implicits_2.13-1.3.15.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\lihaoyi\upickle-core_2.13\1.3.15\upickle-core_2.13-1.3.15.jar [exists ], <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\com\lihaoyi\geny_2.13\0.6.10\geny_2.13-0.6.10.jar [exists ]
Options:
-Yrangepos -Xplugin-require:semanticdb


action parameters:
offset: 0
uri: file:///C:/Users/petermayer/OneDrive%20-%20Microsoft/Documents/Desktop/scraping_projects/aws_scraping/aws_scraper_github/aws-documentation-scraper/src/main/scala/io/scalastic/aws/Model.scala
text:
```scala
@@v
```



#### Error stacktrace:

```
scala.reflect.internal.util.BatchSourceFile.findLine$1(SourceFile.scala:218)
	scala.reflect.internal.util.BatchSourceFile.offsetToLine(SourceFile.scala:221)
	scala.meta.internal.pc.MetalsGlobal$XtensionPositionMetals.toPos(MetalsGlobal.scala:684)
	scala.meta.internal.pc.MetalsGlobal$XtensionPositionMetals.toLsp(MetalsGlobal.scala:697)
	scala.meta.internal.pc.AutoImportsProvider.scala$meta$internal$pc$AutoImportsProvider$$namePos$1(AutoImportsProvider.scala:53)
	scala.meta.internal.pc.AutoImportsProvider$$anonfun$autoImports$2.applyOrElse(AutoImportsProvider.scala:77)
	scala.meta.internal.pc.AutoImportsProvider$$anonfun$autoImports$2.applyOrElse(AutoImportsProvider.scala:58)
	scala.collection.immutable.List.collect(List.scala:267)
	scala.meta.internal.pc.AutoImportsProvider.autoImports(AutoImportsProvider.scala:58)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$autoImports$1(ScalaPresentationCompiler.scala:282)
```
#### Short summary: 

java.lang.ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3