fun main() {
    fun List<String>.getField(x: Pair<Int, Int>) =
        drop(x.first).first()[x.second]

    fun inferPipeSymbol(pos: Pair<Int, Int>, prev: Pair<Int, Int>, next: Pair<Int, Int>) =
        when (pos.first - prev.first to pos.second - prev.second) {
            0 to -1 -> when (next.first - pos.first to next.second - pos.second) {
                0 to -1 -> '-'
                -1 to 0 -> 'L'
                0 to 1 -> 'X'
                1 to 0 -> 'F'
                else -> 'X'
            }
            -1 to 0 -> when (next.first - pos.first to next.second - pos.second) {
                0 to -1 -> '7'
                -1 to 0 -> '|'
                0 to 1 -> 'F'
                1 to 0 -> 'X'
                else -> 'X'
            }
            0 to 1 -> when (next.first - pos.first to next.second - pos.second) {
                0 to -1 -> 'X'
                -1 to 0 -> 'J'
                0 to 1 -> '-'
                1 to 0 -> '7'
                else -> 'X'
            }
            1 to 0 -> when (next.first - pos.first to next.second - pos.second) {
                0 to -1 -> 'J'
                -1 to 0 -> 'X'
                0 to 1 -> 'L'
                1 to 0 -> '|'
                else -> 'X'
            }
            else -> 'X'
        }

    fun List<String>.parsePipeNetwork() = with(mapIndexed { i, s -> i to s.indexOf('S') }.first { it.second != -1 }) {
        listOf(
            'D' to (first + 1 to second),    // Down
            'R' to (first to second + 1),    // Right
            'U' to (first - 1 to second),    // Up
            'L' to (first to second - 1)     // Left
        ).map {
            generateSequence(it.first to it.second) { (dir, pos) ->
                val field = getField(pos)
                val (newDir, newPos) =
                    when (field) {
                        '|' -> when (dir) {
                            'D' -> 'D' to (pos.first + 1 to pos.second)
                            'U' -> 'U' to (pos.first - 1 to pos.second)
                            else -> 'X' to (-1 to -1)
                        }
                        '-' -> when (dir) {
                            'R' -> 'R' to (pos.first to pos.second + 1)
                            'L' -> 'L' to (pos.first to pos.second - 1)
                            else -> 'X' to (-1 to -1)
                        }
                        'L' -> when (dir) {
                            'D' -> 'R' to (pos.first to pos.second + 1)
                            'L' -> 'U' to (pos.first - 1 to pos.second)
                            else -> 'X' to (-1 to -1)
                        }
                        'J' -> when (dir) {
                            'D' -> 'L' to (pos.first to pos.second - 1)
                            'R' -> 'U' to (pos.first - 1 to pos.second)
                            else -> 'X' to (-1 to -1)
                        }
                        '7' -> when (dir) {
                            'U' -> 'L' to (pos.first to pos.second - 1)
                            'R' -> 'D' to (pos.first + 1 to pos.second)
                            else -> 'X' to (-1 to -1)
                        }
                        'F' -> when (dir) {
                            'U' -> 'R' to (pos.first to pos.second + 1)
                            'L' -> 'D' to (pos.first + 1 to pos.second)
                            else -> 'X' to (-1 to -1)
                        }
                        'S' -> 'X' to pos
                        else -> 'X' to (-1 to -1)
                    }
                newDir to newPos
            }
                .takeWhile { it.first != 'X' }
                .map { it.second }
        }
            .first { getField(it.last()) == 'S' }
    }

    fun part1(input: List<String>): Int = input
        .parsePipeNetwork()
        .toList()
        .size / 2


    fun part2(input: List<String>): Int =
        with(input.parsePipeNetwork()) {
            /*
             * This checks how many fields are inside the pipe network line by line
             *
             * If a field is inside the pipe network can be decided by looking at the fields row and line
             * Therefore we have a boolean for each row and line -> row && line = inside the network
             * As iteration goes line by line, row booleans are stored in a boolean list
             *
             * A field is inside the pipe network if:
             *   the line up to the current fields has an odd amount of '|', "L7" and "FJ" combinations
             *   and
             *   the row up to the current field has an odd amount of '-', "FJ" and "7L" combinations
             *
             * Looking at only these combinations is sufficient as they are exhaustive for this problem
             * (consider special case for S though, as it can be any of the pipe symbols)
             *
             * examples:
             * I ... inside
             * O ... outside
             *
             * OO|I|OL---7II|OO
             * OO|I|OL---JOO|II
             * OO|I|OF---7OO|II
             * OO|I|OF---JII|OO
             *
             * O O O O
             * - - - -
             * I I I I
             * - - - -
             * O O O O
             * 7 7 F F
             * | | | |
             * L J L J
             * I O O I
             */
            val pipeNetwork = this.groupBy({ it.first }) { it.second }
            val startSymbol = inferPipeSymbol(this.last(), this.toList().reversed().drop(1).first(), this.first())
            input.foldIndexed(0 to input[0].map { false }) { x, acc, s ->
                s.foldIndexed(Triple(acc.first, acc.second, false)) { y, acc2, ch ->
                    val sym = if (ch == 'S') startSymbol else ch
                    if (pipeNetwork[x]?.contains(y) == true)
                        when (sym) {
                            '|' -> Triple(acc2.first, acc2.second, !acc2.third)
                            '-' -> Triple(
                                acc2.first,
                                acc2.second.mapIndexed { i, b -> if (i == y) !b else b },
                                acc2.third
                            )

                            'F' -> Triple(
                                acc2.first,
                                acc2.second.mapIndexed { i, b -> if (i == y) !b else b },
                                !acc2.third
                            )

                            'L' -> Triple(
                                acc2.first,
                                acc2.second.mapIndexed { i, b -> if (i == y) !b else b },
                                acc2.third
                            )

                            'J' -> Triple(acc2.first, acc2.second, acc2.third)
                            '7' -> Triple(acc2.first, acc2.second, !acc2.third)
                            else -> acc2
                        }
                    else
                        Triple(
                            if (acc2.third && acc2.second[y]) acc2.first + 1 else acc2.first,
                            acc2.second,
                            acc2.third
                        )
                }
                    .run { first to second }
            }
        }.first

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}