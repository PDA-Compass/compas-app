package net.afterday.compas.core.inventory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.afterday.compas.R;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.Player;
import net.afterday.compas.core.player.PlayerProps;
import net.afterday.compas.core.serialization.Jsonable;
import net.afterday.compas.core.serialization.Serializer;
import net.afterday.compas.core.inventory.items.ItemImpl;
import net.afterday.compas.logging.Logger;
import net.afterday.compas.persistency.items.ItemDescriptor;
import net.afterday.compas.persistency.items.ItemsPersistency;
import static net.afterday.compas.core.inventory.items.Item.CATEGORY.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by spaka on 4/18/2018.
 */

public class InventoryImpl implements Inventory
{
    private static final String INVENTORY = "INVENTORY";
    private static final String INVENTORY_ITEMS = "INV_ITEMS";
    private static final String TAG = "InventoryImpl";
    private List<Item> inventoryItems;
    private JsonArray inventoryItemsO;
    private Item activeArmor;
    private Item activeBooster;
    private Item activeDevice;
    private double[] artifacts = new double[Item.ARTIFACT_MODIFIERS_COUNT];
    private JsonArray artifactsO;
    private boolean hasHealthInstant,
                    hasRadInstant;
    private Map<Integer, List<Item>> itemsByLevel;
    private int level = 1;
    private JsonObject o;
    private Serializer serializer;
    private ItemsPersistency itemsPersistency;

    //private Map<Item, JsonObject> itemsWithObjects
    public InventoryImpl(ItemsPersistency persistency, Serializer serializer)
    {
        this.itemsPersistency = persistency;
        this.serializer = serializer;
        this.itemsByLevel = makeItems(persistency.getItemsAddeWithLevel());
        artifacts[Item.RADIATION_EMMITER] = 0;

        inventoryItems = deserializeItems();
        Jsonable io = deserializeInventory();
        JsonObject o;
        if(io != null)
        {
            o = io.toJson();
            if(o.has("level"))
            {
                this.level = o.get("level").getAsInt();
            }
            if(o.has("activeArmor"))
            {
                JsonElement e = o.get("activeArmor");
                if(e.isJsonObject())
                {
                    activeArmor = deserializeItem(e.getAsJsonObject());
                }
            }
            if(o.has("activeBooster"))
            {
                JsonElement e = o.get("activeBooster");
                if(e.isJsonObject())
                {
                    activeBooster = deserializeItem(e.getAsJsonObject());
                }
            }
            if(o.has("activeDevice"))
            {
                JsonElement e = o.get("activeDevice");
                if(e.isJsonObject())
                {
                    activeDevice = deserializeItem(e.getAsJsonObject());
                }
            }
            if(o.has("hasHealthInstant"))
            {
                hasHealthInstant = o.get("hasHealthInstant").getAsBoolean();
            }
            if(o.has("hasRadInstant"))
            {
                hasRadInstant = o.get("hasRadInstant").getAsBoolean();
            }
            if(o.has("artifacts"))
            {
                deserializeArtifacts(o.get("artifacts").getAsJsonArray());
            }else
            {
                initArtifacts();
            }
        }else
        {
            o = new JsonObject();
            o.addProperty("level", level);
            o.add("activeArmor", null);
            o.add("activeBooster", null);
            o.add("activeDevice", null);
            o.addProperty("hasHealthInstant", false);
            o.addProperty("hasRadInstant", false);
            initArtifacts();
            o.add("artifacts", artifactsO);
        }
        this.o = o;
    }

    private void deserializeArtifacts(JsonArray ja)
    {
        int jaSize = ja.size();
        if(jaSize != Item.ARTIFACT_MODIFIERS_COUNT)
        {
            initArtifacts();
            return;
        }
        artifactsO = ja;
        for(int i = 0; i < Item.ARTIFACT_MODIFIERS_COUNT; i++)
        {
            artifacts[i] = ja.get(i).getAsDouble();
        }
    }

    private void initArtifacts()
    {
        artifactsO = new JsonArray(Item.ARTIFACT_MODIFIERS_COUNT);
        for(int i = 0; i < Item.ARTIFACT_MODIFIERS_COUNT; i++)
        {
            artifacts[i] = i == Item.RADIATION_EMMITER ? 0 : 1;
            artifactsO.add(artifacts[i]);
        }
        artifacts[Item.RADIATION_EMMITER] = 0;
    }


