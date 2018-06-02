package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.AnomalyModifier;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.RadiationModifier;

public class Spring extends Artefact
    implements RadiationModifier, AnomalyModifier
{
    public int getName() {
        return R.string.item_spring;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_spring;
    }

    public double getRadiationModifier() {
        return 1.1d;
    }

    public double getAnomalyModifier() {
        return 0.75d;
    }

    public Spring() {
        super();
    }

    public Spring(Parcel in) {
        super(in);
    }

    public static final Creator<Spring> CREATOR =
            new Creator<Spring>() {
                @Override
                public Spring createFromParcel(Parcel source) {
                    return new Spring(source);
                }

                @Override
                public Spring[] newArray(int size) {
                    return new Spring[size];
                }
            };
}
