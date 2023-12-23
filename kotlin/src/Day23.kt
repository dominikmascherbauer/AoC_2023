import java.util.Stack

fun main() {
    fun List<List<Point<Char>>>.getNeighbors(p: Point<Char>, isSlippery: Boolean = true): List<Point<Char>> =
        if (isSlippery)
            when(p.value) {
                '<' -> listOf(this[p.y][p.x - 1])
                '>' -> listOf(this[p.y][p.x + 1])
                'v' -> listOf(this[p.y + 1][p.x])
                '^' -> listOf(this[p.y - 1][p.x])
                else -> listOf(
                    p.y to p.x + 1,
                    p.y to p.x - 1,
                    p.y + 1 to p.x,
                    p.y - 1 to p.x
                ).filter { (y, x) -> x >= 0 && y >= 0 && y < size && x < get(0).size }
                    .map { (y, x) -> this[y][x] }
                    .filter {
                        it.value == '.' || (it.x == p.x + 1 && it.value != '<' && it.value != '#') || (it.x == p.x - 1 && it.value != '>' && it.value != '#')
                                || (it.y == p.y + 1 && it.value != '^' && it.value != '#') || (it.y == p.y - 1 && it.value != 'v' && it.value != '#')
                    }
            }
        else listOf(
            p.y to p.x + 1,
            p.y to p.x - 1,
            p.y + 1 to p.x,
            p.y - 1 to p.x
        ).filter { (y, x) -> x >= 0 && y >= 0 && y < size && x < get(0).size }
            .map { (y, x) -> this[y][x] }
            .filter {it.value != '#' }

    fun part1(input: List<List<Point<Char>>>): Int {
        val stack: Stack<Point<Char>> = Stack<Point<Char>>()
        stack.push(input[0][1])
        val visited = mutableListOf<Point<Char>>()
        val cameFrom = mutableMapOf<Point<Char>, Point<Char>>()

        val paths = mutableListOf<List<Point<Char>>>()

        while (stack.isNotEmpty()) {
            val next = stack.pop()
            visited.add(next)
            if (next.y == input.size - 1) {
                paths.add(visited.toList())
                if (stack.isNotEmpty()) {
                    val unfinishedPath = visited.takeWhile { it != cameFrom[stack.peek()] }
                    visited.clear()
                    visited.addAll(unfinishedPath)
                    visited.add(cameFrom[stack.peek()]!!)
                }
            } else {
                for (neighbor in input.getNeighbors(next)) {
                    if (visited.none { it == neighbor } && stack.none { it == neighbor }) {
                        stack.push(neighbor)
                        cameFrom[neighbor] = next
                    }
                }
            }
        }
        return paths.maxOf { it.size - 1 }
    }

    fun List<List<Point<Char>>>.getNext(cur: Point<Char>, prev: Point<Char>): List<Point<Char>> {
        return listOf(
            cur.y to cur.x + 1,
            cur.y to cur.x - 1,
            cur.y + 1 to cur.x,
            cur.y - 1 to cur.x
        ).filter { (y, x) -> x >= 0 && y >= 0 && y < size && x < get(0).size }
            .map { (y, x) -> this[y][x] }
            .filter {(it.x != prev.x || it.y != prev.y) && it.value != '#' }
    }

    fun List<List<Point<Char>>>.findNextJunctions(p: Point<Char>, next: List<Point<Char>>): List<Triple<Int, Point<Char>, Point<Char>>> {
        return next.map {
            var prev = p
            var cur = it
            var steps = 1
            while (getNext(cur, prev).size == 1) {
                val tmp = cur
                cur = getNext(cur, prev).first()
                prev = tmp
                steps++
            }
            Triple(steps, prev, cur)
        }
    }

    fun part2(input: List<List<Point<Char>>>): Int {
        val graph: MutableMap<Point<Char>, MutableSet<Pair<Int, Point<Char>>>> = mutableMapOf()
        val visited: MutableList<Point<Char>> = mutableListOf()
        val frontier: MutableList<Pair<Point<Char>, List<Point<Char>>>> = mutableListOf(input[0][1] to listOf(input[1][1]))

        while (frontier.isNotEmpty()) {
            val next = frontier.removeFirst()
            visited.add(next.first)
            val nextJunctions = input.findNextJunctions(next.first, next.second)
            for (junction in nextJunctions) {
                graph.putIfAbsent(next.first, mutableSetOf())
                graph.putIfAbsent(junction.third, mutableSetOf())
                graph[next.first]!!.add(junction.first to junction.third)
                graph[junction.third]!!.add(junction.first to next.first)
                if (junction.third !in visited && frontier.none { it.first == junction.third })
                    frontier.add(junction.third to input.getNext(junction.third, junction.second))
            }
        }

        visited.clear()
        val frontier2: MutableList<Triple<Point<Char>, Int, List<Point<Char>>>> = mutableListOf(Triple(input[0][1], 0, listOf()))
        val paths = mutableListOf<Int>()

        while (frontier2.isNotEmpty()) {
            val next = frontier2.removeFirst()
            if(next.first.y == input.size - 1) {
                paths.add(next.second)
            } else {
                for (neighbor in graph[next.first]!!) {
                    if (neighbor.second !in next.third && neighbor.second != next.first) {
                        frontier2.add(Triple(neighbor.second, next.second + neighbor.first, next.third.plus(next.first)))
                    }
                }
            }
        }

        return paths.max()
    }

    val input = readInput("Day23")
        .mapIndexed { y, s -> s.mapIndexed { x, c -> Point(x,y,c) } }
    println(part1(input))
    println(part2(input))
}