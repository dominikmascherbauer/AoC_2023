fun main() {
    // somehow gravity always pulls to the left, so we have to rotate the grid accordingly before, lol xd
    fun List<String>.applyGravity(): List<String> =
        map { s ->
            s.split("#")
                .joinToString("#") {
                    it.toList().sortedDescending().joinToString("")
                }
        }

    // gravity pulls rocks to the left but weight pulls downwards,  ... weird <insert jeremy clarkson "Anyway">, lets just rotate accordingly before
    fun List<String>.getWeight(): Int =
        mapIndexed { i, s -> i to s }
            .sumOf { (i, s) -> s.count { it == 'O' } * (size - i) }

    fun part1(input: List<String>): Int =
        input.rotateLeft()
            .applyGravity()
            .rotateRight()
            .getWeight()

    fun part2(input: List<String>): Int =
        generateSequence(input) {
            it.rotateLeft().applyGravity()   // roll north
                .rotateRight().applyGravity()   // roll west
                .rotateRight().applyGravity()   // roll south
                .rotateRight().applyGravity()   // roll east
                .rotateLeft().rotateLeft()      // rotate back to initial position for weight calculation
        }.runningFold(listOf<List<String>>()) { acc, grid -> acc.plus(listOf(grid)) }
            .drop(1)    // ditch empty list in the beginning
            .first { it.dropLast(1).contains(it.last()) }
            .run { indexOfFirst { it == last() } to dropLast(1) }
            // get grid iteration 1_000_000_000 (list starts with iteration 0 = input)
            .run { second[first + ((1_000_000_000 - first) % (second.size - first))] }
            .getWeight()


    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}