fun main() {
    fun List<String>.step(dir: Char, x: Int, y: Int): List<Triple<Char,Int,Int>> =
            when (dir) {
                'R' -> when (get(y)[x]) {
                    '\\' -> listOf(Triple('D', x, y + 1))
                    '/' -> listOf(Triple('U', x, y - 1))
                    '|' -> listOf(Triple('U', x, y - 1), Triple('D', x, y + 1))
                    else -> listOf(Triple(dir, x + 1, y))
                }

                'L' -> when (get(y)[x]) {
                    '\\' -> listOf(Triple('U', x, y - 1))
                    '/' -> listOf(Triple('D', x, y + 1))
                    '|' -> listOf(Triple('U', x, y - 1), Triple('D', x, y + 1))
                    else -> listOf(Triple(dir, x - 1, y))
                }

                'U' -> when (get(y)[x]) {
                    '\\' -> listOf(Triple('L', x - 1, y))
                    '/' -> listOf(Triple('R', x + 1, y))
                    '-' -> listOf(Triple('L', x - 1, y), Triple('R', x + 1, y))
                    else -> listOf(Triple(dir, x, y - 1))
                }

                'D' -> when (get(y)[x]) {
                    '\\' -> listOf(Triple('R', x + 1, y))
                    '/' -> listOf(Triple('L', x - 1, y))
                    '-' -> listOf(Triple('R', x + 1, y), Triple('L', x - 1, y))
                    else -> listOf(Triple(dir, x, y + 1))
                }

                else -> listOf(Triple('0', -1, -1))
            }

    fun part1(input: List<String>): Int =
        with(mutableSetOf<Triple<Char,Int,Int>>()) {
            generateSequence(listOf(Triple('R', 0, 0))) { l ->
                l.onEach { add(it) }
                    .flatMap { (dir, x, y) -> input.step(dir, x, y) }
                    .filter { (_, x, y) -> 0 <= x && x < input[0].length && 0 <= y && y < input.size }
                    .filterNot { contains(it) }
            }.takeWhile { it.isNotEmpty() }.fold(setOf<Pair<Int, Int>>()) { acc, l ->
                acc.plus(l.map { (_, x, y) -> x to y }.toSet())
            }.size
        }

    fun part2(input: List<String>): Int {
        val tmp = List(input[0].length) {
            with(mutableSetOf<Triple<Char,Int,Int>>()) {
                generateSequence(listOf(Triple('R', 0, it))) { l ->
                    l.onEach { add(it) }
                        .flatMap { (dir, x, y) -> input.step(dir, x, y) }
                        .filter { (_, x, y) -> 0 <= x && x < input[0].length && 0 <= y && y < input.size }
                        .filterNot { contains(it) }
                }.takeWhile { it.isNotEmpty() }.fold(setOf<Pair<Int, Int>>()) { acc, l ->
                    acc.plus(l.map { (_, x, y) -> x to y }.toSet())
                }.size
            }
        }.plus(List(input[0].length) {
            with(mutableSetOf<Triple<Char,Int,Int>>()) {
                generateSequence(listOf(Triple('L', input[0].length - 1, it))) { l ->
                    l.onEach { add(it) }
                        .flatMap { (dir, x, y) -> input.step(dir, x, y) }
                        .filter { (_, x, y) -> 0 <= x && x < input[0].length && 0 <= y && y < input.size }
                        .filterNot { contains(it) }
                }.takeWhile { it.isNotEmpty() }.fold(setOf<Pair<Int, Int>>()) { acc, l ->
                    acc.plus(l.map { (_, x, y) -> x to y }.toSet())
                }.size
            }
        }).plus(
            List(input[0].length) {
                with(mutableSetOf<Triple<Char,Int,Int>>()) {
                    generateSequence(listOf(Triple('D', it, 0))) { l ->
                        l.onEach { add(it) }
                            .flatMap { (dir, x, y) -> input.step(dir, x, y) }
                            .filter { (_, x, y) -> 0 <= x && x < input[0].length && 0 <= y && y < input.size }
                            .filterNot { contains(it) }
                    }.takeWhile { it.isNotEmpty() }.fold(setOf<Pair<Int, Int>>()) { acc, l ->
                        acc.plus(l.map { (_, x, y) -> x to y }.toSet())
                    }.size
                }
            }
        ).plus(List(input[0].length) {
            with(mutableSetOf<Triple<Char,Int,Int>>()) {
                generateSequence(listOf(Triple('U', it, input.size - 1))) { l ->
                    l.onEach { add(it) }
                        .flatMap { (dir, x, y) -> input.step(dir, x, y) }
                        .filter { (_, x, y) -> 0 <= x && x < input[0].length && 0 <= y && y < input.size }
                        .filterNot { contains(it) }
                }.takeWhile { it.isNotEmpty() }.fold(setOf<Pair<Int, Int>>()) { acc, l ->
                    acc.plus(l.map { (_, x, y) -> x to y }.toSet())
                }.size
            }
        })
        return tmp.max()
    }

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}