    //Gali buti iki 5 artifaktu
    public Item addItem(ItemDescriptor itemD, String code)
    {
//        ItemDescriptor itemD = itemsPersistency.getItemForCode(code);
//        if(itemD == null)
//        {
//            return null;
//        }

        Item item = new ItemImpl(itemD, code);
        if(item.getItemDescriptor().isArtefact())
        {
            int artifactsCount = getArtifactsCount();
            if(artifactsCount >= MAX_ARTIFACTS_COUNT)
            {
                net.afterday.compas.logging.Logger.e(R.string.message_artifact_full);
                return null;
            }
            applyArtifactModifier(item, Item.RADIATION_MODIFIER);
            applyArtifactModifier(item, Item.RADIATION_EMMITER);
            applyArtifactModifier(item, Item.ANOMALY_MODIFIER);
            applyArtifactModifier(item, Item.HEALTH_MODIFIER);
            applyArtifactModifier(item, Item.BURER_MODIFIER);
            applyArtifactModifier(item, Item.CONTROLLER_MODIFIER);
            applyArtifactModifier(item, Item.MENTAL_MODIFIER);
            applyArtifactModifier(item, Item.MONOLITH_MODIFIER);
        }
        if(item.hasModifier(Item.RADIATION_INSTANT) && item.getModifier(Item.RADIATION_INSTANT) < 0d)
        {
            this.hasRadInstant = true;
            o.addProperty("hasRadInstant", true);
        }
        if(item.hasModifier(Item.HEALTH_INSTANT) && item.getModifier(Item.HEALTH_INSTANT) > 0d)
        {
            this.hasHealthInstant = true;
            o.addProperty("hasHealthInstant", true);
        }
        inventoryItems.add(item);
        //inventoryItemsO.add(item.toJson());
        serializer.serialize(INVENTORY_ITEMS, item.getId(), item);
        return item;
    }

    private int getArtifactsCount()
    {
        int count = 0;
        for (Item i: inventoryItems)
        {
            if(i.getItemDescriptor().isArtefact())
                count++;
        }
        return count;
    }

    private void applyArtifactModifier(Item i, int modifierId)
    {
        if(i.hasModifier(modifierId))
        {
            if(modifierId == Item.RADIATION_EMMITER)
            {
                artifacts[modifierId] += i.getModifier(modifierId);
                artifactsO.set(modifierId, new JsonPrimitive(artifacts[modifierId]));
                return;
            }
            artifacts[modifierId] *= i.getModifier(modifierId);
            artifactsO.set(modifierId, new JsonPrimitive(artifacts[modifierId]));
        }
    }

    @Override
    public boolean hasActiveArmor()
    {
        return activeArmor != null && !activeArmor.isConsumed();
    }

    @Override
    public boolean hasHealthInstant()
    {
        return hasHealthInstant;
    }

    @Override
    public boolean hasRadiationInstant()
    {
        return hasRadInstant;
    }

    @Override
    public Item consumeArmor(long delta)
    {
        if(activeArmor != null)
        {
            activeArmor.consume(delta);
            serializer.serialize(INVENTORY, this);
        }
        return activeArmor;
    }

    @Override
    public Item getActiveArmor()
    {
        return activeArmor;
    }

    @Override
    public Item getActiveBooster()
    {
    return activeBooster;
    }

    @Override
    public Item getActiveDevice()
    {
        return activeDevice;
    }

    @Override
    public void setPlayerLevel(int level)
    {
        this.level = level;
        o.addProperty("level", level);
        serializer.serialize(INVENTORY, this);
    }

    @Override
    public boolean hasActiveBooster()
    {
        return activeBooster != null && !activeBooster.isConsumed();
    }

    @Override
    public Item consumeBooster(long delta)
    {
        if(activeBooster != null)
        {
            activeBooster.consume(delta);
            serializer.serialize(INVENTORY, this);
        }
        return activeBooster;
    }

    @Override
    public boolean hasActiveDevice()
    {
        return activeDevice != null && !activeDevice.isConsumed();
    }

    @Override
    public Item consumeDevice(long delta)
    {
        if(activeDevice != null)
        {
            activeDevice.consume(delta);
            serializer.serialize(INVENTORY, this);
        }
        return activeDevice;
    }

