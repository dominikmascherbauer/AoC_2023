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
        val frontier = mutableListOf(0 to input[0][1])
        val visited = mutableListOf<Pair<Int, Point<Char>>>()
        val cameFrom = mutableMapOf<Point<Char>, Point<Char>>()

        while (frontier.isNotEmpty()) {
            val next = frontier.removeFirst()
            visited.add(next)
            for (neighbor in input.getNeighbors(next.second)) {
                val lastVisited = visited.firstOrNull { it.second == neighbor }
                //if (lastVisited == null || cameFrom[next.second] == lastVisited.second) {
                    //lastVisited?.first?.add(next.first.first() + 1)
                    val lastScheduled = frontier.firstOrNull { it.second == neighbor } //?.first?.add(next.first.first() + 1)

                    if (lastVisited != null && lastVisited.first < next.first - 1) {
                        visited.remove(lastVisited)
                        frontier.add((next.first + 1) to neighbor)
                        cameFrom[neighbor] = next.second
                    } else if(lastScheduled != null && lastScheduled.first < next.first - 1) {
                        frontier.remove(lastScheduled)
                        frontier.add((next.first + 1) to neighbor)
                        cameFrom[neighbor] = next.second
                    } else if (visited.none { it.second == neighbor } && frontier.none { it.second == neighbor }) {
                        frontier.add((next.first + 1) to neighbor)
                        cameFrom[neighbor] = next.second
                    }
                //}
            }
        }

        var last = visited.first { it.second.y == input.size - 1 }
        val path = mutableListOf(last)
        while (cameFrom[last.second] != null) {
            last = visited.first { it.second == cameFrom[last.second] }
            path.add(0, last)
        }

        return path.size - 1
        // 2122 too high
    }

    fun part2(input: List<List<Point<Char>>>): Int {
        return 0
    }

    val input = readInput("Day23")
        .mapIndexed { y, s -> s.mapIndexed { x, c -> Point(x,y,c) } }
    println(part1(input))
    println(part2(input))
}