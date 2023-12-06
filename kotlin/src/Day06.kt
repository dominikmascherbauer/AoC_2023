private fun Pair<Long, Long>.marginOfError() =
    first + 1 - 2 * (0..first).first { it * (first - it) > second }

fun main() {
    fun part1(input: List<String>): Long = input
        .map { line ->
            line.split(' ')
                .filter { it.any(Char::isDigit) }
                .map(String::toLong)
        }
        .zipWithNext { a, b -> a.zip(b) }
        .first()
        .map { it.marginOfError() }
        .reduce { acc, i -> acc * i }

    fun part2(input: List<String>): Long = input
        .map { it.filter(Char::isDigit).toLong() }
        .zipWithNext()
        .first()
        .marginOfError()

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}