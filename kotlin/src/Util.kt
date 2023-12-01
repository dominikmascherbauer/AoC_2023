import java.io.File

/**
 * Reads lines from the given resource text file.
 */
fun readInput(name: String) = File(ClassLoader.getSystemResource(name).path).readLines()
