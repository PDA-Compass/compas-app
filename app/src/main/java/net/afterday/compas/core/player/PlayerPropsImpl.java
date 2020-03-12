package net.afterday.compas.core.player;

import android.util.Log;

import com.google.gson.JsonObject;

import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.serialization.Jsonable;

/**
 * Created by spaka on 4/18/2018.
 */

public class PlayerPropsImpl implements PlayerProps
{
    public static final int LEVEL_XP = 50;
    private static final String TAG = "PlayerPropsImpl";
    private Player.STATE state;
    private double radiation;
    private double radiationImpact;
    private double health;
    private double boosterPercents = 0;
    private double devicePercents = 0;
    private double armorPercents = 0;
    private double[] impacts;
    private boolean burerHit;
    private boolean controllerHit;
    private boolean anomalyHit;
    private boolean mentalHit;
    private boolean monolithHit;
    private boolean emissionHit;
    private boolean hasHealthInstant,
                    hasRadiationInstant;
    private Player.FRACTION fraction;
    private int xpPoints;
    private JsonObject o;

    public PlayerPropsImpl(Player.STATE state)
    {
        this.state = state;
    }

    public PlayerPropsImpl(PlayerProps pProps)
    {
        state = pProps.getState();
        radiation = pProps.getRadiation();
        health = pProps.getHealth();
        boosterPercents = pProps.getBoosterPercents();
        devicePercents = pProps.getDevicePercents();
        armorPercents = pProps.getArmorPercents();
        xpPoints = pProps.getXpPoints();
        hasHealthInstant = pProps.hasHealthInstant();
        hasRadiationInstant = pProps.hasRadiationInstant();
        fraction = pProps.getFraction();
    }
    
    public PlayerPropsImpl(Jsonable jsonable)
    {
        
    }

    @Override
    public double getHealth()
    {
        return health;
        //Nemirtingumas testavimo tikslams
        //return Math.max(health, 1);
    }

    @Override
    public double getRadiation()
    {
        return radiation;
    }

    @Override
    public double getArtefactImpact()
    {
        if(impacts != null && impacts.length > Influence.ARTEFACT)
        {
            return impacts[Influence.ARTEFACT];
        }
        return 0;
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
        if(impacts != null && impacts.length >= Influence.RADIATION)
        {
            return impacts[Influence.RADIATION];
        }
        return 0;
    }

    @Override
    public double getHealthImpact()
    {
        if(impacts != null)
        {
            if(fraction == Player.FRACTION.MONOLITH && impacts.length >= Influence.MONOLITH)
            {
                return impacts[Influence.MONOLITH];
            }
            if(fraction == Player.FRACTION.DARKEN && impacts.length >= Influence.RADIATION)
            {
                return impacts[Influence.RADIATION];
            }
            else if(impacts.length >= Influence.HEALTH)
            {
                return impacts[Influence.HEALTH];
            }
        }
        return 0d;
    }

    @Override
    public double getControllerImpact()
    {
        if (impacts != null && impacts.length >= Influence.CONTROLLER)
        {
            return impacts[Influence.CONTROLLER];
        }
        return 0;
    }

    @Override
    public double getBurerImpact()
    {
        if (impacts != null && impacts.length >= Influence.BURER)
        {
            return impacts[Influence.BURER];
        }
        return 0;
    }

    @Override
    public double getMentalImpact()
    {
        if (impacts != null && impacts.length >= Influence.MENTAL)
        {
            return impacts[Influence.MENTAL];
        }
        return 0;
    }

    @Override
    public double getMonolithImpact()
    {
        if (impacts != null && impacts.length >= Influence.MONOLITH)
        {
            return impacts[Influence.MONOLITH];
        }
        return 0;
    }

    @Override
    public double getAnomalyImpact()
    {
        if (impacts != null && impacts.length >= Influence.ANOMALY)
        {
            return impacts[Influence.ANOMALY];
        }
        return 0;
    }

    @Override
    public double getBoosterPercents()
    {
        //return 100;
        return boosterPercents;
    }

    @Override
    public double getDevicePercents()
    {
        //return 100;
        return devicePercents;
    }

    @Override
    public double getArmorPercents()
    {
        //return 100;
        return armorPercents;
    }

    @Override
    public void addHealth(double health)
    {
        setHealth(this.health + health);
    }

    @Override
    public void addRadiation(double radiation)
    {
        setRadiation(this.radiation + radiation);
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
        setHealth(this.health - health);
    }

    @Override
    public void subtractRadiation(double radiation)
    {
        setRadiation(this.radiation - radiation);
    }

    @Override
    public void setBoosterPercents(double percents)
    {
        boosterPercents = normalize(percents);
    }

    @Override
    public void setDevicePercents(double percents)
    {
        devicePercents = normalize(percents);
    }

    @Override
    public void setArmorPercents(double percents)
    {
        armorPercents = normalize(percents);
    }

    @Override
    public Player.STATE getState()
    {
        return state;
    }

    @Override
    public void setState(Player.STATE state)
    {
        this.state = state;
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
        Log.d(TAG, "setHealth: " + health);
        this.health = health;
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
        this.radiation = radiation;
    }

    public void setAnomalyHit(boolean anomalyHit)
    {
        this.anomalyHit = anomalyHit;
    }

    public void setRadiationImpact(double radiationImpact)
    {
        this.radiationImpact = radiationImpact;
    }

    public void setImpacts(double[] influences)
    {
        impacts = influences;
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

    public void setMonolithHit(boolean hit)
    {
        monolithHit = hit;
    }

    public void setEmissionHit(boolean hit)
    {
        emissionHit = true;
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

    @Override
    public boolean monolithHit()
    {
        return monolithHit;
    }

    @Override
    public boolean emissionHit()
    {
        return emissionHit;
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
    public boolean setFraction(Player.FRACTION fraction)
    {
        if(this.fraction == fraction)
        {
            return false;
        }
        this.fraction = fraction;
        return true;
    }

    @Override
    public Player.FRACTION getFraction()
    {
        return fraction;
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
        str += "Radiation: " + radiation + ",\n";
        str += "RadiationImpact: " + radiationImpact + ",\n";
        str += "Health: " + health + ",\n";
        str += "State: " + state.toString();
        return str;
    }

    @Override
    public double[] getImpacts()
    {
        return impacts;
//        int max = 0;
//        int index = -1;
//        for(int i = 0; i < impacts.length; i++)
//        {
//            if(impacts[i] > max)
//            {
//                index = i;
//            }
//        }
//        return index;
    }

    @Override
    public JsonObject toJson()
    {
        return null;
    }
}