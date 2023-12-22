var lowPulses = 0
var highPulses = 0

val rxProg = mutableListOf<Pair<Int, String>>()
val kcProg = mutableListOf<Pair<Int, List<Pair<String, Signal>>>>()
var count: Int = 0

enum class Signal {
    LOW, HIGH;
    fun invert(): Signal = if (this == LOW) HIGH else LOW
}

sealed class Module(val name: String, val targets: MutableList<Module> = mutableListOf()) {
    class FlipFlop(name: String, var state: Signal = Signal.LOW) : Module(name) {
        override fun doSomething(signal: Signal, sender: Module) {
            if (signal == Signal.LOW) {
                targets.onEach { it.apply(state, this) }
                    .forEach { it.doSomething(state, this) }
            }
        }
        override fun apply(signal: Signal, sender: Module) {
            if (signal == Signal.LOW) {
                state = state.invert()
                if (state == Signal.LOW)
                    lowPulses += targets.size
                else
                    highPulses += targets.size
            }
        }
    }

    class Conjunction(name: String, val inputs: MutableMap<Module, Signal> = mutableMapOf()) : Module(name) {
        private var sendSignal: Signal = Signal.LOW
        val sendBuffer: MutableList<Signal> = mutableListOf()
        override fun doSomething(signal: Signal, sender: Module) {
            val send: Signal = sendBuffer.removeFirst()
            targets.onEach { it.apply(send, this) }
                .onEach { it.doSomething(send, this) }
        }

        override fun apply(signal: Signal, sender: Module) {
            inputs[sender] = signal
            sendSignal = if (inputs.values.all { it == Signal.HIGH }) Signal.LOW else Signal.HIGH
            sendBuffer.add(sendSignal)
            if (sendSignal == Signal.HIGH) {
                if (name == "ph" || name == "vn" || name == "kt" || name == "hn")
                    rxProg.add(count to name)
            }
            if (name == "kc" && inputs.any { it.value == Signal.HIGH }) {
                kcProg.add(count to (inputs.map { it.key.name to it.value }))
            }
            if (sendSignal == Signal.LOW)
                lowPulses += targets.size
            else
                highPulses += targets.size
        }
        fun addInput(input: Module): Signal? =
            inputs.put(input, Signal.LOW)
    }

    class Output(name: String): Module(name) {
        override fun doSomething(signal: Signal, sender: Module) {
            // do nothing for outputs
        }
    }

    data object Broadcaster: Module("broadcaster") {
        override fun doSomething(signal: Signal, sender: Module) {

            if (signal == Signal.LOW)
                lowPulses += targets.size
            else
                highPulses += targets.size

            targets.onEach { it.apply(signal, this) }
                .forEach { it.doSomething(signal, this) }
        }
    }

    data object Button: Module("button", mutableListOf(Broadcaster)) {
        override fun doSomething(signal: Signal, sender: Module) {
            Broadcaster.doSomething(signal, this)
        }
        fun press() {
            lowPulses += 1
            doSomething(Signal.LOW, this)
        }
    }

    abstract fun doSomething(signal: Signal, sender: Module)
    fun addTarget(target: Module): Boolean =
        targets.add(target)
    open fun apply(signal: Signal, sender: Module) {}
}

fun main() {
    fun part1(input: List<String>): Int {
        val button = Module.Button

        for (i in 1..1000)
            button.press()

        return highPulses * lowPulses
    }

    fun part2(input: List<String>): Long {
        /*
        val lcm1 = lcm(4017L,lcm(4089L, 3903L))
        var lcmTot = lcm1 + 4
        val lcm2 = 3797L
        var other = lcm2
        while (lcmTot != other) {
            if(lcmTot < other)
                lcmTot += lcm1
            else {
                var x = 1
                if(lcmTot - other > lcm2)
                    x = ((lcmTot - other) / lcm2).toInt()
                other += lcm2 * x
            }
        }

         */
        // 16696774872028
        // 16696774871028
        // lcm(lcm(4017L,lcm(4089L, 3903L)) + 4L, 3797L) - 1000

        // vn -> 3793  (start offset 4)
        // ph -> 3895  (start offset 12)
        // hn -> 3889  (start offset 132)
        // kt -> 4085  (start offset 8)

        var lcm1 = 3793L
        var lcm1Tot = lcm1 + 4
        val lcm2 = 3895L
        var lcm2Tot = lcm2 + 12
        while (lcm1Tot != lcm2Tot) {
            if(lcm1Tot < lcm2Tot)
                lcm1Tot += lcm1
            else {
                lcm2Tot += lcm2
            }
        }
        lcm1 = lcm1Tot
        val lcm3 = 3889L
        var lcm3Tot = lcm3 + 132
        while (lcm1Tot != lcm3Tot) {
            if(lcm1Tot < lcm3Tot)
                lcm1Tot += lcm1
            else {
                var x = 1
                if(lcm1Tot - lcm3Tot > lcm3)
                    x = ((lcm1Tot - lcm3Tot) / lcm3.toDouble()).toInt()
                lcm3Tot += lcm3 * x
            }
        }
        lcm1 = lcm1Tot
        val lcm4 = 4085L
        var lcm4Tot = lcm4 + 8
        while (lcm1Tot != lcm4Tot) {
            if(lcm1Tot < lcm4Tot)
                lcm1Tot += lcm1
            else {
                var x = 1
                if(lcm1Tot - lcm4Tot > lcm4)
                    x = ((lcm1Tot - lcm4Tot) / lcm4.toDouble()).toInt()
                lcm4Tot += lcm4 * x
            }
        }
/*
        var counter = 0L
        val flipFlopProgression = mutableListOf<List<Pair<String, Signal>>>()
        val kcProgression = mutableListOf<Pair<Long, List<Pair<String, Signal>>>>()
        while (rxLowPulses != 1 || rxHighPulses != 0) {
            //flipFlopProgression.add(flipFlops.map { it.name to it.state })
            if (conjunctions.first { it.name == "kc" }.inputs.any { it.value == Signal.HIGH })
                kcProgression.add(counter to conjunctions.first { it.name == "kc" }.inputs.map { it.key.name to it.value })
            rxLowPulses = 0
            rxHighPulses = 0
            count++
            conjunctions.forEach { it.sendBuffer.clear() }
            button.press()
            counter++
            if(rxLowPulses >= 1) {
                println("Got $rxLowPulses rx low pulses and $rxHighPulses rx high pulses after $counter iterations\n")
            }
        }

 */

        return 0 //counter
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

    val broadcaster = Module.Broadcaster
    val modules = flipFlops.plus(conjunctions).plus(broadcaster)
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

    println(part1(input))
    //println(part2(input))
}