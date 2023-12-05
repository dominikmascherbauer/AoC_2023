import kotlin.math.min

fun List<String>.parseMap(name: String) = dropWhile { it != name }
    .drop(1)
    .takeWhile { it.isNotEmpty() }
    .fold(mutableListOf<Pair<LongRange, LongRange>>()) { acc, s ->
        val mapping = s.split(' ', limit = 3).map { it.toLong() }
        acc.add(mapping[1]..<(mapping[1] + mapping[2]) to mapping[0]..<(mapping[0] + mapping[2]))
        acc
    }

fun LongRange.mapSeeds(map: List<Pair<LongRange, LongRange>>): List<LongRange> {
    var cur = first
    val res = mutableListOf<LongRange>()
    while (cur <= last) {
        val x = map.firstOrNull { cur in it.first }
        if (x == null) {
            val start = cur
            cur = min((last + 1), map.filter { it.first.first - cur > 0 }.minOfOrNull { it.first.first } ?: (last + 1))
            res.add(start..<cur)
        } else if (last <= x.first.last) {
            res.add((x.second.first + (cur - x.first.first))..((x.second.first + (last - x.first.first))))
            cur = last + 1
        } else {
            res.add((x.second.first + (cur - x.first.first))..x.second.last)
            cur = x.first.last + 1
        }
    }
    return res
}

fun main() {
    fun part1(input: List<String>): Long {
        val soil = input.parseMap("seed-to-soil map:")
        val fertilizer = input.parseMap("soil-to-fertilizer map:")
        val water = input.parseMap("fertilizer-to-water map:")
        val light = input.parseMap("water-to-light map:")
        val temperature = input.parseMap("light-to-temperature map:")
        val humidity = input.parseMap("temperature-to-humidity map:")
        val location = input.parseMap("humidity-to-location map:")

        val res = input[0].split(' ').drop(1)
            .map { it.toLong() }
            .map { num -> soil.firstOrNull { num in it.first }?.run { second.first + (num - first.first) } ?: num }
            .map { num -> fertilizer.firstOrNull { num in it.first }?.run { second.first + (num - first.first) } ?: num }
            .map { num -> water.firstOrNull { num in it.first }?.run { second.first + (num - first.first) } ?: num }
            .map { num -> light.firstOrNull { num in it.first }?.run { second.first + (num - first.first) } ?: num }
            .map { num -> temperature.firstOrNull { num in it.first }?.run { second.first + (num - first.first) } ?: num }
            .map { num -> humidity.firstOrNull { num in it.first }?.run { second.first + (num - first.first) } ?: num }
            .map { num -> location.firstOrNull { num in it.first }?.run { second.first + (num - first.first) } ?: num }

        //910845529
        return res.min()
    }

    fun part2(input: List<String>): Long {
        val soil = input.parseMap("seed-to-soil map:")
        val fertilizer = input.parseMap("soil-to-fertilizer map:")
        val water = input.parseMap("fertilizer-to-water map:")
        val light = input.parseMap("water-to-light map:")
        val temperature = input.parseMap("light-to-temperature map:")
        val humidity = input.parseMap("temperature-to-humidity map:")
        val location = input.parseMap("humidity-to-location map:")

        val res = input[0].split(' ').drop(1)
            .map { it.toLong() }
            .fold(mutableMapOf<Long, Long>()) { acc, l ->
                val x = acc.firstNotNullOfOrNull { if (it.value == -1L) it.key else null }
                if (x == null) acc[l] = -1L else acc[x] = l
                acc
            }
            .map { it.key..<it.key+it.value }

        val res2 = res
            .flatMap { it.mapSeeds(soil) }
            .flatMap { it.mapSeeds(fertilizer) }
            .flatMap { it.mapSeeds(water) }
            .flatMap { it.mapSeeds(light) }
            .flatMap { it.mapSeeds(temperature) }
            .flatMap { it.mapSeeds(humidity) }
            .flatMap { it.mapSeeds(location) }

        //77435348
        return res2.map{ it.first }.min()
    }

    val input = readInput("Day05")
    /*
    val seeds: List<Long> = input[0]
        .split(' ')
        .drop(1)
        .map { it.toLong() }
    val mapCascade: List<List<Pair<LongRange, LongRange>>> = input
        .filter { ':' in it }
        .drop(1)
        .map { it }
    */
    println(part1(input))
    println(part2(input))
}