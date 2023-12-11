import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun List<Pair<Int, Int>>.sumOfGalaxyDistances(expansion: Long, emptyRows: List<Int>, emptyCols: List<Int>): Long =
        foldIndexed(0L) { i, acc, (x1, y1) ->
            acc + this
                .drop(i + 1)
                .sumOf {(x2, y2) ->
                    // '-1' because there is already 1 empty line in the map
                    abs(x1 - x2) + (expansion - 1) * emptyRows.count { min(x1,x2) < it && it < max(x1, x2) } +
                    abs(y1 - y2) + (expansion - 1) * emptyCols.count { min(y1,y2) < it && it < max(y1, y2) }
                }
        }


    fun part1(galaxies: List<Pair<Int, Int>>, emptyRows: List<Int>, emptyCols: List<Int>): Long =
        galaxies.sumOfGalaxyDistances(2, emptyRows, emptyCols)

    fun part2(galaxies: List<Pair<Int, Int>>, emptyRows: List<Int>, emptyCols: List<Int>): Long =
        galaxies.sumOfGalaxyDistances(1_000_000, emptyRows, emptyCols)


    val input = readInput("Day11")

    val emptyRows = input
        .mapIndexed { i, s -> i to s }
        .filter { p -> p.second.all { it == '.' } }
        .map { it.first }
    val emptyCols = input
        .first()
        .mapIndexed { i, c -> i to c }
        .filter { p -> input.all { it[p.first] == '.' } }
        .map { it.first }
    val galaxies = input
        .flatMapIndexed { x, s -> s.mapIndexed { y, c -> if (c == '#') x to y else null } }
        .filterNotNull()

    println(part1(galaxies, emptyRows, emptyCols))
    println(part2(galaxies, emptyRows, emptyCols))
}