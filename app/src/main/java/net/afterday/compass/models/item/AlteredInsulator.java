package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.RadiationEmitter;

public class AlteredInsulator extends Artefact
    implements RadiationEmitter
{
    public int getName() {
        return R.string.item_altered_insulator;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_altered_insulator;
    }

    public double getRadiationEmit() {
        return 7d;
    }

    public AlteredInsulator() {
        super();
    }

    public AlteredInsulator(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<AlteredInsulator> CREATOR =
            new Parcelable.Creator<AlteredInsulator>() {
                @Override
                public AlteredInsulator createFromParcel(Parcel source) {
                    return new AlteredInsulator(source);
                }

                @Override
                public AlteredInsulator[] newArray(int size) {
                    return new AlteredInsulator[size];
                }
            };
}
