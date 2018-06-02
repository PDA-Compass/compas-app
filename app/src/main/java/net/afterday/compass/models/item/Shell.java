package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.RadiationEmitter;

public class Shell extends Artefact
    implements RadiationEmitter
{
    public int getName() {
        return R.string.item_shell;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_shell;
    }

    public double getRadiationEmit() {
        return 1d;
    }

    public Shell() {
        super();
    }

    public Shell(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Shell> CREATOR =
            new Parcelable.Creator<Shell>() {
                @Override
                public Shell createFromParcel(Parcel source) {
                    return new Shell(source);
                }

                @Override
                public Shell[] newArray(int size) {
                    return new Shell[size];
                }
            };
}
