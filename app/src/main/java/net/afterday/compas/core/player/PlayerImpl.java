package net.afterday.compas.core.player;

import android.util.Log;

import com.google.gson.JsonObject;

import net.afterday.compas.core.events.PlayerEventsListener;
import net.afterday.compas.core.gameState.Frame;
import net.afterday.compas.core.gameState.FrameImpl;
import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.influences.InfluencesPack;
import net.afterday.compas.core.inventory.Inventory;

import net.afterday.compas.core.inventory.items.Events.ItemAdded;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.serialization.Jsonable;
import net.afterday.compas.core.serialization.Serializer;
import net.afterday.compas.persistency.items.ItemDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spaka on 4/18/2018.
 */

public class PlayerImpl implements Player
{
    private static final String TAG = "PlayerImpl";
    private static final String PLAYER = "player";
    private long mLastInfls = System.currentTimeMillis();
    private PlayerProps mPlayerProps;
    private Inventory mInventory;
    private STATE playerState;
    private ImpactsImpl impacts;
    private Impacts.STATE impactsState;
    private static final long MINUTE = 60 * 1000;
    private List<PlayerEventsListener> playerEventsListeners = new ArrayList<>();
    private Serializer serializer;
    private double hBefore;
    private double rBefore;
    private JsonObject o;
    public PlayerImpl(Inventory inventory, Serializer serializer)
    {
        this.serializer = serializer;
        Jsonable jso = serializer.deserialize(PLAYER);
        double health = 100;
        double rad = 0;
        int xp = 1;
        playerState = STATE.ALIVE;
        FRACTION f = FRACTION.STALKER;
        if(jso != null)
        {
            o = jso.toJson();
            if(o.has("health"))
            {
                health = o.get("health").getAsDouble();
            }
            if(o.has("radiation"))
            {
                rad = o.get("radiation").getAsDouble();
            }
            if(o.has("state"))
            {
                //String str = o.get("state").getAsString();
                playerState = Player.STATE.fromString(o.get("state").getAsString());
            }
            if(o.has("fraction"))
            {
                f = FRACTION.fromString(o.get("fraction").getAsString());
            }
            if(o.has("xpPoints"))
            {
                xp = o.get("xpPoints").getAsInt();
            }
        }else
        {
            o = new JsonObject();
            o.addProperty("health", health);
            o.addProperty("radiation", rad);
            o.addProperty("state", playerState.toString());
            o.addProperty("xpPoints", xp);
            o.addProperty("fraction", f.toString());
        }
        hBefore = health;
        rBefore = rad;
        mPlayerProps = new PlayerPropsImpl(playerState);
        mPlayerProps.setFraction(f);
        ((PlayerPropsImpl)mPlayerProps).setHasHealthInstant(inventory.hasHealthInstant());
        ((PlayerPropsImpl)mPlayerProps).setHasRadiationInstant(inventory.hasRadiationInstant());
        if(inventory.hasActiveBooster())
        {
            mPlayerProps.setBoosterPercents(inventory.getActiveBooster().getPercentsLeft());
        }
        if(inventory.hasActiveDevice())
        {
            mPlayerProps.setDevicePercents(inventory.getActiveDevice().getPercentsLeft());
        }
        if(inventory.hasActiveArmor())
        {
            mPlayerProps.setArmorPercents(inventory.getActiveArmor().getPercentsLeft());
        }
        ((PlayerPropsImpl)mPlayerProps).setHealth(health);
        ((PlayerPropsImpl)mPlayerProps).setRadiation(rad);
        ((PlayerPropsImpl)mPlayerProps).setXpPoints(xp);
        mInventory = inventory;
        this.impacts = new ImpactsImpl(mPlayerProps, serializer);
    }

