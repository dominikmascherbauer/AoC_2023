fun main() {
    val numberMap = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )

    fun part1(input: List<String>): Int =
        input.map { s -> s.filter { it.isDigit() }
                .map { it.digitToInt() }
            }
            .sumOf { it.first() * 10 + it.last() }

    fun part2(input: List<String>): Int =
        input.map { s ->
            s.mapIndexed { i, c ->
                if (c.isDigit()) c.digitToInt() else numberMap.entries.firstOrNull {
                    s.drop(i).startsWith(it.key)
                }?.value
            }.filterNotNull()
        }
            .sumOf { it.first() * 10 + it.last() }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}