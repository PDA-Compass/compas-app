package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.RadiationModifier;

public class Bubble extends Artefact
    implements RadiationModifier
{
    public int getName() {
        return R.string.item_bubble;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_bubble;
    }

    public double getRadiationModifier() {
        return 0.8d;
    }

    public Bubble() {
        super();
    }

    public Bubble(Parcel in) {
        super(in);
    }

    public static final Creator<Bubble> CREATOR =
            new Creator<Bubble>() {
                @Override
                public Bubble createFromParcel(Parcel source) {
                    return new Bubble(source);
                }

                @Override
                public Bubble[] newArray(int size) {
                    return new Bubble[size];
                }
            };
}
