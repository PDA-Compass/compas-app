package net.afterday.compas.engine.core.player;

import net.afterday.compas.engine.common.Time;
import net.afterday.compas.engine.core.log.Log;

import net.afterday.compas.engine.core.influences.Influence;
import net.afterday.compas.engine.core.influences.InfluencesPack;
import net.afterday.compas.engine.core.inventory.items.Item;
import net.afterday.compas.engine.core.serialization.Serializer;
import net.afterday.compas.engine.game.Cooldown;
import net.afterday.compas.engine.util.Convert;

public class ImpactsImpl implements Impacts
{
    private static final String PLAYER_PROPS = "playerProps";
    private static final String TAG = "ImpactsImpl";

    private InfluencesPack inflPack;
    private PlayerProps oldProps;
    private PlayerPropsImpl newProps;
    private double healthModifier = 1;
    private double radModifier = 1;
    private double[] mImpacts;
    private long accumulatedBurer = 0;
    private long accumulatedController = 0;
    private long accumulatedAnomaly = 0;
    private long accumulatedMental = 0;
    private long accumulatedMonolith = 0;
    private long accumulatedEmission = 0;
    private Impacts.STATE state;

    public ImpactsImpl(PlayerProps playerProps, Serializer serializer)
    {
        newProps = (PlayerPropsImpl) playerProps;
    }

    public void prepare(InfluencesPack inflPack, long delta)
    {
        this.state = null;

        if(newProps.getState().getCode() == Player.DEAD && isHealing(newProps, inflPack))
        {
            newProps.setState(Player.STATE.ALIVE);
        }
        this.inflPack = inflPack;
        oldProps = newProps;
        newProps = new PlayerPropsImpl(oldProps);
        mImpacts = inflPack.getInfluences();

        healthModifier = 1;
        radModifier = 1;
    }

    public void healByInfluence(double delta)
    {
        newProps.setHealth(newProps.getHealth() + (2 * (delta / Time.MINUTE)));
        if(newProps.getHealth() >= 30)
        {
            newProps.setRadiation(newProps.getRadiation() - (radModifier * 3 * (delta / Time.MINUTE)));
        }
    }


    public void calculateAccumulated(double delta)
    {
        if(newProps.getState().getCode() == Player.DEAD)
        {
            return;
        }
        if((newProps.getRadiation() <= 1d) && (mImpacts[Influence.RADIATION] <= 0d) && !(state == STATE.DAMAGE))
        {
            //Esant sukaupus iki 1 SV radiacijos, gyvybe atsistato savaime, sukaupta radiacija po truputi išsinulina savaime po 100% gyvybes pasiekimo (1 SV per minute greiciu).
            newProps.setHealth(newProps.getHealth() + (healthModifier * 0.05 * (delta / Time.MINUTE)));
            newProps.setRadiation(newProps.getRadiation() - (radModifier * 0.005 * (delta / Time.MINUTE)));
        }else if(newProps.getRadiation() > 1d)
        {
            //Esant sukaupus virš 1 SV radiacijos, gyvybe pradeda po truputi mažeti, su greičiu, atitinkančiu prikauptai radiacijai
            double radMod = getRadMod(newProps.getRadiation());
            newProps.setHealth(newProps.getHealth() - radMod * (delta / Time.MINUTE));
            if(newProps.getHealth() <= 0)
            {
                newProps.setState(Player.STATE.W_DEAD_RADIATION);
            }
        }
    }

    private double getHealthToSet(int inflId, double delta, double strength)
    {
        if (strength < Influence.MIN)
        {
            return newProps.getHealth();
        }

        double multi = 0.5d;
        if (strength >= Influence.PEAK) {
            multi = inflId == Influence.RADIATION ? 10d : 4d;
        }else if (strength >= Influence.MAX) {
            multi = 2d;
        } else if (strength >= Influence.MED) {
            multi = 1d;
        }
        return newProps.getHealth() - multi * (delta / Time.MINUTE);
    }

    private double getRadiationToSet(double delta, double strength)
    {
        if(mImpacts[Influence.RADIATION] >= Influence.PEAK)
        {
            return newProps.getRadiation() + 1000 * (delta / Time.HOUR);
        }
        if(mImpacts[Influence.RADIATION] <= 0.1)
        {
            return newProps.getRadiation();
        }
        return newProps.getRadiation() + mImpacts[Influence.RADIATION] * (delta / Time.HOUR);
    }

