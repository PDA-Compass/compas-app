package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.HealthInstant;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.RadiationInstant;

public class ScientificMedkit extends InventoryItem
        implements HealthInstant, RadiationInstant
{
    public int getName() {
        return R.string.item_scientific_medkit;
    }

    public String getDescription() {
        return "Simple medkit";
    }

    public int getImage() {
        return R.drawable.item_scientific_medkit;
    }

    @Override
    public double getInstantHealth() {
        return 50d;
    }

    public double getRadiationInstant() {
        return -3d;
    }

    public ScientificMedkit() {
        super();
    }

    public ScientificMedkit(Parcel in) {
        super(in);
    }

    public static final Creator<ScientificMedkit> CREATOR =
            new Creator<ScientificMedkit>() {
                @Override
                public ScientificMedkit createFromParcel(Parcel source) {
                    return new ScientificMedkit(source);
                }

                @Override
                public ScientificMedkit[] newArray(int size) {
                    return new ScientificMedkit[size];
                }
            };
}
