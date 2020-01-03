@file:JvmName("App")

package com.andrefmrocha.jetbrains

import com.andrefmrocha.jetbrains.generation.JsonGenerator
import java.io.File

fun main() {
    JsonGenerator(File("src/main/resources/file.json")).build(
        "Test",
        "com.andrefmrocha.test",
        "src/main/kotlin"
    )
}
