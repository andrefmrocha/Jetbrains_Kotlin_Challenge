package com.andrefmrocha.generation

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File

abstract class Generator {
    private fun getNestedClassName(name: String): String = name.capitalize()

    private fun getStatement(map: Map<String, Any>, name: String): String {
        val stmtBuilder = StringBuilder()
        stmtBuilder.append("$name(")
        map.entries.forEachIndexed { index, entry ->
            val value = entry.value
            when (value) {
                is String -> {
                    stmtBuilder.append(""""$value"""")
                    if (index < map.values.size - 1)
                        stmtBuilder.append(",")
                }
                is Number -> {
                    stmtBuilder.append(value)
                    if (index < map.values.size - 1)
                        stmtBuilder.append(",")
                }
                is Map<*, *> -> {
                    stmtBuilder.append(this.getStatement(value as Map<String, Any>, getNestedClassName(entry.key)))
                }
            }
        }
        stmtBuilder.append(')')
        return stmtBuilder.toString()
    }

    private fun buildDataClass(
        className: String,
        packageName: String,
        targetPath: File,
        map: Map<String, Any>
    ) {
        val constructorBuilder = FunSpec.constructorBuilder()
        val classBuilder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
        map.forEach { (key, value) ->
            when (value) {
                is String -> {
                    constructorBuilder.addParameter(key, String::class)
                    classBuilder.addProperty(PropertySpec.builder(key, String::class).initializer(key).build())
                }
                is Number -> {
                    constructorBuilder.addParameter(key, Number::class)
                    classBuilder.addProperty(PropertySpec.builder(key, Number::class).initializer(key).build())
                }
                is Map<*, *> -> {
                    val nestedClassName = getNestedClassName(key)
                    buildDataClass(
                        nestedClassName,
                        packageName,
                        targetPath,
                        value as Map<String, Any>
                    )
                    val classInitializer = ClassName(packageName, nestedClassName)

                    constructorBuilder.addParameter(key, classInitializer)
                    classBuilder.addProperty(
                        PropertySpec.builder(key, classInitializer).initializer(key).build()
                    )
                }
            }
        }


        val builtClass = constructorBuilder.build()

        FileSpec.builder(packageName, className)
            .addType(
                classBuilder.primaryConstructor(builtClass)
                    .build()
            )
            .build()
            .writeTo(targetPath)
    }

    protected fun build(className: String, packageName: String, targetPath: File, map: Map<String, Any>) {
        this.buildDataClass(className, packageName, targetPath, map)
        FileSpec.builder(packageName, "${className}Instantiation")
            .addFunction(
                FunSpec.builder("${className}Info")
                    .addStatement("return ${getStatement(map, className)}")
                    .build()
            ).build()
            .writeTo(targetPath)
    }
}