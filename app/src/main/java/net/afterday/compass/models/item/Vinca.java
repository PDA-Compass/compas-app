package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.HealthInstant;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.RadiationInstant;

public class Vinca extends InventoryItem
    implements HealthInstant, RadiationInstant
{
    public int getName() {
        return R.string.item_vinca;
    }

    public String getDescription() {
        return "Some strange pills";
    }

    public int getImage() {
        return R.drawable.item_vinca;
    }

    @Override
    public double getInstantHealth() {
        return 40d;
    }

    @Override
    public double getRadiationInstant() {
        return -1d;
    }
    
    public Vinca() {
        super();
    }
    
    public Vinca(Parcel in) {
        super(in);
    }
    
    public static final Parcelable.Creator<Vinca> CREATOR =
            new Parcelable.Creator<Vinca>() {
                @Override
                public Vinca createFromParcel(Parcel source) {
                    return new Vinca(source);
                }

                @Override
                public Vinca[] newArray(int size) {
                    return new Vinca[size];
                }
            };
}
