package net.afterday.compas.stalker.setup

import net.afterday.compas.engine.core.player.Player
import net.afterday.compas.engine.setup.SetupFraction

class SetupFractionStalker : SetupFraction() {
    override fun setup() {
        fractions["MONOLITH953"] = Player.FRACTION.MONOLITH //Режим монолитовца
        fractions["GAMEMASTER751"] = Player.FRACTION.GAMEMASTER //Режим игромастера
        fractions["STALKER982"] = Player.FRACTION.STALKER //Режим обычного игрока
        fractions["DARKEN256"] = Player.FRACTION.DARKEN //Режим Тёмного
    }
}