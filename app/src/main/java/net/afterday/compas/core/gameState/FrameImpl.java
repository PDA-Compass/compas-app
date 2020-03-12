package net.afterday.compas.core.gameState;

import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.Player;
import net.afterday.compas.core.player.PlayerProps;
import net.afterday.compas.core.player.PlayerPropsImpl;

/**
 * Created by spaka on 4/18/2018.
 */

public class FrameImpl implements Frame
{
    private PlayerProps mPlayerProps;

    public FrameImpl(PlayerProps playerProps)
    {
        this.mPlayerProps = playerProps;
    }

    @Override
    public PlayerProps getPlayerProps()
    {
        return mPlayerProps;
    }


}
