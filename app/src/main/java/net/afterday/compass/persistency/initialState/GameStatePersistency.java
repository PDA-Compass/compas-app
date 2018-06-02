package net.afterday.compass.persistency.initialState;

import net.afterday.compass.core.gameState.GameState;

/**
 * Created by Justas Spakauskas on 2/10/2018.
 */

public interface GameStatePersistency
{
    GameState loadState();
    void storeState(GameState gameState);
}
