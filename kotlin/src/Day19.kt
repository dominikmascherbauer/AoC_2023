import Part.Companion.parse
import kotlin.math.max
import kotlin.math.min

private data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    constructor(s: String) : this(s.parse('x'), s.parse('m'), s.parse('a'), s.parse('s'))

    val sum: Int =
        x + m + a + s

    companion object {
        fun String.parse(c: Char): Int =
            dropWhile { it != c }.drop(2).takeWhile(Char::isDigit).toInt()
    }
}

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

    fun String.combinations(workflows: Map<String, List<String>>, min: Part, max: Part): Pair<Part, Part>? =
        when (this) {
            "A" -> min to max
            "R" -> null
            else -> {
                val np: Pair<Part, Part>? = min to max
                workflows[this]!!.fold(listOf<Pair<Part, Part>?>() to np) { acc, cond ->
                    if (acc.second == null)
                        acc
                    else {
                        val op = cond.drop(1).firstOrNull()
                        val variable = cond.first()
                        val value = cond.drop(2).takeWhile { it.isDigit() }.toIntOrNull()
                        val target = cond.split(':').last()
                        when (op) {
                            '>' -> when (variable) {
                                'x' -> acc.first.plus(if (value!! > max.x ) null else target.combinations(workflows, Part(max(min.x, value + 1), min.m, min.a, min.s), max)) to target.combinations(workflows, Part(max(min.x, value + 1), min.m, min.a, min.s), max)
                                'm' -> acc.first.plus(if (value!! > max.m ) null else target.combinations(workflows, Part(min.x, max(min.m, value + 1), min.a, min.s), max)) to null
                                'a' -> acc.first.plus(if (value!! > max.a ) null else target.combinations(workflows, Part(min.x, min.m, max(min.a, value + 1), min.s), max)) to null
                                's' -> acc.first.plus(if (value!! > max.s ) null else target.combinations(workflows, Part(min.x, min.m, min.a, max(min.s, value + 1)), max)) to null
                                else -> acc // should not happen
                            }
                            '<' -> when (variable) {
                                'x' -> acc.first.plus(if (value!! < min.x ) null else target.combinations(workflows, min, Part(min(max.x, value - 1), max.m, max.a, max.s))) to null
                                'm' -> acc.first.plus(if (value!! < min.m ) null else target.combinations(workflows, min, Part(max.x, min(max.m, value - 1), max.a, max.s))) to null
                                'a' -> acc.first.plus(if (value!! < min.a ) null else target.combinations(workflows, min, Part(max.x, max.m, min(max.a, value - 1), max.s))) to null
                                's' -> acc.first.plus(if (value!! < min.s ) null else target.combinations(workflows, min, Part(max.x, max.m, max.a, min(max.s, value - 1)))) to null
                                else -> acc // should not happen
                            }
                            else -> acc.first.plus(cond.combinations(workflows, min, max)) to acc.second
                        }
                    }
                }.second
            }
                    /*
                    .mapIndexed { i, cond ->
                    val op = cond.drop(1).firstOrNull()
                    val variable = cond.first()
                    val value = cond.drop(2).takeWhile { it.isDigit() }.toIntOrNull()
                    val target = cond.split(':').last()
                    when (op) {
                        '>' -> when (variable) {
                            'x' -> if (value!! > max.x ) null else target.combinations(workflows, Part(max(min.x, value + 1), min.m, min.a, min.s), max)
                            'm' -> if (value!! > max.m ) null else target.combinations(workflows, Part(min.x, max(min.m, value + 1), min.a, min.s), max)
                            'a' -> if (value!! > max.a ) null else target.combinations(workflows, Part(min.x, min.m, max(min.a, value + 1), min.s), max)
                            's' -> if (value!! > max.s ) null else target.combinations(workflows, Part(min.x, min.m, min.a, max(min.s, value + 1)), max)
                            else -> null // should not happen
                        }
                        '<' -> when (variable) {
                            'x' -> if (value!! < min.x ) null else target.combinations(workflows, min, Part(min(max.x, value - 1), max.m, max.a, max.s))
                            'm' -> if (value!! < min.m ) null else target.combinations(workflows, min, Part(max.x, min(max.m, value - 1), max.a, max.s))
                            'a' -> if (value!! < min.a ) null else target.combinations(workflows, min, Part(max.x, max.m, min(max.a, value - 1), max.s))
                            's' -> if (value!! < min.s ) null else target.combinations(workflows, min, Part(max.x, max.m, max.a, min(max.s, value - 1)))
                            else -> null // should not happen
                        }
                        else -> cond.combinations(workflows, min, max)
                    }
                }
                    .filterNotNull()
                    .fold(Part(4000,4000,4000,4000) to Part(1,1,1,1)) { acc, next ->
                        Part(min(acc.first.x, next.first.x), min(acc.first.m, next.first.m), min(acc.first.a, next.first.a), min(acc.first.s, next.first.s)) to
                        Part(max(acc.second.x, next.second.x), max(acc.second.m, next.second.m), max(acc.second.a, next.second.a), max(acc.second.s, next.second.s))
                    }

                     */
        }

    fun part2(workflows: Map<String, List<String>>): Long {
        val (min, max) = "in".combinations(workflows, Part(1,1,1,1), Part(4000,4000,4000,4000))!!

        return (max.x - min.x + 1L) * (max.m - min.m + 1L) * (max.a - min.a + 1L) * (max.s - min.s + 1L)
    }

    val input = readInput("Day19")

    val workflows: Map<String, List<String>> = input
        .takeWhile { it.isNotEmpty() }
        .associate { w -> w.takeWhile { it.isLetter() } to w.dropWhile { it.isLetter() }.drop(1).dropLast(1).split(',') }
    val parts: List<Part> = input.dropWhile { it.isNotEmpty() }.drop(1).map { Part(it) }

    println(part1(workflows, parts))
    //println(part2(workflows))
}