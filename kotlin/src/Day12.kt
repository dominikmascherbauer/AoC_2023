fun main() {
    fun String.countPermutations(): Long {
        val cache = hashMapOf<Pair<Char, Pair<Int, Int>>, Long>()
        fun String.countPermutationsAux(missing: Int, missingBroken: Int, brokenGroups: List<Int>, curGroupLen: Int = 0): Long {
            return if (missingBroken < 0 || missing < missingBroken)
                0
            else if (isEmpty() || brokenGroups.isEmpty())
                1
            else
                when (first()) {
                    '.' -> cache.getOrPut(first() to (missing to missingBroken)) {
                        drop(1).countPermutationsAux(missing, missingBroken, brokenGroups)
                    }

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