package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.RadiationModifier;

public class Fireball extends Artefact
    implements RadiationModifier
{
    public int getName() {
        return R.string.item_fireball;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_fireball;
    }

    public double getRadiationModifier() {
        return 0.7d;
    }

    public Fireball() {
        super();
    }

    public Fireball(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Fireball> CREATOR =
            new Parcelable.Creator<Fireball>() {
                @Override
                public Fireball createFromParcel(Parcel source) {
                    return new Fireball(source);
                }

                @Override
                public Fireball[] newArray(int size) {
                    return new Fireball[size];
                }
            };
}
