package net.afterday.compas.engine.persistency.initialState;

import net.afterday.compas.engine.core.gameState.GameState;

public interface GameStatePersistency
{
    GameState loadState();
    void storeState(GameState gameState);
}