package net.afterday.compass.core.gameState;

import net.afterday.compass.core.influences.Influence;
import net.afterday.compass.core.player.Player;
import net.afterday.compass.core.player.PlayerProps;
import net.afterday.compass.core.player.PlayerPropsImpl;

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
