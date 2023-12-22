import kotlin.math.min
import kotlin.math.max

private data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    constructor(s: String) : this(s.parse('x'), s.parse('m'), s.parse('a'), s.parse('s'))

    val sum: Int =
        x + m + a + s
    fun combinations(other: Part): Long =
        (other.x - x + 1).toLong() * (other.m - m + 1) * (other.a - a + 1) * (other.s - s + 1)

    fun setX(newX: Int) = Part(newX, m, a, s)
    fun setM(newM: Int) = Part(x, newM, a, s)
    fun setA(newA: Int) = Part(x, m, newA, s)
    fun setS(newS: Int) = Part(x, m, a, newS)
}

private fun String.parse(c: Char): Int =
    dropWhile { it != c }.drop(2).takeWhile(Char::isDigit).toInt()

fun main() {
    fun part1(workflows: Map<String, List<String>>, parts: List<Part>): Int =
        parts.sumOf { part ->
            generateSequence(workflows["in"]) { l ->
                l.first { rule ->
                    rule.length == 1 || (rule[1] != '<' && rule[1] != '>') || when (rule.first()) {
                        'x' -> if (rule[1] == '<') part.x < rule.parse('x') else part.x > rule.parse('x')
                        'm' -> if (rule[1] == '<') part.m < rule.parse('m') else part.m > rule.parse('m')
                        'a' -> if (rule[1] == '<') part.a < rule.parse('a') else part.a > rule.parse('a')
                        's' -> if (rule[1] == '<') part.s < rule.parse('s') else part.s > rule.parse('s')
                        else -> false // invalid variable
                    }
                }.run {
                    when (val next = split(':').last()) {
                        "A" -> listOf("A")
                        "R" -> listOf("R")
                        else -> workflows[next] // this also works if a workflow is the default path (as split returns only one result
                    }
                }
            }
            .first { it.size == 1 }
            .run { if ("A" in this) part.sum else 0 }
        }

    fun List<String>.combinations(min: Part, max: Part, workflows: Map<String, List<String>>): Long =
        if (min.x > max.x || min.m > max.m || min.a > max.a || min.s > max.s)
            0
        else
            when (val rule = first()) {
                "A" -> min.combinations(max)
                "R" -> 0
                else -> if (':' !in rule)
                    workflows[rule]!!.combinations(min, max, workflows)
                else {
                    val (minTrue, maxTrue) = when (rule.first()) {
                        'x' -> if (rule[1] == '<') min to max.setX(min(max.x, rule.parse('x') - 1)) else min.setX(max(min.x, rule.parse('x') + 1)) to max
                        'm' -> if (rule[1] == '<') min to max.setM(min(max.m, rule.parse('m') - 1)) else min.setM(max(min.m, rule.parse('m') + 1)) to max
                        'a' -> if (rule[1] == '<') min to max.setA(min(max.a, rule.parse('a') - 1)) else min.setA(max(min.a, rule.parse('a') + 1)) to max
                        's' -> if (rule[1] == '<') min to max.setS(min(max.s, rule.parse('s') - 1)) else min.setS(max(min.s, rule.parse('s') + 1)) to max
                        else -> min to max // invalid variable
                    }
                    val (minFalse, maxFalse) = when (rule.first()) {
                        'x' -> if (rule[1] == '>') min to max.setX(min(max.x, rule.parse('x'))) else min.setX(max(min.x, rule.parse('x'))) to max
                        'm' -> if (rule[1] == '>') min to max.setM(min(max.m, rule.parse('m'))) else min.setM(max(min.m, rule.parse('m'))) to max
                        'a' -> if (rule[1] == '>') min to max.setA(min(max.a, rule.parse('a'))) else min.setA(max(min.a, rule.parse('a'))) to max
                        's' -> if (rule[1] == '>') min to max.setS(min(max.s, rule.parse('s'))) else min.setS(max(min.s, rule.parse('s'))) to max
                        else -> min to max // invalid variable
                    }
                    drop(1).combinations(minFalse, maxFalse, workflows) + when (rule.split(':').last()) {
                        "A" -> minTrue.combinations(maxTrue)
                        "R" -> 0
                        else -> workflows[rule.split(':').last()]!!.combinations(minTrue, maxTrue, workflows)
                    }
                }
            }

    fun part2(workflows: Map<String, List<String>>): Long =
        workflows["in"]!!.combinations(Part(1,1,1,1), Part(4000,4000,4000,4000), workflows)
    // 125657431183201 is wrong ... well, turns out it isn't on second try ... (I literally put this same number in twice for it to fail on the first try)

    val input = readInput("Day19")

    val workflows: Map<String, List<String>> = input
        .takeWhile { it.isNotEmpty() }
        .associate { w -> w.takeWhile { it.isLetter() } to w.dropWhile { it.isLetter() }.drop(1).dropLast(1).split(',') }
    val parts: List<Part> = input.dropWhile { it.isNotEmpty() }.drop(1).map { Part(it) }

    println(part1(workflows, parts))
    println(part2(workflows))
}