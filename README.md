# Scala.js gamedev with Canvas 2D

## Requirements

 - JDK 8 (or later; openjdk 8 or 11 recommended)
 - [Mill](http://www.lihaoyi.com/mill/) (0.7.4 or later) for building

## Quickstart

1. Run `mill server.run` for a dev HTTP server and open `http://127.0.0.1:8080`

2. Run `mill` in background:

```
mill -j 4 -w all _.compile
```

## Quick overview

 - `Loader` pre-loads resources using a Canvas 2D based progress bar and
   creates instances of a `CanvasRenderer`, a `Controller` and `Audio`. On
   success, it instantiates a `GameLoop` implementation.
 - `CanvasRenderer` sets up the canvas HTML element and exposes the 2D context
   providing scaling without smoothing (for pixel art). It will draw any
   `Drawable`.
 - `Controller` is a game controller abstraction. There's a keyboard
   implementation.
 - `Audio` creates audio instances and plays them.
 - `GameLoop` provides a game loop implementation with fixed update (at 80 FPS)
   and draw at 60 FPS (using request animation frame).

## Further info

 - https://www.scala-js.org/doc/
 - http://www.lihaoyi.com/mill/
 - https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D


