package net.afterday.compass.core.player;

/**
 * Created by spaka on 5/22/2018.
 */

public interface XpChanged
{
    boolean levelChanged();
    int getLevel();
    int getXp();
    int getLevelXpPercents();
}
