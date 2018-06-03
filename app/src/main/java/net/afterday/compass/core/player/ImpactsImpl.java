package net.afterday.compass.core.player;

import android.util.Log;

import net.afterday.compass.core.influences.Influence;
import net.afterday.compass.core.influences.InfluencesPack;
import net.afterday.compass.core.inventory.items.Item;
import net.afterday.compass.core.serialization.Serializer;
import net.afterday.compass.util.Convert;

/**
 * Created by spaka on 4/18/2018.
 */

public class ImpactsImpl implements Impacts
{
    private static final String PLAYER_PROPS = "playerProps";
    private static final String TAG = "ImpactsImpl";
    private InfluencesPack inflPack;
    private static final int MINUTE = 60 * 1000;
    private static final int HOUR = MINUTE * 60;
    private static final int BURER_COOLDOWN = 5 * 1000;
    private static final int CONTROLLER_COOLDOWN = BURER_COOLDOWN;
    private static final int MENTAL_COOLDOWN = BURER_COOLDOWN;
    private static final int ANOMALY_COOLDOWN = BURER_COOLDOWN;

    private PlayerProps oldProps;
    private PlayerPropsImpl newProps;
    private double healthModifier = 1;
    private double radModifier = 1;
    private double[] mImpacts;
    private long delta;
    private long accumulatedBurer = 0;
    private long accumulatedController = 0;
    private long accumulatedAnomaly = 0;
    private long accumulatedMental = 0;
    private long controllerLastHit = 0;
    private long burerLastHit = 0;

    public ImpactsImpl(PlayerProps playerProps, Serializer serializer)
    {
        newProps = (PlayerPropsImpl) playerProps;
    }

    public void prepare(InfluencesPack inflPack, long delta)
    {
        if(inflPack.influencedBy(Influence.HEALTH) && newProps.getState().getCode() == Player.DEAD)
        {
            newProps.setState(Player.STATE.ALIVE);
        }
        this.inflPack = inflPack;
        oldProps = newProps;
        newProps = new PlayerPropsImpl(oldProps);
        mImpacts = inflPack.getInfluences();

        this.delta = delta;
        healthModifier = 1;
        radModifier = 1;
        return;
    }

    public void healByInfluence(long delta)
    {
        newProps.setHealth(newProps.getHealth() + (2 * ((double)delta / MINUTE)));
        if(newProps.getHealth() >= 30)
        {
            newProps.setRadiation(newProps.getRadiation() - (radModifier * 3 * ((double)delta / MINUTE)));
        }
//        if(newProps.getState() != Player.STATE.ALIVE)
//        {
//            Log.e(TAG, "---------------healByInfluence5 " + Thread.currentThread().getName() + "  " + newProps);
//            newProps.setState(Player.STATE.ALIVE);
//        }
//        Log.e(TAG, "healByInfluence6 " + Thread.currentThread().getName() + "  " + newProps);
    }


    public void calculateAccumulated(long delta)
    {
        if(newProps.getState().getCode() == Player.DEAD)
        {
            return;
        }
        if(newProps.getRadiation() <= 1 && !inflPack.inDanger())
        {
            //Esant sukaupus iki 1 SV radiacijos, gyvybe atsistato savaime, sukaupta radiacija po truputi išsinulina savaime po 100% gyvybes pasiekimo (1 SV per minute greiciu).
            newProps.setHealth(newProps.getHealth() + (healthModifier * 0.5 * ((double)delta / MINUTE)));
            newProps.setRadiation(newProps.getRadiation() - (radModifier * 0.5 * ((double)delta / MINUTE)));
//            if(newProps.getHealth() == 100)
//            {
//
//            }
        }else if(newProps.getRadiation() > 1)
        {
            //Esant sukaupus virš 1 SV radiacijos, gyvybe pradeda po truputi mažeti, su greičiu, atitinkančiu prikauptai radiacijai
            double radMod = getRadMod(newProps.getRadiation());
            newProps.setHealth(newProps.getHealth() - radMod * ((double) delta / MINUTE));
            if(newProps.getHealth() <= 0)
            {
                newProps.setState(Player.STATE.W_DEAD_RADIATION);
            }
        }
    }


