val cardValues: Map<Char, Int> = mapOf(
    'J' to 1,
    '2' to 2,
    '3' to 3,
    '4' to 4,
    '5' to 5,
    '6' to 6,
    '7' to 7,
    '8' to 8,
    '9' to 9,
    'T' to 10,
    'Q' to 11,
    'K' to 12,
    'A' to 13,
)

fun main() {
    fun part1(input: List<String>): Int {
        val res = input
            .map { it.split(' ') }
            .map { it[0] to it[1].toInt() }
            .map { it.first.groupBy { it }.mapValues { it.value.size } to it }
            .sortedWith(compareBy({ -it.first.size }, {it.first.maxOf { it.value }}, {cardValues[it.second.first[0]]}, {cardValues[it.second.first[1]]}, {cardValues[it.second.first[2]]}, {cardValues[it.second.first[3]]}, {cardValues[it.second.first[4]]}))
            .foldIndexed(0) { i, acc, v -> acc + v.second.second * (i+1)}

        return res
    }

    fun part2(input: List<String>): Int {
        val res = input
            .map { it.split(' ') }
            .map { it[0] to it[1].toInt() }
            .map { p -> p.first.filter { it != 'J' }.groupBy { it }.mapValues { it.value.size + p.first.count { it == 'J' } } to p }
            .map { p -> if (p.second.first.count { it == 'J' } == 5) mapOf('J' to 5) to p.second else p }
            .sortedWith(compareBy({ -it.first.size }, {it.first.maxOf { it.value }}, {cardValues[it.second.first[0]]}, {cardValues[it.second.first[1]]}, {cardValues[it.second.first[2]]}, {cardValues[it.second.first[3]]}, {cardValues[it.second.first[4]]}))
            .foldIndexed(0) { i, acc, v -> acc + v.second.second * (i+1)}

        return res
    }

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}