    private boolean hit(int infl, long delta)
    {
        Log.e(TAG,"TryToHit: " + infl);
        switch (infl)
        {
            case Influence.BURER:
                accumulatedBurer += delta;
                if(accumulatedBurer >= Cooldown.BURER)
                {
                    accumulatedBurer = 0;
                    newProps.changeHealth(-20, infl);
                    newProps.setHit(Influence.BURER, true);
                    return true;
                }
                break;
            case Influence.CONTROLLER:
                accumulatedController += delta;
                if(accumulatedController >= Cooldown.CONTROLLER)
                {
                    accumulatedController = 0;
                    newProps.changeHealth(-20, infl);
                    newProps.setHit(Influence.CONTROLLER, true);
                    return true;
                }
                break;
            case Influence.ANOMALY:
                accumulatedAnomaly += delta;
                if(accumulatedAnomaly >= Cooldown.ANOMALY)
                {
                    accumulatedAnomaly = 0;
                    newProps.changeHealth(-30, infl);
                    newProps.setHit(Influence.ANOMALY, true);
                    return true;
                }
                break;
            case Influence.MENTAL:
                if(newProps.getFraction() == Player.FRACTION.MONOLITH)
                {
                    return false;
                }
                accumulatedMental += delta;
                if(accumulatedMental >= Cooldown.MENTAL)
                {
                    accumulatedMental = 0;
                    newProps.subtractHealth(30); //Ментальное излучение
                    newProps.changeHealth(-30, infl);
                    newProps.setHit(Influence.MENTAL, true);
                    return true;
                }
                break;
            case Influence.MONOLITH:
                if(newProps.getFraction() == Player.FRACTION.MONOLITH)
                {
                    return false;
                }
                accumulatedMonolith += delta;
                if(accumulatedMonolith >= Cooldown.MONOLITH)
                {
                    accumulatedMonolith = 0;
                    newProps.changeHealth(-10, infl);
                    newProps.setHit(Influence.MONOLITH, true);
                    return true;
                }
                break;
            case Influence.EMISSION:
                if(newProps.getFraction() == Player.FRACTION.MONOLITH)
                {
                    return false;
                }
                accumulatedEmission += delta;
                if(accumulatedEmission >= Cooldown.EMISSION)
                {
                    accumulatedEmission = 0;
                    newProps.changeHealth(-30, infl);
                    newProps.setHit(Influence.EMISSION, true);
                    return true;
                }
                break;
        }
        return false;
    }

    public void calculateEnvDamage(long delta)
    {
        Log.e(TAG, "calculateEnvDamage " + Thread.currentThread().getName());
        if(newProps.getFraction() == Player.FRACTION.GAMEMASTER)
        {
            return;
        }
        for(int i = 0; i < mImpacts.length; i++)
        {
            double strength = mImpacts[i];
            if(strength <= 0d
                    || i == Influence.HEALTH
                    || i == Influence.ARTEFACT
                    || (
                            (newProps.getFraction() == Player.FRACTION.MONOLITH)
                                    && (i == Influence.MENTAL || i == Influence.MONOLITH)
                                || (newProps.getFraction() == Player.FRACTION.DARKEN)
                                    && (i == Influence.RADIATION)))
            {
                continue;
            }
            if(i == Influence.RADIATION)
            {
                newProps.setHealth(getHealthToSet(i, delta, strength));
                if (newProps.getHealth() <= 0)
                {
                    newProps.setState(Player.STATE.W_DEAD_RADIATION);
                    break;
                }
                newProps.setRadiation(getRadiationToSet(delta, strength));
                continue;
            }else if (i == Influence.EMISSION && inflPack.isEmission() && strength >= Influence.MAX_SATELLITES && newProps.getFraction() != Player.FRACTION.MONOLITH && !hit(i, delta))
            {
                newProps.setHealth(newProps.getHealth() - 2d * ((double) delta / Time.MINUTE));
                if(newProps.getHealth() <= 0d)
                {
                    newProps.setState(Player.STATE.DEAD_EMISSION);
                    break;
                }
                continue;
            }else if(strength < Influence.PEAK || !hit(i, delta))
            {
                double h = getHealthToSet(i, delta, strength);
                Log.e(TAG, "------------------------------------------ health: " + h + " infl: " + i + " strength: " + strength);
                newProps.setHealth(h);
                if(newProps.getHealth() <= 0d)
                {
                    newProps.setState(Player.STATE.DeadStatusFromInfluence(i));
                    break;
                }
            }
        }
        Log.e(TAG, "" + newProps.getState());
        return;
    }

    public PlayerProps getPlayerProps()
    {
        newProps.setImpacts(mImpacts);
        return newProps;
    }

