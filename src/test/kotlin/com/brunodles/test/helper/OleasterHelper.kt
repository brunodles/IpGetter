package com.brunodles.test.helper

import com.mscharhag.oleaster.runner.StaticRunnerSupport
import kotlin.reflect.KClass

fun <T : Any> given(aClass: KClass<T>, block: () -> Unit) {
    given(aClass.java, block)
}

fun <T> given(aClass: Class<T>, block: () -> Unit) {
    given(aClass.simpleName, block)
}

fun given(text: String, block: () -> Unit) {
    StaticRunnerSupport.describe("Given $text", block)
}

fun context(text: String, block: () -> Unit) {
    StaticRunnerSupport.describe("Context $text", block)
}

fun on(text: String, block: () -> Unit) {
    StaticRunnerSupport.describe("On $text", block)
}

fun with(text: String, block: () -> Unit) {
    StaticRunnerSupport.describe("With $text", block)
}

fun xwith(text: String, block: () -> Unit) {}

fun xit(text: String, block: () -> Unit) {}

fun xon(text: String, block: () -> Unit) {}