import java.io.File

fun main() {
    val x = File(ClassLoader.getSystemResource("day1").path)
        .readLines()
        .map { s -> s.filter { it.isDigit() }
            .map { it.digitToInt() }
        }
        .sumOf { it.first() * 10 + it.last() }
    println(x)
}