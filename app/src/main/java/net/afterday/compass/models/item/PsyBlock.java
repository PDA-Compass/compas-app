package net.afterday.compass.models.item;

import android.os.Parcel;

import net.afterday.compass.R;
import net.afterday.compass.models.BoosterModifier;
import net.afterday.compass.models.InventoryItem;
import net.afterday.compass.models.PsiModifier;

public class PsyBlock extends InventoryItem
    implements BoosterModifier, PsiModifier
{
    public int getName() {
        return R.string.item_psyblock;
    }

    public String getDescription() {
        return "Reduces all radiation influence for 1 minute";
    }

    public int getImage() {
        return R.drawable.item_psy_block;
    }

    public long getDuration() {
        return 60L * 1000L;
    }

    public double getPsiModifier() {
        return 0d;
    }

    public PsyBlock() {
        super();
    }

    public PsyBlock(Parcel in) {
        super(in);
    }

    public static final Creator<PsyBlock> CREATOR =
            new Creator<PsyBlock>() {
                @Override
                public PsyBlock createFromParcel(Parcel source) {
                    return new PsyBlock(source);
                }

                @Override
                public PsyBlock[] newArray(int size) {
                    return new PsyBlock[size];
                }
            };
}
