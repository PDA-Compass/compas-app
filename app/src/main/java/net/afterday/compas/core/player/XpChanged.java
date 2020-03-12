package net.afterday.compas.core.player;

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