    public void updateProps(long delta)
    {
        if(oldProps.getRadiation() <= 1)
        {
            newProps.setHealth(oldProps.getHealth() + (healthModifier * 0.5 * ((double)delta / MINUTE)));
            if(oldProps.getHealth() == 100)
            {
                newProps.setRadiation(oldProps.getRadiation() - (1 / (radModifier * ((double)delta / MINUTE))));
            }
        }else if(oldProps.getRadiation() > 1)
        {
            newProps.setHealth(oldProps.getHealth() - getRadMod(oldProps.getRadiation()) * 5 * ((double) delta / MINUTE));
        }
    }

    private void calculateEnvDamage()
    {

    }

    private double getHealthToSet(int inflId, long delta, double strength)
    {
        if (strength >= Influence.PEAK)
        {
            if(inflId == Influence.RADIATION)
            {
                return newProps.getHealth() - 10 * ((double) delta / MINUTE);
            }
            return newProps.getHealth();  //Nes kiti influencai dauzo kas 5 sekundes.
        }
        if(strength >= Influence.MAX)
        {
            return newProps.getHealth() - 2 * ((double) delta / MINUTE);
        }
        if(strength >= Influence.MED)
        {
            return newProps.getHealth() - 1 * ((double) delta / MINUTE);
        }
        if(strength >= Influence.MIN)
        {
            return newProps.getHealth() - 0.5 * ((double) delta / MINUTE);
        }
        return newProps.getHealth();
    }

    private double getRadiationToSet(long delta, double strength)
    {
        if(mImpacts[Influence.RADIATION] >= Influence.PEAK)
        {
            return 16;
        }
        if(mImpacts[Influence.RADIATION] <= 0.1)
        {
            return newProps.getRadiation();
        }
        return newProps.getRadiation() + mImpacts[Influence.RADIATION] * ((double) delta / HOUR);
    }

