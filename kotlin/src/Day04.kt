fun main() {
    fun List<String>.parseCard() =
        map { card ->
            card.split(':')
                .last()
                .split('|', limit = 2)
                .map { nums ->
                    nums.split(' ')
                        .filter { it.any(Char::isDigit) }
                        .map { it.trim(' ').toInt() }
                }
        }

    fun part1(input: List<String>): Int = input
        .parseCard()
        .fold(0) { acc, row ->
            acc + row[1].filter { it in row[0] }.run { (1 shl size) shr 1 } // some magic 2^correctNumbers.size / 2
        }

    fun part2(input: List<String>): Int = input
        .parseCard()
        .foldIndexed(mutableMapOf<Int, Int>()) { i, acc, row ->
            acc.compute(i) { _, v -> 1 + (v ?: 0) }
            row[1].filter { it in row[0] }
                .forEachIndexed { j, _ ->
                    acc.compute(i + j + 1) { _, v -> acc[i]!! + (v ?: 0) }
                }
            acc
        }
        .map { it.value }
        .sum()

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}