package net.afterday.compas.app.pda.radbar

import net.afterday.compas.app.view.Radbar
import net.afterday.compas.engine.core.EventBus
import net.afterday.compas.engine.engine.system.player.PlayerState

class RadBarService (val radBar: Radbar) {
    val playerState = EventBus.Player.stateStream
    var rad = 0.0

    init {
         playerState.subscribe{
             if (PlayerState.rad != rad){
                 rad = PlayerState.rad
                 radBar.setRadiation(rad.toDouble())
             }
         }
    }
}