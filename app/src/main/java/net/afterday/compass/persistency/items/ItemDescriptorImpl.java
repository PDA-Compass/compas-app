package net.afterday.compass.persistency.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static net.afterday.compass.core.inventory.items.Item.*;

/**
 * Created by Justas Spakauskas on 3/25/2018.
 */

public class ItemDescriptorImpl implements ItemDescriptor
{
    private static final String TAG = "ItemDescriptorImpl";
    private int imageId;
    private String title;
    private int titleId = -1;
    private double[] modifiers = new double[MODIFIERS_COUNT];
    private boolean isArtifact = false;
    private boolean isBooster = false;
    private boolean isArmor = false;
    private long duration;
    private int xpPoints;
    private String description;
    private boolean isConsumable = true;
    private boolean isUsable = false;
    private boolean isDropable = true;
    private int descriptionId = -1;

    public ItemDescriptorImpl()
    {

    }

    public ItemDescriptorImpl(String title)
    {
        this.title = title;
        initModifiers();
    }

    public ItemDescriptorImpl(int titleId)
    {
        this.titleId = titleId;
        initModifiers();
    }

    private void initModifiers()
    {
        for(int i = 0; i < MODIFIERS_COUNT; i++)
        {
            modifiers[i] = NULL_MODIFIER;
        }
    }

    public ItemDescriptorImpl setImage(int image)
    {
        this.imageId = image;
        return this;
    }

    public ItemDescriptorImpl setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public ItemDescriptorImpl setTitleId(int titleId)
    {
        this.titleId = titleId;
        return this;
    }

    public ItemDescriptorImpl setDescription(String description)
    {
        this.description = description;
        return this;
    }

    public ItemDescriptorImpl setDescription(int descriptionId)
    {
        this.descriptionId = descriptionId;
        return this;
    }

    public ItemDescriptorImpl addModifier(int modifier, double value)
    {
        modifiers[modifier] = value;
        return this;
    }

    public ItemDescriptorImpl setArtefact(boolean isArtifact)
    {
        this.isArtifact = isArtifact;
        return this;
    }

    public ItemDescriptorImpl setBooster(boolean isBooster)
    {
        this.isBooster = isBooster;
        return this;
    }

    public ItemDescriptorImpl setArmor(boolean isArmor)
    {
        this.isArmor = isArmor;
        return this;
    }

    public ItemDescriptorImpl setDuration(long duration)
    {
        this.duration = duration;
        return this;
    }

    public ItemDescriptorImpl setXpPoints(int xpPoints)
    {
        this.xpPoints = xpPoints;
        return this;
    }

    public ItemDescriptorImpl setConsumable(boolean isConsumable)
    {
        this.isConsumable = isConsumable;
        return this;
    }

    public ItemDescriptorImpl setUsable(boolean isUsable)
    {
        this.isUsable = isUsable;
        return this;
    }

    public ItemDescriptorImpl setDropable(boolean isDropable)
    {
        this.isDropable = isDropable;
        return this;
    }

    @Override
    public int getImage()
    {
        return imageId;
    }

    @Override
    public String getName()
    {
        return title;
    }

    @Override
    public int getNameId() {
        return titleId;
    }

    @Override
    public boolean isBooster()
    {
        return isBooster;
    }

    @Override
    public boolean isArmor()
    {
        return isArmor;
    }

    @Override
    public boolean isArtefact()
    {
        return isArtifact;
    }

    @Override
    public boolean isSingleUse()
    {
        return !(isArtifact || isBooster || isArmor);
    }

    @Override
    public boolean isConsumable()
    {
        return isConsumable;
    }

    @Override
    public long getDuration()
    {
        return duration;
    }

    @Override
    public double[] getModifiers()
    {
        return modifiers;
    }

    @Override
    public int getXpPoints()
    {
        return xpPoints;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public int getDescriptionId()
    {
        return descriptionId;
    }

    @Override
    public boolean isUsable()
    {
        return isUsable;
    }

    @Override
    public boolean isDropable()
    {
        return isDropable;
    }

    public String toString()
    {
        return "";
    }
    
    public JsonObject toJson()
    {
        JsonObject o = new JsonObject();
        o.addProperty("imageId", "" + imageId);
        o.addProperty("title", title);
        o.addProperty("titleId", titleId);
        o.addProperty("isArtifact", isArtifact);
        o.addProperty("isBooster", isBooster);
        o.addProperty("isArmor", isArmor);
        o.addProperty("duration", duration);
        o.addProperty("xpPoints", xpPoints);
        o.addProperty("description", description);
        o.addProperty("isConsumable", isConsumable);
        o.addProperty("isUsable", isUsable);
        o.addProperty("isDropable", isDropable);
        o.addProperty("descriptionId", descriptionId);
        JsonArray a = new JsonArray(MODIFIERS_COUNT);
        for(int i = 0; i < MODIFIERS_COUNT; i++)
        {
            a.add(modifiers[i]);
        }
        o.add("modifiers", a);
        return o;
    }

    public static ItemDescriptorImpl fromJson(JsonObject o)
    {
        int imageId = o.get("imageId").getAsInt();
        String title = o.get("title").getAsString();
        int titleId = o.get("titleId").getAsInt();
        boolean isArtifact = o.get("isArtifact").getAsBoolean();
        boolean isBooster = o.get("isBooster").getAsBoolean();
        boolean isArmor = o.get("isArmor").getAsBoolean();
        int duration = o.get("duration").getAsInt();
        int xpPoints = o.get("xpPoints").getAsInt();
        String description = o.get("description").getAsString();
        boolean isConsumable = o.get("isConsumable").getAsBoolean();
        boolean isUsable = o.get("isUsable").getAsBoolean();
        boolean isDropable = o.get("isDropable").getAsBoolean();
        int descriptionId = o.get("descriptionId").getAsInt();
        JsonArray a = o.get("modifiers").getAsJsonArray();
        ItemDescriptorImpl iImpl = new ItemDescriptorImpl()
                .setImage(imageId)
                .setTitle(title)
                .setTitleId(titleId)
                .setArtefact(isArtifact)
                .setBooster(isBooster)
                .setArmor(isArmor)
                .setDuration(duration)
                .setXpPoints(xpPoints)
                .setDescription(description)
                .setDescription(descriptionId)
                .setConsumable(isConsumable)
                .setUsable(isUsable)
                .setDropable(isDropable);
        for(int i = 0; i < MODIFIERS_COUNT; i++)
        {
            iImpl.addModifier(i, a.get(i).getAsDouble());
        }
        return iImpl;
    }
}
