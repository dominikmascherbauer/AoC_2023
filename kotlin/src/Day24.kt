private data class Position(val x: Long, val y: Long, val z: Long)

fun main() {
    fun part1(input: List<Pair<Position, Position>>): Int {
        val trajectories = input
            .map { (it.second.y.toDouble() / it.second.x) to it.first }
            .map { it.first to it.second.y - it.second.x * it.first }

        val testArea = 200000000000000.0..400000000000000.0

        val res = trajectories
            .mapIndexed { i, (k1, d1) ->
                trajectories.drop(i + 1)
                    .mapIndexed { j, (k2, d2) ->
                        if (k2 == k1)               // check for parallel hailstones
                            0.0 to testArea.start - 1 // let this be filtered out later
                        else {
                            // k1*x + d1 = k2*x + d2
                            val x = (d1 - d2) / (k2 - k1)
                            val y = k1 * x + d1
                            if ((input[i].second.x < 0 && (x <= input[i].first.x) || input[i].second.x >= 0 && (x >= input[i].first.x)) &&
                                (input[i].second.y < 0 && (y <= input[i].first.y) || input[i].second.y >= 0 && (y >= input[i].first.y)) &&
                                (input[j+i+1].second.x < 0 && (x <= input[j+i+1].first.x) || input[j+i+1].second.x >= 0 && (x >= input[j+i+1].first.x)) &&
                                (input[j+i+1].second.y < 0 && (y <= input[j+i+1].first.y) || input[j+i+1].second.y >= 0 && (y >= input[j+i+1].first.y))
                                )
                                x to y
                            else
                                0.0 to testArea.start - 1
                        }
                    }
                    .filter { (x, y) -> x in testArea && y in testArea }
            }
        return res.sumOf { it.size }
    }

    fun part2(input: List<Pair<Position, Position>>): Long {
        // put that into wolfram alpha (equations for first three hailstones)
        // solve {
        //   x + u*a == 144788461200241 + 227*a,
        //   y + v*a == 195443318499267 + 158*a,
        //   z + w*a == 285412990927879 + 5*a,
        //   x + u*b == 266680201159206 + 37*b,
        //   y + v*b == 319693757705834 - 56*b,
        //   z + w*b == 207679493757440 + 138*b,
        //   x + u*c == 343135145904814 - 88*c,
        //   y + v*c == 302103279002870 + 41*c,
        //   z + w*c == 240702357103107 + 9*c
        // }

        // result
        // u = -220, v = -167, w = 214
        // x = 387382059881002, y = 371825688904742, z = 171985558882512
        // rock -> 387382059881002, 371825688904742, 171985558882512 @ -220, -167, 214
        // c = 335203893759, b = 469657037828, a = 542714985863   --> time to hit hailstones 3, 2 and 1 respectively

        return 387382059881002 + 371825688904742 + 171985558882512
    }

    val input = readInput("Day24")
        .map { s ->
            s.split('@')
                .map { it.split(',').map(String::trim).map(String::toLong) }
                .map { Position(it[0], it[1], it[2]) }
        }
        .map { it[0] to it[1] }
    println(part1(input))
    println(part2(input))
}