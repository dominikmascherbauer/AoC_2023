private fun Pair<Long, Long>.marginOfError() =
    first - 2 * binSearch(0, first/2, first/4) + 1

private fun Pair<Long, Long>.binSearch(start: Long, end: Long, cur: Long): Long =
    if (cur * (first - cur) < second)
        binSearch(cur + 1, end, (cur + 1 + end)/2)
    else if ((cur - 1) * (first - (cur - 1)) >= second)
        binSearch(start, cur - 1, (cur - 1 + start)/2)
    else
        cur

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