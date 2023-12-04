fun main() {
    fun part1(input: List<String>): Int {
        val res = input
            .map { it.split(':').last().split('|') }
            .map { it[0].split(' ').filter { it.any { it.isDigit() } }.map { it.trim(' ').toInt() } to it[1].split(' ').filter { it.any { it.isDigit() } }.map { it.trim(' ').toInt() } }
            .map { it.first.intersect(it.second) }
            .filter { it.isNotEmpty() }
            .map { it.drop(1).fold(1) { acc, _ -> acc * 2 } }
            .sum()
        return res
    }

    fun part2(input: List<String>): Int {
        val res = input
            .map { it.split(':').last().split('|') }
            .map { it[0].split(' ').filter { it.any { it.isDigit() } }.map { it.trim(' ').toInt() } to it[1].split(' ').filter { it.any { it.isDigit() } }.map { it.trim(' ').toInt() } }
            .map { 1 to it.first.intersect(it.second).size }
            .toMutableList()

        var i = 0
        for (r in res) {
            var j = 1
            while (j <= r.second) {
                val old = res[i + j]
                res[i + j] = old.first + r.first to old.second
                j++
            }
            i++
        }
        return res.map { it.first }.sum()
    }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}