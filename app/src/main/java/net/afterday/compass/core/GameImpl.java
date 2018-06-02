package net.afterday.compass.core;

import net.afterday.compass.core.fraction.Fraction;
import net.afterday.compass.core.gameState.Frame;
import net.afterday.compass.core.influences.InfluencesPack;
import net.afterday.compass.core.inventory.Inventory;
import net.afterday.compass.core.inventory.InventoryImpl;
import net.afterday.compass.core.inventory.items.Events.DropItem;
import net.afterday.compass.core.inventory.items.Item;
import net.afterday.compass.core.player.Player;
import net.afterday.compass.core.player.PlayerImpl;
import net.afterday.compass.core.serialization.Serializer;
import net.afterday.compass.persistency.PersistencyProvider;
import net.afterday.compass.persistency.items.ItemsPersistency;

import java.util.List;
import java.util.Map;

public class GameImpl implements Game
{
    private int mLevel = 0;
    private long mLastInfls = 0;
    private PlayerImpl mPlayer;
    private Controls controls;

    public GameImpl(Controls controls, PersistencyProvider persistencyProvider, Serializer serializer)
    {
        this.controls = controls;
        mLastInfls = System.currentTimeMillis();
        mPlayer = new PlayerImpl(new InventoryImpl(persistencyProvider.getItemsPersistency(), serializer));
    }

    @Override
    public Frame start()
    {
        controls.startInfluences();
        return null;
    }

    public Frame acceptInfluences(InfluencesPack influencesPack)
    {
//        long now = System.currentTimeMillis();
//        long delta = now - mLastInfls;
//
//        mLastInfls = now;
        return mPlayer.acceptInfluences(influencesPack);
    }

    @Override
    public Player getPlayer()
    {
        return mPlayer;
    }

    @Override
    public Inventory getInventory()
    {
        return mPlayer.getInventory();
    }

    @Override
    public boolean addItem(String code)
    {
        return mPlayer.addItem(code);
    }

    @Override
    public Frame useItem(Item item)
    {
        return mPlayer.useItem(item);
    }

    public Frame dropItem(DropItem dropItem)
    {
        return null;
    }

    public Frame changeFraction(Fraction fraction)
    {
        return null;
    }
}
