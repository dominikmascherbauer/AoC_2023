@OptIn(ExperimentalStdlibApi::class)
fun main() {
    /*
    fun List<Point<String>>.countSwitchesBeforeX(gridX: Map<Int, List<Point<String>>>, x: Int): Int {
        val before = filter { it.x < x }.groupBy { it.value }
        var count = before.count { it.value.size == 1 }
        count += before.filter { it.value.size != 1 }.count { e -> gridX[e.value.first().x]!!.first { abs(it.y - e.value.first().y) == 1 } != gridX[e.value.last().x]!!.first { abs(it.y - e.value.last().y) == 1 }  }
        return count
    }

    fun List<Point<String>>.countSwitchesBeforeY(gridY: Map<Int, List<Point<String>>>, y: Int): Int {
        return 0
    }

     */

    fun part1(input: List<Triple<Char, Int, String>>): Int {
        val (gridY, gridX) = input.fold(Point(0,0,"") to listOf<Point<String>>()) { (last, grid), trench ->
                when (trench.first) {
                    'R' -> Point(last.x + trench.second, last.y,"") to grid.plus(List(trench.second) { Point(last.x + it + 1, last.y, trench.third) })
                    'L' -> Point(last.x - trench.second, last.y,"") to grid.plus(List(trench.second) { Point(last.x - it - 1, last.y, trench.third) })
                    'D' -> Point(last.x, last.y + trench.second,"") to grid.plus(List(trench.second) { Point(last.x, last.y + it + 1, trench.third) })
                    'U' -> Point(last.x, last.y - trench.second,"") to grid.plus(List(trench.second) { Point(last.x, last.y - it - 1, trench.third) })
                    else ->  last to grid // should not happen
                }
            }
            .second
            .run { groupBy { it.y } to groupBy { it.x } }

        val (minY, minX) = gridY.keys.min() to gridX.keys.min()
        val (maxY, maxX) = gridY.keys.max() to gridX.keys.max()

        val expandedGrid = List(maxY - minY + 1) { i ->
            val y = i + minY
            List(maxX - minX + 1) { j ->
                val x = j + minX
                if (x < gridY[y]!!.minOf { it.x } || x > gridY[y]!!.maxOf { it.x } || y < gridX[x]!!.minOf { it.y } || y > gridX[x]!!.maxOf { it.y }) 'O' else 'I'
            }
        }

        val expandedGrid2 = expandedGrid
            .foldIndexed(0 to List(maxX - minX + 1) { false }) { i, acc, row ->
                val y = i + minY
                row.foldIndexed(Triple(acc.first, false, acc.second)) { j, acc2, c ->
                    val x = j + minX
                    if (gridY[y]!!.any { it.x == x }) {
                        var rowIn = acc2.second
                        var colsIn = acc2.third
                        if (gridY[y]!!.none { it.x == x - 1 }) {
                            if (gridY[y]!!.none { it.x == x + 1 })
                                rowIn = !rowIn
                            else if (gridX[x]!!.any { it.y == y - 1 })
                                rowIn = !rowIn
                        } else if (gridY[y]!!.none { it.x == x + 1 }) {
                            if (gridX[x]!!.any { it.y == y - 1 })
                                rowIn = !rowIn
                        }
                        if (gridX[x]!!.none { it.y == y - 1 }) {
                            if (gridX[x]!!.none { it.y == y + 1 })
                                colsIn = colsIn.mapIndexed { k, old -> if (k == j) !old else old }
                            else if (gridY[y]!!.any { it.x == x - 1 })
                                colsIn = colsIn.mapIndexed { k, old -> if (k == j) !old else old }
                        } else if (gridX[x]!!.none { it.y == y + 1 }) {
                            if (gridY[y]!!.any { it.x == x - 1 })
                                colsIn = colsIn.mapIndexed { k, old -> if (k == j) !old else old }
                        }
                        Triple(acc2.first + 1, rowIn, colsIn)
                    } else if (acc2.second && acc2.third[j])
                        Triple(acc2.first + 1, true, acc2.third)
                    else
                        acc2
                }.run { first to third }
            }

        /*
        val expandedGrid2 = expandedGrid.mapIndexed { i, r ->
            val y = i + minY
            r.mapIndexed { j, c ->
                val x = j + minX
                if (c == 'I' &&
                    (
                        gridY[y]!!.any { it.x == x } ||
                        (
                            gridY[y]!!.countSwitchesBeforeX(gridX, x) % 2 == 1 &&
                            gridX[x]!!.countSwitchesBeforeY(gridY, y) % 2 == 1
                            //gridY[y]!!.sortedBy { it.x }.zipWithNext().filter { it.first.x < x }.count { it.first.x + 1 != it.second.x } % 2 == 1 &&
                            //gridX[x]!!.sortedBy { it.y }.zipWithNext().filter { it.first.y < y }.count { it.first.y + 1 != it.second.y } % 2 == 1
                        )
                    )) 'I' else 'O'
            }
        }

         */
        /*
        val expandedGrid3 = expandedGrid2
            .map { it.joinToString("") }
            .rotateLeft()
            .mapIndexed { i, r ->
                val x = i + minX
                r.mapIndexed { j, c ->
                    val y = j + minY
                    if (c == 'I' &&
                        (
                            gridX[x]!!.any { it.y == y } ||
                            (
                                r.withIndex().drop(j).first { (k, c) -> c == 'O' || gridX[x]!!.any { it.y == k + minY } }.value == 'I' &&
                                r.withIndex().take(j).reversed().first { (k, c) -> c == 'O' || gridX[x]!!.any { it.y == k + minY } }.value == 'I' )
                            )
                    ) 'I' else 'O'
                }
            }

         */
        return expandedGrid2.first
    }

    fun part2(input: List<Triple<Char, Int, String>>): Long {
        val correctedInput = input.map {
            when (it.third.last()) {
                '0' -> 'R'
                '1' -> 'D'
                '2' -> 'L'
                '3' -> 'U'
                else -> 'X' // should not happen
            } to it.third.drop(1).dropLast(1).hexToInt()
        }

        val (gridY, gridX) = correctedInput.fold(listOf(0L to 0L)) { grid, trench ->
            val last = grid.last()
            when (trench.first) {
                'R' -> grid.plus((last.first + trench.second) to last.second)
                'L' -> grid.plus((last.first - trench.second) to last.second)
                'D' -> grid.plus(last.first to (last.second + trench.second))
                'U' -> grid.plus(last.first to (last.second - trench.second))
                else ->  grid // should not happen
            }
        }
            .zipWithNext()
            .run { groupBy { it.first.first } to groupBy { it.first.second } }

        val (minY, minX) = gridY.keys.min() to gridX.keys.min()
        val (maxY, maxX) = gridY.keys.max() to gridX.keys.max()

        /*
        generateSequence(0) { it + 1 }
            .take((maxY - minY + 1).toInt())
            .forEach {
                println(generateSequence(0) { it + 1 }
                    .take((maxX - minX + 1).toInt())
                    .map { '-' }
                    .joinToString(""))
            }

         */
        /*
        val expandedGrid = generateSequence(0) { it + 1 }
            .take((maxY - minY + 1).toInt())
            .foldIndexed(0L to List((maxX - minX + 1).toInt()) { false }) { i, acc, _ ->
                val y = i + minY
                generateSequence(0) { it + 1 }
                    .take((maxX - minX + 1).toInt())
                    .foldIndexed(Triple(acc.first, false, acc.second)) { j, acc2, c ->
                        val x = j + minX
                        if (gridY[y]!!.any { it.x == x }) {
                            var rowIn = acc2.second
                            var colsIn = acc2.third
                            if (gridY[y]!!.none { it.x == x - 1 }) {
                                if (gridY[y]!!.none { it.x == x + 1 })
                                    rowIn = !rowIn
                                else if (gridX[x]!!.any { it.y == y - 1 })
                                    rowIn = !rowIn
                            } else if (gridY[y]!!.none { it.x == x + 1 }) {
                                if (gridX[x]!!.any { it.y == y - 1 })
                                    rowIn = !rowIn
                            }
                            if (gridX[x]!!.none { it.y == y - 1 }) {
                                if (gridX[x]!!.none { it.y == y + 1 })
                                    colsIn = colsIn.mapIndexed { k, old -> if (k == j) !old else old }
                                else if (gridY[y]!!.any { it.x == x - 1 })
                                    colsIn = colsIn.mapIndexed { k, old -> if (k == j) !old else old }
                            } else if (gridX[x]!!.none { it.y == y + 1 }) {
                                if (gridY[y]!!.any { it.x == x - 1 })
                                    colsIn = colsIn.mapIndexed { k, old -> if (k == j) !old else old }
                            }
                            Triple(acc2.first + 1, rowIn, colsIn)
                        } else if (acc2.second && acc2.third[j])
                            Triple(acc2.first + 1, true, acc2.third)
                        else
                            acc2
                    }.run { first to third }
            }

         */
        return 0
    }

    val input = readInput("Day18")
        .map { s ->
            Triple(
                s.first(),
                s.dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }.toInt(),
                s.dropWhile { it != '#' }.take(7)
            )
        }
    println(part1(input))
    println(part2(input))
}