    public Frame acceptInfluences(InfluencesPack inflPack, long delta)
    {
        impacts.prepare(inflPack, delta);
        //Log.e(TAG,"!!!!!!!!!!!!!!!! acceptInfluences 1 " + Thread.currentThread().getName() + " -- " + impacts);
        impacts.artifactsImpact(mInventory.getArtifacts());
        if(mInventory.hasActiveBooster())
        {
            impacts.boosterImpact(mInventory.consumeBooster(delta));
        }
        if(mInventory.hasActiveDevice())
        {
            impacts.deviceImpact(mInventory.consumeDevice(delta));
        }
        if(impacts.getState() == Impacts.STATE.HEALING)
        {
            //Log.d(TAG, "HEALING!!");
            //Log.e(TAG,"!!!!!!!!!!!!!!!! acceptInfluences 2 " + Thread.currentThread().getName() + " -- " + impacts);
            impacts.healByInfluence(delta);
            //Log.e(TAG,"!!!!!!!!!!!!!!!! acceptInfluences 3 " + Thread.currentThread().getName() + " -- " + impacts);
            return makeFrame(impacts);
        }
        if(playerState.getCode() != Player.ALIVE)
        {
            //Log.e(TAG,"!!!!!!!!!!!!!!!! acceptInfluences 4 " + Thread.currentThread().getName() + " -- " + impacts);
            return makeFrame(impacts);
        }
        if(impacts.getState() == Impacts.STATE.CLEAR)
        {
            if(mInventory.hasActiveArmor() && mInventory.getActiveArmor().hasModifier(Item.HEALTH_MODIFIER))
            {
                impacts.armorImpact(mInventory.getActiveArmor());
            }
            impacts.calculateAccumulated(delta);
            //Log.e(TAG,"!!!!!!!!!!!!!!!! acceptInfluences 5 " + Thread.currentThread().getName() + " -- " + impacts);
            return makeFrame(impacts);
        }
        if(impacts.getState() == Impacts.STATE.DAMAGE)
        {
            //Log.d(TAG, "DAMAGE!!");
            if(mInventory.hasActiveArmor())
            {
                impacts.armorImpact(mInventory.consumeArmor(delta));
            }
            impacts.calculateAccumulated(delta);
            impacts.calculateEnvDamage(delta);
            //Log.e(TAG,"!!!!!!!!!!!!!!!! acceptInfluences 6 " + Thread.currentThread().getName() + " -- " + impacts);
            return makeFrame(impacts);
        }

        return new FrameImpl(mPlayerProps);
    }

    private Frame makeFrame(ImpactsImpl impacts)
    {
        boolean dirty = false;
        mPlayerProps = impacts.getPlayerProps();
        Player.STATE ps = mPlayerProps.getState();
        if(ps != playerState)
        {
            o.addProperty("state", ps.toString());
            dirty = true;
        }
        if(mPlayerProps.getRadiation() != rBefore)
        {
            rBefore = mPlayerProps.getRadiation();
            o.addProperty("radiation", rBefore);
            dirty = true;
        }
        if(mPlayerProps.getHealth() != hBefore)
        {
            hBefore = mPlayerProps.getHealth();
            o.addProperty("health", hBefore);
            dirty = true;
        }
        if(dirty)
        {
            serializer.serialize(PLAYER, this);
        }
        validateState(playerState, ps);
        Impacts.STATE is = impacts.getState();
        validateImpactsState(impactsState, is);
        playerState = ps;
        impactsState = is;
        return new FrameImpl(mPlayerProps);
    }

    private void validateImpactsState(Impacts.STATE prevState, Impacts.STATE newState)
    {
        if(prevState == newState)
        {
            return;
        }
        for(PlayerEventsListener l : playerEventsListeners)
        {
            l.onImpactsStateChanged(prevState, newState);
        }
    }

    private void validateState(Player.STATE prevState, Player.STATE newState)
    {
        if(prevState == newState)
        {
            return;
        }
        for(PlayerEventsListener l : playerEventsListeners)
        {
            l.onPlayerStateChanged(prevState, newState);
        }
//        if(newState == STATE.DEAD_CONTROLLER)
//        {
//
//            controls.stopInfluences();
//            countDown.startCountDown(newState);
//        }
    }

    public Frame acceptInfluences(InfluencesPack inflPack)
    {
        long now = System.currentTimeMillis();
        long delta = now - mLastInfls;
        mLastInfls = now;
        return acceptInfluences(inflPack, delta);
//        if(inflPack.influencedBy(Influence.HEALTH))
//        {
//            mIsSafe = true;
//            if(acceptsHealing())
//            {
//                mHealth += 2 * (inflPack.getInfluence() * (delta / (60 * 1000)));
//                if(mHealth > 100)
//                {
//                    mHealth = 100;
//                }
//            }
//        }
//        double[] infls = mInventory.modifyInfluences(inflPack, delta);
//        FrameImpl frameImpl = new FrameImpl(mPlayerProps, delta);
    }

    @Override
    public PlayerProps getPlayerProps()
    {
        return mPlayerProps;
    }