    private double getRadMod(double rad)
    {
        return Convert.map(rad, 1d,16d,1d, 7d);
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
        radModifier = Math.max(artifacts[Item.RADIATION_MODIFIER], 1) * Math.max(artifacts[Item.RADIATION_EMMITER], 1);
        healthModifier = artifacts[Item.HEALTH_MODIFIER];
        mImpacts[Influence.HEALTH] *= artifacts[Item.HEALTH_MODIFIER];
        mImpacts[Influence.RADIATION] += artifacts[Item.RADIATION_EMMITER];
        mImpacts[Influence.RADIATION] *= artifacts[Item.RADIATION_MODIFIER];
        mImpacts[Influence.ANOMALY] *= artifacts[Item.ANOMALY_MODIFIER];
        mImpacts[Influence.BURER] *= artifacts[Item.BURER_MODIFIER];
        mImpacts[Influence.CONTROLLER] *= artifacts[Item.CONTROLLER_MODIFIER];
        mImpacts[Influence.MENTAL] *= artifacts[Item.MENTAL_MODIFIER];
        mImpacts[Influence.MONOLITH] *= artifacts[Item.MONOLITH_MODIFIER];
    }

    @Override
    public void itemImpact(Item item)
    {
    }

    private void consumeItem(Item item)
    {
        if(item.hasModifier(Item.RADIATION_MODIFIER))
        {
            mImpacts[Influence.RADIATION] *= item.getModifier(Item.RADIATION_MODIFIER);
            radModifier *= item.getModifier(Item.RADIATION_MODIFIER);
        }
        if(item.hasModifier(Item.ANOMALY_MODIFIER))
        {
            mImpacts[Influence.ANOMALY] *= item.getModifier(Item.ANOMALY_MODIFIER);
        }
        if(item.hasModifier(Item.MENTAL_MODIFIER))
        {
            mImpacts[Influence.MENTAL] *= item.getModifier(Item.MENTAL_MODIFIER);
        }
        if(item.hasModifier(Item.MONOLITH_MODIFIER))
        {
            mImpacts[Influence.MONOLITH] *= item.getModifier(Item.MONOLITH_MODIFIER);
        }
        if(item.hasModifier(Item.CONTROLLER_MODIFIER))
        {
            mImpacts[Influence.CONTROLLER] *= item.getModifier(Item.CONTROLLER_MODIFIER);
        }
        if(item.hasModifier(Item.HEALTH_MODIFIER))
        {
            mImpacts[Influence.HEALTH] *= item.getModifier(Item.HEALTH_MODIFIER);
            healthModifier *= item.getModifier(Item.HEALTH_MODIFIER);
        }
    }

    public void addHealthModifier(double healthModifier)
    {
        this.healthModifier *= healthModifier;
    }

    @Override
    public void boosterImpact(Item item)
    {
        consumeItem(item);
    }

    public void deviceImpact(Item item)
    {
        consumeItem(item);
    }

    @Override
    public void armorImpact(Item item)
    {
        consumeItem(item);
    }

    @Override
    public Impacts.STATE getState()
    {
        if(this.state != null)
        {
            return this.state;
        }
        if(isHealing(newProps, inflPack))
        {
            this.state = Impacts.STATE.HEALING;
            return this.state;
        }
        boolean clear = true;
        for(int i = 0; i < mImpacts.length; i++)
        {
            if(i == Influence.ARTEFACT)
            {
                continue;
            }
            if((i == Influence.EMISSION || i == Influence.MENTAL) && newProps.getFraction() == Player.FRACTION.MONOLITH)
            {
                continue;
            }
            if(i == Influence.EMISSION && inflPack.isEmission() && mImpacts[i] >= Influence.MAX_SATELLITES)
            {
                clear = false;
                break;
            }
            if(mImpacts[i] > 0d)
            {
                clear = false;
                break;
            }
        }
        if(clear)
        {
            this.state = STATE.CLEAR;
            return this.state;
        }
        if(mImpacts[Influence.RADIATION] > 0d ||
           mImpacts[Influence.MENTAL] > 0d ||
                mImpacts[Influence.MONOLITH] > 0d ||
           mImpacts[Influence.CONTROLLER] > 0d ||
           mImpacts[Influence.ANOMALY] > 0d ||
           mImpacts[Influence.BURER] > 0d ||
                mImpacts[Influence.EMISSION] > Influence.MAX_SATELLITES
                )
        {
            this.state = STATE.DAMAGE;
            return this.state;
        }

        return STATE.CLEAR;
    }

    private boolean isHealing(PlayerProps playerProps, InfluencesPack inflPack)
    {
        Player.FRACTION fraction = playerProps.getFraction();
        return (fraction == Player.FRACTION.MONOLITH && inflPack.influencedBy(Influence.MONOLITH)) || (fraction != Player.FRACTION.MONOLITH && inflPack.influencedBy(Influence.HEALTH)) || (fraction == Player.FRACTION.DARKEN && inflPack.influencedBy(Influence.RADIATION)) || (fraction != Player.FRACTION.DARKEN && inflPack.influencedBy(Influence.HEALTH));
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
        Log.e(TAG, "MONOLITH: " + mImpacts[Influence.MONOLITH]);
    }
}
