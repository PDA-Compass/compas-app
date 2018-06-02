package net.afterday.compass.core.player;

import android.util.Log;

import net.afterday.compass.core.events.PlayerEventsListener;
import net.afterday.compass.core.gameState.Frame;
import net.afterday.compass.core.gameState.FrameImpl;
import net.afterday.compass.core.influences.InfluencesPack;
import net.afterday.compass.core.inventory.Inventory;

import net.afterday.compass.core.inventory.items.Events.ItemAdded;
import net.afterday.compass.core.inventory.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spaka on 4/18/2018.
 */

public class PlayerImpl implements Player
{
    private static final String TAG = "PlayerImpl";
    private long mLastInfls = System.currentTimeMillis();
    private PlayerProps mPlayerProps;
    private Inventory mInventory;
    private STATE playerState;
    private ImpactsImpl impacts;
    private Impacts.STATE impactsState;
    private static final long MINUTE = 60 * 1000;
    private List<PlayerEventsListener> playerEventsListeners = new ArrayList<>();
    public PlayerImpl(Inventory inventory)
    {
        playerState = STATE.ALIVE;
        mPlayerProps = new PlayerPropsImpl(playerState);
        ((PlayerPropsImpl)mPlayerProps).setHealth(100d);
        ((PlayerPropsImpl)mPlayerProps).setRadiation(0d);
        mInventory = inventory;
        this.impacts = new ImpactsImpl(mPlayerProps);
    }

    public Frame acceptInfluences(InfluencesPack inflPack, long delta, long now)
    {
//        long now = System.currentTimeMillis();
//        //long delta = now - mLastInfls;
//        mLastInfls = now;
        //Log.e(TAG, "acceptInfluences: ")
//        if(inflPack.influencedBy(Influence.HEALTH))
//        {
//            boolean breakPoint = true;
//        }
        impacts.prepare(inflPack, delta, now);
        //Log.e(TAG,"!!!!!!!!!!!!!!!! acceptInfluences 1 " + Thread.currentThread().getName() + " -- " + impacts);
        impacts.artifactsImpact(mInventory.getArtifacts());
        if(mInventory.hasActiveBooster())
        {
            impacts.boosterImpact(mInventory.consumeBooster(delta));
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
        mPlayerProps = impacts.getPlayerProps();
        Player.STATE ps = mPlayerProps.getState();
        validateState(playerState, ps);
        Impacts.STATE is = impacts.getState();
        validateImpactsState(impactsState, is);
        playerState = ps;
        impactsState = is;
        mPlayerProps.setState(ps);
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
        if(prevState == STATE.ABDUCTED && newState == STATE.DEAD_BURER)
        {
            //mPlayerProps = mPlayerProps;
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
        return acceptInfluences(inflPack, delta, now);
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
    public boolean acceptsInfluence(int inflId)
    {

        return false;
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
    public boolean addItem(String code)
    {
        Item item = mInventory.addItem(code);
        if(item != null)
        {
            boolean changed = mPlayerProps.addXpPoints(item.getItemDescriptor().getXpPoints());
            ItemAddedEvent e = new ItemAddedEvent(item);
            e.setLevelXpPercents(mPlayerProps.getLevelXp());
            e.setLevel(mPlayerProps.getLevel());
            e.setLevelChanged(changed);
            mInventory.setPlayerLevel(e.getLevel());
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
        return dropped;
    }

    @Override
    public Frame useItem(Item item)
    {
        mPlayerProps = mInventory.useItem(item, mPlayerProps);
        setInstants();
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
        if(state == STATE.DEAD_BURER)
        {
            boolean t = true; //breakpoint
        }
        this.getPlayerProps().setState(state);
        if(state.getCode() == Player.DEAD)
        {
            ((PlayerPropsImpl)mPlayerProps).setHealth(0);
        }
        validateState(this.playerState, state);
        this.playerState = state;
        return new FrameImpl(mPlayerProps);
    }

    public PlayerProps getProps()
    {
        return mPlayerProps;
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