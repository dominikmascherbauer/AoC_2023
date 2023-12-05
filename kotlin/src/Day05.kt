fun List<String>.parseMapAt(index: Int) = drop(index)
    .takeWhile { it.isNotEmpty() }
    .map { it.split(' ').map(String::toLong) }
    .associate { it[1]..<it[1] + it[2] to it[0]..<it[0] + it[2] }

fun Map<LongRange, LongRange>.mapSeeds(seeds: LongRange): List<LongRange> =
    filter { seeds.first in it.key }
        .map {
            if (seeds.last <= it.key.last)
                listOf(it.value.first + (seeds.first - it.key.first)..it.value.first + (seeds.last - it.key.first))
            else
                listOf(it.value.first + (seeds.first - it.key.first)..it.value.last)
                    .plus(mapSeeds(it.key.last+1..seeds.last))
        }
        .firstOrNull()
        // add an imaginary range that does a one to one mapping
        ?: plus(listOf(seeds.first..<
                filter { it.key.first - seeds.first > 0 }
                    .map { it.key.first }
                    .plus(seeds.last+1)
                    .min()
                )
                .associateWith { it }
            )
            .mapSeeds(seeds)


fun main() {
    fun part1(seeds: List<Long>, mapCascade: List<Map<LongRange, LongRange>>): Long = seeds
        .minOf { seed ->
            mapCascade.fold(seed) { acc, map ->
                map.filter { acc in it.key }
                    .map { acc + (it.value.first - it.key.first) }
                    .firstOrNull()
                    ?: acc
            }
        }

    fun part2(seeds: List<Long>, mapCascade: List<Map<LongRange, LongRange>>): Long = seeds
        .mapIndexed { i, seed -> i to seed }
        .filter { it.first % 2 == 0 }
        .map { it.second..<it.second + seeds[it.first+1] }
        .flatMap { seedRange ->
            mapCascade.fold(listOf(seedRange)) { acc, map ->
                acc.flatMap { map.mapSeeds(it) }
            }
        }
        .minOf { it.first }


    val input = readInput("Day05")

    val seeds: List<Long> = input[0]
        .split(' ')
        .drop(1)
        .map { it.toLong() }
    val mapCascade: List<Map<LongRange, LongRange>> = input
        .mapIndexed { i, s -> i to s }
        .filter { ':' in it.second }
        .drop(1)
        .map { input.parseMapAt(it.first + 1) }

    println(part1(seeds, mapCascade))
    println(part2(seeds, mapCascade))
}