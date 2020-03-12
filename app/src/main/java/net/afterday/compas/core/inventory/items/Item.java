package net.afterday.compas.core.inventory.items;

import net.afterday.compas.core.player.PlayerProps;
import net.afterday.compas.persistency.items.ItemDescriptor;
import net.afterday.compas.core.serialization.Jsonable;

/**
 * Created by Justas Spakauskas on 1/28/2018.
 */

public interface Item extends Jsonable
{
    enum CATEGORY {
        WEAPONS(0),
        ARTIFACTS(1),
        BOOSTERS(2),
        ARMORS(3),
        MEDKITS(4),
        HABAR(5),
        UPGRADES(6),
        ANTIRADS(7),
        FOOD(8),
        AMMO(9),
        FILTERS(10),
        DEVICES(11);

        private int id;
        CATEGORY(int id)
        {
            this.id = id;
        }
        public int getId()
        {
            return this.id;
        }
    }

    int WEAPONS = 0;
    int ARTIFACTS = 1;
    int BOOSTER = 2;
    int ARMOR = 3;
    int MEDKITS = 4;
    int HABAR = 5;
    int UPGRADES = 6;
    int ANTIRADS = 7;
    int FOOD = 8;
    int AMMO = 9;
    int FILTERS = 10;
    int DEVICES = 11;

    int ALL = 99;
    int HEALTH_MODIFIER  = 0;
    int RADIATION_MODIFIER = 1;
    int ANOMALY_MODIFIER = 2;
    int MENTAL_MODIFIER = 3;
    int BURER_MODIFIER = 4;
    int RADIATION_EMMITER = 5;
    int CONTROLLER_MODIFIER = 6;
    int HEALTH_INSTANT = 7;
    int RADIATION_INSTANT = 8;
    int MONOLITH_MODIFIER = 9;

    public static final int MODIFIERS_COUNT = 10;
    public static final int ARTIFACT_MODIFIERS_COUNT = 10;
    boolean hasModifier(int modifierType);
    double getModifier(int modifierType);
    boolean isActive();
    void setActive(boolean isActive);
    boolean isConsumed();
    void consume(long time);
    int getPercentsLeft();
    String getCode();
    PlayerProps modifyProps(PlayerProps playerProps);
    PlayerProps modifyProps(PlayerProps playerProps, long delta);
    String getId();
    ItemDescriptor getItemDescriptor();
}
