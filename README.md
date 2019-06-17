# JupyterLab xkcd-extension ported to Scala.js

Show a random xkcd.com comic in a JupyterLab panel

A port of the [JupyterLab example xkcd-extension](https://github.com/jupyterlab/jupyterlab_xkcd) that is used in
[the extension tutorial](https://jupyterlab.readthedocs.io/en/stable/developer/xkcd_extension_tutorial.html)
to Scala.js.

Uses JupyterLab typings from [ScalablyTyped](https://github.com/oyvindberg/ScalablyTyped).

## Prerequisites

* JupyterLab

## Installation

```bash
jupyter labextension install @sbrunk/jupyterlab-xkcd-scalajs
```

## Development

For a development install (requires npm version 4 or later), do the following in the repository directory:

```bash
npm install
jupyter labextension link .
```

To rebuild the JupyterLab app:

```bash
jupyter lab build
```

For a fast dev cycle run sbt in watch mode in one terminal:
```
sbt ~fastOptJS
```

And jupyter lab also in watch mode in another terminal
```
jupyter lab --watch
```

That should trigger jupyter lab to pick up any changed JS output and use it after the next browser refresh.