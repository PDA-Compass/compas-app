package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.RadiationModifier;

public class Droplets extends Artefact
    implements RadiationModifier
{
    public int getName() {
        return R.string.item_droplets;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_droplets;
    }

    public double getRadiationModifier() {
        return 0.9d;
    }

    public Droplets() {
        super();
    }

    public Droplets(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Droplets> CREATOR =
            new Parcelable.Creator<Droplets>() {
                @Override
                public Droplets createFromParcel(Parcel source) {
                    return new Droplets(source);
                }

                @Override
                public Droplets[] newArray(int size) {
                    return new Droplets[size];
                }
            };
}
