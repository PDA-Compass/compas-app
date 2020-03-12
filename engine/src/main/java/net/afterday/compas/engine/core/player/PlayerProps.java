package net.afterday.compas.engine.core.player;

import net.afterday.compas.engine.core.serialization.Jsonable;

public interface PlayerProps  extends Jsonable, PlayerPropsNew
{
    Player.STATE getState();
    void setState(Player.STATE state);

    //region Xp
    int getLevelXp();
    boolean addXpPoints(int xp);
    void setXpPoints(int xp);
    int getXpPoints();
    int getLevel();
    //endregion

    long getController();
    long getZombified();
    double getMental();

    //region Impact
    double getArtefactImpact();
    double getRadiationImpact();
    double getHealthImpact();
    double getControllerImpact();
    double getBurerImpact();
    double getMentalImpact();
    double getMonolithImpact();
    double getAnomalyImpact();

    double[] getImpacts();
    float getImpact(int influence);
    boolean isHit(int influence);
    void setHit(int influence, boolean value);
    //endregion

    //region Radiation
    double getRadiation();
    void addRadiation(double radiation);
    void setRadiation(double radiation);
    //endregion

    void subtractRadiation(double radiation);
    boolean hasHealthInstant();
    boolean hasRadiationInstant();


}
