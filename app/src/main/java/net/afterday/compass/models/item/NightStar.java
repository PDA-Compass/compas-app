package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.PsiModifier;
import net.afterday.compass.models.RadiationModifier;

public class NightStar extends Artefact
    implements PsiModifier, RadiationModifier
{
    public int getName() {
        return R.string.item_night_star;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_night_star;
    }

    public double getPsiModifier() {
        return 0.4d;
    }

    public double getRadiationModifier() {
        return 1.6d;
    }

    public NightStar() {
        super();
    }

    public NightStar(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<NightStar> CREATOR =
            new Parcelable.Creator<NightStar>() {
                @Override
                public NightStar createFromParcel(Parcel source) {
                    return new NightStar(source);
                }

                @Override
                public NightStar[] newArray(int size) {
                    return new NightStar[size];
                }
            };
}
