import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.kotlinpoet.*
import java.io.File
import java.io.FileReader

fun main() {
    val map: Map<String, Any> = Gson().fromJson(FileReader("file.json"), object : TypeToken<Map<String, Any>>() {}.type)
    val constructorBuilder = FunSpec.constructorBuilder()
    val classBuilder = TypeSpec.classBuilder("Test")
        .addModifiers(KModifier.DATA)
    map.forEach { (key, value) ->
        when (value) {
            is String ->  {
                constructorBuilder.addParameter(key, String::class)
                classBuilder.addProperty(PropertySpec.builder(key, String::class).initializer(key).build())
            }
            is Number ->  {
                constructorBuilder.addParameter(key, Number::class)
                classBuilder.addProperty(PropertySpec.builder(key, Number::class).initializer(key).build())
            }
        }
    }

    FileSpec.builder("com.andrefmrocha.generated", "Test")
        .addType(
            classBuilder.primaryConstructor(constructorBuilder.build()).build()
        )
        .build()
        .writeTo(File("src/main/kotlin"))
}