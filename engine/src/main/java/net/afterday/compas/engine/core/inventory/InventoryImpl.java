package net.afterday.compas.engine.core.inventory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.afterday.compas.engine.core.inventory.items.Item;
import net.afterday.compas.engine.core.player.PlayerProps;
import net.afterday.compas.engine.core.serialization.Jsonable;
import net.afterday.compas.engine.core.serialization.Serializer;
import net.afterday.compas.engine.core.inventory.items.ItemImpl;
import net.afterday.compas.engine.core.inventory.items.ItemDescriptor;
import net.afterday.compas.engine.persistency.items.ItemsPersistency;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryImpl implements Inventory
{
    static int MAX_ARTIFACTS_COUNT = 5;

    private static final String INVENTORY = "INVENTORY";
    private static final String INVENTORY_ITEMS = "INV_ITEMS";
    private static final String TAG = "InventoryImpl";
    private List<Item> inventoryItems;

    private double[] artifacts = new double[Item.ARTIFACT_MODIFIERS_COUNT];
    private JsonArray artifactsO;
    private boolean hasHealthInstant;
    private boolean hasRadInstant;
    private Map<Integer, List<Item>> itemsByLevel;
    private int level = 1;
    private JsonObject o;
    private Serializer serializer;
    private ItemsPersistency itemsPersistency;

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
            //TODO: Restore equipment from JSON
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
            //TODO: save equipment to JSON
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

    /**
     * Add item to inventory.
     * <p>
     *     Max artefacts in inventory MAX_ARTIFACTS_COUNT
     * </p>
     */
    public Item addItem(ItemDescriptor itemD, String code)
    {
        Item item = new ItemImpl(itemD, code);
        if(item.getItemDescriptor().isArtefact())
        {
            int artifactsCount = getArtifactsCount();
            if(artifactsCount >= MAX_ARTIFACTS_COUNT)
            {
                //Audit.e(R.string.message_artifact_full); //TODO: (Mikhail) add message
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
            double modifier = i.getModifier(modifierId);
            if(modifierId == Item.RADIATION_EMMITER)
            {
                artifacts[modifierId] += modifier;
            }
            else {
                artifacts[modifierId] *= modifier;
            }
            artifactsO.set(modifierId, new JsonPrimitive(artifacts[modifierId]));
        }
    }

    private void removeArtifactModifier(Item i, int modifierId)
    {
        if(i.hasModifier(modifierId))
        {
            double modifier = i.getModifier(modifierId);
            if(modifierId == Item.RADIATION_EMMITER)
            {
                artifacts[modifierId] -= modifier;
            }
            else {
                artifacts[modifierId] /= modifier;
            }
            artifactsO.set(modifierId, new JsonPrimitive(artifacts[modifierId]));
        }
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
    public void setPlayerLevel(int level)
    {
        this.level = level;
        o.addProperty("level", level);
        serializer.serialize(INVENTORY, this);
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
            removeArtifactModifier(item, Item.RADIATION_EMMITER);
            removeArtifactModifier(item, Item.RADIATION_MODIFIER);
            removeArtifactModifier(item, Item.MENTAL_MODIFIER);
            removeArtifactModifier(item, Item.MONOLITH_MODIFIER);
            removeArtifactModifier(item, Item.ANOMALY_MODIFIER);
            removeArtifactModifier(item, Item.BURER_MODIFIER);
            removeArtifactModifier(item, Item.CONTROLLER_MODIFIER);
            removeArtifactModifier(item, Item.HEALTH_MODIFIER);
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


    //region Armor
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
    //endregion

}
