package net.afterday.compas.engine.core.influences

import java.util.*

interface Emission {
    val startTime: Calendar
    val notifyBefore: Int
    val duration: Int
    val fake: Boolean
}
