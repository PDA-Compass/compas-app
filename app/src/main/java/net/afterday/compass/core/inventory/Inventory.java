package net.afterday.compass.core.inventory;

import net.afterday.compass.core.inventory.items.Item;
import net.afterday.compass.core.player.PlayerProps;
import net.afterday.compass.core.serialization.Jsonable;

import java.util.List;

/**
 * Created by spaka on 4/18/2018.
 */

public interface Inventory extends Jsonable
{
    int MAX_ARTIFACTS_COUNT = 5;
    Item addItem(String code);
    boolean hasActiveArmor();
    boolean hasHealthInstant();
    boolean hasRadiationInstant();
    Item consumeArmor(long delta);
    Item getActiveArmor();
    Item getActiveBooster();
    void setPlayerLevel(int level);
    boolean hasActiveBooster();
    Item consumeBooster(long delta);
    PlayerProps useItem(Item item, PlayerProps playerProps);
    boolean dropItem(Item item);
    List<Item> getItems();
    List<Item> getBoosters();
    List<Item> getArmors();
    double[] getArtifacts();

}
