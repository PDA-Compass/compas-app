package net.afterday.compas.engine.core.player;

import net.afterday.compas.engine.core.inventory.items.Item;

public interface Impacts
{
    enum STATE {
        HEALING, CLEAR, DAMAGE, DEAD
    }
    void artifactsImpact(double[] artifacts);
    void itemImpact(Item item);
    void boosterImpact(Item item);
    void armorImpact(Item item);
    STATE getState();
}
