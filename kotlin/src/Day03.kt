fun main() {
    fun List<String>.parseWith(regex: Regex) =
        foldIndexed(sequenceOf<Pair<Int, MatchResult>>()) { i, acc, s -> regex.findAll(s).map { i to it }.plus(acc) }

    fun Sequence<Pair<Int, MatchResult>>.toSymbolMap() =
        groupBy({ it.first }) { it.second.range.first }
    fun List<String>.getNumberMap() =
        parseWith(Regex("[0-9]+"))
            .groupBy({ it.first }) { it.second.value.toInt() to it.second.range.first-1..it.second.range.last+1 }


    fun part1(numbers: Map<Int, List<Pair<Int, IntRange>>>, symbols: Map<Int, List<Int>>): Int = numbers
        .flatMap { row ->
            row.value
                .filter { num ->
                    symbols.getOrDefault(row.key - 1, listOf())
                        .plus(symbols.getOrDefault(row.key, listOf()))
                        .plus(symbols.getOrDefault(row.key + 1, listOf()))
                        .any { it in num.second }
                }
                .map { it.first }
        }
        .sum()

    fun part2(numbers: Map<Int, List<Pair<Int, IntRange>>>, gears: Map<Int, List<Int>>): Int = gears
            .flatMap { row ->
                row.value
                    .map { g ->
                        numbers.getOrDefault(row.key - 1, listOf())
                            .plus(numbers.getOrDefault(row.key, listOf()))
                            .plus(numbers.getOrDefault(row.key + 1, listOf()))
                            .filter { g in it.second }
                            .map { it.first }
                    }
                    .filter { it.size == 2 }
            }
            .sumOf { it.reduce(Int::times) }

    val input = readInput("Day03")

    val numbers = input.getNumberMap()
    val symbols = input.parseWith(Regex("[^0-9.]")).toSymbolMap()
    val gears = input.parseWith(Regex("[*]")).toSymbolMap()

    println(part1(numbers, symbols))
    println(part2(numbers, gears))
}