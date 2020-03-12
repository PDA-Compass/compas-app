package net.afterday.compas.engine.engine.influences;

import net.afterday.compas.engine.core.log.Log;

import net.afterday.compas.engine.core.influences.Influence;
import net.afterday.compas.engine.core.influences.InfluencesPack;

import java.util.ArrayList;
import java.util.List;

public class InflPack implements InfluencesPack
{
    private static final String TAG = "InflPack";
    private long time;

    private boolean mInfRad = false;
    private boolean mInflAno = false;
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
        this.time = System.currentTimeMillis();

        for(int i = 0; i < Influence.INFLUENCE_COUNT; i++)
        {
            mInfluences[i] = 0;
        }
    }


    public void addInfluence(int type, double strength)
    {
        Log.d(TAG, "addInfluence: " + getInfluenceName(type) + " " + strength);
        mInfluences[type] = Math.max(mInfluences[type], strength);
        switch (type)
        {
            case Influence.RADIATION: mInfRad = true; break;
            case Influence.ANOMALY: mInflAno = true; break;
            case Influence.CONTROLLER: mInflCon = true; break;
            case Influence.MENTAL: mInflMen = true; break;
            case Influence.BURER: mInflBur = true; break;
            case Influence.HEALTH: mInflHel = true; break;
            case Influence.ARTEFACT: mInflArt = true; break;
            case Influence.MONOLITH: mInflMon = true; break;
            case Influence.EMISSION: mInflEm = true; break;
        }
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

    public boolean inDanger()
    {
        return mInfRad || mInflBur || mInflMen || mInflCon || mInflAno|| mInflMon ;
    }

    @Override
    public boolean isClear()
    {
        return !(mInfRad || mInflBur || mInflMen || mInflCon || mInflAno || mInflHel|| mInflMon );
    }

    @Override
    public boolean influencedBy(int influenceType)
    {
        switch (influenceType)
        {
            case Influence.RADIATION: return mInfRad;
            case Influence.ANOMALY: return mInflAno;
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
        StringBuilder infls = new StringBuilder();
        for(int i = 0; i < Influence.INFLUENCE_COUNT; i++)
        {
            if(influencedBy(i))
            {
                size++;
                infls.append(getInfluenceName(i)).append(": ").append(getInfluence(i)).append("\n");
            }
        }

        str += "Influences: " + "(" + size + ")\n";
        str += infls;
        return str;
    }
}
