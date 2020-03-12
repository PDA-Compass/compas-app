package net.afterday.compas.core.inventory;

import net.afterday.compas.R;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.PlayerProps;
import net.afterday.compas.core.serialization.Jsonable;
import net.afterday.compas.persistency.items.ItemDescriptor;

import java.util.List;
import java.util.Map;

/**
 * Created by spaka on 4/18/2018.
 */

public interface Inventory extends Jsonable
{
    int MAX_ARTIFACTS_COUNT = 5;
    Item addItem(ItemDescriptor item, String code);
    boolean hasActiveArmor();
    boolean hasHealthInstant();
    boolean hasRadiationInstant();
    Item consumeArmor(long delta);
    Item getActiveArmor();
    Item getActiveBooster();
    Item getActiveDevice();
    void setPlayerLevel(int level);
    boolean hasActiveBooster();
    boolean hasActiveDevice();
    Item consumeBooster(long delta);
    Item consumeDevice(long delta);
    PlayerProps useItem(Item item, PlayerProps playerProps);
    boolean dropItem(Item item);
    List<Item> getItems();
    List<Item> getBoosters();
    List<Item> getDevices();
    List<Item> getArmors();
    double[] getArtifacts();
}
