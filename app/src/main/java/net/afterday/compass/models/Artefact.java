package net.afterday.compass.models;

import android.os.Parcel;

public abstract class Artefact extends InventoryItem
{
    public Artefact() {
        super();
    }

    public Artefact(Parcel in) {
        super(in);
    }
}
