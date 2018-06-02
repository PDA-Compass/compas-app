package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.RadiationModifier;

public class Crystal extends Artefact
    implements RadiationModifier
{
    public int getName() {
        return R.string.item_crystal;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_crystal;
    }

    public double getRadiationModifier() {
        return 0.6d;
    }

    public Crystal() {
        super();
    }

    public Crystal(Parcel in) {
        super(in);
    }

    public static final Creator<Crystal> CREATOR =
            new Creator<Crystal>() {
                @Override
                public Crystal createFromParcel(Parcel source) {
                    return new Crystal(source);
                }

                @Override
                public Crystal[] newArray(int size) {
                    return new Crystal[size];
                }
            };
}
