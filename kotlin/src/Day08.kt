fun main() {
    fun part1(directions: List<Char>, map: Map<String, Pair<String, String>>): Int {
        var cur = "AAA"
        var steps = 0
        while (cur != "ZZZ") {
            cur = if (directions[steps%directions.size] == 'L')
                map[cur]!!.first
            else
                map[cur]!!.second
            steps++
        }
        return steps
    }

    fun gcd(a: Long, b: Long): Long {
        var curA = a
        var curB = b
        while (curB > 0)
        {
            val temp = curB
            curB = curA % curB
            curA = temp
        }
        return curA
    }

    fun lcm(a: Long, b: Long): Long {
        return a * (b / gcd(a, b))
    }

    fun part2(directions: List<Char>, map: Map<String, Pair<String, String>>): Long {
        val res = map.filter{ it.key.last() == 'A' }.map {
            var cur = it.key
            var steps = 0
            while (cur.last() != 'Z') {
                cur = if (directions[steps%directions.size] == 'L')
                    map[cur]!!.first
                else
                    map[cur]!!.second
                steps++
            }
            steps.toLong()
        }

        var result = res[0]
        for (x in res.drop(1)) {
            result = lcm(result, x)
        }

        return result
    }

    val input = readInput("Day08")

    val directions = input.first().toList()
    val map = input.drop(2).map { s -> Regex("[A-Z]+").findAll(s).map { it.value }.toList() }.filter { it.isNotEmpty() }.associate { it[0] to (it[1] to it[2]) }

    println(part1(directions, map))
    println(part2(directions, map))
}