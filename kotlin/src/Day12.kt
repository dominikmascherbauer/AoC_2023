fun main() {
    fun String.countPermutations(): Long {
        val cache = hashMapOf<Pair<Char, Pair<Int, Int>>, Long>()
        fun String.countPermutationsAux(missing: Int, missingBroken: Int, brokenGroups: List<Int>, curGroupLen: Int = 0): Long {
            // if too many working or broken springs were replaced for '?' chars return 0
            // as it is impossible to get a valid permutation without placing exactly the amount of broken spring required
            return if (missingBroken < 0 || missing < missingBroken)
                0
            // return 1 for a valid permutation
            // a permutation is valid if we are at the of the input string, or we have no more groups to cover (implies that all required broken springs are set)
            else if (isEmpty() || brokenGroups.isEmpty())
                1
            else
                when (first()) {
                    // current char is '.', here we are in a safe place to cache the result (would be enough to just cache if the next char is a '#', but who cares)
                    '.' -> cache.getOrPut(first() to (missing to missingBroken)) {
                        drop(1).countPermutationsAux(missing, missingBroken, brokenGroups)
                    }
                    // current char is '#', we have to check the next char in order to further proceed
                    '#' -> when (drop(1).firstOrNull()) {
                        // current broken sequence ends -> check if it is valid
                        '.' -> if (brokenGroups.first() != curGroupLen + 1) 0 else drop(1).countPermutationsAux(missing, missingBroken, brokenGroups.drop(1))
                        // current broken sequence goes on -> early stop if it next is not valid
                        '#' -> if (curGroupLen + 2 > brokenGroups.first()) 0 else drop(1).countPermutationsAux(missing, missingBroken, brokenGroups, curGroupLen + 1)
                        // we are at the end of the input -> only reachable if we have a valid permutation
                        null -> 1
                        // next char is '?', split and go on with both
                        else -> "${first()}#${drop(2)}".countPermutationsAux(missing - 1, missingBroken - 1, brokenGroups, curGroupLen) +
                                "${first()}.${drop(2)}".countPermutationsAux(missing - 1, missingBroken, brokenGroups, curGroupLen)
                    }
                    // current char is '?', split and go on, only happens if previous char was '.' or at the very first char
                    else -> "#${drop(1)}".countPermutationsAux(missing - 1, missingBroken - 1, brokenGroups, curGroupLen) +
                            ".${drop(1)}".countPermutationsAux(missing - 1, missingBroken, brokenGroups, curGroupLen)
                }
        }

        val brokenGroups = Regex("[0-9]+").findAll(this).map { m -> m.value.toInt() }.toList()
        return split(' ')[0].countPermutationsAux(count { it == '?' }, brokenGroups.sum() - count { it == '#' }, brokenGroups)
    }

    fun part1(input: List<String>): Long =
        input.sumOf { it.countPermutations() }

    fun part2(input: List<String>): Long = input
        .map {
            it.split(' ')[0]
                .plus('?')
                .repeat(5)
                .dropLast(1)
                .plus(' ')
                .plus(
                    it.split(' ')[1]
                        .plus(',')
                        .repeat(5)
                )
        }
        .sumOf { it.countPermutations() }

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}