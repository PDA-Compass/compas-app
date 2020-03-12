package net.afterday.compas.engine.core.gameState;

import net.afterday.compas.engine.core.player.Equipment;
import net.afterday.compas.engine.core.player.PlayerProps;

public class FrameImpl implements Frame
{
    private PlayerProps mPlayerProps;
    private Equipment mEquipment;

    public FrameImpl(PlayerProps playerProps, Equipment equipment)
    {
        this.mPlayerProps = playerProps;
        this.mEquipment = equipment;
    }

    @Override
    public PlayerProps getPlayerProps()
    {
        return mPlayerProps;
    }

    @Override
    public Equipment getEquipment() {
        return mEquipment;
    }
}
