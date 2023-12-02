val maxCubes = mapOf(
    "red" to 12,
    "green" to 13,
    "blue" to 14
)

fun String.parseDigit() = this.dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }.toInt()
fun String.parseColor() = this.dropWhile { !it.isLetter() }.takeWhile { it.isLetter() }
fun List<String>.parseGames() =
    this.map { l ->
        l.drop(l.indexOf(':'))
            .split(';')
            .flatMap { it.split(',') }
            .map { it.parseColor() to it.parseDigit() }
    }

fun main() {
    fun part1(input: List<String>): Int = input
        .parseGames()
        .mapIndexed { i, l -> i+1 to l }
        .filter { game -> game.second.all { it.second <= maxCubes.getValue(it.first) } }
        .sumOf { it.first }

    fun part2(input: List<String>): Int = input
        .parseGames()
        .sumOf { game ->
            game.groupBy({ it.first }) { it.second }
                .values
                .map { it.max() }
                .reduce(Int::times)
        }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}