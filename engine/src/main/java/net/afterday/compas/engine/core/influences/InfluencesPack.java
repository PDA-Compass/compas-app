package net.afterday.compas.engine.core.influences;

import net.afterday.compas.engine.core.events.EventsPack;

public interface InfluencesPack extends EventsPack
{
    static final int WIFI = 0;
    static final int BLUETOOTH = 1;
    boolean influencedBy(int influenceType);
    boolean inDanger();
    boolean isClear();
    long creationTime();
    double[] getInfluences();
    double getInfluence(int inflType);
    void addInfluence(int inflType, double strength);
    void setEmission(boolean emission);
    boolean isEmission();
    int getSource();
}
