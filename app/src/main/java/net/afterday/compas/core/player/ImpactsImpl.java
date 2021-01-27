package net.afterday.compas.core.player;

import android.provider.ContactsContract;
import android.util.Log;

import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.influences.InfluencesPack;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.serialization.Serializer;
import net.afterday.compas.util.Convert;

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
    private static final int BURER_COOLDOWN = 10 * 1000; //Бюрер
    private static final int CONTROLLER_COOLDOWN = 10 * 1000; //Контролёр
    private static final int MENTAL_COOLDOWN = 10 * 1000; //Ментальное излучение
    private static final int ANOMALY_COOLDOWN = 10 * 1000; //Аномалия
    private static final int SPRINGBOARD_COOLDOWN = 10 * 1000; //Трамплин
    private static final int FUNNEL_COOLDOWN = 10 * 1000; //Воронка
    private static final int CAROUSEL_COOLDOWN = 10 * 1000; //Карусель
    private static final int ELEVATOR_COOLDOWN = 10 * 1000; //Лифт
    private static final int FRYING_COOLDOWN = 10 * 1000; //Жарка
    private static final int ELECTRA_COOLDOWN = 10 * 1000; //Электра
    private static final int MEATGRINDER_COOLDOWN = 10 * 1000; //Мясорубка
    private static final int KISSEL_COOLDOWN = 10 * 1000; //Кисель
    private static final int SODA_COOLDOWN = 10 * 1000; //Газировка
    private static final int ACIDFOG_COOLDOWN = 10 * 1000; //Кислотный туман
    private static final int BURNINGFLUFF_COOLDOWN = 10 * 1000; //Жгучий пух
    private static final int RUSTYHAIR_COOLDOWN = 10 * 1000; //Ржавые волосы
    private static final int SPATIALBUBBLE_COOLDOWN = 10 * 1000; //Пространственный пузырь
    private static final int MONOLITH_COOLDOWN = 15 * 1000; //Зов Монолита
    private static final int EMISSION_COOLDOWN = 15 * 1000; //Выброс

    private PlayerProps oldProps;
    private PlayerPropsImpl newProps;
    private double healthModifier = 1;
    private double radModifier = 1;
    private double[] mImpacts;
    private long delta;
    private long accumulatedBurer = 0;
    private long accumulatedController = 0;
    private long accumulatedAnomaly = 0;
    private long accumulatedSpringboard = 0;
    private long accumulatedFunnel = 0;
    private long accumulatedCarousel = 0;
    private long accumulatedElevator = 0;
    private long accumulatedFrying = 0;
    private long accumulatedElectra = 0;
    private long accumulatedMeatgrinder = 0;
    private long accumulatedKissel = 0;
    private long accumulatedSoda = 0;
    private long accumulatedAcidfog = 0;
    private long accumulatedBurningfluff = 0;
    private long accumulatedRustyhair = 0;
    private long accumulatedSpatialbubble = 0;
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
        if((newProps.getRadiation() <= 1d) && (mImpacts[Influence.RADIATION] <= 0d) && !(state == STATE.DAMAGE))
        {
            //Esant sukaupus iki 1 SV radiacijos, gyvybe atsistato savaime, sukaupta radiacija po truputi išsinulina savaime po 100% gyvybes pasiekimo (1 SV per minute greiciu).
            newProps.setHealth(newProps.getHealth() + (healthModifier * 0.05 * ((double)delta / MINUTE)));
            newProps.setRadiation(newProps.getRadiation() - (radModifier * 0.005 * ((double)delta / MINUTE)));
//            if(newProps.getHealth() == 100)
//            {
//
//            }
        }else if(newProps.getRadiation() > 1d)
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

    private double getHealthToSet(int inflId, long delta, double strength)
    {
        if (strength >= Influence.PEAK)
        {
            if(inflId == Influence.RADIATION)
            {
                return newProps.getHealth() - 10d * ((double) delta / MINUTE);
            }
            return newProps.getHealth() - 4d * ((double) delta / MINUTE);  //Nes kiti influencai dauzo kas 5 sekundes.
        }
        if(strength >= Influence.MAX)
        {
            return newProps.getHealth() - 2d * ((double) delta / MINUTE);
        }
        if(strength >= Influence.MED)
        {
            return newProps.getHealth() - 1d * ((double) delta / MINUTE);
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
            return newProps.getRadiation() + 1000 * ((double) delta / HOUR);
        }
        if(mImpacts[Influence.RADIATION] <= 0.1)
        {
            return newProps.getRadiation();
        }
        return newProps.getRadiation() + mImpacts[Influence.RADIATION] * ((double) delta / HOUR);
    }

    private boolean hit(int infl, long delta)
    {
        Log.e(TAG,"TryToHit: " + infl);
        switch (infl)
        {
            case Influence.BURER:
                accumulatedBurer += delta;
                if(accumulatedBurer >= BURER_COOLDOWN)
                {
                    accumulatedBurer = 0;
                    newProps.subtractHealth(20); //Бюрер
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
                    newProps.subtractHealth(20); //Контролёр
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
                    newProps.subtractHealth(30); //Аномалия
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_ANOMALY);
                    }
                    newProps.setAnomalyHit(true);
                    return true;
                }
                return false;
            case Influence.SPRINGBOARD:
                accumulatedSpringboard += delta;
                if(accumulatedSpringboard >= SPRINGBOARD_COOLDOWN)
                {
                    accumulatedSpringboard = 0;
                    newProps.subtractHealth(30); //Трамплин
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_SPRINGBOARD);
                    }
                    newProps.setSpringboardHit(true);
                    return true;
                }
                return false;
            case Influence.FUNNEL:
                accumulatedFunnel += delta;
                if(accumulatedFunnel >= FUNNEL_COOLDOWN)
                {
                    accumulatedFunnel = 0;
                    newProps.subtractHealth(30); //Воронка
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_FUNNEL);
                    }
                    newProps.setFunnelHit(true);
                    return true;
                }
                return false;
            case Influence.CAROUSEL:
                accumulatedCarousel += delta;
                if(accumulatedCarousel >= CAROUSEL_COOLDOWN)
                {
                    accumulatedCarousel = 0;
                    newProps.subtractHealth(30); //Карусель
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_CAROUSEL);
                    }
                    newProps.setCarouselHit(true);
                    return true;
                }
                return false;
            case Influence.ELEVATOR:
                accumulatedElevator += delta;
                if(accumulatedElevator >= ELEVATOR_COOLDOWN)
                {
                    accumulatedElevator = 0;
                    newProps.subtractHealth(30); //Лифт
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_ELEVATOR);
                    }
                    newProps.setElevatorHit(true);
                    return true;
                }
                return false;
            case Influence.FRYING:
                accumulatedFrying += delta;
                if(accumulatedFrying >= FRYING_COOLDOWN)
                {
                    accumulatedFrying = 0;
                    newProps.subtractHealth(30); //Жарка
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_FRYING);
                    }
                    newProps.setFryingHit(true);
                    return true;
                }
                return false;
            case Influence.ELECTRA:
                accumulatedElectra += delta;
                if(accumulatedElectra >= ELECTRA_COOLDOWN)
                {
                    accumulatedElectra = 0;
                    newProps.subtractHealth(30); //Электра
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_ELECTRA);
                    }
                    newProps.setElectraHit(true);
                    return true;
                }
                return false;
            case Influence.MEATGRINDER:
                accumulatedMeatgrinder += delta;
                if(accumulatedMeatgrinder >= MEATGRINDER_COOLDOWN)
                {
                    accumulatedMeatgrinder = 0;
                    newProps.subtractHealth(30); //Мясорубка
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_MEATGRINDER);
                    }
                    newProps.setMeatgrinderHit(true);
                    return true;
                }
                return false;
            case Influence.KISSEL:
                accumulatedKissel += delta;
                if(accumulatedKissel >= KISSEL_COOLDOWN)
                {
                    accumulatedKissel = 0;
                    newProps.subtractHealth(30); //Кисель
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_KISSEL);
                    }
                    newProps.setKisselHit(true);
                    return true;
                }
                return false;
            case Influence.SODA:
                accumulatedSoda += delta;
                if(accumulatedSoda >= SODA_COOLDOWN)
                {
                    accumulatedSoda = 0;
                    newProps.subtractHealth(30); //Газировка
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_SODA);
                    }
                    newProps.setSodaHit(true);
                    return true;
                }
                return false;
            case Influence.ACIDFOG:
                accumulatedAcidfog += delta;
                if(accumulatedAcidfog >= ACIDFOG_COOLDOWN)
                {
                    accumulatedAcidfog = 0;
                    newProps.subtractHealth(30); //Кислотный туман
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_ACIDFOG);
                    }
                    newProps.setAcidfogHit(true);
                    return true;
                }
                return false;
            case Influence.BURNINGFLUFF:
                accumulatedBurningfluff += delta;
                if(accumulatedBurningfluff >= BURNINGFLUFF_COOLDOWN)
                {
                    accumulatedBurningfluff = 0;
                    newProps.subtractHealth(30); //Жгучий пух
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_BURNINGFLUFF);
                    }
                    newProps.setBurningfluffHit(true);
                    return true;
                }
                return false;
            case Influence.RUSTYHAIR:
                accumulatedRustyhair += delta;
                if(accumulatedRustyhair >= RUSTYHAIR_COOLDOWN)
                {
                    accumulatedRustyhair = 0;
                    newProps.subtractHealth(30); //Ржавые волосы
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_RUSTYHAIR);
                    }
                    newProps.setRustyhairHit(true);
                    return true;
                }
                return false;
            case Influence.SPATIALBUBBLE:
                accumulatedSpatialbubble += delta;
                if(accumulatedSpatialbubble >= SPATIALBUBBLE_COOLDOWN)
                {
                    accumulatedSpatialbubble = 0;
                    newProps.subtractHealth(30); //Пространственный пузырь
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_DEAD_SPATIALBUBBLE);
                    }
                    newProps.setSpatialbubbleHit(true);
                    return true;
                }
                return false;
            case Influence.MENTAL:
                if(newProps.getFraction() == Player.FRACTION.MONOLITH)
                {
                    return false;
                }
                accumulatedMental += delta;
                if(accumulatedMental >= MENTAL_COOLDOWN)
                {
                    accumulatedMental = 0;
                    newProps.subtractHealth(30); //Ментальное излучение
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_MENTALLED);
                    }
                    newProps.setMentalHit(true);
                    return true;
                }
                return false;
            case Influence.MONOLITH:
                if(newProps.getFraction() == Player.FRACTION.MONOLITH)
                {
                    return false;
                }
                accumulatedMonolith += delta;
                if(accumulatedMonolith >= MONOLITH_COOLDOWN)
                {
                    accumulatedMonolith = 0;
                    newProps.subtractHealth(10); //Зов Монолита
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.W_MENTALLED);
                    }
                    newProps.setMonolithHit(true);
                    return true;
                }
                return false;
            case Influence.EMISSION:
                if(newProps.getFraction() == Player.FRACTION.MONOLITH)
                {
                    return false;
                }
                accumulatedEmission += delta;
                if(accumulatedEmission >= EMISSION_COOLDOWN)
                {
                    accumulatedEmission = 0;
                    newProps.subtractHealth(30); //Выброс
                    if(newProps.getHealth() <= 0)
                    {
                        newProps.setState(Player.STATE.DEAD_EMISSION);
                    }
                    newProps.setEmissionHit(true);
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
            case Influence.SPRINGBOARD: return Player.STATE.W_DEAD_SPRINGBOARD;
            case Influence.FUNNEL: return Player.STATE.W_DEAD_FUNNEL;
            case Influence.CAROUSEL: return Player.STATE.W_DEAD_CAROUSEL;
            case Influence.ELEVATOR: return Player.STATE.W_DEAD_ELEVATOR;
            case Influence.FRYING: return Player.STATE.W_DEAD_FRYING;
            case Influence.ELECTRA: return Player.STATE.W_DEAD_ELECTRA;
            case Influence.MEATGRINDER: return Player.STATE.W_DEAD_MEATGRINDER;
            case Influence.KISSEL: return Player.STATE.W_DEAD_KISSEL;
            case Influence.SODA: return Player.STATE.W_DEAD_SODA;
            case Influence.ACIDFOG: return Player.STATE.W_DEAD_ACIDFOG;
            case Influence.BURNINGFLUFF: return Player.STATE.W_DEAD_BURNINGFLUFF;
            case Influence.RUSTYHAIR: return Player.STATE.W_DEAD_RUSTYHAIR;
            case Influence.SPATIALBUBBLE: return Player.STATE.W_DEAD_SPATIALBUBBLE;
            case Influence.BURER: return Player.STATE.W_DEAD_BURER;
            case Influence.CONTROLLER: return Player.STATE.W_CONTROLLED;
            case Influence.MENTAL: return Player.STATE.W_MENTALLED;
            case Influence.RADIATION: return Player.STATE.DEAD_RADIATION;
            case Influence.EMISSION: return Player.STATE.DEAD_EMISSION;
        }
        return Player.STATE.ALIVE;
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
            if(strength <= 0d || i == Influence.HEALTH || i == Influence.ARTEFACT || ((newProps.getFraction() == Player.FRACTION.MONOLITH) && (i == Influence.MENTAL || i == Influence.MONOLITH) || (newProps.getFraction() == Player.FRACTION.DARKEN) && (i == Influence.RADIATION)))
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
                newProps.setHealth(newProps.getHealth() - 2d * ((double) delta / MINUTE));
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
                    newProps.setState(getDeathState(i));
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
        mImpacts[Influence.SPRINGBOARD] *= artifacts[Item.SPRINGBOARD_MODIFIER];
        mImpacts[Influence.FUNNEL] *= artifacts[Item.FUNNEL_MODIFIER];
        mImpacts[Influence.CAROUSEL] *= artifacts[Item.CAROUSEL_MODIFIER];
        mImpacts[Influence.ELEVATOR] *= artifacts[Item.ELEVATOR_MODIFIER];
        mImpacts[Influence.FRYING] *= artifacts[Item.FRYING_MODIFIER];
        mImpacts[Influence.ELECTRA] *= artifacts[Item.ELECTRA_MODIFIER];
        mImpacts[Influence.MEATGRINDER] *= artifacts[Item.MEATGRINDER_MODIFIER];
        mImpacts[Influence.KISSEL] *= artifacts[Item.KISSEL_MODIFIER];
        mImpacts[Influence.SODA] *= artifacts[Item.SODA_MODIFIER];
        mImpacts[Influence.ACIDFOG] *= artifacts[Item.ACIDFOG_MODIFIER];
        mImpacts[Influence.BURNINGFLUFF] *= artifacts[Item.BURNINGFLUFF_MODIFIER];
        mImpacts[Influence.RUSTYHAIR] *= artifacts[Item.RUSTYHAIR_MODIFIER];
        mImpacts[Influence.SPATIALBUBBLE] *= artifacts[Item.SPATIALBUBBLE_MODIFIER];
        mImpacts[Influence.BURER] *= artifacts[Item.BURER_MODIFIER];
        mImpacts[Influence.CONTROLLER] *= artifacts[Item.CONTROLLER_MODIFIER];
        mImpacts[Influence.MENTAL] *= artifacts[Item.MENTAL_MODIFIER];
        mImpacts[Influence.MONOLITH] *= artifacts[Item.MONOLITH_MODIFIER];
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
            radModifier *= item.getModifier(Item.RADIATION_MODIFIER);
        }
        if(item.hasModifier(Item.ANOMALY_MODIFIER))
        {
            mImpacts[Influence.ANOMALY] *= item.getModifier(Item.ANOMALY_MODIFIER);
        }
        if(item.hasModifier(Item.SPRINGBOARD_MODIFIER))
        {
            mImpacts[Influence.SPRINGBOARD] *= item.getModifier(Item.SPRINGBOARD_MODIFIER);
        }
        if(item.hasModifier(Item.FUNNEL_MODIFIER))
        {
            mImpacts[Influence.FUNNEL] *= item.getModifier(Item.FUNNEL_MODIFIER);
        }
        if(item.hasModifier(Item.CAROUSEL_MODIFIER))
        {
            mImpacts[Influence.CAROUSEL] *= item.getModifier(Item.CAROUSEL_MODIFIER);
        }
        if(item.hasModifier(Item.ELEVATOR_MODIFIER))
        {
            mImpacts[Influence.ELEVATOR] *= item.getModifier(Item.ELEVATOR_MODIFIER);
        }
        if(item.hasModifier(Item.FRYING_MODIFIER))
        {
            mImpacts[Influence.FRYING] *= item.getModifier(Item.FRYING_MODIFIER);
        }
        if(item.hasModifier(Item.ELECTRA_MODIFIER))
        {
            mImpacts[Influence.ELECTRA] *= item.getModifier(Item.ELECTRA_MODIFIER);
        }
        if(item.hasModifier(Item.MEATGRINDER_MODIFIER))
        {
            mImpacts[Influence.MEATGRINDER] *= item.getModifier(Item.MEATGRINDER_MODIFIER);
        }
        if(item.hasModifier(Item.KISSEL_MODIFIER))
        {
            mImpacts[Influence.KISSEL] *= item.getModifier(Item.KISSEL_MODIFIER);
        }
        if(item.hasModifier(Item.SODA_MODIFIER))
        {
            mImpacts[Influence.SODA] *= item.getModifier(Item.SODA_MODIFIER);
        }
        if(item.hasModifier(Item.ACIDFOG_MODIFIER))
        {
            mImpacts[Influence.ACIDFOG] *= item.getModifier(Item.ACIDFOG_MODIFIER);
        }
        if(item.hasModifier(Item.BURNINGFLUFF_MODIFIER))
        {
            mImpacts[Influence.BURNINGFLUFF] *= item.getModifier(Item.BURNINGFLUFF_MODIFIER);
        }
        if(item.hasModifier(Item.RUSTYHAIR_MODIFIER))
        {
            mImpacts[Influence.RUSTYHAIR] *= item.getModifier(Item.RUSTYHAIR_MODIFIER);
        }
        if(item.hasModifier(Item.SPATIALBUBBLE_MODIFIER))
        {
            mImpacts[Influence.SPATIALBUBBLE] *= item.getModifier(Item.SPATIALBUBBLE_MODIFIER);
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
        consumeItem(item, delta);
        newProps.setBoosterPercents(item.getPercentsLeft());
    }

    public void deviceImpact(Item item)
    {
        consumeItem(item, delta);
        newProps.setDevicePercents(item.getPercentsLeft());
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
                mImpacts[Influence.SPRINGBOARD] > 0d ||
                mImpacts[Influence.FUNNEL] > 0d ||
                mImpacts[Influence.CAROUSEL] > 0d ||
                mImpacts[Influence.ELEVATOR] > 0d ||
                mImpacts[Influence.FRYING] > 0d ||
                mImpacts[Influence.ELECTRA] > 0d ||
                mImpacts[Influence.MEATGRINDER] > 0d ||
                mImpacts[Influence.KISSEL] > 0d ||
                mImpacts[Influence.SODA] > 0d ||
                mImpacts[Influence.ACIDFOG] > 0d ||
                mImpacts[Influence.BURNINGFLUFF] > 0d ||
                mImpacts[Influence.RUSTYHAIR] > 0d ||
                mImpacts[Influence.SPATIALBUBBLE] > 0d ||
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
        Log.e(TAG, "SPRINGBOARD: " + mImpacts[Influence.SPRINGBOARD]);
        Log.e(TAG, "FUNNEL: " + mImpacts[Influence.FUNNEL]);
        Log.e(TAG, "CAROUSEL: " + mImpacts[Influence.CAROUSEL]);
        Log.e(TAG, "ELEVATOR: " + mImpacts[Influence.ELEVATOR]);
        Log.e(TAG, "FRYING: " + mImpacts[Influence.FRYING]);
        Log.e(TAG, "ELECTRA: " + mImpacts[Influence.ELECTRA]);
        Log.e(TAG, "MEATGRINDER: " + mImpacts[Influence.MEATGRINDER]);
        Log.e(TAG, "KISSEL: " + mImpacts[Influence.KISSEL]);
        Log.e(TAG, "SODA: " + mImpacts[Influence.SODA]);
        Log.e(TAG, "ACIDFOG: " + mImpacts[Influence.ACIDFOG]);
        Log.e(TAG, "BURNINGFLUFF: " + mImpacts[Influence.BURNINGFLUFF]);
        Log.e(TAG, "RUSTYHAIR: " + mImpacts[Influence.RUSTYHAIR]);
        Log.e(TAG, "SPATIALBUBBLE: " + mImpacts[Influence.SPATIALBUBBLE]);
        Log.e(TAG, "BURER: " + mImpacts[Influence.BURER]);
        Log.e(TAG, "ARTEFACT: " + mImpacts[Influence.ARTEFACT]);
        Log.e(TAG, "MONOLITH: " + mImpacts[Influence.MONOLITH]);
    }
}
