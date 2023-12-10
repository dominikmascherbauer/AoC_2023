fun main() {
    fun part1(input: List<String>): Int {
        val map = input.mapIndexed { i, s -> i to s }
        val start: Pair<Int, Int> = input.mapIndexed { i, s -> i to s }.filter { 'S' in it.second }.map { it.first to it.second.indexOf('S') }.first()

        val res = generateSequence(Triple('D', 0, start)) {
            when (it.first) {
                'D' -> Triple (
                    when (map[it.third.first + 1].second[it.third.second]) {
                        '|' -> 'D'
                        'J' -> 'L'
                        'L' -> 'R'
                        else -> 'u' // undefined
                    },
                    it.second + 1,
                    it.third.first + 1 to it.third.second
                )
                'U' -> Triple (
                    when (map[it.third.first - 1].second[it.third.second]) {
                        '|' -> 'U'
                        '7' -> 'L'
                        'F' -> 'R'
                        else -> 'u' // undefined
                    },
                    it.second + 1,
                    it.third.first - 1 to it.third.second
                )
                'R' -> Triple (
                    when (map[it.third.first].second[it.third.second + 1]) {
                        '-' -> 'R'
                        '7' -> 'D'
                        'J' -> 'U'
                        else -> 'u' // undefined
                    },
                    it.second + 1,
                    it.third.first to it.third.second + 1
                )
                'L' -> Triple (
                    when (map[it.third.first].second[it.third.second - 1]) {
                        '-' -> 'L'
                        'F' -> 'D'
                        'L' -> 'U'
                        else -> 'u' // undefined
                    },
                    it.second + 1,
                    it.third.first to it.third.second - 1
                )
                // else undefined (if back at start -> loop)
                else -> Triple ('0', 0, 0 to 0)
            }
        }
            .filter { 'S' == map[it.third.first].second[it.third.second] || '0' == it.first }
            .drop(1)
            .first()

        return res.second / 2
    }

    fun part2(input: List<String>): Int {
        val map = input.mapIndexed { i, s -> i to s }
        val start: Pair<Int, Int> = input.mapIndexed { i, s -> i to s }.filter { 'S' in it.second }.map { it.first to it.second.indexOf('S') }.first()

        val res = generateSequence(Triple('D', listOf<Pair<Int, Int>>(), start)) {
            when (it.first) {
                'D' -> Triple (
                    when (map[it.third.first + 1].second[it.third.second]) {
                        '|' -> 'D'
                        'J' -> 'L'
                        'L' -> 'R'
                        else -> 'u' // undefined
                    },
                    it.second.plus(it.third.first + 1 to it.third.second),
                    it.third.first + 1 to it.third.second
                )
                'U' -> Triple (
                    when (map[it.third.first - 1].second[it.third.second]) {
                        '|' -> 'U'
                        '7' -> 'L'
                        'F' -> 'R'
                        else -> 'u' // undefined
                    },
                    it.second.plus(it.third.first - 1 to it.third.second),
                    it.third.first - 1 to it.third.second
                )
                'R' -> Triple (
                    when (map[it.third.first].second[it.third.second + 1]) {
                        '-' -> 'R'
                        '7' -> 'D'
                        'J' -> 'U'
                        else -> 'u' // undefined
                    },
                    it.second.plus(it.third.first to it.third.second + 1),
                    it.third.first to it.third.second + 1
                )
                'L' -> Triple (
                    when (map[it.third.first].second[it.third.second - 1]) {
                        '-' -> 'L'
                        'F' -> 'D'
                        'L' -> 'U'
                        else -> 'u' // undefined
                    },
                    it.second.plus(it.third.first to it.third.second - 1),
                    it.third.first to it.third.second - 1
                )
                // else undefined (if back at start -> loop)
                else -> Triple ('0', listOf(), 0 to 0)
            }
        }
            .filter { 'S' == map[it.third.first].second[it.third.second] || '0' == it.first }
            .drop(1)
            .map { it.second }
            .first()

        val res2 = res.sortedWith(compareBy(
            { it.first },
            { it.second }
        ))
            .groupBy({ it.first }) { it.second }

        val res3 = input.foldIndexed(Triple(false, input[0].map { false }, 0)) { x, acc, s ->
                val newAcc = s.foldIndexed(acc) { y, acc2, ch ->
                    if (res2[x]?.contains(y) == true)
                        when (ch) {
                            '|' -> Triple(!acc2.first, acc2.second, acc2.third)
                            '-' -> Triple(acc2.first, acc2.second.mapIndexed { i, b -> if (i == y) !b else b}, acc2.third)
                            'F' -> Triple(!acc2.first, acc2.second.mapIndexed { i, b -> if (i == y) !b else b}, acc2.third)
                            'L' -> Triple(acc2.first, acc2.second.mapIndexed { i, b -> if (i == y) !b else b}, acc2.third)
                            'J' -> Triple(acc2.first, acc2.second, acc2.third)
                            '7' -> Triple(!acc2.first, acc2.second, acc2.third)
                            else -> acc2
                        }
                    else
                        Triple(acc2.first, acc2.second, if (acc2.first && acc2.second[y]) acc2.third + 1 else acc2.third)
                }
                Triple(false, newAcc.second, newAcc.third)
            }
            .third




        return res3
    }

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}