package net.afterday.compass.core.player;

import android.util.Log;

import net.afterday.compass.core.influences.Influence;

/**
 * Created by spaka on 4/18/2018.
 */

public class PlayerPropsImpl implements PlayerProps
{
    public static final int LEVEL_XP = 50;
    private static final String TAG = "PlayerPropsImpl";
    private Player.STATE mState;
    private double mRadiation;
    private double mRadiationImpact;
    private double mHealth;
    private double mBoosterPercents = 0;
    private double mArmorPercents = 0;
    private double[] mImpacts;
    private boolean burerHit;
    private boolean controllerHit;
    private boolean anomalyHit;
    private boolean mentalHit;
    private boolean hasHealthInstant,
                    hasRadiationInstant;
    private int xpPoints;

    public PlayerPropsImpl(Player.STATE state)
    {
        //Log.d(TAG, "---------- State: " + state);
        mState = state;
    }

    public PlayerPropsImpl(PlayerProps pProps)
    {
        mState = pProps.getState();
        mRadiation = pProps.getRadiation();
        mHealth = pProps.getHealth();
        mBoosterPercents = pProps.getBoosterPercents();
        mArmorPercents = pProps.getArmorPercents();
        xpPoints = pProps.getXpPoints();
        hasHealthInstant = pProps.hasHealthInstant();
        hasRadiationInstant = pProps.hasRadiationInstant();
    }

    @Override
    public double getHealth()
    {
        return mHealth;
    }

    @Override
    public double getRadiation()
    {
        return mRadiation;
    }

    @Override
    public double getArtefactImpact()
    {
        return mImpacts[Influence.ARTEFACT];
    }

    @Override
    public long getController()
    {
        return 0;
    }

    @Override
    public long getZombified()
    {
        return 0;
    }

    @Override
    public double getMental()
    {
        return 0;
    }

    @Override
    public double getRadiationImpact()
    {
        return mImpacts[Influence.RADIATION];
    }

    @Override
    public double getHealthImpact()
    {
        if (mImpacts != null && mImpacts.length >= Influence.HEALTH)
        {
            return mImpacts[Influence.HEALTH];
        }
        return 0d;
    }

    @Override
    public double getControllerImpact()
    {
        return mImpacts[Influence.CONTROLLER];
    }

    @Override
    public double getBurerImpact()
    {
        return mImpacts[Influence.BURER];
    }

    @Override
    public double getMentalImpact()
    {
        return mImpacts[Influence.MENTAL];
    }

    @Override
    public double getAnomalyImpact()
    {
        return mImpacts[Influence.ANOMALY];
    }

    @Override
    public double getBoosterPercents()
    {
        //return 100;
        return mBoosterPercents;
    }

    @Override
    public double getArmorPercents()
    {
        //return 100;
        return mArmorPercents;
    }

    @Override
    public void addHealth(double health)
    {
        setHealth(mHealth + health);
    }

    @Override
    public void addRadiation(double radiation)
    {
        setRadiation(mRadiation + radiation);
    }

    @Override
    public boolean addXpPoints(int xp)
    {
        int oldLevel = getLevel();
        xpPoints += xp;
        return oldLevel != getLevel();
    }

    @Override
    public void setXpPoints(int xp)
    {
        xpPoints = xp;
    }

    @Override
    public int getXpPoints()
    {
        return xpPoints;
    }

    @Override
    public int getLevel()
    {
        return calcLevel(xpPoints);
    }

    @Override
    public void subtractHealth(double health)
    {
        setHealth(mHealth - health);
    }

    @Override
    public void subtractRadiation(double radiation)
    {
        setRadiation(mRadiation - radiation);
    }

    @Override
    public void setBoosterPercents(double percents)
    {
        mBoosterPercents = normalize(percents);
    }

    @Override
    public void setArmorPercents(double percents)
    {
        mArmorPercents = normalize(percents);
    }

    @Override
    public Player.STATE getState()
    {
        return mState;
    }

    @Override
    public void setState(Player.STATE state)
    {
        mState = state;
    }

    private int calcLevel(int xp)
    {
        //Log.d(TAG, "calcLevel: " + xp + " --- " + xp / 50);
        return Math.min(1 + (xp / 50), 5);
    }

    public void setHealth(double health)
    {
        if (health > 100)
        {
            health = 100;
        } else if (health < 0)
        {
            health = 0;
        }
        //Log.d(TAG, "setHealth: " + health);
        mHealth = health;
    }

    public void setRadiation(double radiation)
    {
        //Log.d(TAG, "setRadiation: " + radiation);
        if (radiation > 16)
        {
            radiation = 16;
        } else if (radiation < 0)
        {
            radiation = 0;
        }
        mRadiation = radiation;
    }

    public void setAnomalyHit(boolean anomalyHit)
    {
        this.anomalyHit = anomalyHit;
    }

    public void setRadiationImpact(double radiationImpact)
    {
        mRadiationImpact = radiationImpact;
    }

    public void setImpacts(double[] influences)
    {
        mImpacts = influences;
    }

    private double normalize(double number)
    {
        if (number > 100)
        {
            return 100;
        } else if (number < 0)
        {
            return 0;
        }
        return number;
    }

    public void setBurerHit(boolean hit)
    {
        burerHit = hit;
    }

    public void setControllerHit(boolean hit)
    {
        controllerHit = hit;
    }

    public void setMentalHit(boolean hit)
    {
        mentalHit = hit;
    }

    @Override
    public boolean burerHit()
    {
        return burerHit;
    }

    @Override
    public boolean controllerHit()
    {
        return controllerHit;
    }

    @Override
    public boolean anomalyHit()
    {
        return anomalyHit;
    }

    @Override
    public boolean mentalHit()
    {
        return mentalHit;
    }

    public void setHasHealthInstant(boolean hasHealthInstant)
    {
        this.hasHealthInstant = hasHealthInstant;
    }

    public void setHasRadiationInstant(boolean hasRadiationInstant)
    {
        this.hasRadiationInstant = hasRadiationInstant;
    }

    @Override
    public boolean hasHealthInstant()
    {
        return hasHealthInstant;
    }

    @Override
    public boolean hasRadiationInstant()
    {
        return hasRadiationInstant;
    }

    @Override
    public int getLevelXp()
    {
        return 100 / LEVEL_XP * (xpPoints % LEVEL_XP);
    }

    @Override
    public String toString()
    {
        String str = "Player props:\n";
        str += "Radiation: " + mRadiation + ",\n";
        str += "RadiationImpact: " + mRadiationImpact + ",\n";
        str += "Health: " + mHealth + ",\n";
        str += "State: " + mState.toString();
        return str;
    }

    @Override
    public int getMaxImpactCode()
    {
        int max = 0;
        int index = -1;
        for(int i = 0; i < mImpacts.length; i++)
        {
            if(mImpacts[i] > max)
            {
                index = i;
            }
        }
        return index;
    }

}
