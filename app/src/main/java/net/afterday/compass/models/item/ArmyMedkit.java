package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.HealthInstant;
import net.afterday.compass.models.InventoryItem;

public class ArmyMedkit extends InventoryItem
    implements HealthInstant
{
    public int getName() {
        return R.string.item_army_medkit;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_army_medkit;
    }

    @Override
    public double getInstantHealth() {
        return 30d;
    }

    public ArmyMedkit() {
        super();
    }

    public ArmyMedkit(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<ArmyMedkit> CREATOR =
            new Parcelable.Creator<ArmyMedkit>() {
                @Override
                public ArmyMedkit createFromParcel(Parcel source) {
                    return new ArmyMedkit(source);
                }

                @Override
                public ArmyMedkit[] newArray(int size) {
                    return new ArmyMedkit[size];
                }
            };
}
