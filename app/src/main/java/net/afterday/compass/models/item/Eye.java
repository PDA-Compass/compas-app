package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.HealthModifier;

public class Eye extends Artefact
    implements HealthModifier
{
    public int getName() {
        return R.string.item_eye;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_eye;
    }

    public double getHealthModifier() {
        return 2d;
    }

    public Eye() {
        super();
    }

    public Eye(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Eye> CREATOR =
            new Parcelable.Creator<Eye>() {
                @Override
                public Eye createFromParcel(Parcel source) {
                    return new Eye(source);
                }

                @Override
                public Eye[] newArray(int size) {
                    return new Eye[size];
                }
            };
}