    @Override
    public PlayerProps useItem(Item item, PlayerProps playerProps)
    {
        if(item.getItemDescriptor().isSingleUse())
        {
            playerProps = item.modifyProps(playerProps);
            if(item.isConsumed())
            {
                removeItem(item);
            }
            //return playerProps;
        }else if(!item.isConsumed() && item.getItemDescriptor().isArmor())
        {
            activeArmor = item;
            o.add("activeArmor", item.toJson());
            item.setActive(true);
            removeItem(item);
            playerProps.setArmorPercents(item.getPercentsLeft());
            //return playerProps;
        }else if(!item.isConsumed() && item.getItemDescriptor().isBooster())
        {
            activeBooster = item;
            o.add("activeBooster", item.toJson());
            item.setActive(true);
            removeItem(item);
            playerProps.setBoosterPercents(item.getPercentsLeft());
            //return playerProps;
        }else if(!item.isConsumed() && item.getItemDescriptor().isDevice())
        {
            activeDevice = item;
            o.add("activeDevice", item.toJson());
            item.setActive(true);
            removeItem(item);
            playerProps.setDevicePercents(item.getPercentsLeft());
            //return playerProps;
        }
        validateInstants();
        serializer.serialize(INVENTORY, this);
        //validateHealthInstant();
        return playerProps;
    }

    private List<Item> getItemsByLevel()
    {
        List<Item> items = new ArrayList<>();
        for(int i : itemsByLevel.keySet())
        {
            if(i > level)
            {
                continue;
            }
            items.addAll(itemsByLevel.get(i));
        }
        return items;
    }

    private boolean removeItem(Item item)
    {
        if(inventoryItems.contains(item))
        {
            //inventoryItemsO.remove(item.toJson());
            boolean removed = inventoryItems.remove(item);
            if(removed)
            {
                serializer.remove(INVENTORY_ITEMS, item.getId());
            }
            return removed;
        }
        return false;
    }

    private void validateInstants()
    {
        boolean hInstant = false;
        boolean rInstant = false;
        for (Item i : inventoryItems)
        {
            if(i.hasModifier(Item.HEALTH_INSTANT) && i.getModifier(Item.HEALTH_INSTANT) > 0d)
            {
                hInstant = true;
            }
            if(i.hasModifier(Item.RADIATION_INSTANT) && i.getModifier(Item.RADIATION_INSTANT) < 0d)
            {
                rInstant = true;
            }
        }
        hasHealthInstant = hInstant;
        o.addProperty("hasHealthInstant", hInstant);
        hasRadInstant = rInstant;
        o.addProperty("hasRadInstant", hInstant);
    }

    @Override
    public boolean dropItem(Item item)
    {
        boolean removed = removeItem(item);
        if(item.getItemDescriptor().isArtefact())
        {
            if(item.hasModifier(Item.RADIATION_EMMITER))
            {
                artifacts[Item.RADIATION_EMMITER] -= item.getModifier(Item.RADIATION_EMMITER);
                artifactsO.set(Item.RADIATION_EMMITER, new JsonPrimitive(artifacts[Item.RADIATION_EMMITER]));
            }
            if(item.hasModifier(Item.RADIATION_MODIFIER))
            {
                artifacts[Item.RADIATION_MODIFIER] /= item.getModifier(Item.RADIATION_MODIFIER);
                artifactsO.set(Item.RADIATION_MODIFIER, new JsonPrimitive(artifacts[Item.RADIATION_MODIFIER]));
            }
            if(item.hasModifier(Item.MENTAL_MODIFIER))
            {
                artifacts[Item.MENTAL_MODIFIER] /= item.getModifier(Item.MENTAL_MODIFIER);
                artifactsO.set(Item.MENTAL_MODIFIER, new JsonPrimitive(artifacts[Item.MENTAL_MODIFIER]));
            }
            if(item.hasModifier(Item.MONOLITH_MODIFIER))
            {
                artifacts[Item.MONOLITH_MODIFIER] /= item.getModifier(Item.MONOLITH_MODIFIER);
                artifactsO.set(Item.MONOLITH_MODIFIER, new JsonPrimitive(artifacts[Item.MONOLITH_MODIFIER]));
            }
            if(item.hasModifier(Item.ANOMALY_MODIFIER))
            {
                artifacts[Item.ANOMALY_MODIFIER] /= item.getModifier(Item.ANOMALY_MODIFIER);
                artifactsO.set(Item.ANOMALY_MODIFIER, new JsonPrimitive(artifacts[Item.ANOMALY_MODIFIER]));
            }
            if(item.hasModifier(Item.BURER_MODIFIER))
            {
                artifacts[Item.BURER_MODIFIER] /= item.getModifier(Item.BURER_MODIFIER);
                artifactsO.set(Item.BURER_MODIFIER, new JsonPrimitive(artifacts[Item.BURER_MODIFIER]));
            }
            if(item.hasModifier(Item.CONTROLLER_MODIFIER))
            {
                artifacts[Item.CONTROLLER_MODIFIER] /= item.getModifier(Item.CONTROLLER_MODIFIER);
                artifactsO.set(Item.CONTROLLER_MODIFIER, new JsonPrimitive(artifacts[Item.CONTROLLER_MODIFIER]));
            }
            if(item.hasModifier(Item.HEALTH_MODIFIER))
            {
                artifacts[Item.HEALTH_MODIFIER] /= item.getModifier(Item.HEALTH_MODIFIER);
                artifactsO.set(Item.HEALTH_MODIFIER, new JsonPrimitive(artifacts[Item.HEALTH_MODIFIER]));
            }
        }
        validateInstants();
        serializer.serialize(INVENTORY, this);
        return removed;
    }

