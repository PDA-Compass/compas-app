package net.afterday.compass.persistency.hardcoded;

import net.afterday.compass.core.influences.Influence;
import net.afterday.compass.persistency.influences.InfluencesPersistency;

import java.util.ArrayList;
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
        wifiInfls.add("50:c7:bf:40:4f:55");  //Extender 1
        wifiInfls.add("50:c7:bf:55:18:6d");  //Extender 2
        wifiInfls.add("04:8d:38:c2:9f:71");  //H100 (Router)
        wifiInfls.add("34:de:34:31:55:bd");  //Various 100 (Modem)
        wifiInfls.add("5e:cf:7f:f7:57:a8");  //Radiation R100 (27)
        wifiInfls.add("62:01:94:10:8f:a6");  //Healing H100 (13)
        wifiInfls.add("62:01:94:10:8f:9d");  //Anomaly A100 (20)
        wifiInfls.add("5e:cf:7f:d1:87:9f");  //Mental M100 (3)
        wifiInfls.add("5e:cf:7f:d1:8b:96");  //Controller C100 (19)
        wifiInfls.add("5e:cf:7f:d1:92:62");  //Burer B100 (2)
        wifiInfls.add("a2:20:a6:25:41:3c");  //A100 usb esp7
        wifiInfls.add("50:c7:bf:55:18:6d");  //M100 usb tp-link
        wifiInfls.add("a2:20:a6:24:44:37");  //B100 usb esp7
        wifiInfls.add("50:c7:bf:40:4f:55");  //C100 usb tp-link
        wifiInfls.add("f8:9d:31:00:10:d2");  //R100 usb long
        wifiInfls.add("a8:40:41:cf:79:81");  //H100-plus usb
        wifiInfls.add("5e:cf:7f:d1:96:dc");  //C100 (box)
        wifiInfls.add("5e:cf:7f:ee:f5:46");  //C100 (box)

        return wifiInfls;
    }
}
