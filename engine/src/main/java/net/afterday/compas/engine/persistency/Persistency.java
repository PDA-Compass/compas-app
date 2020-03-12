package net.afterday.compas.engine.persistency;

import net.afterday.compas.engine.core.influences.Influence;
import net.afterday.compas.engine.core.inventory.items.Item;
import net.afterday.compas.engine.core.player.Player;

import java.util.List;

import io.reactivex.Single;
import java.util.Optional;

public interface Persistency
{
    Single<List<Item>> getPossibleItems();
    Single<Optional<Player>> getStoredPlayer();
    Single<List<Influence>> getPossibleInfluences();
}
