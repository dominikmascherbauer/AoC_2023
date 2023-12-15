fun main() {
    fun String.runningHash() =
        fold(0) { acc, c ->
            ((acc + c.code) * 17) % 256
        }

    fun part1(input: List<String>): Int =
        input.sumOf { it.runningHash() }

    fun part2(input: List<String>): Int =
        input
            .groupBy { it.takeWhile(Char::isLowerCase).runningHash() }
            .mapValues { e ->
                e.value
                    .filterIndexed { i, s ->
                        !s.endsWith('-') && e.value.drop(i + 1).none { it == s.takeWhile(Char::isLowerCase) + '-' }
                    }
                    .groupBy({ it.takeWhile(Char::isLowerCase) }) { v ->
                        v.dropWhile { it.isLowerCase() || it == '=' }.toInt()
                    }
                    .map { it.value.last() }
            }
            .filterValues { it.isNotEmpty() }
            .map { e ->
                e.value.foldIndexed(0) { index, acc, i ->
                    acc + (e.key + 1) * (index + 1) * i
                }
            }
            .sum()

    val input = readInput("Day15")[0].split(',')
    println(part1(input))
    println(part2(input))
}