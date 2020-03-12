package net.afterday.compas.fragment;

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

import net.afterday.compas.R;
import net.afterday.compas.core.inventory.Inventory;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.Player;
import net.afterday.compas.engine.events.ItemEventsBus;
import net.afterday.compas.engine.events.PlayerEventBus;
import net.afterday.compas.logging.Logger;
import net.afterday.compas.LocalMainService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class InventoryFragment extends DialogFragment
{
    public static final String TAG_INVENTORY = "inventory";
    public static final String TAG_CATEGORY = "category";
    public static final String TYPE = "type";
    public static final int ALL = 0;
    public static final int BOOSTERS = 1;
    public static final int ARMORS = 2;
    public static final int DEVICES = 3;

    List<Item> mInventory;

    Map<Item.CATEGORY, List<Item>> itemsByCategory;
    List<Item.CATEGORY> categories;
    GridView mInventoryGrid;
    private View v;
    private int type;
    private CategoriesAdapter categoriesAdapter;
    private InventoryImageAdapter itemsAdapter;
    private boolean SHOW_CATEGORIES_ON_BACK = false;
    private CompositeDisposable subscriptions = new CompositeDisposable();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if(b != null)
        {
            type = b.getInt(TYPE, Item.ALL);
        }
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
        subscriptions.add(Observable.merge(ItemEventsBus.instance().getItemUsedEvents(), ItemEventsBus.instance().getItemDroppedEvents()).observeOn(AndroidSchedulers.mainThread()).subscribe((i) -> {clearItem(i); showItemsOfCategory(i.getItemDescriptor().getCategory().getId());}));
        subscriptions.add(PlayerEventBus.instance().getPlayerStateStream().observeOn(AndroidSchedulers.mainThread()).subscribe((ps) -> {if(ps.getCode() != Player.ALIVE) {close();}}));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Observable.timer(300, TimeUnit.SECONDS).take(1).subscribe((t) -> closePopup(v));
        View v = inflater.inflate(R.layout.popup_inventory, container, false);

        v.findViewById(R.id.openPrefs).setOnClickListener((c) -> openPrefs(c));
        v.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SHOW_CATEGORIES_ON_BACK)
                {
                    showCategories();
                }else
                {
                    close();
                }
            }
        });

        // Get the views
        mInventoryGrid = (GridView) v.findViewById(R.id.inventory_grid);
        loadInventory();

        ////Log.d("Inv", mInventory.toString());

        return v;
    }

    public void openPrefs(View view)
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("settings");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.show(ft, "settings");
    }

    public void close() {
        dismiss();
    }

    private void loadInventory() {
        //Log.d("loadInventory");
        ItemEventsBus itemEventsBus = LocalMainService.getInstance().getItemEventBus();
        itemEventsBus.getUserItems().observeOn(AndroidSchedulers.mainThread()).take(1)
                .subscribe((ui) -> {
                    mInventory = ui.getItems();
                    if(type == Item.ALL)
                    {
                        showCategories();
                    }else
                    {
                        showItemsOfCategory(type);
                    }
                });
        itemEventsBus.requestItems();
    }

    private void openItemInfo(Item item) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("item");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ItemInfoFragment newFragment = ItemInfoFragment.newInstance(item);
        newFragment.show(ft, "item");
        newFragment.setCallback(new ItemInfoCallback() {
            @Override
            public void onItemInfoClosed(Item item)
            {

            }

            @Override
            public void onItemUsed(Item item)
            {

            }

            @Override
            public void onItemDropped(Item item)
            {

            }

        });
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if(subscriptions != null && !subscriptions.isDisposed())
        {
            subscriptions.dispose();
        }
    }

    private void clearItem(Item item)
    {
        if(mInventory.contains(item))
        {
            mInventory.remove(item);
        }
    }

    private void showCategories()
    {
        SHOW_CATEGORIES_ON_BACK = false;
        categoriesAdapter = new CategoriesAdapter(getActivity(), mInventory);
        mInventoryGrid.setAdapter(categoriesAdapter);
        mInventoryGrid.setOnItemClickListener((parent, view, position, id) ->
        {
            List<Item> items = categoriesAdapter.getItemsByCategory(position);
            if(items.size() > 0)
            {
                openCategoryItems(items);
            }
        });
    }

    private void showItemsOfCategory(int category)
    {
        List<Item> itemsOfCategory = new ArrayList<>();
        for(Item i : mInventory)
        {
            if(i.getItemDescriptor().getCategory().getId() == category)
            {
                itemsOfCategory.add(i);
            }
        }
        openCategoryItems(itemsOfCategory);
    }

    private void openCategoryItems(List<Item> items)
    {
        SHOW_CATEGORIES_ON_BACK = true;
        itemsAdapter = new InventoryImageAdapter(getActivity(), items);
        mInventoryGrid.setAdapter(itemsAdapter);
        mInventoryGrid.setOnItemClickListener((parent, view, position, id) -> {
            openItemInfo((Item) itemsAdapter.getItem(position));
        });
    }
}
