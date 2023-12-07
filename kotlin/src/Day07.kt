fun main() {
    fun Char.cardOrder(): Char = when (this) {
        'X' -> 'a'
        '2' -> 'b'
        '3' -> 'c'
        '4' -> 'd'
        '5' -> 'e'
        '6' -> 'f'
        '7' -> 'g'
        '8' -> 'h'
        '9' -> 'i'
        'T' -> 'j'
        'J' -> 'k'
        'Q' -> 'l'
        'K' -> 'm'
        'A' -> 'n'
        else -> 'z'
    }

    fun List<String>.calcBids(withJoker: Boolean = false) =
        map { it.split(' ').zipWithNext { s, i -> (if (withJoker) s.replace('J', 'X') else s) to i.toInt() }.first() }
            .map { p ->
                Triple(
                    p.first
                        .filter { it != 'X' }
                        .groupBy { it }
                        .values
                        .map { v -> v.size + p.first.count { it == 'X' } }
                        .ifEmpty { listOf(5) },
                    p.first,
                    p.second
                )
            }
            .sortedWith(
                compareBy(
                    { -it.first.size },     // first sort by number of different cards: AAAAA > (AAAAK | AAAKK) > (AAAKQ | AAKKQ) > AAKQT > AKQT9
                    { it.first.max() },     // then by number of a kind of card: AAAAK > AAAKK, AAAKQ > AAKKQ
                    { it.second.map(Char::cardOrder).toString() }     // then by the first higher card (AAAAK > AAAKA > KAAAA)
                )
            )
            .foldIndexed(0) { i, acc, v -> acc + v.third * (i+1)}

    fun part1(input: List<String>): Int = input.calcBids()

    fun part2(input: List<String>): Int = input.calcBids(true)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}