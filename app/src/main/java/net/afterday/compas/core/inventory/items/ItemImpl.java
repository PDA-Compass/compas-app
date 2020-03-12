package net.afterday.compas.core.inventory.items;

import com.google.gson.JsonObject;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.PlayerProps;
import net.afterday.compas.core.serialization.Jsonable;
import net.afterday.compas.persistency.items.ItemDescriptor;

import java.util.UUID;

/**
 * Created by Justas Spakauskas on 3/25/2018.
 */

public class ItemImpl implements Item
{
    private static final String TAG = "ItemImpl";
    private JsonObject o = new JsonObject();
    private ItemDescriptor descriptor;
    private boolean isConsumed = false;
    private double[] modifiers;
    private long left = 0;
    private boolean isActive = false;
    private String id;
    private String code;

    public ItemImpl(ItemDescriptor descriptor)
    {
        this.descriptor = descriptor;
        modifiers = descriptor.getModifiers();
        left = descriptor.getDuration();
        id = UUID.randomUUID().toString();
    }

    public ItemImpl(ItemDescriptor descriptor, String code)
    {
        this.descriptor = descriptor;
        modifiers = descriptor.getModifiers();
        left = descriptor.getDuration();
        id = UUID.randomUUID().toString();
        this.code = code;
        o.addProperty("id", id);
        //o.add("itemDescriptor", descriptor.toJson());
        o.addProperty("left", left);
        o.addProperty("isActive", isActive);
        o.addProperty("isConsumed", isConsumed);
        o.addProperty("code", code);
    }

    public ItemImpl(JsonObject o, ItemDescriptor descriptor)
    {
        this.descriptor = descriptor;
        modifiers = descriptor.getModifiers();
        if(o.has("id"))
        {
            id = o.get("id").getAsString();
        }
        if(o.has("left"))
        {
            left = o.get("left").getAsLong();
        }
        if(o.has("isActive"))
        {
            isActive = o.get("isActive").getAsBoolean();
        }
        if(o.has("isConsumed"))
        {
            isConsumed = o.get("isConsumed").getAsBoolean();
        }
        if(o.has("code"))
        {
            code = o.get("code").getAsString();
        }
        this.o = o;
    }

    @Override
    public boolean hasModifier(int modifierType)
    {
        if(modifiers.length > modifierType)
        {
            double m = modifiers[modifierType];
            if(m != ItemDescriptor.NULL_MODIFIER)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getModifier(int modifierType)
    {
        if(modifiers.length > modifierType)
        {
            return modifiers[modifierType];
        }
        return ItemDescriptor.NULL_MODIFIER;
    }

    @Override
    public boolean isActive()
    {
        return isActive;
    }

    @Override
    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
        o.addProperty("isActive", isActive);
    }

    @Override
    public boolean isConsumed()
    {
        if(descriptor.isSingleUse())
        {
            return isConsumed;
        }
        if(descriptor.getDuration() < 0)
        {
            return false;
        }
        return left <= 0;
    }

    @Override
    public void consume(long time)
    {
        if(descriptor.getDuration() < 0)
        {
            return;
        }
        left -= time;
        o.addProperty("left", left);
    }

    @Override
    public int getPercentsLeft()
    {
        if(descriptor.getDuration() < 0 || left == descriptor.getDuration())
        {
            return 100;
        }
        return (int) (left * 100 / Math.max(descriptor.getDuration(), 1));
    }

    @Override
    public PlayerProps modifyProps(PlayerProps playerProps)
    {
        if(!isConsumed && descriptor.isSingleUse())
        {
            if(hasModifier(Item.HEALTH_INSTANT))
            {
                playerProps.addHealth(getModifier(Item.HEALTH_INSTANT));
            }
            if(hasModifier(Item.RADIATION_INSTANT))
            {
                playerProps.addRadiation(getModifier(Item.RADIATION_INSTANT));
            }
            isConsumed = true;
            o.addProperty("isConsumed", isConsumed);
        }
        return playerProps;
    }

    @Override
    public String getCode()
    {
        return code;
    }

    @Override
    public PlayerProps modifyProps(PlayerProps playerProps, long delta)
    {
        left -= delta;
        o.addProperty("left", left);
        return playerProps;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public ItemDescriptor getItemDescriptor()
    {
        return descriptor;
    }

    public String toString()
    {
        JsonObject o = new JsonObject();

        return "";
    }

    @Override
    public JsonObject toJson()
    {
        return o;
    }
}
