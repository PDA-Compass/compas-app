package net.afterday.compass.models;

public class Influence {
    public String name;
    public int signal;

    public int radiation;
    public int anomaly;
    public int mental;
    public int burer;
    public int controller;
    public int health;

    public int radiationDistance;
    public int anomalyDistance;
    public int mentalDistance;
    public int burerDistance;
    public int controllerDistance;
    public int healthDistance;

    @Override
    public String toString() {
        return name + " (" + signal + ")";
    }
}
