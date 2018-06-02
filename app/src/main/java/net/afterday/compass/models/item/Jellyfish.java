package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.HealthModifier;

public class Jellyfish extends Artefact
    implements HealthModifier
{
    public int getName() {
        return R.string.item_jellyfish;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_jellyfish;
    }

    public double getHealthModifier() {
        return 1.5d;
    }

    public Jellyfish() {
        super();
    }

    public Jellyfish(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Jellyfish> CREATOR =
            new Parcelable.Creator<Jellyfish>() {
                @Override
                public Jellyfish createFromParcel(Parcel source) {
                    return new Jellyfish(source);
                }

                @Override
                public Jellyfish[] newArray(int size) {
                    return new Jellyfish[size];
                }
            };
}
