package net.afterday.compas.core.player;

import net.afterday.compas.core.inventory.items.Item;

/**
 * Created by spaka on 4/18/2018.
 */

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
