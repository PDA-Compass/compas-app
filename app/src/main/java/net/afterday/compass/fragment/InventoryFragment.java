package net.afterday.compass.fragment;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import net.afterday.compass.R;
import net.afterday.compass.StalkerApp;
import net.afterday.compass.core.inventory.items.Item;
import net.afterday.compass.engine.events.ItemEventsBus;
import net.afterday.compass.logging.Log;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class InventoryFragment extends DialogFragment
{
    List<Item> mInventory;
    GridView mInventoryGrid;
    private static int type;

    public static InventoryFragment newInstance() {
        return newInstance(Item.ALL);
    }

    public static InventoryFragment newInstance(int type) {
        InventoryFragment f = new InventoryFragment();
        InventoryFragment.type = type;
        //Log.d("Inventory fragment", Thread.currentThread().getName());
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.popup_inventory, container, false);

        // Assign close button
        v.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopup(v);
            }
        });

        // Get the views
        mInventoryGrid = (GridView) v.findViewById(R.id.inventory_grid);
        mInventoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openItemInfo(position);
            }
        });

        loadInventory();

        ////Log.d("Inv", mInventory.toString());

        return v;
    }

    public void closePopup(View view) {
        dismiss();
    }

    private void loadInventory() {
        Log.d("loadInventory");
        ItemEventsBus itemEventsBus = StalkerApp.getInstance().getItemEventBus();
        itemEventsBus.getUserItems().observeOn(AndroidSchedulers.mainThread()).take(1)
                .subscribe((ui) -> {
//                    JsonObject jo = ui.toJson();
//                    long before = System.currentTimeMillis();
//                    String str = jo.toString();
//                    long after = System.currentTimeMillis();
//                    long before1 = System.currentTimeMillis();
//                    String str1 = jo.toString();
//                    long after1 = System.currentTimeMillis();
//                    android.util.Log.d("Inventory fragment", "1: " + (after - before) + " 2: " + (after1 - before1));
//                    android.util.Log.d("Inventory fragment", "Items: " + ui.getItems().size() + " Thread: " + Thread.currentThread().getName());
                    //mInventoryGrid.setAdapter(new InventoryImageAdapter(getActivity(), ui.getItems())); return;
                    switch (type)
                    {
                        case Item.ALL: mInventory = ui.getItems(); break;
                        case Item.BOOSTER: mInventory = ui.getBoosters(); break;
                        case Item.ARMOR: mInventory = ui.getArmors(); break;
                    }
                    mInventoryGrid.setAdapter(new InventoryImageAdapter(getActivity(), mInventory));
                });
        itemEventsBus.requestItems();

    }

    private void openItemInfo(int position) {
        Item item = mInventory.get(position);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("item");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dia//Log.
        ItemInfoFragment newFragment = ItemInfoFragment.newInstance(item);
        newFragment.show(ft, "item");
        newFragment.setCallback(new ItemInfoClosed() {
            @Override
            public void onItemInfoClosed() {
                dismiss();
            }
        });
    }
}
