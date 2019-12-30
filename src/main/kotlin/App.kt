import com.andrefmrocha.generation.JsonGenerator
import java.io.File

fun main() {
    JsonGenerator(File("file.json")).build("Test", "com.andrefmrocha.meias", "src/main/kotlin")
}
