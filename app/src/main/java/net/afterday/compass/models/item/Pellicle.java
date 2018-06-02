package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.PsiModifier;
import net.afterday.compass.models.RadiationModifier;

public class Pellicle extends Artefact
    implements RadiationModifier, PsiModifier
{
    public int getName() {
        return R.string.item_pellicle;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_pellicle;
    }

    public double getRadiationModifier() {
        return 1.4d;
    }

    public double getPsiModifier() {
        return 0.6d;
    }

    public Pellicle() {
        super();
    }

    public Pellicle(Parcel in) {
        super(in);
    }

    public static final Creator<Pellicle> CREATOR =
            new Creator<Pellicle>() {
                @Override
                public Pellicle createFromParcel(Parcel source) {
                    return new Pellicle(source);
                }

                @Override
                public Pellicle[] newArray(int size) {
                    return new Pellicle[size];
                }
            };
}
