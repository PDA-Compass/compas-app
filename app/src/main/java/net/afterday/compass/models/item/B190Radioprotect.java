package net.afterday.compass.models.item;

import android.os.Parcel;
import android.os.Parcelable;

import net.afterday.compass.R;
import net.afterday.compass.models.BoosterModifier;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.RadiationModifier;

public class B190Radioprotect extends InventoryItem
    implements BoosterModifier, RadiationModifier
{
    public int getName() {
        return R.string.item_b190;
    }

    public String getDescription() {
        return "Reduces all radiation influence for 1 minute";
    }

    public int getImage() {
        return R.drawable.item_b190;
    }

    public long getDuration() {
        return 60L * 1000L;
    }

    public double getRadiationModifier() {
        return 0d;
    }

    public B190Radioprotect() {
        super();
    }

    public B190Radioprotect(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<B190Radioprotect> CREATOR =
            new Parcelable.Creator<B190Radioprotect>() {
                @Override
                public B190Radioprotect createFromParcel(Parcel source) {
                    return new B190Radioprotect(source);
                }

                @Override
                public B190Radioprotect[] newArray(int size) {
                    return new B190Radioprotect[size];
                }
            };
}
