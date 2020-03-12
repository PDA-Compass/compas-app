package net.afterday.compas.persistency;

import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.Player;

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
