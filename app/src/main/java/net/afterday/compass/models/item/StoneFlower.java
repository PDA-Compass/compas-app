package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.Artefact;
import net.afterday.compass.models.PsiModifier;
import net.afterday.compass.models.RadiationModifier;

public class StoneFlower extends Artefact
    implements RadiationModifier, PsiModifier
{
    public int getName() {
        return R.string.item_stone_flower;
    }

    public String getDescription() {
        return "Those are used in the army";
    }

    public int getImage() {
        return R.drawable.item_stone_flower;
    }

    public double getRadiationModifier() {
        return 1.2d;
    }

    public double getPsiModifier() {
        return 0.8d;
    }

    public StoneFlower() {
        super();
    }

    public StoneFlower(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<StoneFlower> CREATOR =
            new Parcelable.Creator<StoneFlower>() {
                @Override
                public StoneFlower createFromParcel(Parcel source) {
                    return new StoneFlower(source);
                }

                @Override
                public StoneFlower[] newArray(int size) {
                    return new StoneFlower[size];
                }
            };
}
