import com.andrefmrocha.generation.JsonGenerator
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.security.MessageDigest

class JsonGeneratorTest {

    private val sourcePath = "source-code"
    private fun String.asResource(): String =
        object {}.javaClass.getResource(this).path

    private fun String.toMD5(): String {
        val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
        return bytes.toHex()
    }

    private fun ByteArray.toHex(): String {
        return this.joinToString("") { "%02x".format(it) }
    }

    private fun contentEquals(original: String, generated: String) {
        val file1 = File(original.asResource()).readText()
        val file2 = File(generated).readText()
        Assert.assertTrue(file1.toMD5() == file2.toMD5())
    }

    @Test
    fun basicType() {
        val basicFolder = "$sourcePath/basic"
        val file = File("basic.json".asResource())
        val tmp =
            createTempDir("$sourcePath/basic-test")
        JsonGenerator(file).build(
            "Basic", "",
            tmp
        )
        val files = arrayOf(
            Pair(
                "$basicFolder/Basic.kt",
                "${tmp}/Basic.kt"
            ),
            Pair(
                "$basicFolder/BasicInstantiation.kt",
                "${tmp}/BasicInstantiation.kt"
            )
        )
        files.forEach { (first, second) -> contentEquals(first, second) }
    }

    @Test
    fun nestedType() {
        val basicFolder = "$sourcePath/nested"
        val file = File("nested.json".asResource())
        val temp = createTempDir("$sourcePath/nested-test")
        JsonGenerator(file).build(
            "Nested", "",
            temp
        )

        val files = arrayOf(
            Pair(
                "$basicFolder/Nested.kt",
                "${temp.absolutePath}/Nested.kt"
            ),
            Pair(
                "$basicFolder/NestedClass.kt",
                "${temp.absolutePath}/NestedClass.kt"
            ),
            Pair(
                "$basicFolder/NestedInstantiation.kt",
                "${temp.absolutePath}/NestedInstantiation.kt"
            )
        )
        files.forEach { (first, second) -> contentEquals(first, second) }
    }


}