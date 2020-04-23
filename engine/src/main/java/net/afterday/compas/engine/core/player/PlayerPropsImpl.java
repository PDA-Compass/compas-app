package net.afterday.compas.engine.core.player;

import net.afterday.compas.engine.common.Time;
import net.afterday.compas.engine.core.log.Log;

import com.google.gson.JsonObject;

import net.afterday.compas.engine.core.influences.Influence;
import net.afterday.compas.engine.core.serialization.Jsonable;
import org.jetbrains.annotations.NotNull;

public class PlayerPropsImpl implements PlayerProps
{
    public static final int LEVEL_XP = 50;
    private static final String TAG = "PlayerPropsImpl";
    private Player.STATE state;
    private double radiation;
    private double radiationImpact;
    private double health;
    private double[] impacts;

    private boolean[] hits;
    private boolean hasHealthInstant,
                    hasRadiationInstant;
    private Player.FRACTION fraction;
    private int xpPoints;
    private JsonObject o;

    public PlayerPropsImpl(Player.STATE state)
    {
        hits = new boolean[Influence.INFLUENCE_COUNT];
        this.state = state;
    }

    public PlayerPropsImpl(@NotNull PlayerProps pProps)
    {
        hits = new boolean[Influence.INFLUENCE_COUNT];
        state = pProps.getState();
        radiation = pProps.getRadiation();
        health = pProps.getHealth();

        xpPoints = pProps.getXpPoints();
        hasHealthInstant = pProps.hasHealthInstant();
        hasRadiationInstant = pProps.hasRadiationInstant();
        fraction = pProps.getFraction();
    }
    
    public PlayerPropsImpl(Jsonable jsonable)
    {
        hits = new boolean[Influence.INFLUENCE_COUNT];
    }

    //region Health
    @Override
    public double getHealth()
    {
        return health;
    }

    @Override
    public void addHealth(double health)
    {
        setHealth(this.health + health);
    }

    public void setHealth(double health)
    {
        this.health = normalize(health, 0, 100);
        Log.d(TAG, "setHealth: " + this.health);
    }

    /**
     * Change health use delta and influence
     * @return true when player alive.
     */
    @Override
    public boolean changeHealth(double delta, int influence) {
        this.setHealth(this.getHealth() - delta);
        if(this.getHealth() <= 0d)
        {
            this.setState(Player.STATE.DeadStatusFromInfluence(influence));
            return false;
        }
        return true;
    }
    //endregion

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
        if(impacts != null)
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
            if(fraction == Player.FRACTION.MONOLITH)
            {
                return impacts[Influence.MONOLITH];
            }
            if(fraction == Player.FRACTION.DARKEN)
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
    public float getImpact(int influence) {
        if (impacts != null)
        {
            return (float)impacts[influence];
        }
        return 0;
    }

    @Override
    public boolean isHit(int influence) {
        return hits[influence];
    }

    @Override
    public void setHit(int influence, boolean value) {
        hits[influence] = value;
    }

    @Override
    public double getControllerImpact()
    {
        if (impacts != null)
        {
            return impacts[Influence.CONTROLLER];
        }
        return 0;
    }

    @Override
    public double getBurerImpact()
    {
        if (impacts != null)
        {
            return impacts[Influence.BURER];
        }
        return 0;
    }

    @Override
    public double getMentalImpact()
    {
        if (impacts != null)
        {
            return impacts[Influence.MENTAL];
        }
        return 0;
    }

    @Override
    public double getMonolithImpact()
    {
        if (impacts != null)
        {
            return impacts[Influence.MONOLITH];
        }
        return 0;
    }

    @Override
    public double getAnomalyImpact()
    {
        if (impacts != null)
        {
            return impacts[Influence.ANOMALY];
        }
        return 0;
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

    public void setRadiation(double radiation)
    {
        //Log.d(TAG, "setRadiation: " + radiation);
        this.radiation = normalize(radiation, 0, 16);
    }

    public void setImpacts(double[] influences)
    {
        impacts = influences;
    }

    private double normalize(double number)
    {
        return normalize(number, 0, 100);
    }
    private double normalize(double number, double min, double max)
    {
        if (number > max)
        {
            return max;
        } else if (number < min)
        {
            return min;
        }
        return number;
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
    public void setFraction(Player.FRACTION fraction)
    {
        this.fraction = fraction;
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
    }

    @Override
    public JsonObject toJson()
    {
        return null;
    }
}