fun main() {
    fun List<String>.transpose(): List<String> {
        val cols = get(0).length
        val rows = size
        return generateSequence (0) { it+1 }
            .take(cols)
            .map { j ->
                generateSequence(0) { it + 1 }
                    .take(rows)
                    .map { i ->
                        this[i][j]
                    }
                    .fold("") { acc, c -> acc + c }
            }
            .toList()
    }


    fun part1(input: List<String>): Int {
        val res = listOf("").plus(input)
            .mapIndexed { i, s -> i to s }
            .filter { it.second.isEmpty() }
            .map { (i, _) ->
                input.drop(i)
                    .takeWhile { it.isNotEmpty() }
            }
            .map { grid ->
                val r = grid.mapIndexed { i, row -> i to row }
                    .filter { (i, _) ->
                        var j = 0
                        var ret = i >= 0 && i < grid.size - 1
                        while (i - j >= 0 && i + j + 1 < grid.size) {
                            if (grid[i - j] != grid[i + j + 1]) {
                                ret = false
                                break
                            }
                            j++
                        }
                        ret
                    }
                    .map { it.first + 1 }
                    .firstOrNull() ?: 0

                val transposed = grid.transpose()
                val c = transposed
                    .mapIndexed { i, col -> i to col }
                    .filter { (i, _) ->
                        var j = 0
                        var ret = i >= 0 && i < transposed.size - 1
                        while (i - j >= 0 && i + j + 1 < transposed.size) {
                            if (transposed[i - j] != transposed[i + j + 1]) {
                                ret = false
                                break
                            }
                            j++
                        }
                        ret
                    }
                    .map { it.first + 1 }
                    .firstOrNull() ?: 0

                100*r + c
            }
        return res.sum()
    }

    fun part2(input: List<String>): Int {
        val res = listOf("").plus(input)
            .mapIndexed { i, s -> i to s }
            .filter { it.second.isEmpty() }
            .map { (i, _) ->
                input.drop(i)
                    .takeWhile { it.isNotEmpty() }
            }
            .map { grid ->
                val transposed = grid.transpose()
                val diff = grid.mapIndexed { i, row ->
                    var j = 0
                    var diff = 0
                    var lastDiffRow = 0
                    while (i - j >= 0 && i + j + 1 < grid.size && diff <= 1) {
                        if (grid[i - j] != grid[i + j + 1]) {
                            diff += grid[i - j].filterIndexed { k, c -> c != grid[i + j + 1][k] }.length
                            lastDiffRow = i - j
                        }
                        j++
                    }
                    if (diff == 1) {
                        100 * (i + 1)
                        //lastDiffRow to grid[lastDiffRow].mapIndexed { k, c -> k to (c != grid[lastDiffRow + 1][k]) }.filter { it.second }.first().first
                    } else {
                        null
                    }
                }.firstOrNull { it != null } ?:
                    transposed.mapIndexed { i, col ->
                        var j = 0
                        var diff = 0
                        var lastDiffCol = 0
                        while (i - j >= 0 && i + j + 1 < transposed.size && diff <= 1) {
                            if (transposed[i - j] != transposed[i + j + 1]) {
                                diff += transposed[i - j].filterIndexed { k, c -> c != transposed[i + j + 1][k] }.length
                                lastDiffCol = i - j
                            }
                            j++
                        }
                        if (diff == 1) {
                            i + 1
                            //transposed[lastDiffCol].mapIndexed { k, c -> i to (c != transposed[lastDiffCol + 1][k]) }.filter { it.second }.first().first // to lastDiffCol
                        } else {
                            null
                        }
                    }.first { it != null }

                diff!!
                /*
                val newGrid: List<String> = grid.mapIndexed { i, row ->
                    if (i == diff!!.first)
                        row.mapIndexed { j, c ->
                            if (j == diff.second)
                                if (c == '.')
                                    '#'
                                else
                                    '.'
                            else
                                c
                        }
                            .fold("") { acc, c -> acc + c }
                    else
                        row
                }
                val newTransposed = newGrid.transpose()

                val r = newGrid.mapIndexed { i, row -> i to row }
                    .filter { (i, _) ->
                        var j = 0
                        var ret = i >= 0 && i < newGrid.size - 1
                        while (i - j >= 0 && i + j + 1 < newGrid.size) {
                            if (newGrid[i - j] != newGrid[i + j + 1]) {
                                ret = false
                                break
                            }
                            j++
                        }
                        ret
                    }
                    .map { it.first + 1 }
                    .firstOrNull() ?: 0

                if (r != 0)
                    r * 100
                else {
                    val c = newTransposed
                        .mapIndexed { i, col -> i to col }
                        .filter { (i, _) ->
                            var j = 0
                            var ret = i >= 0 && i < newTransposed.size - 1
                            while (i - j >= 0 && i + j + 1 < newTransposed.size) {
                                if (newTransposed[i - j] != newTransposed[i + j + 1]) {
                                    ret = false
                                    break
                                }
                                j++
                            }
                            ret
                        }
                        .map { it.first + 1 }
                        .firstOrNull() ?: 0
                    c
                }

                 */
            }
        return res.sum()
    }

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
