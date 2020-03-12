package net.afterday.compas.engine.influences.BluetoothInfluences;

import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.core.influences.InfluencesPack;
import net.afterday.compas.engine.influences.InflPack;
import net.afterday.compas.engine.influences.InfluenceExtractionStrategy;
import net.afterday.compas.sensors.Bluetooth.BluetoothScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spaka on 4/2/2018.
 */

public class BluetoothExtractionStrategy implements InfluenceExtractionStrategy<List<BluetoothScanResult>, Double>
{
    private static final String TAG = "BluetoothExtractor";
    private List<BluetoothScanResult> emitNext;
    private static final int CONNECTION_COOLDOWN = 3000;
    private double lastStrength;
    private long lastReceived;

    public BluetoothExtractionStrategy()
    {
        emitNext = new ArrayList<>();
    }
//    @Override
//    public InfluencesPack makeInfluences(List<BluetoothScanResult> results)
//    {
//        long now = System.currentTimeMillis();
//
//        List<BluetoothScanResult> filtered = new ArrayList<>();
//        List<Integer> added = new ArrayList<>();
//        for(BluetoothScanResult bsr : emitNext)
//        {
//            if((now - bsr.getScanTime()) > CONNECTION_COOLDOWN)
//            {
//                continue;
//            }
//            int index = getIndex(results, bsr);
//            if(index > -1)
//            {
//                filtered.add(results.get(index));
//                added.add(index);
//            }else
//            {
//                filtered.add(bsr);
//            }
//        }
//        for(int i = 0; i < results.size(); i++)
//        {
//            if(!added.contains(i))
//            {
//                filtered.add(results.get(i));
//            }
//        }
//        InflPack ip = new InflPack();
//        for(BluetoothScanResult bsr : filtered)
//        {
//            ip.addInfluence(Influence.ARTEFACT, bsr.getStrength());
//        }
//        emitNext = filtered;
//        return ip;
//    }

    private int getIndex(List<BluetoothScanResult> results, BluetoothScanResult bsr)
    {
        for(int i = 0; i < results.size(); i++)
        {
            if(results.get(i).getName().equals(bsr.getName()))
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Double makeInfluences(List<BluetoothScanResult> i)
    {
        long now = System.currentTimeMillis();
        if(i.isEmpty())
        {
            if(lastReceived < now - CONNECTION_COOLDOWN)
            {
                return Influence.NULL;
            }else
            {
                return lastStrength;
            }
        }
        for(BluetoothScanResult bs : i)
        {
            if(bs.getStrength() > lastStrength)
            {
                lastStrength = bs.getStrength();
                lastReceived = bs.getScanTime();
            }
        }
        return lastStrength;
    }
}
