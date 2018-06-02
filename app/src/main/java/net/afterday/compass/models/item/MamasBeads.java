package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.BurerModifier;
import net.afterday.compass.models.HealthModifier;

public class MamasBeads extends Artefact
    implements BurerModifier, HealthModifier
{
    public int getName() {
        return R.string.item_mamas_beads;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_mamas_beads;
    }

    public double getBurerModifier() {
        return 0.1d;
    }

    public double getHealthModifier() {
        return 6d;
    }

    public MamasBeads() {
        super();
    }

    public MamasBeads(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<MamasBeads> CREATOR =
            new Parcelable.Creator<MamasBeads>() {
                @Override
                public MamasBeads createFromParcel(Parcel source) {
                    return new MamasBeads(source);
                }

                @Override
                public MamasBeads[] newArray(int size) {
                    return new MamasBeads[size];
                }
            };
}
