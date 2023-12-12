fun main() {
    val cache = mutableMapOf<Pair<Char, Triple<Int,Int,Int>>, Long>()

    fun String.permutationsV2(nWorking: Int, nBroken: Int, nums: Sequence<Int>, currentBroken: Int = 0, replaced: Char = '?'): Long {
        return if (nWorking < 0 || nBroken < 0)
            0L
        else if (nums.firstOrNull() != null && (nums.first() < currentBroken || (replaced == '.' && currentBroken != 0 && nums.first() != currentBroken)))
            0L
        else if (nWorking == 0 && nBroken == 0 && this.isEmpty())
            1L
        else if (cache.containsKey(replaced to Triple(this.length, nWorking, nBroken)))
            cache[replaced to Triple(this.length,nWorking,nBroken)]!!
        else {
            val first = this.first()
            val rest = this.drop(1)
            val (newNums, newCurrentBroken) = if (replaced == '.' && currentBroken != 0)
                nums.drop(1) to 0
            else
                nums to currentBroken
            cache[replaced to Triple(this.length, nWorking, nBroken)] = when (first) {
                '.' -> rest.permutationsV2(nWorking, nBroken, newNums, newCurrentBroken, '.')
                '#' -> rest.permutationsV2(nWorking, nBroken, newNums, newCurrentBroken + 1, '#')
                else -> rest.permutationsV2(nWorking - 1, nBroken, newNums, newCurrentBroken, '.') +
                        rest.permutationsV2(nWorking, nBroken - 1, newNums, newCurrentBroken + 1, '#')
            }
            cache[replaced to Triple(this.length, nWorking, nBroken)]!!
        }
    }

    fun part1(input: List<String>): Long {
        val res = input.map { row ->
            val nums = Regex("[0-9]+").findAll(row).map { it.value.toInt() }
            val nKnownBroken = row.count { it == '#' }
            val nUnknown = row.count { it == '?' }
            val nBroken = nums.sum()
            val nUnknownBroken = nBroken - nKnownBroken

            assert(nUnknown >= nUnknownBroken)

            cache.clear()
            val x = row.split(' ')[0].permutationsV2(nUnknown - nUnknownBroken, nUnknownBroken, nums)
            x
        }

        return res.sumOf { it }
    }

    fun part2(input: List<String>): Long {
        val res = input
            .map { it.split(' ')[0].plus('?').repeat(5).dropLast(1).plus(' ').plus(it.split(' ')[1].plus(',').repeat(5)) }
            .mapIndexed { i, row ->
                val nums = Regex("[0-9]+").findAll(row).map { it.value.toInt() }
                val nKnownBroken = row.count { it == '#' }
                val nUnknown = row.count { it == '?' }
                val nBroken = nums.sum()
                val nUnknownBroken = nBroken - nKnownBroken

                assert(nUnknown >= nUnknownBroken)

                cache.clear()
                val x = row.split(' ')[0].permutationsV2(nUnknown - nUnknownBroken,nUnknownBroken,nums)
                //println("${i + 1}: $x from $row")
                x
            }

        return res.sumOf { it }
    }

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}