fun main() {
    fun part1(input: List<String>): Int {
        var lastRolled = List(input[0].length) { it -> -1 }

        val res = input.mapIndexed { i, s ->
            s.mapIndexed { j, c ->
                when (c) {
                    'O' -> {
                        if (lastRolled[j] >= i) {
                            val nextRock = input.mapIndexed { i,s -> i to s }.drop(i).firstOrNull { (it.second[j] == 'O' && lastRolled[j] < it.first) || it.second[j] == '#' }
                            if (nextRock == null || nextRock.second[j] == '#')
                                '.'
                            else {
                                lastRolled = lastRolled.mapIndexed { k, l -> if (k == j) nextRock.first else l }
                                'O'
                            }
                        } else
                            'O'
                    }
                    '#' -> '#'
                    else -> {
                        val nextRock = input.mapIndexed { i,s -> i to s }.drop(i).firstOrNull { (it.second[j] == 'O' && lastRolled[j] < it.first) || it.second[j] == '#' }
                        if (nextRock == null || nextRock.second[j] == '#')
                            '.'
                        else {
                            lastRolled = lastRolled.mapIndexed { k, l -> if (k == j) nextRock.first else l }
                            'O'
                        }
                    }
                }
            }
        }

        return res.mapIndexed { i, s -> s.count { it == 'O' } * (res.size - i) }.sum()
    }

    fun part2(input: List<String>): List<String> {
        var lastRolled = List(input[0].length) { it -> -1 }
        var res = input.mapIndexed { i, s ->
            s.mapIndexed { j, c ->
                when (c) {
                    'O' -> {
                        if (lastRolled[j] >= i) {
                            val nextRock = input.mapIndexed { i,s -> i to s }.drop(i).firstOrNull { (it.second[j] == 'O' && lastRolled[j] < it.first) || it.second[j] == '#' }
                            if (nextRock == null || nextRock.second[j] == '#')
                                '.'
                            else {
                                lastRolled = lastRolled.mapIndexed { k, l -> if (k == j) nextRock.first else l }
                                'O'
                            }
                        } else
                            'O'
                    }
                    '#' -> '#'
                    else -> {
                        val nextRock = input.mapIndexed { i,s -> i to s }.drop(i).firstOrNull { (it.second[j] == 'O' && lastRolled[j] < it.first) || it.second[j] == '#' }
                        if (nextRock == null || nextRock.second[j] == '#')
                            '.'
                        else {
                            lastRolled = lastRolled.mapIndexed { k, l -> if (k == j) nextRock.first else l }
                            'O'
                        }
                    }
                }
            }
        }

        //res.forEach { println(it.joinToString().replace(", ", "")) }
        //println()

        lastRolled = List(input.size) { it -> -1 }
        res = res.mapIndexed { i, s ->
            s.mapIndexed { j, c ->
                when (c) {
                    'O' -> {
                        if (lastRolled[i] >= j) {
                            val nextRock = s.mapIndexed { k, ch -> k to ch }.drop(j).firstOrNull { (it.second == 'O' && lastRolled[i] < it.first) || it.second == '#' }
                            if (nextRock == null || nextRock.second == '#')
                                '.'
                            else {
                                lastRolled = lastRolled.mapIndexed { k, l -> if (k == i) nextRock.first else l }
                                'O'
                            }
                        } else
                            'O'
                    }
                    '#' -> '#'
                    else -> {
                        val nextRock = s.mapIndexed { k, ch -> k to ch }.drop(j).firstOrNull { (it.second == 'O' && lastRolled[i] < it.first) || it.second == '#' }
                        if (nextRock == null || nextRock.second == '#')
                            '.'
                        else {
                            lastRolled = lastRolled.mapIndexed { k, l -> if (k == i) nextRock.first else l }
                            'O'
                        }
                    }
                }
            }
        }

        //res.forEach { println(it.joinToString().replace(", ", "")) }
        //println()

        lastRolled = List(input[0].length) { it -> -1 }
        res = res.reversed()
        res = res.mapIndexed { i, s ->
            s.mapIndexed { j, c ->
                when (c) {
                    'O' -> {
                        if (lastRolled[j] >= i) {
                            val nextRock = res.mapIndexed { i,s -> i to s }.drop(i).firstOrNull { (it.second[j] == 'O' && lastRolled[j] < it.first) || it.second[j] == '#' }
                            if (nextRock == null || nextRock.second[j] == '#')
                                '.'
                            else {
                                lastRolled = lastRolled.mapIndexed { k, l -> if (k == j) nextRock.first else l }
                                'O'
                            }
                        } else
                            'O'
                    }
                    '#' -> '#'
                    else -> {
                        val nextRock = res.mapIndexed { i,s -> i to s }.drop(i).firstOrNull { (it.second[j] == 'O' && lastRolled[j] < it.first) || it.second[j] == '#' }
                        if (nextRock == null || nextRock.second[j] == '#')
                            '.'
                        else {
                            lastRolled = lastRolled.mapIndexed { k, l -> if (k == j) nextRock.first else l }
                            'O'
                        }
                    }
                }
            }
        }
        res = res.reversed()

        //res.forEach { println(it.joinToString().replace(", ", "")) }
        //println()

        lastRolled = List(input.size) { it -> -1 }
        res = res.mapIndexed { i, s ->
            val r = s.reversed()
            r.mapIndexed { j, c ->
                when (c) {
                    'O' -> {
                        if (lastRolled[i] >= j) {
                            val nextRock = r.mapIndexed { k, ch -> k to ch }.drop(j).firstOrNull { (it.second == 'O' && lastRolled[i] < it.first) || it.second == '#' }
                            if (nextRock == null || nextRock.second == '#')
                                '.'
                            else {
                                lastRolled = lastRolled.mapIndexed { k, l -> if (k == i) nextRock.first else l }
                                'O'
                            }
                        } else
                            'O'
                    }
                    '#' -> '#'
                    else -> {
                        val nextRock = r.mapIndexed { k, ch -> k to ch }.drop(j).firstOrNull { (it.second == 'O' && lastRolled[i] < it.first) || it.second == '#' }
                        if (nextRock == null || nextRock.second == '#')
                            '.'
                        else {
                            lastRolled = lastRolled.mapIndexed { k, l -> if (k == i) nextRock.first else l }
                            'O'
                        }
                    }
                }
            }.reversed()
        }

        return res.map { it.joinToString().replace(", ", "") }
    }

    val input = readInput("Day14")
    println(part1(input))
    //println(part2(input))

    var l: Long = 0
    var res = part2(input)
    val results = mutableListOf<Pair<Int, List<String>>>()
    var loopAt = 0
    while (l < 10000) {
        //println("After ${l+2} iterations:")
        val weight = res.mapIndexed { i, s -> s.count { it == 'O' } * (res.size - i) }.sum()
        val candidates = results.filter { it.first == weight }
        if (candidates.isNotEmpty()) {
            val equal = candidates.filter { it.second.zip(res).all { (a, b) -> a == b } }
            if (equal.isNotEmpty()) {
                println("found a loop")
                loopAt = results.indexOf(equal.first())
                break
            }
        }
        results.add(weight to res)
        res = part2(res)
        l++
    }

    println("loops from $loopAt for ${results.size - loopAt} iterations\n")

    val index = loopAt + (1_000_000_000 - loopAt - 1) % (results.size - loopAt)
    println("after 1_000_000_000 iterations: (Using result of iteration $index)")

    //results[index].second.forEach { println(it) }
    println(results[index].first)
}