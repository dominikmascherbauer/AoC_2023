fun main() {
    fun List<String>.step(dir: Char, x: Int, y: Int): List<Triple<Char,Int,Int>> =
        when (dir to this[y][x]) {
            'R' to '\\', 'L' to '/' -> listOf(Triple('D', x, y + 1))
            'R' to '/', 'L' to '\\' -> listOf(Triple('U', x, y - 1))
            'R' to '|', 'L' to '|' -> listOf(Triple('D', x, y + 1), Triple('U', x, y - 1))
            'D' to '\\', 'U' to '/' -> listOf(Triple('R', x + 1, y))
            'D' to '/', 'U' to '\\' -> listOf(Triple('L', x - 1, y))
            'D' to '-', 'U' to '-' -> listOf(Triple('R', x + 1, y), Triple('L', x - 1, y))
            else -> when (dir) {
                'R' -> listOf(Triple(dir, x + 1, y))
                'L' -> listOf(Triple(dir, x - 1, y))
                'D' -> listOf(Triple(dir, x, y + 1))
                'U' -> listOf(Triple(dir, x, y - 1))
                else -> listOf(Triple('0',-1,-1))
            }
        }

    fun traverse(grid: List<String>, dir: Char, x: Int, y: Int): Int =
        with(mutableSetOf<Triple<Char,Int,Int>>()) {
            generateSequence(listOf(Triple(dir, x, y))) { l ->
                l.onEach { add(it) }
                    .flatMap { (dir, x, y) -> grid.step(dir, x, y) }
                    .filter { (_, x, y) -> 0 <= x && x < grid[0].length && 0 <= y && y < grid.size }
                    .filterNot { contains(it) }
            }.takeWhile { it.isNotEmpty() }.fold(setOf<Pair<Int, Int>>()) { acc, l ->
                acc.plus(l.map { (_, x, y) -> x to y }.toSet())
            }.size
        }

    fun part1(input: List<String>): Int =
        traverse(input, 'R', 0, 0)

    fun part2(input: List<String>): Int =
        List(input[0].length) { it }
            .flatMap {
                listOf(traverse(input, 'R', 0, it),
                    traverse(input, 'L', input[0].length - 1, it),
                    traverse(input, 'D', it, 0),
                    traverse(input, 'D', it, input.size - 1)
                )
            }
            .max()

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}