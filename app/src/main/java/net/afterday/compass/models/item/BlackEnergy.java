package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.RadiationEmitter;

public class BlackEnergy extends Artefact
    implements RadiationEmitter
{
    public int getName() {
        return R.string.item_black_energy;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_black_energy;
    }

    public double getRadiationEmit() {
        return 15d;
    }

    public BlackEnergy() {
        super();
    }

    public BlackEnergy(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<BlackEnergy> CREATOR =
            new Parcelable.Creator<BlackEnergy>() {
                @Override
                public BlackEnergy createFromParcel(Parcel source) {
                    return new BlackEnergy(source);
                }

                @Override
                public BlackEnergy[] newArray(int size) {
                    return new BlackEnergy[size];
                }
            };

}
