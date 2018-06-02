package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.AnomalyModifier;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.HealthModifier;
import net.afterday.compass.models.PsiModifier;
import net.afterday.compass.models.RadiationModifier;

public class Mica extends Artefact
    implements RadiationModifier, PsiModifier, AnomalyModifier, HealthModifier
{
    public int getName() {
        return R.string.item_mica;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_mica;
    }

    public double getRadiationModifier() {
        return 1.25d;
    }

    public double getPsiModifier() {
        return 1.25d;
    }

    public double getAnomalyModifier() {
        return 1.25d;
    }

    public double getHealthModifier() {
        return 7d;
    }

    public Mica() {
        super();
    }

    public Mica(Parcel in) {
        super(in);
    }

    public static final Creator<Mica> CREATOR =
            new Creator<Mica>() {
                @Override
                public Mica createFromParcel(Parcel source) {
                    return new Mica(source);
                }

                @Override
                public Mica[] newArray(int size) {
                    return new Mica[size];
                }
            };
}
