private data class Coordinate(val x: Int, val y: Int, val z: Int) {
    override fun toString(): String = "$x,$y,$z"
}

private class Brick(val start: Coordinate, val end: Coordinate, val supports: MutableList<Brick> = mutableListOf(), val supportedBy: MutableList<Brick> = mutableListOf()) {
    override fun toString(): String = "$start~$end"
    fun overlaps(other: Brick): Boolean =
        (start.x..end.x).any { it in other.start.x..other.end.x } &&
        (start.y..end.y).any { it in other.start.y..other.end.y }
    fun dropTo(zPos: Int): Brick =
        Brick(Coordinate(start.x, start.y, zPos), Coordinate(end.x, end.y, end.z - start.z + zPos))
}

fun main() {
    fun part1(input: List<Brick>): Int =
        input.count{ brick -> brick.supports.all { supportedBrick -> supportedBrick.supportedBy.size >= 2 } }


    fun part2(input: List<Brick>): Int =
        input.sumOf { brick ->
            val toDisintegrate = mutableListOf(brick)
            val disintegrated = mutableListOf<Brick>()
            while (toDisintegrate.isNotEmpty()) {
                val next = toDisintegrate.removeFirst()
                disintegrated.add(next)
                for (candidate in next.supports) {
                    if (candidate.supportedBy.all { it in disintegrated })
                        toDisintegrate.add(candidate)
                }
            }
            disintegrated.size - 1
        }

    val input = readInput("Day22")
        .map { s ->
            s.split('~')
                .map { it.split(',').map(String::toInt) }
                .map { Coordinate(it[0], it[1], it[2]) }
                .run { Brick(this[0], this[1]) }
        }
        .sortedBy { it.start.z }
        .fold(listOf<Brick>()) { settled, brick ->
            val overlapping = settled.filter { it.overlaps(brick) }
            if (overlapping.isEmpty()) {
                settled.plus(brick.dropTo(1))
            } else {
                val zPos = overlapping.maxOf { it.end.z }
                val supporting = overlapping.filter { it.end.z == zPos }
                val dropped = brick.dropTo(zPos + 1)
                for (support in supporting) {
                    dropped.supportedBy.add(support)
                    support.supports.add(dropped)
                }
                settled.plus(dropped)
            }
        }

    println(part1(input))
    println(part2(input))
}