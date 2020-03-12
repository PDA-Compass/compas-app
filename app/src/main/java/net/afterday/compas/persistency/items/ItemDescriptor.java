package net.afterday.compas.persistency.items;

import net.afterday.compas.core.inventory.Inventory;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.serialization.Jsonable;

/**
 * Created by Justas Spakauskas on 3/25/2018.
 */

public interface ItemDescriptor
{
    double NULL_MODIFIER = -99999999d;
    int getImage();
    String getName();
    int getNameId();
    boolean isBooster();
    boolean isDevice();
    boolean isArmor();
    boolean isArtefact();
    boolean isSingleUse();
    boolean isConsumable();
    long getDuration();
    double[] getModifiers();
    int getXpPoints();
    String getDescription();
    int getDescriptionId();
    boolean isUsable();
    boolean isDropable();
    Item.CATEGORY getCategory();
}
