import com.andrefmrocha.generation.JsonGenerator
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.security.MessageDigest

class JsonGeneratorTest {

    private val sourcePath = "source-code"
    private fun String.asResource(): String =
        object {}.javaClass.getResource(this).path
    private fun String.toMD5() : String {
        val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
        return bytes.toHex()
    }

    private fun ByteArray.toHex(): String {
        return this.joinToString("") { "%02x".format(it) }
    }

    private fun contentEquals(original: String, generated: String) {
        val file1 = File(original.asResource()).readText()
        val file2 = File(generated.asResource()).readText()
        Assert.assertTrue(file1.toMD5() == file2.toMD5())
    }

    @Test
    fun basicType() {
        val basicFolder = "$sourcePath/basic"
        val basicTestFolder = "$sourcePath/basic-test"
        val file = File("basic.json".asResource())
        JsonGenerator(file).build("Basic", "",
            basicTestFolder.asResource())
        val files = arrayOf(
            Pair("$basicFolder/Basic.kt",
                "$basicTestFolder/Basic.kt"),
            Pair("$basicFolder/BasicInstantiation.kt",
                "$basicTestFolder/BasicInstantiation.kt")
        )
        files.forEach { (first, second) -> contentEquals(first, second) }
    }

    @Test
    fun nestedType() {
        val basicFolder = "$sourcePath/nested"
        val basicTestFolder = "$sourcePath/nested-test"
        val file = File("nested.json".asResource())
        JsonGenerator(file).build("Nested", "",
            basicTestFolder.asResource())
        val files = arrayOf(
            Pair("$basicFolder/Nested.kt",
                "$basicTestFolder/Nested.kt"),
            Pair("$basicFolder/NestedClass.kt",
                "$basicTestFolder/NestedClass.kt"),
            Pair("$basicFolder/NestedInstantiation.kt",
                "$basicTestFolder/NestedInstantiation.kt")
        )
        files.forEach { (first, second) -> contentEquals(first, second) }
    }


}