name := "jupyterlab-xkcd-scalajs"

enablePlugins(ScalaJSPlugin)  

resolvers += Resolver.bintrayRepo("oyvindberg", "ScalablyTyped")
libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.7",
  "com.lihaoyi" %%% "ujson" % "0.7.1",
  ScalablyTyped.J.jupyterlab__application,
  ScalablyTyped.J.jupyterlab__apputils
)

scalaVersion := "2.12.8"

scalaJSLinkerConfig ~= { _.withESFeatures(_.withUseECMAScript2015(true)) }
scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) }
scalacOptions += "-P:scalajs:sjsDefinedByDefault"