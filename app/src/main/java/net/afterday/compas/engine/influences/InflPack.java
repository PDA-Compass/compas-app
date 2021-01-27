package net.afterday.compas.engine.influences;

import android.util.Log;

import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.influences.InfluencesPack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justas Spakauskas on 3/4/2018.
 */

public class InflPack implements InfluencesPack
{
    private List<Influence> influences;
    private static final String TAG = "InflPack";
    private long time;
    private long averageInflUpdatingTime;

    private boolean mInfRad = false;
    private boolean mInflAno = false;
    private boolean mInflSpr = false;
    private boolean mInflFun = false;
    private boolean mInflCar = false;
    private boolean mInflElev = false;
    private boolean mInflFry = false;
    private boolean mInflElec = false;
    private boolean mInflMea = false;
    private boolean mInflKis = false;
    private boolean mInflSod = false;
    private boolean mInflAci = false;
    private boolean mInflBurn = false;
    private boolean mInflRus = false;
    private boolean mInflSpa = false;
    private boolean mInflMen = false;
    private boolean mInflBur = false;
    private boolean mInflCon = false;
    private boolean mInflHel = false;
    private boolean mInflArt = false;
    private boolean mInflMon = false;
    private boolean mInflEm = false;
    private boolean isEmission = false;
    private double[] mInfluences = new double[Influence.INFLUENCE_COUNT];

    public InflPack()
    {
        this.influences = new ArrayList<>();
        this.time = System.currentTimeMillis();

        for(int i = 0; i < Influence.INFLUENCE_COUNT; i++)
        {
            mInfluences[i] = 0;
        }
//        //Log.d(TAG, "*************************  Constructor " + influences);
//        this.influences = influences == null ? new ArrayList<>() : influences;
    }


    public void addInfluence(int type, double strength)
    {
        Log.d(TAG, "addInfluence: " + getInfluenceName(type) + " " + strength);
        mInfluences[type] = Math.max(mInfluences[type], strength);
//        Log.d(TAG, influence.getTypeId() + " influence added");
        switch (type)
        {
            case Influence.RADIATION: mInfRad = true; break;
            case Influence.ANOMALY: mInflAno = true; break;
            case Influence.SPRINGBOARD: mInflSpr = true; break;
            case Influence.FUNNEL: mInflFun = true; break;
            case Influence.CAROUSEL: mInflCar = true; break;
            case Influence.ELEVATOR: mInflElev = true; break;
            case Influence.FRYING: mInflFry = true; break;
            case Influence.ELECTRA: mInflElec = true; break;
            case Influence.MEATGRINDER: mInflMea = true; break;
            case Influence.KISSEL: mInflKis = true; break;
            case Influence.SODA: mInflSod = true; break;
            case Influence.ACIDFOG: mInflAci = true; break;
            case Influence.BURNINGFLUFF: mInflBurn = true; break;
            case Influence.RUSTYHAIR: mInflRus = true; break;
            case Influence.SPATIALBUBBLE: mInflSpa = true; break;
            case Influence.CONTROLLER: mInflCon = true; break;
            case Influence.MENTAL: mInflMen = true; break;
            case Influence.BURER: mInflBur = true; break;
            case Influence.HEALTH: mInflHel = true; break;
            case Influence.ARTEFACT: mInflArt = true; break;
            case Influence.MONOLITH: mInflMon = true; break;
            case Influence.EMISSION: mInflEm = true; break;
        }
//        Log.d(TAG, "Added " + influence.getTypeId() + " " + mInfRad + " " + Thread.currentThread().getName());
//        this.influences.add(influence);
    }

    @Override
    public void setEmission(boolean emission)
    {
        this.isEmission = emission;
    }

    @Override
    public boolean isEmission()
    {
        return isEmission;
    }

    public void setAvgInfluenceEmittingTime(long avgTime)
    {
        averageInflUpdatingTime = avgTime;
    }