    @Override
    public List<Item> getItems()
    {
        List<Item> allItems = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        List<Item> artifacts = new ArrayList<>();
        for (Item i : inventoryItems)
        {
            if(i.getItemDescriptor().isArtefact())
            {
                artifacts.add(i);
            }else
            {
                items.add(i);
            }
        }
        allItems.addAll(artifacts);
        allItems.addAll(getItemsByLevel());
        allItems.addAll(items);
        return allItems;
    }

    private Map<Integer, List<Item>> makeItems(Map<Integer, List<ItemDescriptor>> descriptors)
    {
        Map<Integer, List<Item>> itemsByLevel = new HashMap<>();
        for(int level : descriptors.keySet())
        {
            List<Item> l = new ArrayList<>();
            for(ItemDescriptor d : descriptors.get(level))
            {
                l.add(new ItemImpl(d));
            }
            itemsByLevel.put(level, l);
        }
        return itemsByLevel;
    }

    @Override
    public List<Item> getBoosters()
    {
        List<Item> boosters = new ArrayList<>();
        for (Item i : inventoryItems)
        {
            if(i.getItemDescriptor().isBooster())
            {
                boosters.add(i);
            }
        }
        return boosters;
    }

    @Override
    public List<Item> getDevices()
    {
        List<Item> devices = new ArrayList<>();
        for (Item i : inventoryItems)
        {
            if(i.getItemDescriptor().isDevice())
            {
                devices.add(i);
            }
        }
        return devices;
    }

    @Override
    public List<Item> getArmors()
    {
        List<Item> armors = new ArrayList<>();
        for (Item i : inventoryItems)
        {
            if(i.getItemDescriptor().isArmor())
            {
                armors.add(i);
            }
        }
        return armors;
    }

    @Override
    public double[] getArtifacts()
    {
        return artifacts;
    }

    @Override
    public JsonObject toJson()
    {
        return o;
    }

    private List<Item> deserializeItems()
    {
        List<Item> items = new ArrayList<>();
        List<Jsonable> jsonables = serializer.deserializeList(INVENTORY_ITEMS);
        for(Jsonable j : jsonables)
        {
            Item i = deserializeItem(j.toJson());
            if(i == null)
            {
                continue;
            }
            items.add(i);
        }
        return items;
    }

    private Item deserializeItem(JsonObject jo)
    {
        if(jo == null || !jo.has("code"))
        {
            return null;
        }
        String code = jo.get("code").getAsString();
        ItemDescriptor iDesc = itemsPersistency.getItemForCode(code);
        if(iDesc == null)
        {
            return null;
        }
        return new ItemImpl(jo, iDesc);
    }

    private Jsonable deserializeInventory()
    {
        return serializer.deserialize(INVENTORY);
    }

}