    @Override
    public Inventory getInventory()
    {
        return mInventory;
    }

    @Override
    public boolean addItem(ItemDescriptor i, String code)
    {
        Item item = mInventory.addItem(i, code);
        if(item != null)
        {
            boolean changed = mPlayerProps.addXpPoints(item.getItemDescriptor().getXpPoints());
            ItemAddedEvent e = new ItemAddedEvent(item);
            e.setLevelXpPercents(mPlayerProps.getLevelXp());
            e.setLevel(mPlayerProps.getLevel());
            e.setLevelChanged(changed);
            mInventory.setPlayerLevel(e.getLevel());
            o.addProperty("xpPoints", mPlayerProps.getXpPoints());
            serializer.serialize(PLAYER, this);
            for(PlayerEventsListener pli : playerEventsListeners)
            {
                if(changed)
                {
                    pli.onPlayerLevelChanged(mPlayerProps.getLevel());
                }
                pli.onItemAdded(e);
            }
        }
        setInstants();
        return item != null;
    }

    @Override
    public boolean dropItem(Item item)
    {
        boolean dropped = mInventory.dropItem(item);
        setInstants();
        if(dropped)
        {
            for(PlayerEventsListener l : playerEventsListeners)
            {
                l.onItemDropped(item);
            }
        }
        return dropped;
    }

    @Override
    public Frame useItem(Item item)
    {
        mPlayerProps = mInventory.useItem(item, mPlayerProps);
        setInstants();
        for(PlayerEventsListener l : playerEventsListeners)
        {
            l.onItemUsed(item);
        }
        return new FrameImpl(mPlayerProps);
    }

    private void setInstants()
    {
        ((PlayerPropsImpl)mPlayerProps).setHasHealthInstant(mInventory.hasHealthInstant());
        ((PlayerPropsImpl)mPlayerProps).setHasRadiationInstant(mInventory.hasRadiationInstant());
    }

    @Override
    public void addPlayerEventsListener(PlayerEventsListener playerEventsListener)
    {
        playerEventsListeners.add(playerEventsListener);
    }

    @Override
    public Frame setState(STATE state)
    {

        Log.e(TAG, "-*-*-*-*-*-*-*setState: " + state);
        this.getPlayerProps().setState(state);
        if(state.getCode() == Player.DEAD)
        {
            ((PlayerPropsImpl)mPlayerProps).setHealth(0);
            o.addProperty("health", 0);
        }
        validateState(this.playerState, state);
        this.playerState = state;
        o.addProperty("state", state.toString());
        serializer.serialize(PLAYER, this);
        return new FrameImpl(mPlayerProps);
    }

    @Override
    public boolean setFraction(FRACTION fraction)
    {
        FRACTION f = mPlayerProps.getFraction();
        if(f == fraction)
        {
            return false;
        }
        if(mPlayerProps.setFraction(fraction))
        {
            for(PlayerEventsListener l : playerEventsListeners)
            {
                l.onFractionChanged(fraction, f);
            }
        }
        o.addProperty("fraction", fraction.toString());
        serializer.serialize(PLAYER, this);
        return true;
    }

    @Override
    public boolean reborn()
    {
        mPlayerProps.addHealth(100);
        mPlayerProps.setRadiation(0);
        o.addProperty("health", 100);
        o.addProperty("radiation", 0);
        setState(STATE.ALIVE);
        return true;
    }

    @Override
    public JsonObject toJson()
    {
        return o;
    }

    private static class ItemAddedEvent implements ItemAdded
    {
        private Item item;
        private boolean levelChanged;
        private int level;
        private int xp;
        private int levelXpPercents;

        ItemAddedEvent(Item item)
        {
            this.item = item;
        }

        @Override
        public boolean levelChanged()
        {
            return levelChanged;
        }

        @Override
        public int getLevel()
        {
            return level;
        }

        @Override
        public int getXp()
        {
            return xp;
        }

        @Override
        public int getLevelXpPercents()
        {
            return levelXpPercents;
        }

        @Override
        public Item getItem()
        {
            return item;
        }

        private void setLevelChanged(boolean levelChanged)
        {
            this.levelChanged = levelChanged;
        }

        private void setLevelXpPercents(int xpPercents)
        {
            this.levelXpPercents = xpPercents;
        }

        private void setXp(int xp)
        {
            this.xp = xp;
        }

        private void setLevel(int level)
        {
            this.level = level;
        }
    }
}