package net.afterday.compass.core.inventory.items;

import net.afterday.compass.core.player.PlayerProps;
import net.afterday.compass.persistency.items.ItemDescriptor;
import net.afterday.compass.core.serialization.Jsonable;

/**
 * Created by Justas Spakauskas on 1/28/2018.
 */

public interface Item extends Jsonable
{
    int ARMOR = 1;
    int BOOSTER = 2;
    int ALL = 0;
    int HEALTH_MODIFIER  = 0;
    int RADIATION_MODIFIER = 1;
    int ANOMALY_MODIFIER = 2;
    int MENTAL_MODIFIER = 3;
    int BURER_MODIFIER = 4;
    int RADIATION_EMMITER = 5;
    int CONTROLLER_MODIFIER = 6;
    int HEALTH_INSTANT = 7;
    int RADIATION_INSTANT = 8;

    public static final int MODIFIERS_COUNT = 9;
    public static final int ARTIFACT_MODIFIERS_COUNT = 7;
    boolean hasModifier(int modifierType);
    double getModifier(int modifierType);
    boolean isActive();
    void setActive(boolean isActive);
    boolean isConsumed();
    void consume(long time);
    int getPercentsLeft();
    PlayerProps modifyProps(PlayerProps playerProps);
    PlayerProps modifyProps(PlayerProps playerProps, long delta);
    String getId();
    String getCode();
    ItemDescriptor getItemDescriptor();
}
