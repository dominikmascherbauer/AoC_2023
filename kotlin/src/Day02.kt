import kotlin.math.max

val maxVals = mapOf(
    "red" to 12,
    "green" to 13,
    "blue" to 14
)

fun main() {
    fun part1(input: List<String>): Int {
        val x = input
            .map { line -> line.dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }.toInt() to line.dropWhile { it != ':' }.drop(1).split(';') }
            .filter { p -> p.second.all { it.split(',').all { it.dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }.toInt() <= maxVals[it.dropWhile { !it.isLetter() }]!! } } }
            .map { it.first }
            .sum()
        return x
    }

    fun part2(input: List<String>): Int {
        val x = input
            .map { line -> line.dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }.toInt() to line.dropWhile { it != ':' }.drop(1).split(';') }
            .map { line -> line.second.flatMap { it.split(',').map { it.dropWhile { !it.isLetter() } to it.dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }.toInt() } } }
            .map { line -> line.fold(mutableListOf(0,0,0)) { acc, pair -> if (pair.first == "red") acc[0] = max(acc[0], pair.second) else if (pair.first == "green") acc[1] = max(acc[1], pair.second) else acc[2] = max(acc[2], pair.second); acc } }
            .map { line -> line.fold(1) { acc, i -> acc * i } }
        print(x)

        return 0
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}