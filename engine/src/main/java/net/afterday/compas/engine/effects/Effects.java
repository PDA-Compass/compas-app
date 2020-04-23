package net.afterday.compas.engine.effects;

import io.reactivex.rxjava3.core.Observable;
import net.afterday.compas.engine.core.player.Impacts;
import net.afterday.compas.engine.core.player.Player;

public interface Effects
{
    void setPlayerStatesStream(Observable<Player.STATE> statesStream);
    void setImpactsStatesStream(Observable<Impacts.STATE> impactsStatesStream);
    void setPlayerLevelStream(Observable<Integer> playerLevelStream);
}
