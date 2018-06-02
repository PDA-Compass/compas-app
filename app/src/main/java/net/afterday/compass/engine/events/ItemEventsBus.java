package net.afterday.compass.engine.events;

import net.afterday.compass.core.events.Event;
import net.afterday.compass.core.inventory.Inventory;
import net.afterday.compass.core.inventory.items.Events.AddItem;
import net.afterday.compass.core.inventory.items.Item;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Justas Spakauskas on 3/25/2018.
 */

public class ItemEventsBus
{
    private static ItemEventsBus instance;
    private final Subject<Inventory> userItems = PublishSubject.create();
    private final Subject<String> addItemsEvents = PublishSubject.create();
    private final Subject<String> requestUserItems = PublishSubject.create();
    private final Subject<Item> useItem = PublishSubject.create();
    private final Subject<Item> dropItem = PublishSubject.create();
    private final Subject<AddItem> addItem = PublishSubject.create();
    private final Subject<Event> userItemsRequests = PublishSubject.create();
    private final Subject<Item> itemAdded = PublishSubject.create();
    private final Subject<String> addItemFailed = PublishSubject.create();
    private final Subject<Item> requestItem = PublishSubject.create();

    private ItemEventsBus()
    {

    }

    public void requestItems()
    {
        this.requestUserItems.onNext("REQUEST");
    }

    public Observable<String> getUserItemsRequests()
    {
        return requestUserItems;
    }

    public Observable<Inventory> getUserItems()
    {
        return this.userItems;
    }

    public void userItemsLoaded(Inventory inventory)
    {
        //Log.d("User items loaded: " + items);
        this.userItems.onNext(inventory);
    }

    public void addItem(String itemCode)
    {
        this.addItemsEvents.onNext(itemCode);
    }

    public void useItem(Item item)
    {
        this.useItem.onNext(item);
    }

    public void dropItem(Item dropItem)
    {
        this.dropItem.onNext(dropItem);
    }

    public Observable<String> getAddItemEvents()
    {
        return this.addItemsEvents;
    }

    public void itemAdded(Item item)
    {
        //Log.d("EVENT BUS: itemAdded - " + item);
        this.itemAdded.onNext(item);

    }

    public Observable<Item> getItemAddedEvents()
    {
        return this.itemAdded;
    }

    public Observable<Item> getDropItemEvents()
    {
        return this.dropItem;
    }

    public void itemUnknown(String code)
    {
        this.addItemFailed.onNext(code);
    }

    public Observable<String> getUnknownItemEvents()
    {
        return this.addItemFailed;
    }

    public Observable<Item> getUseItemRequests()
    {
        return this.useItem;
    }

    public static ItemEventsBus instance()
    {
        if(instance == null)
        {
            instance = new ItemEventsBus();
        }
        return instance;
    }
}
