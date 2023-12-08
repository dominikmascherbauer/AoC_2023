import java.io.File

/**
 * Reads lines from the given resource text file.
 */
fun readInput(name: String) = File(ClassLoader.getSystemResource(name).path).readLines()

/**
 * Greatest common divisor (gcd) for two Long values
 */
fun gcd(a: Long, b: Long): Long =
    generateSequence(a to b) {
        it.second to it.first % it.second
    }
        .first { it.second <= 0 }
        .first

/**
 * lowest common multiple (lcm) for two Long values
 */
fun lcm(a: Long, b: Long): Long =
    a * (b / gcd(a, b))
