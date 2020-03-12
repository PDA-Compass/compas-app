package net.afterday.compas.engine.engine.influences;

public interface InfluenceExtractionStrategy <T, I>
{
    I makeInfluences(T i);
}
