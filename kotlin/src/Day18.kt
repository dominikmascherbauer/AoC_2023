import kotlin.math.abs

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    fun part1(input: List<Triple<Char, Int, String>>): Long =
        input.fold(listOf(0L to 0L)) { grid, trench ->
            val last = grid.last()
            when (trench.first) {
                'R' -> grid.plus((last.first + trench.second) to last.second)
                'L' -> grid.plus((last.first - trench.second) to last.second)
                'D' -> grid.plus(last.first to (last.second + trench.second))
                'U' -> grid.plus(last.first to (last.second - trench.second))
                else -> grid // should not happen
            }
        }
            .zipWithNext { (x1, y1), (x2, y2)  -> x1*y2 - y1*x2 + abs(x1 - x2) + abs(y1 - y2) } // shoelace algorithm + outer edges
            .sum()
            .div(2L)
            .plus(1L)

    fun part2(input: List<Triple<Char, Int, String>>): Long =
        input
            .map {  // fix input
                Triple(
                    when (it.third.last()) {
                        '0' -> 'R'
                        '1' -> 'D'
                        '2' -> 'L'
                        '3' -> 'U'
                        else -> 'X' // should not happen
                    },
                    it.third.drop(1).dropLast(1).hexToInt(),
                    it.third
                )
            }
            .run { part1(this) } // then do the same as in part1

    val input = readInput("Day18")
        .map { s ->
            Triple(
                s.first(),
                s.dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }.toInt(),
                s.dropWhile { it != '#' }.take(7)
            )
        }
    println(part1(input))
    println(part2(input))
}