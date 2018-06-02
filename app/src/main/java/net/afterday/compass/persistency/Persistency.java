package net.afterday.compass.persistency;

import net.afterday.compass.core.inventory.items.Item;
import net.afterday.compass.core.player.Player;
import net.afterday.compass.models.Influence;

import java.util.List;

import io.reactivex.Single;
import java8.util.Optional;

/**
 * Created by Justas Spakauskas on 1/28/2018.
 */

public interface Persistency
{
    Single<List<Item>> getPossibleItems();
    Single<Optional<Player>> getStoredPlayer();
    Single<List<Influence>> getPossibleInfluences();
}
