package net.afterday.compas.engine.core.player

interface PlayerPropsNew {
    //region Health
    var health: Double
    fun addHealth(health: Double)
    fun subtractHealth(health: Double)
    fun changeHealth(delta: Double, influence: Int): Boolean
    //endregion

    var fraction: Player.FRACTION
}