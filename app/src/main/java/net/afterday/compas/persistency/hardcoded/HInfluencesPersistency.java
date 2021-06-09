package net.afterday.compas.persistency.hardcoded;

import net.afterday.compas.core.influences.Emission;
import net.afterday.compas.core.influences.Influence;
import net.afterday.compas.persistency.influences.InfluencesPersistency;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Justas Spakauskas on 2/3/2018.
 */

public class HInfluencesPersistency implements InfluencesPersistency
{
    @Override
    public List<Influence> getPossibleInfluences()
    {
        return null;
    }

    @Override
    public List<String> getRegisteredWifiModules()
    {
        List<String> wifiInfls = new ArrayList<>();

        //Afterday Project
        wifiInfls.add("04:8d:38:c2:9f:71");  //H100 (Router)
        wifiInfls.add("34:de:34:31:55:bd");  //Various 100 (Modem)
        wifiInfls.add("a2:20:a6:25:41:3c");  //A100 usb esp7
        wifiInfls.add("a2:20:a6:24:44:37");  //B100 usb esp7
        wifiInfls.add("5e:cf:7f:fa:43:9a");  //Z100 usb esp12
        wifiInfls.add("5e:cf:7f:fa:3f:99");  //H100 usb esp12
        wifiInfls.add("50:c7:bf:55:18:6d");  //M100 usb tp-link
        wifiInfls.add("50:c7:bf:40:4f:55");  //C100 usb tp-link
        wifiInfls.add("f8:9d:31:00:10:d2");  //R100 usb long flexible
        wifiInfls.add("a8:40:41:cf:79:81");  //H100-plus usb

        //Afterday Project #2
        wifiInfls.add("5e:cf:7f:d1:96:dc");  // (1)
        wifiInfls.add("5e:cf:7f:d1:92:62");  // (2)
        wifiInfls.add("5e:cf:7f:d1:87:9f");  // (3)
        wifiInfls.add("5e:cf:7f:23:d9:0f");  // (5)
        wifiInfls.add("1a:fe:34:d5:ea:fc");  // (7)
        wifiInfls.add("62:01:94:1a:cc:05");  // (8)
        wifiInfls.add("5e:cf:7f:d1:79:8f");  // (10)
        wifiInfls.add("5e:cf:7f:d1:65:07");  // (11)
        wifiInfls.add("62:01:94:10:8f:a6");  // (13)
        wifiInfls.add("62:01:94:1a:cc:7e");  // (15)
        wifiInfls.add("5e:cf:7f:f7:58:9c");  // (16)
        wifiInfls.add("5e:cf:7f:a4:7c:89");  // (17)
        wifiInfls.add("62:01:94:06:92:26");  // (18)
        wifiInfls.add("5e:cf:7f:d1:8b:96");  // (19)
        wifiInfls.add("62:01:94:10:8f:9d");  // (20)
        wifiInfls.add("5e:cf:7f:a4:79:f3");  // (21)
        wifiInfls.add("5e:cf:7f:23:f3:d1");  // (22)
        wifiInfls.add("5e:cf:7f:f7:de:2c");  // (23)
        wifiInfls.add("5e:cf:7f:23:d8:da");  // (24)
        wifiInfls.add("5e:cf:7f:a4:6b:c0");  // (25)
        wifiInfls.add("5e:cf:7f:f7:57:a8");  // (27)
        wifiInfls.add("5e:cf:7f:ee:f5:46");  // (29)
        wifiInfls.add("5e:cf:7f:a3:5c:8c");  // (30)

        //

        return wifiInfls;
    }

    @Override
    public List<Emission> getEmissions()
    {
        List<Emission> emissions = new ArrayList<Emission>();

        //Test Emissions
        emissions.add(emission(at(1, 1, 18, 0), 2, 30));
        emissions.add(emission(at(1, 14, 20, 0), 2, 30));
        emissions.add(emission(at(1, 28, 22, 0), 2, 0, true));
        emissions.add(emission(at(2, 1, 13, 0), 2, 30));
        emissions.add(emission(at(2, 14, 18, 0), 2, 30));
        emissions.add(emission(at(2, 28, 22, 0), 2, 30));
        emissions.add(emission(at(3, 1, 10, 0), 2, 0, true));
        emissions.add(emission(at(3, 14, 13, 0), 2, 30));
        emissions.add(emission(at(3, 28, 18, 0), 2, 30));
        emissions.add(emission(at(4, 1, 12, 0), 2, 30));
        emissions.add(emission(at(4, 14, 18, 0), 2, 0, true));
        emissions.add(emission(at(4, 28, 20, 0), 2, 30));
        emissions.add(emission(at(5, 1, 18, 0), 2, 30));
        emissions.add(emission(at(5, 14, 20, 0), 2, 30));
        emissions.add(emission(at(5, 28, 22, 0), 2, 0, true));
        emissions.add(emission(at(9, 1, 18, 0), 2, 30));
        emissions.add(emission(at(9, 14, 20, 0), 2, 30));
        emissions.add(emission(at(9, 28, 22, 0), 2, 0, true));
        emissions.add(emission(at(10, 1, 13, 0), 2, 30));
        emissions.add(emission(at(10, 14, 18, 0), 2, 30));
        emissions.add(emission(at(10, 28, 22, 0), 2, 30));
        emissions.add(emission(at(11, 1, 10, 0), 2, 0, true));
        emissions.add(emission(at(11, 14, 13, 0), 2, 30));
        emissions.add(emission(at(11, 28, 18, 0), 2, 30));
        emissions.add(emission(at(12, 1, 18, 0), 2, 30));
        emissions.add(emission(at(12, 14, 20, 0), 2, 0, true));
        emissions.add(emission(at(12, 28, 22, 0), 2, 30));

        //New Year Emission
        emissions.add(emission(at(12, 31, 23, 59), 9, 2));

        //Game Emissions

        return emissions;
    }

    private Emission emission(Calendar startAt, int notifyBefore, int duration, boolean isFake)
    {
        return new Emission()
        {
            @Override
            public Calendar getStartTime()
            {
                return startAt;
            }

            @Override
            public int notifyBefore()
            {
                return notifyBefore;
            }

            @Override
            public int duration()
            {
                return duration;
            }

            @Override
            public boolean isFake()
            {
                return isFake;
            }
        };
    }

    private Emission emission(Calendar startAt, int notifyBefore, int duration)
    {
        return emission(startAt, notifyBefore, duration, false);
    }

//    private Emission emission(int afterMins, int notifyBefore, int duration)
//    {
//        return new Emission()
//        {
//            @Override
//            public Calendar getStartTime()
//            {
//                return afterMins(afterMins);
//            }
//
//            @Override
//            public int notifyBefore()
//            {
//                return notifyBefore;
//            }
//
//            @Override
//            public int duration()
//            {
//                return duration;
//            }
//
//            @Override
//            public boolean isFake()
//            {
//
//            }
//        };
//    }

    private Calendar afterMins(int mins)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + mins);
        return c;
    }

    private Calendar at(int month, int day, int hour, int min)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        c.set(Calendar.SECOND, 0);
        return c;
    }
}
