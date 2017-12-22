# Lice

CI|status
:---|:---:
Travis CI|[![Build Status](https://travis-ci.org/lice-lang/lice-tiny.svg?branch=master)](https://travis-ci.org/lice-lang/lice-tiny)
AppVeyor|[![Build status](https://ci.appveyor.com/api/projects/status/57kniub26a4m150q?svg=true)](https://ci.appveyor.com/project/ice1000/lice-tiny)
CircleCI|[![CircleCI](https://circleci.com/gh/lice-lang/lice-tiny.svg?style=svg)](https://circleci.com/gh/lice-lang/lice-tiny)
CodeShip|[ ![Codeship Status for lice-lang/lice-tiny](https://app.codeship.com/projects/7bbd4360-c775-0135-a9be-1ef45f12ce0c/status?branch=master)](https://app.codeship.com/projects/261396)

[![JitPack](https://jitpack.io/v/lice-lang/lice-tiny.svg)](https://jitpack.io/#lice-lang/lice-tiny)<br/>
[![license](https://img.shields.io/github/license/lice-lang/lice-tiny.svg)](https://github.com/lice-lang/lice-tiny)

This is the tiny version of the Lice programming language.  
Lice is very easy to learn -- especially this tiny version.

Still, Lice provides full support for call-by-name, call-by-need, and call-by-value.
In this version, you can only find the basic language constructs (the `def` family, `lambda` family, control flows, boolean and integer operations), list operations and File/GUI/Netword APIs are removed.

I tried my best to make the compiled jar small.

+ [code example](./example.lice)

## Lice performance

Code to run:

```lisp
; loops
(def loop count block (|>
    (-> i 0)
    (while (< i count) (|> (block i)
    (-> i (+ i 1))))))

; invoking the function
(loop 200000 (lambda i (|>
    (defexpr let x y block (|>
        (-> x y) ; this is actually an issue of lice.
        (block)
        (undef x)))
    (let reimu 100 (lambda (|> x))))))

(print "loop count: " i)
```

Condition|Time
:---:|:---:
Lice call Java using `extern`|350ms
Lice call Java using Lice API|295ms
Pure Java|13ms
Pure Lice|897ms
Java call Lice using Lice API|629ms

## Lice invoking Java

Lice has handy APIs for interacting with Java.

```lisp
; declare an extern function
; must be a static Java function
(extern "java.util.Objects" "equals")

; calling the extern function
(equals 1 1)
```

## Java invoking Lice

This project provides handy APIs for running Lice codes from Java.

```java
System.out.println(Lice.run("(+ 1 1)")); // prints 2

System.out.println(Lice.run(new File("example.lice"))); // run codes in a file
```
