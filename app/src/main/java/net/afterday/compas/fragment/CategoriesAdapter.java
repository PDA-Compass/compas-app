package net.afterday.compas.fragment;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import net.afterday.compas.R;
import net.afterday.compas.core.inventory.items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by spaka on 6/6/2018.
 */

public class CategoriesAdapter extends BaseAdapter
{
    private static final List<Pair<Item.CATEGORY, Integer>> categories = new ArrayList<>();
    private static final Map<Item.CATEGORY, Integer> categoryImages = new HashMap<>();
    private final List<Pair<Item.CATEGORY, Pair<List<Item>, Integer>>> categoryItems = new ArrayList<>();
    private final Context context;
    static {
        categories.add(new Pair<>(Item.CATEGORY.MEDKITS, R.drawable.cat_medkits));
        categories.add(new Pair<>(Item.CATEGORY.ANTIRADS, R.drawable.cat_antirads));
        categories.add(new Pair<>(Item.CATEGORY.BOOSTERS, R.drawable.cat_boosters));
        categories.add(new Pair<>(Item.CATEGORY.ARTIFACTS, R.drawable.cat_artefacts));
        categories.add(new Pair<>(Item.CATEGORY.WEAPONS, R.drawable.cat_weapons));
        categories.add(new Pair<>(Item.CATEGORY.UPGRADES, R.drawable.cat_upgrades));
        categories.add(new Pair<>(Item.CATEGORY.ARMORS, R.drawable.cat_suits));
        categories.add(new Pair<>(Item.CATEGORY.HABAR, R.drawable.cat_habar));
        categories.add(new Pair<>(Item.CATEGORY.FOOD, R.drawable.cat_food));
        categories.add(new Pair<>(Item.CATEGORY.AMMO, R.drawable.cat_ammo));
        categories.add(new Pair<>(Item.CATEGORY.FILTERS, R.drawable.cat_filters));
        categories.add(new Pair<>(Item.CATEGORY.DEVICES, R.drawable.cat_devices));
    }

    public CategoriesAdapter(Context c, List<Item> inventory)
    {
        context = c;
        Map<Item.CATEGORY, List<Item>> itemsOfCategory = new HashMap<>();
        for (Item i : inventory)
        {
            Item.CATEGORY cat = i.getItemDescriptor().getCategory();
            if(itemsOfCategory.containsKey(cat))
            {
                itemsOfCategory.get(i.getItemDescriptor().getCategory()).add(i);
            }else
            {
                List<Item> items = new ArrayList<>();
                items.add(i);
                itemsOfCategory.put(cat, items);
            }
        }
        for (Pair<Item.CATEGORY, Integer> catImg : categories)
        {
            if(itemsOfCategory.containsKey(catImg.first))
            {
                categoryItems.add(new Pair<>(catImg.first, new Pair<>(itemsOfCategory.get(catImg.first), catImg.second)));
            }else
            {
                categoryItems.add(new Pair<>(catImg.first, new Pair<>(new ArrayList<>(), catImg.second)));
            }
        }
    }

    @Override
    public int getCount()
    {
        return categories.size();
    }

    @Override
    public Object getItem(int i)
    {
        return categoryItems.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        ImageView imageView;
        if (view == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) view;
        }
        Pair<List<Item>, Integer> img = categoryItems.get(i).second;
        imageView.setImageResource(img.second);
        if(img.first.size() == 0)
        {
            imageView.setAlpha(50);
        }
        return imageView;
    }

    public List<Item> getItemsByCategory(int position)
    {
        return categoryItems.get(position).second.first;
    }
}
