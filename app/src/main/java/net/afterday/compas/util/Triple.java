package net.afterday.compas.util;

/**
 * Created by spaka on 6/14/2018.
 */

public class Triple <A, B, C>
{
    public final A first;
    public final B second;
    public final C third;

    public Triple(A first, B second, C third)
    {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
