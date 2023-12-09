fun main() {
    fun List<List<List<Long>>>.part1(): Long =
        sumOf { it.foldRight(0L) { longs, acc -> longs.last() + acc } }

    fun List<List<List<Long>>>.part2(): Long =
        sumOf { it.foldRight(0L) { longs, acc -> longs.first() - acc } }

    val input = readInput("Day09")
        .map { s -> Regex("[-0-9]+").findAll(s).map { it.value.toLong() }.toList() }
        .map { init ->
            generateSequence(init) { it.zipWithNext { a, b -> b - a } }
                .takeWhile { seq -> seq.any { it != 0L } }
                .toList()
        }
    println(input.part1())
    println(input.part2())
}