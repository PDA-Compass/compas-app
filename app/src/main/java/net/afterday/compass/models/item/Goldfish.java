package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.HealthModifier;
import net.afterday.compass.models.PsiModifier;

public class Goldfish extends Artefact
    implements HealthModifier, PsiModifier
{
    public int getName() {
        return R.string.item_goldfish;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_goldfish;
    }

    public double getHealthModifier() {
        return 4d;
    }

    public double getPsiModifier() {
        return 0.1d;
    }

    public Goldfish() {
        super();
    }

    public Goldfish(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Goldfish> CREATOR =
            new Parcelable.Creator<Goldfish>() {
                @Override
                public Goldfish createFromParcel(Parcel source) {
                    return new Goldfish(source);
                }

                @Override
                public Goldfish[] newArray(int size) {
                    return new Goldfish[size];
                }
            };
}
