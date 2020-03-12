package net.afterday.compas.engine.setup

import net.afterday.compas.engine.core.influences.Emission
import java.util.*
import kotlin.collections.ArrayList

abstract class SetupEmissions {
    val emissions: ArrayList<Emission> = ArrayList()
    abstract fun setup(): Unit

    class EmissionImpl(override val startTime: Calendar) : Emission {
        override var notifyBefore: Int = 20
        override var duration: Int = 30
        override var fake: Boolean = false
    }

    fun at(month: Int, day: Int, hour: Int, min: Int, block: EmissionImpl.()->Unit) {
        val c = Calendar.getInstance()
        c[Calendar.MONTH] = month - 1
        c[Calendar.DAY_OF_MONTH] = day
        c[Calendar.HOUR_OF_DAY] = hour
        c[Calendar.MINUTE] = min
        c[Calendar.SECOND] = 0

        val emission = EmissionImpl(c)
        emission.apply(block)
        emissions.add(emission)
    }
}