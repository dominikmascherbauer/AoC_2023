enum class Signal {
    LOW, HIGH;
    fun invert(): Signal = if (this == LOW) HIGH else LOW
}

sealed class Module(val name: String, val targets: MutableList<Module> = mutableListOf()) {
    class FlipFlop(name: String, var state: Signal = Signal.LOW) : Module(name) {
        override fun generateSignals(signal: Signal, sender: Module): List<Triple<Module, Signal, Module>> = when(signal) {
                Signal.LOW -> {
                    state = state.invert()
                    targets.map{ Triple(this, state, it) }
                }
                Signal.HIGH -> listOf()
            }

        override fun reset() {
            state = Signal.LOW
        }
    }

    class Conjunction(name: String, val inputs: MutableMap<Module, Signal> = mutableMapOf()) : Module(name) {
        fun addInput(input: Module): Signal? =
            inputs.put(input, Signal.LOW)

        override fun generateSignals(signal: Signal, sender: Module): List<Triple<Module, Signal, Module>> {
            inputs[sender] = signal
            val sendSignal = if (inputs.values.all { it == Signal.HIGH }) Signal.LOW else Signal.HIGH
            return targets.map { Triple(this, sendSignal, it) }
        }

        override fun reset() =
            inputs.keys.forEach {
                inputs[it] = Signal.LOW
            }
    }

    class Output(name: String): Module(name) {
        override fun generateSignals(signal: Signal, sender: Module): List<Triple<Module, Signal, Module>> =
            listOf() // no signal generated by output modules
    }

    data object Broadcaster: Module("broadcaster") {
        override fun generateSignals(signal: Signal, sender: Module): List<Triple<Module, Signal, Module>> =
            targets.map { Triple(this, signal, it) }
    }

    data object Button: Module("button", mutableListOf(Broadcaster)) {
        var numberOfPresses = 0
        override fun generateSignals(signal: Signal, sender: Module): List<Triple<Module, Signal, Module>> =
            listOf(Triple(this, signal, Broadcaster))

        fun press(): List<Triple<Module, Signal, Module>> {
            numberOfPresses++
            return generateSignals(Signal.LOW, this)
        }

        override fun reset() {
            numberOfPresses = 0
        }
    }

    abstract fun generateSignals(signal: Signal, sender: Module): List<Triple<Module, Signal, Module>>
    open fun reset() {} // nothing to reset by default, but let subclasses add a reset behavior
    fun addTarget(target: Module): Boolean =
        targets.add(target)
}

fun main() {
    fun part1(): Int {
        val button = Module.Button
        var lowPulses = 0
        var highPulses = 0
        while (button.numberOfPresses < 1000) {
            val signals = button.press().toMutableList()
            while (signals.isNotEmpty()) {
                val (sender, signal, receiver) = signals.removeFirst()
                if (signal == Signal.LOW) lowPulses++ else highPulses++
                signals.addAll(receiver.generateSignals(signal, sender))
            }
        }

        return highPulses * lowPulses
    }

    fun part2(modules: List<Module>): Long {
        val button = Module.Button
        val outputGenerator = modules.first { it.targets.size == 1 && it.targets.first().name == "rx" } as Module.Conjunction
        val loops: Map<Module, MutableList<Int>> = outputGenerator.inputs.map { it.key to mutableListOf<Int>() }.toMap()
        while (loops.values.any { it.size < 2 }) {
            val signals = button.press().toMutableList()
            while (signals.isNotEmpty()) {
                val (sender, signal, receiver) = signals.removeFirst()
                signals.addAll(receiver.generateSignals(signal, sender))
                if (receiver == outputGenerator && signal == Signal.HIGH) {
                    loops[sender]!!.add(button.numberOfPresses)
                }
            }
        }
        return loops.values
            .map { it.zipWithNext { a, b -> b - a }.first().toLong() }
            .reduce { acc, loop -> lcm(acc, loop) }
    }

    val input = readInput("Day20")

    val flipFlops = input.filter { it.first() == '%' }
        .map { s ->
            Module.FlipFlop(s.drop(1).takeWhile { it.isLetter() })
        }
    val conjunctions = input.filter { it.first() == '&' }
        .map { s ->
            Module.Conjunction(s.drop(1).takeWhile { it.isLetter() })
        }
    val modules = flipFlops.plus(conjunctions).plus(Module.Broadcaster)
    modules.forEach { module ->
        input.map { if (it.first() == '%' || it.first() == '&') it.drop(1) else it }
            .first { it.split(' ').first() == module.name }
            .dropWhile { it != '>' }
            .drop(1)
            .split(',')
            .map { it.trim() }
            .forEach { target ->
                val targetModule = modules.firstOrNull { it.name == target } ?: Module.Output(target)
                module.addTarget(targetModule)
                if (targetModule is Module.Conjunction) {
                    targetModule.addInput(module)
                }
            }
    }

    println(part1())
    modules.forEach { it.reset() }
    Module.Button.reset()
    println(part2(modules))
}