    public boolean inDanger()
    {
        return mInfRad || mInflBur || mInflMen || mInflCon ||
                mInflAno || mInflMon || mInflSpr || mInflFun ||
                mInflCar || mInflElev || mInflFry || mInflElec ||
                mInflMea || mInflKis || mInflSod || mInflAci ||
                mInflBurn || mInflRus || mInflSpa;
    }

    @Override
    public boolean isClear()
    {
        return !(mInfRad || mInflBur || mInflMen || mInflCon ||
                mInflAno || mInflHel|| mInflMon || mInflSpr ||
                mInflFun || mInflCar || mInflElev || mInflFry ||
                mInflElec || mInflMea || mInflKis || mInflSod ||
                mInflAci || mInflBurn || mInflRus || mInflSpa);
    }

    @Override
    public boolean influencedBy(int influenceType)
    {
        switch (influenceType)
        {
            case Influence.RADIATION: return mInfRad;
            case Influence.ANOMALY: return mInflAno;
            case Influence.SPRINGBOARD: return mInflSpr;
            case Influence.FUNNEL: return mInflFun;
            case Influence.CAROUSEL: return mInflCar;
            case Influence.ELEVATOR: return mInflElev;
            case Influence.FRYING: return mInflFry;
            case Influence.ELECTRA: return mInflElec;
            case Influence.MEATGRINDER: return mInflMea;
            case Influence.KISSEL: return mInflKis;
            case Influence.SODA: return mInflSod;
            case Influence.ACIDFOG: return mInflAci;
            case Influence.BURNINGFLUFF: return mInflBurn;
            case Influence.RUSTYHAIR: return mInflRus;
            case Influence.SPATIALBUBBLE: return mInflSpa;
            case Influence.CONTROLLER: return mInflCon;
            case Influence.MENTAL: return mInflMen;
            case Influence.BURER: return mInflBur;
            case Influence.HEALTH: return mInflHel;
            case Influence.ARTEFACT: return mInflArt;
            case Influence.MONOLITH: return mInflMon;
            case Influence.EMISSION: return mInflEm;
        }
        return false;
    }

    @Override
    public long creationTime()
    {
        return time;
    }

    @Override
    public double[] getInfluences()
    {
        return mInfluences.clone();
    }

    @Override
    public double getInfluence(int influenceType)
    {
       return mInfluences[influenceType];
    }

    @Override
    public int getSource()
    {
        return 0;
    }

    private String getInfluenceName(int type)
    {
        switch (type)
        {
            case Influence.RADIATION: return "Radiation";
            case Influence.ANOMALY: return "Anomaly";
            case Influence.SPRINGBOARD: return "Springboard";
            case Influence.FUNNEL: return "Funnel";
            case Influence.CAROUSEL: return "Carousel";
            case Influence.ELEVATOR: return "Elevator";
            case Influence.FRYING: return "Frying";
            case Influence.ELECTRA: return "Electra";
            case Influence.MEATGRINDER: return "Meatgrinder";
            case Influence.KISSEL: return "Kissel";
            case Influence.SODA: return "Soda";
            case Influence.ACIDFOG: return "Acidfog";
            case Influence.BURNINGFLUFF: return "Burningfluff";
            case Influence.RUSTYHAIR: return "Rustyhair";
            case Influence.SPATIALBUBBLE: return "Spatialbubble";
            case Influence.CONTROLLER: return "Controller";
            case Influence.MENTAL: return "Mental";
            case Influence.BURER: return "Burer";
            case Influence.HEALTH: return "Health";
            case Influence.ARTEFACT: return "Artefact";
            case Influence.MONOLITH: return "Monolith";
        }
        return "Unknown";
    }

    @Override
    public String toString()
    {
        String newL = System.getProperty("line.separator");
        String str = "Influences pack:" + newL;
        str += "Time: " + creationTime() + newL;
        int size = 0;
        String infls = "";
        for(int i = 0; i < Influence.INFLUENCE_COUNT; i++)
        {
            if(influencedBy(i))
            {
                size++;
                infls += getInfluenceName(i) + ": " + getInfluence(i) + "\n";
            }
        }

        str += "Influences: " + "(" + size + ")\n";
        str += infls;
        return str;
    }
}
