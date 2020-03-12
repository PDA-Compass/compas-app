package net.afterday.compas.engine.core.player;

public interface XpChanged
{
    boolean levelChanged();
    int getLevel();
    int getXp();
    int getLevelXpPercents();
}