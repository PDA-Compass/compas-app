package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.AnomalyModifier;
import net.afterday.compass.models.ArmorModifier;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.PsiModifier;
import net.afterday.compass.models.RadiationSumModifier;

public class SSP99 extends InventoryItem
    implements ArmorModifier, AnomalyModifier, RadiationSumModifier, PsiModifier
{
    public int getName() {
        return R.string.item_ssp99;
    }

    public String getDescription() {
        return "Simple leather jacket. Makes you more resistant to collected radiation";
    }

    public int getImage() {
        return R.drawable.item_ssp_99_ecologist;
    }

    public long getDuration() {
        return 10L * 60L * 1000L;
    }

    public double getRadiationSumModifier() {
        return .2d;
    }

    public double getAnomalyModifier() {
        return 0.4d;
    }

    public double getPsiModifier() {
        return 0.6d;
    }

    public SSP99() {
        super();
    }

    public SSP99(Parcel in) {
        super(in);
    }

    public static final Creator<SSP99> CREATOR =
            new Creator<SSP99>() {
                @Override
                public SSP99 createFromParcel(Parcel source) {
                    return new SSP99(source);
                }

                @Override
                public SSP99[] newArray(int size) {
                    return new SSP99[size];
                }
            };
}
