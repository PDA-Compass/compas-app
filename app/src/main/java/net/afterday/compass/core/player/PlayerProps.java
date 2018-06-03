package net.afterday.compass.core.player;

import net.afterday.compass.core.serialization.Jsonable;

/**
 * Created by spaka on 4/20/2018.
 */

public interface PlayerProps extends Jsonable
{
    double getHealth();
    double getRadiation();
    double getArtefactImpact();
    long getController();
    long getZombified();
    double getMental();
    double getRadiationImpact();
    double getHealthImpact();
    double getControllerImpact();
    double getBurerImpact();
    double getMentalImpact();
    double getAnomalyImpact();
    double getBoosterPercents();
    double getArmorPercents();
    void addHealth(double health);
    void addRadiation(double radiation);
    void setState(Player.STATE state);
    boolean addXpPoints(int xp);
    void setXpPoints(int xp);
    int getXpPoints();
    int getLevel();
    void subtractHealth(double health);
    void subtractRadiation(double radiation);
    void setBoosterPercents(double percents);
    void setArmorPercents(double percents);
    boolean burerHit();
    boolean controllerHit();
    boolean anomalyHit();
    boolean mentalHit();
    boolean hasHealthInstant();
    boolean hasRadiationInstant();
    int getLevelXp();
    Player.STATE getState();
    int getMaxImpactCode();
}
