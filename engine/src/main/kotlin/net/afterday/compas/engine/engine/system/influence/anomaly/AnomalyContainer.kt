package net.afterday.compas.engine.engine.system.influence.anomaly

open class AnomalyContainer {
    private val handlers: MutableList<AnomalyHandler> = mutableListOf()

    init {

    }

    fun registerHandler(handler: AnomalyHandler){
        handlers.add(handler)
    }

    fun getLetterMap(): HashMap<String, Int> {
        val map = HashMap<String, Int>()
        for (i in handlers.indices){
            map[handlers[i].getLetter()] = i
        }
        return map
    }

    fun handle(event: AnomalyEvent) {
        val handler = handlers[event.type]
        handler.handle(event)
    }
}