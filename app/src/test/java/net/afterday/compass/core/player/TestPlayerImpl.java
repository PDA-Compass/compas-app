package net.afterday.compass.core.player;

import android.support.v4.util.Pair;

import net.afterday.compass.core.gameState.Frame;
import net.afterday.compass.core.influences.Influence;
import net.afterday.compass.core.influences.InfluencesPack;
import net.afterday.compass.core.inventory.items.Item;
import net.afterday.compass.engine.influences.InflPack;
import net.afterday.compass.core.inventory.items.ItemImpl;
import net.afterday.compass.persistency.items.ItemDescriptorImpl;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spaka on 4/28/2018.
 */


public class TestPlayerImpl
{
    private PlayerImpl player;
    private List<Pair<InfluencesPack, Long>> testdata = new ArrayList<>();
    private String lnMark = "-----------------------------------";
    private static final long MINUTE = 60 * 1000;

    public TestPlayerImpl()
    {
        player = new PlayerImpl();
    }

    @Test
    public void testAcceptInfluences()
    {
//        long min = 1000 * 60;
//        long delta = 1000 * 30;
//
//        println((100 - 50 * ((double)delta / min)) + " --- ");
//        return;

        test1();
//        test2();
       // addArtifacts();
        addBooster();
 //       mins10Clear();
        println(player.getProps().toString());
        println(lnMark);
        for(Pair<InfluencesPack, Long> ipl : testdata)
        {
            println("Delta: " + ipl.second.toString());
            println(ipl.first.toString());
            Frame f = player.acceptInfluences(ipl.first, ipl.second);
            println(f.getPlayerProps().toString());
            println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }
        assertEquals("shouldnt", "work");
    }

    private Pair<InfluencesPack, Long> clear(long delta)
    {
        return new Pair<>(new InflPack(), delta);
    }

    private Pair<InfluencesPack, Long> influencesPack(long delta, Pair<Integer, Double> ...infls)
    {
        InfluencesPack ip = new InflPack();
        for(int i = 0; i < infls.length; i++)
        {
            ip.addInfluence(infls[i].first, infls[i].second);
        }
        return new Pair<>(ip, delta);
    }

    private Pair<Integer, Double> pair(int i, double s)
    {
        return new Pair<>(i, s);
    }

    private void test1()
    {
        testdata.add(influencesPack(MINUTE, pair(Influence.RADIATION, 10000)));
        testdata.add(influencesPack(MINUTE, pair(Influence.RADIATION, 10000)));
        testdata.add(influencesPack(MINUTE, pair(Influence.RADIATION, 10000)));
        testdata.add(influencesPack(MINUTE, pair(Influence.RADIATION, 10000)));
        testdata.add(influencesPack(MINUTE, pair(Influence.RADIATION, 10000)));
//        testdata.add(influencesPack(MINUTE * 5, pair(Influence.RADIATION, 10000)));
    }

    private void test2()
    {
        testdata.add(influencesPack(MINUTE, pair(Influence.HEALTH, 10000)));
        testdata.add(influencesPack(MINUTE, pair(Influence.HEALTH, 10000)));
        testdata.add(influencesPack(MINUTE, pair(Influence.HEALTH, 10000)));
        testdata.add(influencesPack(MINUTE, pair(Influence.HEALTH, 10000)));
        testdata.add(influencesPack(MINUTE, pair(Influence.HEALTH, 10000)));
        testdata.add(influencesPack(MINUTE, pair(Influence.HEALTH, 10000)));
        testdata.add(influencesPack(MINUTE, pair(Influence.HEALTH, 10000)));
        testdata.add(influencesPack(MINUTE, pair(Influence.HEALTH, 10000)));
    }

    private void mins10Clear()
    {
        testdata.add(clear(MINUTE));
        testdata.add(clear(MINUTE));
        testdata.add(clear(MINUTE));
        testdata.add(clear(MINUTE));
        testdata.add(clear(MINUTE));
        testdata.add(clear(MINUTE));
        testdata.add(clear(MINUTE));
        testdata.add(clear(MINUTE));
        testdata.add(clear(MINUTE));
        testdata.add(clear(MINUTE));
    }

    private void addBooster()
    {
        Item b = new ItemImpl(new ItemDescriptorImpl("Test").addModifier(Item.RADIATION_MODIFIER, 0).setBooster(true).setDuration(MINUTE));
        player.addItem(b);
        player.useItem(b);
    }

    private void addArtifacts()
    {
        player.addItem(new ItemImpl(new ItemDescriptorImpl("Test").addModifier(Item.RADIATION_EMMITER, 5).setArtefact(true)));
    }

    private void println(String str)
    {
        System.out.println(str);
    }
}
