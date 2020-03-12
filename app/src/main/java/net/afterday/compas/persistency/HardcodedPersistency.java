package net.afterday.compas.persistency;

import net.afterday.compas.core.fraction.Fraction;
import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.Player;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import java8.util.Optional;

/**
 * Created by Justas Spakauskas on 1/28/2018.
 */

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

    public Single<Fraction> getPossibleFractions()
    {
        return null;
    }

    public Single<List<Influence>> getPossibleInfluences(){return null;}
}
