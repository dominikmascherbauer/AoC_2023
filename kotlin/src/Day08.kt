fun main() {
    fun part1(directions: List<Char>, map: Map<String, Pair<String, String>>): Int =
        generateSequence(0 to "AAA") {
            it.first + 1 to (if (directions[it.first % directions.size] == 'L') map[it.second]!!.first else map[it.second]!!.second)
        }
            .first { it.second == "ZZZ" }
            .first

    fun part2(directions: List<Char>, map: Map<String, Pair<String, String>>): Long =
        generateSequence(map.keys.filter { it.last() == 'A' }.map{ 0 to it }) { gen ->
            gen.map {
                if (it.second.last() == 'Z')
                    it
                else
                    it.first + 1 to (if (directions[it.first % directions.size] == 'L') map[it.second]!!.first else map[it.second]!!.second)
            }
        }
            .first { it.all { it.second.last() == 'Z' } }
            .map { it.first.toLong() }
            .reduce { acc, b -> lcm(acc, b) }

    val input = readInput("Day08")

    val directions = input.first().toList()
    val map = input.drop(2)
        .map { s -> Regex("[A-Z]+").findAll(s).map { it.value }.toList() }
        .filter { it.isNotEmpty() }
        .associate { it[0] to (it[1] to it[2]) }

    println(part1(directions, map))
    println(part2(directions, map))
}