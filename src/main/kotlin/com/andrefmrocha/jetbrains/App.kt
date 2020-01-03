@file:JvmName("App")
package com.andrefmrocha.jetbrains
import com.andrefmrocha.jetbrains.generation.JsonGenerator
import java.io.File

fun main() {
    JsonGenerator(File("file.json")).build("Test", "com.andrefmrocha.meias", "src/main/kotlin")
}
