package net.afterday.compas.engine.persistency;

import io.reactivex.rxjava3.core.Single;
import net.afterday.compas.engine.core.influences.Influence;
import net.afterday.compas.engine.core.inventory.items.Item;
import net.afterday.compas.engine.core.player.Player;

import java.util.List;

import java.util.Optional;

public interface Persistency
{
    Single<List<Item>> getPossibleItems();
    Single<Optional<Player>> getStoredPlayer();
    Single<List<Influence>> getPossibleInfluences();
}