    private boolean hit(int infl, long delta)
    {
        switch (infl)
        {
            case Influence.BURER:
                accumulatedBurer += delta;
                if(accumulatedBurer >= BURER_COOLDOWN)
                {
                    accumulatedBurer = 0;
                    newProps.subtractHealth(20);
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_BURER);
                    }
                    newProps.setBurerHit(true);
                    return true;
                }
                return false;
            case Influence.CONTROLLER:
                accumulatedController += delta;
                if(accumulatedController >= CONTROLLER_COOLDOWN)
                {
                    accumulatedController = 0;
                    newProps.subtractHealth(20);
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_CONTROLLED);
                    }
                    newProps.setControllerHit(true);
                    return true;
                }
                return false;
            case Influence.ANOMALY:
                accumulatedAnomaly += delta;
                if(accumulatedAnomaly >= ANOMALY_COOLDOWN)
                {
                    accumulatedAnomaly = 0;
                    newProps.subtractHealth(20);
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_ANOMALY);
                    }
                    newProps.setAnomalyHit(true);
                    return true;
                }
                return false;
            case Influence.MENTAL:
                accumulatedMental += delta;
                if(accumulatedMental >= MENTAL_COOLDOWN)
                {
                    accumulatedMental = 0;
                    newProps.subtractHealth(20);
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_MENTALLED);
                    }
                    newProps.setMentalHit(true);
                    return true;
                }
                return false;
        }
        return false;
    }

    private Player.STATE getDeathState(int infl)
    {
        switch (infl)
        {
            case Influence.ANOMALY: return Player.STATE.W_DEAD_ANOMALY;
            case Influence.BURER: return Player.STATE.W_DEAD_BURER;
            case Influence.CONTROLLER: return Player.STATE.W_CONTROLLED;
            case Influence.MENTAL: return Player.STATE.W_MENTALLED;
            case Influence.RADIATION: return Player.STATE.DEAD_RADIATION;
        }
        return Player.STATE.ALIVE;
    }

    public void calculateEnvDamage(long delta)
    {
        for(int i = 0; i < mImpacts.length; i++)
        {
            if(i == Influence.HEALTH || i == Influence.ARTEFACT)
            {
                continue;
            }
            double strength = mImpacts[i];
            if(i == Influence.RADIATION)
            {
                newProps.setHealth(getHealthToSet(i, delta, strength));
                if(newProps.getHealth() <= 0)
                {
                    newProps.setState(Player.STATE.W_DEAD_RADIATION);
                    break;
                }
                newProps.setRadiation(getRadiationToSet(delta, strength));
                continue;
            }else if(strength < Influence.PEAK)
            {
                newProps.setHealth(getHealthToSet(i, delta, mImpacts[i]));
                if(newProps.getHealth() <= 0)
                {
                    newProps.setState(getDeathState(i));
                    break;
                }
                continue;
            }
            hit(i, delta);
            if(newProps.getHealth() <= 0)
            {
                break;
            }
        }
        return;
//        double r = mImpacts[Influence.RADIATION];
//        if(r > 0.1)
//        {
//            if(r >= 15d)
//            {
//                //Log.e(TAG, "Health reduced: " + (oldProps.getHealth() - 10 * ((double) delta / MINUTE)));
//                newProps.setHealth(newProps.getHealth() - 10 * ((double) delta / MINUTE));
//                newProps.setRadiation(16);
//            }else if(r >= 7)
//            {
//                //Log.w(TAG, "Health reduced: " + (oldProps.getHealth() - 2 * ((double) delta / MINUTE)));
//                newProps.setHealth(newProps.getHealth() - 2 * ((double) delta / MINUTE));
//                newProps.setRadiation(newProps.getRadiation() + getRadMod(r) * ((double) delta / MINUTE));
//            }else if(r >= 1)
//            {
//                //Log.w(TAG, "Health reduced: " + (oldProps.getHealth() - 1 * ((double) delta / MINUTE)));
//                newProps.setHealth(newProps.getHealth() - 1 * ((double) delta / MINUTE));
//                newProps.setRadiation(newProps.getRadiation() + getRadMod(r) * ((double) delta / MINUTE));
//            }else if(r >= 0.1)
//            {
//                //Log.w(TAG, "Health reduced: " + (oldProps.getHealth() - 0.5 * ((double) delta / MINUTE)));
//                newProps.setHealth(newProps.getHealth() - 0.5 * ((double) delta / MINUTE));
//                newProps.setRadiation(newProps.getRadiation() + getRadMod(r) * ((double) delta / MINUTE));
//            }
//            if(newProps.getHealth() <= 0)
//            {
//                newProps.setState(Player.STATE.W_DEAD_RADIATION);
//            }
//        }
//
//        double c = mImpacts[Influence.CONTROLLER];
//        if(c > 0.1)
//        {
//            Log.d(TAG, "CONTROLLER: " + c);
//            if(c >= 15d)
//            {
//                Log.d(TAG, "CONTROLLER PEAK");
//                accumulatedController += delta;
//                if(accumulatedController >= CONTROLLER_COOLDOWN)
//                {
//                    Log.d(TAG, "CONTROLLER PEAK: accumulatedController >= CONTROLLER_COOLDOWN");
//                    newProps.subtractHealth(20);
//                    accumulatedController = 0;
//                    if(newProps.getHealth() == 0)
//                    {
//                        newProps.setState(Player.STATE.W_CONTROLLED);
//                    }
//                    newProps.setControllerHit(true);
//                }
//            }
//        }
//
//        double b = mImpacts[Influence.BURER];
//        if(b > 0.1)
//        {
//            Log.d(TAG, "BURER: " + b);
//            if(b >= 15d)
//            {
//                Log.d(TAG, "BURER PEAK");
//                accumulatedBurer += delta;
//                if(accumulatedBurer >= BURER_COOLDOWN)
//                {
//                    Log.d(TAG, "BURER PEAK: accumulatedBurer >= BURER_COOLDOWN");
//                    newProps.subtractHealth(20);
//                    accumulatedBurer = 0;
//                    if(newProps.getHealth() == 0)
//                    {
//                        newProps.setState(Player.STATE.W_DEAD_BURER);
//                    }
//                    newProps.setBurerHit(true);
//                }
//            }
//        }
//
//        double a = mImpacts[Influence.ANOMALY];
//        if(a > 0.1)
//        {
//            Log.d(TAG, "ANOMALY: " + a);
//            if(a >= 15d)
//            {
//                Log.d(TAG, "ANOMALY PEAK");
//                accumulatedAnomaly += delta;
//                if(accumulatedAnomaly >= BURER_COOLDOWN)
//                {
//                    Log.d(TAG, "BURER PEAK: accumulatedBurer >= BURER_COOLDOWN");
//                    newProps.subtractHealth(20);
//                    accumulatedAnomaly = 0;
//                    if(newProps.getHealth() == 0)
//                    {
//                        newProps.setState(Player.STATE.W_DEAD_ANOMALY);
//                    }
//                    newProps.setAnomalyHit(true);
//                }
//            }
//        }
//
//        double m = mImpacts[Influence.MENTAL];
//        if(m > 0.1)
//        {
//            Log.d(TAG, "CONTROLLER: " + m);
//            if(m >= 15d)
//            {
//                Log.d(TAG, "CONTROLLER PEAK");
//                accumulatedController += delta;
//                if(accumulatedController >= MENTAL_COOLDOWN)
//                {
//                    Log.d(TAG, "CONTROLLER PEAK: accumulatedController >= CONTROLLER_COOLDOWN");
//                    newProps.subtractHealth(20);
//                    accumulatedController = 0;
//                    if(newProps.getHealth() == 0)
//                    {
//                        newProps.setState(Player.STATE.W_MENTALLED);
//                    }
//                    newProps.setControllerHit(true);
//                }
//            }
//        }
    }

    private Player.STATE getStateAfterDeath(int inflCode)
    {
        switch (inflCode)
        {
            case Influence.BURER: return Player.STATE.W_DEAD_BURER;
            case Influence.ANOMALY: return Player.STATE.W_DEAD_ANOMALY;
            case Influence.CONTROLLER: return Player.STATE.W_CONTROLLED;
            case Influence.MENTAL: return Player.STATE.W_MENTALLED;
            case Influence.RADIATION: return Player.STATE.W_DEAD_RADIATION;
        }
        return null;
    }

    public PlayerProps calculateRadiation()
    {

        return null;
    }

    public void artefactsImpact(long delta)
    {

    }

    public void setRadModifier(double radiationModifier)
    {

    }

    public void setPsiModifier(double psiModifier)
    {

    }

    public void setAnoModifier(double anoModifier)
    {

    }

    public void setBurerModifier(double burerModifier)
    {

    }

    public void setControllerModifier(double controllerModifier)
    {

    }

    public PlayerProps getPlayerProps()
    {
        newProps.setImpacts(mImpacts);

        return newProps;
    }

    public void addRadiation(double radiation)
    {
        radModifier += radiation;
    }

    public void addRadiationModifier(double radModifier)
    {
        radModifier *= radModifier;
    }

    public void addAnomalyModifier(double anoModifier)
    {

    }

    public void addBurerModifier(double burModifier)
    {

    }

    public void addControllerModifier(double ctrlModifier)
    {

    }

    public void addMentalModifier(double menModifier)
    {

    }

    public void setBooster(Item booster)
    {

    }

    public void setArmor(Item armor)
    {

    }

    private double getRadMod(double rad)
    {
        return Convert.map(rad, 1d,16d,1d, 8d);
    }

    @Override
    public void artifactsImpact(double[] artifacts)
    {
        healthModifier = 1;
        radModifier = 1;
        if(inflPack.influencedBy(Influence.HEALTH))
        {
            healthModifier = artifacts[Item.HEALTH_MODIFIER];
            mImpacts[Influence.HEALTH] *= artifacts[Item.HEALTH_MODIFIER];
            return;
        }
        Log.d(TAG, " -- " + artifacts[Item.CONTROLLER_MODIFIER]);
        radModifier = artifacts[Item.RADIATION_MODIFIER] + artifacts[Item.RADIATION_EMMITER];
        healthModifier = artifacts[Item.HEALTH_MODIFIER];
        mImpacts[Influence.HEALTH] *= artifacts[Item.HEALTH_MODIFIER];
        mImpacts[Influence.RADIATION] += artifacts[Item.RADIATION_EMMITER];
        mImpacts[Influence.RADIATION] *= artifacts[Item.RADIATION_MODIFIER];
        mImpacts[Influence.ANOMALY] *= artifacts[Item.ANOMALY_MODIFIER];
        mImpacts[Influence.BURER] *= artifacts[Item.BURER_MODIFIER];
        mImpacts[Influence.CONTROLLER] *= artifacts[Item.CONTROLLER_MODIFIER];
        mImpacts[Influence.MENTAL] *= artifacts[Item.MENTAL_MODIFIER];
    }

    @Override
    public void itemImpact(Item item)
    {
        if(item.hasModifier(Item.ANOMALY_MODIFIER))
        {

        }
    }

    private void consumeItem(Item item, long delta)
    {
        if(item.hasModifier(Item.RADIATION_MODIFIER))
        {
            mImpacts[Influence.RADIATION] *= item.getModifier(Item.RADIATION_MODIFIER);
        }
        if(item.hasModifier(Item.ANOMALY_MODIFIER))
        {
            mImpacts[Influence.ANOMALY] *= item.getModifier(Item.ANOMALY_MODIFIER);
        }
        if(item.hasModifier(Item.MENTAL_MODIFIER))
        {
            mImpacts[Influence.MENTAL] *= item.getModifier(Item.MENTAL_MODIFIER);
        }
        if(item.hasModifier(Item.CONTROLLER_MODIFIER))
        {
            mImpacts[Influence.CONTROLLER] *= item.getModifier(Item.CONTROLLER_MODIFIER);
        }
        if(item.hasModifier(Item.HEALTH_MODIFIER))
        {
            mImpacts[Influence.HEALTH] *= item.getModifier(Item.HEALTH_MODIFIER);
        }
        //item.consume(delta);
    }

    @Override
    public void boosterImpact(Item item)
    {
        consumeItem(item, delta);
        newProps.setBoosterPercents(item.getPercentsLeft());
    }

    @Override
    public void armorImpact(Item item)
    {
        consumeItem(item, delta);
        newProps.setArmorPercents(item.getPercentsLeft());
    }

    @Override
    public Impacts.STATE getState()
    {
        if(inflPack.influencedBy(Influence.HEALTH))
        {
            return Impacts.STATE.HEALING;
        }
        boolean clear = true;
        for(int i = 0; i < mImpacts.length; i++)
        {
            if(mImpacts[i] != 0 && i != Influence.ARTEFACT)
            {
                clear = false;
                break;
            }
        }
        if(clear)
        {
            return STATE.CLEAR;
        }
        if(mImpacts[Influence.RADIATION] > 0 ||
           mImpacts[Influence.MENTAL] > 0 ||
           mImpacts[Influence.CONTROLLER] > 0 ||
           mImpacts[Influence.ANOMALY] > 0 ||
           mImpacts[Influence.BURER] > 0)
        {
            return STATE.DAMAGE;
        }

        return null;
    }

    private void printImpacts()
    {
        Log.e(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<<Impacts:");
        Log.e(TAG, "HEALTH: " + mImpacts[Influence.HEALTH]);
        Log.e(TAG, "RADIATION: " + mImpacts[Influence.RADIATION]);
        Log.e(TAG, "MENTAL: " + mImpacts[Influence.MENTAL]);
        Log.e(TAG, "CONTROLLER: " + mImpacts[Influence.CONTROLLER]);
        Log.e(TAG, "ANOMALY: " + mImpacts[Influence.ANOMALY]);
        Log.e(TAG, "BURER: " + mImpacts[Influence.BURER]);
        Log.e(TAG, "ARTEFACT: " + mImpacts[Influence.ARTEFACT]);
    }

    ///////////// determining state

}
