package com.andrefmrocha.generation

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader

class JsonGenerator(private val fileName: File) : Generator() {
    fun build(className: String, packageName: String, targetPath: String) =
        this.build(className, packageName, File(targetPath))

    fun build(className: String, packageName: String, targetPath: File) {
        val map: Map<String, Any> =
            Gson().fromJson(FileReader(fileName), object : TypeToken<Map<String, Any>>() {}.type)
        this.build(className, packageName, targetPath, map)
    }
}