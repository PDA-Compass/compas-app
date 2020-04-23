package net.afterday.compas.engine.persistency;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import net.afterday.compas.engine.core.influences.Influence;
import net.afterday.compas.engine.core.inventory.items.Item;
import net.afterday.compas.engine.core.player.Player;

import java.util.List;

import java.util.Optional;

public class HardcodedPersistency implements Persistency
{
    HardcodedPersistency()
    {
    }

    public Single<List<Item>> getPossibleItems()
    {
        return null;
//        return Single.create((SingleOnSubscribe<List<Item>>) emitter -> {
//            if(!emitter.isDisposed())
//            {
//                List<Item> items = new ArrayList<Item>();
//                items.add(new ItemImpl(Items.STALKER_SUIT));
//                items.add(new ItemImpl(Items.CRYSTAL));
//                items.add(new ItemImpl(Items.GOLDFISH));
//                items.add(new ItemImpl(Items.VODKA));
//                items.add(new ItemImpl(Items.ENERGY_DRINK));
//                emitter.onSuccess(items);
//            }
//        });
    }

    public Single<Optional<Player>> getStoredPlayer()
    {
        return Single.create((SingleOnSubscribe<Optional<Player>>) emitter -> {
            if(!emitter.isDisposed())
            {
                //Has no player. It means it's new game.
                emitter.onSuccess(Optional.empty());
            }
        });
    }

    public Single<List<Influence>> getPossibleInfluences(){return null;}
}
