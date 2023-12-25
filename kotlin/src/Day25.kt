private data class Component(val name: String) {
    val connected: MutableList<Component> = mutableListOf()
}

fun main() {
    fun part1(input: List<String>): Int =
        input
            .fold(listOf<Component>()) { acc, s ->
                val names = Regex("[a-z]+").findAll(s).map { it.value }
                val newAcc = acc.plus(names.filter { name -> acc.none { name == it.name } }.map { name -> Component(name) })
                val components = names.map { name -> newAcc.first { name == it.name } }
                components.first().connected.addAll(components.drop(1))
                components.drop(1).forEach { it.connected.add(components.first()) }
                newAcc
            }
            .flatMap { comp ->
                comp.connected.map { if (comp.name < it.name) comp to it else it to comp }
            }
            .toSet()
            .map { (start, goal) ->
                val visited = mutableListOf<Component>()
                val frontier = mutableListOf(0 to start)
                var pathLen = 0
                while (frontier.isNotEmpty()) {
                    val (i, next) = frontier.removeFirst()
                    visited.add(next)
                    if (next == goal) {
                        frontier.clear()
                        pathLen = i
                    } else {
                        next.connected
                            .filter { it !in visited }
                            .filter { it !in frontier.map { f -> f.second } }
                            .filter { next != start || it != goal }   // remove edge between a pair of vertices
                            .forEach { frontier.add(i+1 to it) }
                    }
                }
                pathLen to (start to goal)
            }
            .sortedByDescending { it.first }
            .take(3)
            .map { it.second }
            .onEach { it.first.connected.remove(it.second); it.second.connected.remove(it.first) }
            .first()
            .run {
                listOf(first, second).map { start ->
                    val visited = mutableListOf<Component>()
                    val frontier = mutableListOf(start)
                    while (frontier.isNotEmpty()) {
                        frontier.removeFirst()
                            .apply { visited.add(this) }
                            .connected
                            .filter { it !in visited }
                            .filter { it !in frontier }
                            .forEach { frontier.add(it) }
                    }
                    visited.size
                }.reduce { acc, i -> acc*i }
            }

    val input = readInput("Day25")
    println(part1(input))
    // part2: pushed the big red button to supply all 49 stars and finally create snow again
}