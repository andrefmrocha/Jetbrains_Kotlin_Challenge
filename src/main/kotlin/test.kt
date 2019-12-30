import com.squareup.kotlinpoet.*

fun main(){
    val builder = FileSpec.builder("", "Foo")
    val classBuilder = TypeSpec.classBuilder("User").addModifiers(KModifier.DATA)

    val ctor = FunSpec.constructorBuilder()
    ctor.addParameter("id", Int::class)
    classBuilder.primaryConstructor(ctor.build())

    val idProperty = PropertySpec.builder("id", Int::class).initializer("id").build()
    classBuilder.addProperty(idProperty)

    builder.addType(classBuilder.build())
    builder.build().writeTo(System.out)
}