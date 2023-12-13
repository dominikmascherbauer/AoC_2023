fun main() {
    fun List<String>.findMirror(hasSmudge: Boolean = false): Int? =
        List(size - 1) { i ->
            i + 1 to generateSequence(0) { it + 1 }
                .take(i + 1)
                .take(size - i - 1)
                .sumOf { j -> get(i - j).zip(get(i + j + 1)).count { (a, b) -> a != b } }
        }
        .firstOrNull { it.second == (if (hasSmudge)  1 else 0) }
        ?.first

    fun part1(input: List<Pair<List<String>, List<String>>>): Int =
        input.sumOf { (grid, transposed) ->
            grid.findMirror()?.times(100) ?: transposed.findMirror()!!
        }

    fun part2(input: List<Pair<List<String>, List<String>>>): Int =
        input.sumOf { (grid, transposed) ->
            grid.findMirror(true)?.times(100) ?: transposed.findMirror(true)!!
        }

    val input = readInput("Day13")
        // parse grid by grid
        .fold(listOf(listOf<String>())) { acc, s ->
            if (s.isEmpty())
                acc.plus(listOf(listOf()))
            else
                acc.dropLast(1).plus(listOf(acc.last().plus(s)))
        }
        // transpose each grid
        .map { grid ->
            grid to List(grid[0].length) { i ->
                grid.map { it[i] }.joinToString()
            }
        }

    println(part1(input))
    println(part2(input))
}
