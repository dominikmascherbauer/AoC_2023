fun main() {
    fun List<List<Point<Char>>>.getNeighbors(p: Point<Char>): List<Point<Char>> =
        listOf(
            p.y to p.x + 1,
            p.y to p.x - 1,
            p.y + 1 to p.x,
            p.y - 1 to p.x
        ).filter { (y, x) -> x >= 0 && y >= 0 && y < size && x < get(0).size }
            .map { (y, x) -> this[y][x] }
            .filter { it.value != '#' }

    fun List<List<Point<Char>>>.bfs(start: Point<Char>, maxSteps: Int): List<Pair<Int, Point<Char>>> {
        val frontier: MutableList<Pair<Int, Point<Char>>> = mutableListOf(0 to start)
        val visited: MutableList<Pair<Int, Point<Char>>> = mutableListOf()

        while (frontier.isNotEmpty() && frontier.first().first < maxSteps) {
            val next = frontier.removeFirst()
            visited.add(next)
            val neighbors = getNeighbors(next.second).filter { p -> visited.none { p == it.second  } && frontier.none { p == it.second } }
            frontier.addAll(neighbors.map { (next.first + 1) to it })
        }

        return visited
    }

    fun part1(input: List<List<Point<Char>>>): Int =
        input.bfs(input.flatten().first { it.value == 'S' }, 65)
            .count { it.first % 2 == 0 }


    fun part2(input: List<List<Point<Char>>>): Long {
        val startSymbolPos = input.flatten().first { it.value == 'S' }

        val possibleGrids = listOf(
            startSymbolPos,
            input[startSymbolPos.y][0],
            input[startSymbolPos.y][input[0].size - 1],
            input[0][startSymbolPos.x],
            input[input.size - 1][startSymbolPos.x],
            input[0][0],
            input[0][input[0].size - 1],
            input[input.size - 1][0],
            input[input.size - 1][input[0].size - 1]
        ).map { start ->
            input.bfs(start, 262) // a maximum of 262 steps results into full expansion for all possible starting states
        }

        val reachablePlots = possibleGrids.map { it.count { it.first % 2 == 0 }.toLong() to it.count { it.first % 2 == 1 }.toLong() }
        val reachablePlots131Steps = possibleGrids.map { it.count { it.first < 131 && it.first % 2 == 0 }.toLong() to it.count { it.first < 131 && it.first % 2 == 1 }.toLong() }
        val reachablePlots66Steps = possibleGrids.map { it.count { it.first < 66 && it.first % 2 == 0 }.toLong() to it.count { it.first < 66 && it.first % 2 == 1 }.toLong() }
        val reachablePlots197Steps = possibleGrids.map { it.count { it.first < 197 && it.first % 2 == 0 }.toLong() to it.count { it.first < 197 && it.first % 2 == 1 }.toLong() }

        val steps = 26501365
        val fullGridsAxis: Long = ((steps - 65)/131) - 1L
        var fullGridsQuartersEven: Long = 0
        var fullGridsQuartersOdd: Long = 0
        for (i in 1..<fullGridsAxis) {
            fullGridsQuartersOdd += i/2
            fullGridsQuartersEven += i/2 + i%2
        }

        var result: Long = reachablePlots[0].second
        for (i in 1..4) {
            result += fullGridsAxis / 2 * reachablePlots[i].first + (fullGridsAxis / 2 + 1) * reachablePlots[i].second + reachablePlots131Steps[i].first
            result += (fullGridsAxis + 1) * reachablePlots66Steps[i + 4].first
            result += fullGridsAxis * reachablePlots197Steps[i + 4].second
            result += fullGridsQuartersEven * reachablePlots[i + 4].first + fullGridsQuartersOdd * reachablePlots[i + 4].second
        }
        return result
    }

    val input = readInput("Day21")
        .mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                Point(x, y, c)
            }
        }
    println(part1(input))
    println(part2(input))
}