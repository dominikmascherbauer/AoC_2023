fun main() {
    fun part1(input: List<String>): Int {
        val symbols = mutableListOf<Pair<Int, Int>>()
        val numbers = mutableListOf<Pair<Int, Pair<Int, Pair<Int, Int>>>>()

        input
            .onEachIndexed { i1, s -> s.mapIndexed { i2, c -> c to (i1 to i2) }.filter { !it.first.isDigit() && it.first != '.' }.onEach { symbols.add(it.second) } }
            .onEachIndexed { i1, s -> s.mapIndexed { i2, c -> c to (i1 to i2) }.filter { it.first.isDigit() }.asSequence().forEach {
                if (numbers.none { n -> n.second.first == it.second.first && n.second.second.first <= it.second.second && it.second.second <= n.second.second.second }) numbers.add(
                    s.drop(it.second.second).takeWhile { it.isDigit() }
                        .toInt() to (i1 to (it.second.second to it.second.second + s.drop(it.second.second)
                        .takeWhile { it.isDigit() }.length - 1))
                )
            }}

        val res = numbers
            .filter { n -> symbols.any { n.second.first - 1 <= it.first && it.first <= n.second.first + 1 && n.second.second.first - 1 <= it.second && it.second <= n.second.second.second + 1 } }
            .sumOf { it.first }

        return res
    }

    fun part2(input: List<String>): Int {
        val symbols = mutableListOf<Pair<Int, Int>>()
        val numbers = mutableListOf<Pair<Int, Pair<Int, Pair<Int, Int>>>>()

        input
            .onEachIndexed { i1, s -> s.mapIndexed { i2, c -> c to (i1 to i2) }.filter { it.first == '*' }.onEach { symbols.add(it.second) } }
            .onEachIndexed { i1, s -> s.mapIndexed { i2, c -> c to (i1 to i2) }.filter { it.first.isDigit() }.asSequence().forEach {
                if (numbers.none { n -> n.second.first == it.second.first && n.second.second.first <= it.second.second && it.second.second <= n.second.second.second }) numbers.add(
                    s.drop(it.second.second).takeWhile { it.isDigit() }
                        .toInt() to (i1 to (it.second.second to it.second.second + s.drop(it.second.second)
                        .takeWhile { it.isDigit() }.length - 1))
                )
            }}

        val res = symbols
            .map { s -> numbers.filter { n -> n.second.first - 1 <= s.first && s.first <= n.second.first + 1 && n.second.second.first - 1 <= s.second && s.second <= n.second.second.second + 1 } }
            .filter { it.size == 2 }
            .map { it[0].first * it[1].first }
            .sum()
        return res
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}