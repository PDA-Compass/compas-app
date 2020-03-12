package net.afterday.compas.fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import net.afterday.compas.core.inventory.items.Item;
import java.util.List;

public class InventoryImageAdapter extends BaseAdapter
{
    public static final int CATEGORIES = Item.ALL;
    private Context mContext;
    private List<Item> mInventory;

    public InventoryImageAdapter(Context c, List<Item> inventory) {
        this.mContext = c;
        this.mInventory = inventory;
    }

    public int getCount() {
        return mInventory.size();
    }

    public Object getItem(int position) {
        return mInventory.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mInventory.get(position).getItemDescriptor().getImage());

        return imageView;
    }
}


