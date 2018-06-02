package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.AnomalyModifier;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.PsiModifier;
import net.afterday.compass.models.RadiationModifier;

public class Thorn extends Artefact
    implements RadiationModifier, PsiModifier, AnomalyModifier
{
    public int getName() {
        return R.string.item_thorn;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_thorn;
    }

    public double getPsiModifier() {
        return 1.25d;
    }

    public double getRadiationModifier() {
        return 0.5d;
    }

    public double getAnomalyModifier() {
        return 1.25d;
    }

    public Thorn() {
        super();
    }

    public Thorn(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Thorn> CREATOR =
            new Parcelable.Creator<Thorn>() {
                @Override
                public Thorn createFromParcel(Parcel source) {
                    return new Thorn(source);
                }

                @Override
                public Thorn[] newArray(int size) {
                    return new Thorn[size];
                }
            };
}
