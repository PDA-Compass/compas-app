package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.AnomalyModifier;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.RadiationModifier;

public class Moonlight extends Artefact
    implements AnomalyModifier, RadiationModifier
{
    public int getName() {
        return R.string.item_moonlight;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_moonlight;
    }

    public double getAnomalyModifier() {
        return 0.5d;
    }

    public double getRadiationModifier() {
        return 1.2d;
    }

    public Moonlight() {
        super();
    }

    public Moonlight(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Moonlight> CREATOR =
            new Parcelable.Creator<Moonlight>() {
                @Override
                public Moonlight createFromParcel(Parcel source) {
                    return new Moonlight(source);
                }

                @Override
                public Moonlight[] newArray(int size) {
                    return new Moonlight[size];
                }
            };
}
