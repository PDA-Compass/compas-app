package net.afterday.compas.engine.core.player;

import net.afterday.compas.engine.core.inventory.Inventory;
import net.afterday.compas.engine.core.log.Log;

import com.google.gson.JsonObject;

import net.afterday.compas.engine.core.events.PlayerEventsListener;
import net.afterday.compas.engine.core.gameState.Frame;
import net.afterday.compas.engine.core.gameState.FrameImpl;
import net.afterday.compas.engine.core.influences.InfluencesPack;

import net.afterday.compas.engine.core.inventory.items.Events.ItemAdded;
import net.afterday.compas.engine.core.inventory.items.Item;
import net.afterday.compas.engine.core.serialization.Jsonable;
import net.afterday.compas.engine.core.serialization.Serializer;
import net.afterday.compas.engine.core.inventory.items.ItemDescriptor;

import java.util.ArrayList;
import java.util.List;

public class PlayerImpl implements Player
{
    private static final String TAG = "PlayerImpl";
    private static final String PLAYER = "player";
    private long mLastInfls = System.currentTimeMillis();

    private PlayerProps mPlayerProps;
    private Inventory mInventory;
    private Equipment mEquipment;

    private STATE playerState;

    private ImpactsImpl impacts;
    private Impacts.STATE impactsState;

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

        mInventory = inventory;
        mPlayerProps = new PlayerPropsImpl(playerState);
        mPlayerProps.setFraction(f);

        mEquipment = new EquipmentImpl();
        this.setInstants();

        ((PlayerPropsImpl)mPlayerProps).setHealth(health);
        mPlayerProps.setRadiation(rad);
        mPlayerProps.setXpPoints(xp);

        this.impacts = new ImpactsImpl(mPlayerProps, serializer);
    }

    public Frame acceptInfluences(InfluencesPack inflPack, long delta)
    {
        impacts.prepare(inflPack, delta);
        impacts.artifactsImpact(mInventory.getArtifacts());
        if(mEquipment.getBooster() != null)
        {
            mEquipment.consumeBooster(delta);
            impacts.boosterImpact(mEquipment.getBooster());
        }
        if(mEquipment.getDevice() != null)
        {
            mEquipment.consumeDevice(delta);
            impacts.deviceImpact(mEquipment.getDevice());
        }
        if(impacts.getState() == Impacts.STATE.HEALING)
        {
            impacts.healByInfluence(delta);
            return makeFrame(impacts);
        }
        if(playerState.getCode() != Player.ALIVE)
        {
            return makeFrame(impacts);
        }
        if(impacts.getState() == Impacts.STATE.CLEAR)
        {
            if(mEquipment.getArmor() != null && mEquipment.getArmor().hasModifier(Item.HEALTH_MODIFIER))
            {
                impacts.armorImpact(mEquipment.getArmor());
            }
            impacts.calculateAccumulated(delta);
            return makeFrame(impacts);
        }
        if(impacts.getState() == Impacts.STATE.DAMAGE)
        {
            if(mEquipment.getArmor() != null)
            {
                mEquipment.consumeArmor(delta);
                impacts.armorImpact(mEquipment.getArmor());
            }
            impacts.calculateAccumulated(delta);
            impacts.calculateEnvDamage(delta);
            return makeFrame(impacts);
        }

        return new FrameImpl(mPlayerProps, mEquipment);
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
        return new FrameImpl(mPlayerProps, mEquipment);
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
    }

    public Frame acceptInfluences(InfluencesPack inflPack)
    {
        long now = System.currentTimeMillis();
        long delta = now - mLastInfls;
        mLastInfls = now;
        return acceptInfluences(inflPack, delta);
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
    public Equipment getEquipment() { return mEquipment; }

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
        Boolean isUsed = false;
        isUsed = mEquipment.useItem(item);

        if (!isUsed){
            mPlayerProps = mInventory.useItem(item, mPlayerProps);
        }

        setInstants();
        for(PlayerEventsListener l : playerEventsListeners)
        {
            l.onItemUsed(item);
        }
        return new FrameImpl(mPlayerProps, mEquipment);
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
        return new FrameImpl(mPlayerProps, mEquipment);
    }

    @Override
    public boolean setFraction(FRACTION fraction)
    {
        FRACTION f = mPlayerProps.getFraction();
        if(f == fraction)
        {
            return false;
        }

        if(mPlayerProps.getFraction() != fraction)
        {
            mPlayerProps.setFraction(fraction);
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