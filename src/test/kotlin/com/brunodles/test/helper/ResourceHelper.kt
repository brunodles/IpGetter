package com.brunodles.test.helper

import java.io.File
import java.io.InputStream
import java.util.*

fun Properties.loadRes(path: String): Properties {
    this.load(resAsStream(path))
    return this
}

fun resAsStream(path: String): InputStream? =
        ClassLoader.getSystemClassLoader().getResourceAsStream(path)

fun resAsString(path: String): String =
        File(ClassLoader.getSystemClassLoader().getResource(path).file).readText()