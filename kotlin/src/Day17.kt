import kotlin.math.abs

fun main() {
    fun heuristic(cur: Point<Int>, goal: Point<Int>): Int =
        abs(cur.x - goal.x) + abs(cur.y - goal.y)

    fun List<List<Point<Int>>>.neighbors(cur: Point<Int>): List<Pair<Pair<Int, Int>, Point<Int>>> =
        flatMap { row -> row.filter { heuristic(cur, it) == 1 }.map { ((it.x - cur.x) to (it.y - cur.y)) to it } }

    fun cost(from: Pair<Pair<Int, Int>, Point<Int>>, cameFrom: Map<Pair<Pair<Int, Int>, Point<Int>>, Pair<Pair<Int, Int>, Point<Int>>>): Int {
        var cur = from
        var len = 0
        while (cameFrom[cur] != null) {
            len += cur.second.value
            cur = cameFrom[cur]!!
        }
        return len
    }


    fun part1(input: List<List<Point<Int>>>): Int {
        val start = input.first().first()
        val goal = input.last().last()

        val visited: MutableList<Triple<Pair<Int, Int>, Int, Point<Int>>> = mutableListOf()
        val frontier: MutableList<Triple<Pair<Int, Int>, Int, Point<Int>>> = mutableListOf(Triple(0 to 0, heuristic(start, goal), start))
        val cameFrom: MutableMap<Pair<Pair<Int, Int>, Point<Int>>, Pair<Pair<Int, Int>, Point<Int>>> = mutableMapOf()

        while (frontier.isNotEmpty()) {
            val cur = frontier.minBy { it.second }
            frontier.remove(cur)
            if (cur.third == goal) {
                visited.add(cur)
                //frontier.clear()
            } else {
                visited.add(cur)
                for (neighbor in input.neighbors(cur.third)) {
                    val stepsX: Int = if (neighbor.first.first == 0) 0 else cur.first.first + neighbor.first.first
                    val stepsY: Int = if (neighbor.first.second == 0) 0 else cur.first.second + neighbor.first.second
                    val expandedNeighbor = (stepsX to stepsY) to neighbor.second
                    if (expandedNeighbor == cameFrom[cur.first to cur.third] ||
                        abs(stepsX) == 4 ||
                        abs(stepsY) == 4)
                    {
                        // skip invalid districts
                        continue
                    }

                    val dist = cost(cur.first to cur.third, cameFrom) + neighbor.second.value + heuristic(neighbor.second, goal)

                    if (visited.any { (it.first to it.third) == expandedNeighbor  } && dist < visited.first { (it.first to it.third) == expandedNeighbor }.second ) {
                        visited.replaceAll { if ((it.first to it.third) == expandedNeighbor) Triple(it.first, dist, it.third) else it }
                        cameFrom[expandedNeighbor] = cur.first to cur.third
                    } else if (frontier.any { (it.first to it.third) == expandedNeighbor  } && dist < frontier.first { (it.first to it.third) == expandedNeighbor  }.second) {
                        frontier.replaceAll { if ((it.first to it.third) == expandedNeighbor) Triple(it.first, dist, it.third) else it }
                        cameFrom[expandedNeighbor] = cur.first to cur.third
                    } else if (visited.none { (it.first to it.third) == expandedNeighbor } && frontier.none { (it.first to it.third) == expandedNeighbor }) {
                        frontier.add(Triple(stepsX to stepsY, dist, expandedNeighbor.second))
                        cameFrom[expandedNeighbor] = cur.first to cur.third
                    }
                }
            }
        }

        return visited.filter { it.third == goal }.minOf { cost(it.first to it.third, cameFrom) }
    }

    fun part2(input: List<List<Point<Int>>>): Int {
        val start = input.first().first()
        val goal = input.last().last()

        val visited: MutableMap<Point<Int>, MutableList<Pair<Pair<Int, Int>, Int>>> = mutableMapOf()
        val frontier: MutableMap<Point<Int>, MutableList<Pair<Pair<Int, Int>, Int>>> = mutableMapOf(start to mutableListOf((0 to 0) to heuristic(start, goal)))
        val cameFrom: MutableMap<Pair<Pair<Int, Int>, Point<Int>>, Pair<Pair<Int, Int>, Point<Int>>> = mutableMapOf()

        while (frontier.isNotEmpty()) {
            val cur = frontier.minBy { it.value.minOf { it.second } }.run { key to value.minBy { it.second } }.run { Triple(second.first, second.second, first) }
            frontier[cur.third]!!.remove(cur.first to cur.second)
            if (frontier[cur.third]!!.isEmpty())
                frontier.remove(cur.third)
            visited.putIfAbsent(cur.third, mutableListOf())
            visited[cur.third]!!.add(cur.first to cur.second)
            if (cur.third == goal) {
                // do nothing here
                frontier.clear()
            } else {
                for (neighbor in input.neighbors(cur.third)) {
                    val stepsX: Int = if (neighbor.first.first == 0) 0 else cur.first.first + neighbor.first.first
                    val stepsY: Int = if (neighbor.first.second == 0) 0 else cur.first.second + neighbor.first.second
                    val expandedNeighbor = (stepsX to stepsY) to neighbor.second
                    if (expandedNeighbor == cameFrom[cur.first to cur.third] ||
                        abs(stepsX) == 11 ||
                        abs(stepsY) == 11)
                    {
                        // skip invalid districts
                        continue
                    }
                    if (cur.first != 0 to 0 && abs(cur.first.first) < 4 && abs(cur.first.second) < 4 && abs(stepsX + stepsY) <= abs(cur.first.first + cur.first.second) ) {
                        continue
                    }

                    val dist = cost(cur.first to cur.third, cameFrom) + neighbor.second.value + heuristic(neighbor.second, goal)

                    if (visited.containsKey(expandedNeighbor.second) && dist < (visited[expandedNeighbor.second]!!.firstOrNull { it.first == expandedNeighbor.first }?.second ?: -1)) {
                        visited[expandedNeighbor.second]!!.replaceAll { if (it.first == expandedNeighbor.first) it.first to dist else it }
                        cameFrom[expandedNeighbor] = cur.first to cur.third
                    } else if (frontier.containsKey(expandedNeighbor.second) && dist < (frontier[expandedNeighbor.second]!!.firstOrNull { it.first == expandedNeighbor.first }?.second ?: -1)) {
                        frontier[expandedNeighbor.second]!!.replaceAll { if (it.first == expandedNeighbor.first) it.first to dist else it }
                        cameFrom[expandedNeighbor] = cur.first to cur.third
                    } else if ((!visited.containsKey(expandedNeighbor.second) || visited[expandedNeighbor.second]!!.none { it.first == expandedNeighbor.first }) &&
                        (!frontier.containsKey(expandedNeighbor.second) || frontier[expandedNeighbor.second]!!.none { it.first == expandedNeighbor.first })) {

                        frontier.putIfAbsent(expandedNeighbor.second, mutableListOf())
                        frontier[expandedNeighbor.second]!!.add(expandedNeighbor.first to dist)
                        cameFrom[expandedNeighbor] = cur.first to cur.third
                    }
                }
            }
        }

        return visited[goal]!!.minOf { cost(it.first to goal, cameFrom) }
    }

    val input: List<List<Point<Int>>> = readInput("Day17")
        .mapIndexed{ y, s ->
            s.mapIndexed{ x, c -> Point(x, y, "$c".toInt()) }
        }
    println(part1(input))
    println(part2(input))
}