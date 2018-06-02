package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.HealthModifier;

public class Kolobok extends Artefact
    implements HealthModifier
{
    public int getName() {
        return R.string.item_kolobok;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_kolobok;
    }

    public double getHealthModifier() {
        return 3d;
    }

    public Kolobok() {
        super();
    }

    public Kolobok(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Kolobok> CREATOR =
            new Parcelable.Creator<Kolobok>() {
                @Override
                public Kolobok createFromParcel(Parcel source) {
                    return new Kolobok(source);
                }

                @Override
                public Kolobok[] newArray(int size) {
                    return new Kolobok[size];
                }
            };
}
