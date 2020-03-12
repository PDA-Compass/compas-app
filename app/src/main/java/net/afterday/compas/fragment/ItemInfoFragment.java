package net.afterday.compas.fragment;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.afterday.compas.R;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.Player;
import net.afterday.compas.engine.events.EmissionEventBus;
import net.afterday.compas.engine.events.ItemEventsBus;
import net.afterday.compas.engine.events.PlayerEventBus;
import net.afterday.compas.util.Fonts;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class ItemInfoFragment extends DialogFragment
{
    private ItemInfoCallback mCallback;
    private Typeface mTypeface;
    private LinearLayout mEffectHolder;
    private View v;
    private static Observable<Item> itemViews;
    private static Item mItem;
    private CompositeDisposable subscriptions = new CompositeDisposable();

    public static ItemInfoFragment newInstance(Item item) {
        ItemInfoFragment f = new ItemInfoFragment();
        mItem = item;
        //itemViews = ItemEventsBus.instance().getItemViewRequests();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //itemViews.observeOn(AndroidSchedulers.mainThread()).subscribe((i) -> setupItem(i));
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
        subscriptions.add(PlayerEventBus.instance().getPlayerStateStream().observeOn(AndroidSchedulers.mainThread()).subscribe((ps) -> {if(ps.getCode() != Player.ALIVE) {close();}}));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Observable.timer(300, TimeUnit.SECONDS).take(1).subscribe((t) -> closePopup(v));
        v = inflater.inflate(R.layout.popup_item_info, container);
        setupItem(mItem);
        // Assign close button
        v.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopup(v);
            }
        });


        return v;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if(!subscriptions.isDisposed())
        {
            subscriptions.dispose();
        }
    }

    private void setupItem(Item mItem)
    {
        // Fill in item name
        TextView itemName = (TextView) v.findViewById(R.id.name);
        if(mItem.getItemDescriptor().getNameId() > 0)
        {
            itemName.setText(mItem.getItemDescriptor().getNameId());
        }else
        {
            itemName.setText(mItem.getItemDescriptor().getName());
        }
        //Log.e("Item fragment", mItem.getName());
        itemName.setTextSize(30);
        TextView description = (TextView) v.findViewById(R.id.description);
        description.setTextSize(21);
        if(mItem.getItemDescriptor().getDescriptionId() > 0)
        {
            description.setText(mItem.getItemDescriptor().getDescriptionId());
        }else
        {
            description.setText(mItem.getItemDescriptor().getDescription());
        }


        Button dropButton = (Button) v.findViewById(R.id.drop);
        Button useButton = (Button) v.findViewById(R.id.use);

        try {
            mTypeface = Fonts.instance().getDefaultTypeFace();
            itemName.setTypeface(mTypeface);
            dropButton.setTypeface(mTypeface);
            dropButton.setTextSize(25);
            useButton.setTypeface(mTypeface);
            useButton.setTextSize(25);
            description.setTypeface(mTypeface);
        }
        catch (RuntimeException e) {
            //Log.e("ItemInfoFragment", "Cannot create typeface");
        }
        if(!mItem.getItemDescriptor().isConsumable() || mItem.getItemDescriptor().isArtefact() || mItem.isActive())
        {
            useButton.setVisibility(View.GONE);
        }
        if(!mItem.getItemDescriptor().isDropable() || mItem.isActive())
        {
            dropButton.setVisibility(View.GONE);
        }

        mEffectHolder = (LinearLayout) v.findViewById(R.id.effect_holder);

        // Set image
        ImageView itemImage = (ImageView) v.findViewById(R.id.item_image);
        itemImage.setImageResource(mItem.getItemDescriptor().getImage());

        dropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropItem(mItem);
            }
        });

        useButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useItem();
            }
        });

        for(int i = 0; i < Item.MODIFIERS_COUNT; i++)
        {
            if(mItem.hasModifier(i))
            {
                createEffectView(i, mItem.getModifier(i));
            }
        }

        if(mItem.getItemDescriptor().getName() == "Anabiotic")
        {
            subscriptions.add(EmissionEventBus.instance().getEmissionStateStream().observeOn(AndroidSchedulers.mainThread()).subscribe((active) -> {
                if(active)
                {
                    useButton.setVisibility(View.VISIBLE);
                }else
                {
                    useButton.setVisibility(View.GONE);
                }
            }));
        }

    }

    public void closePopup(View view) {
        try
        {
            dismiss();
        }catch (Exception e){}

    }

    public void close() {
        try
        {
            dismiss();
            if (mCallback != null)
            {
                mCallback.onItemInfoClosed(mItem);
            }
        }catch (Exception e)
        {}
    }

    public void setCallback(ItemInfoCallback callback) {
        mCallback = callback;
    }

    private void dropItem(Item item) {
        //Log.d("ItemInfoFragment", "Dropping item: " + mItem.getId() + ": " + mItem.getName());

//        Intent intent = new Intent("StalkerDropItem");
//        //intent.putExtra("item", mItem);
//        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        ItemEventsBus.instance().dropItem(item);
        close();
    }

    private void useItem() {
        //Log.d("ItemInfoFragment", "Using item: " + mItem.getId() + ": " + mItem.getName());

//        Intent intent = new Intent("StalkerUseItem");
//        //intent.putExtra("item", mItem);
//        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        if(mCallback != null)
        {
            mCallback.onItemUsed(mItem);
        }
        ItemEventsBus.instance().useItem(mItem);
        close();
    }

    private void receiveItem() {
        Intent intent = new Intent("StalkerGiveItem");
        //intent.putExtra("item", mItem);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

        close();
    }

    private LinearLayout createEffectView(int type, double amount) {
        LinearLayout l = new LinearLayout(getActivity());
        l.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        params.setMargins(px, px, px, px);
        l.setLayoutParams(params);

        ImageView effectImage = new ImageView(getActivity());
        effectImage.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        effectImage.setImageResource(getEffectImage(type, amount));
        l.addView(effectImage);

        TextView text = new TextView(getActivity());
        text.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        text.setText(getEffectText(type, amount));
        text.setTextColor(0xFFFFFFFF);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        text.setGravity(Gravity.CENTER);
        if (mTypeface != null) {
            text.setTypeface(mTypeface);
        }
        l.addView(text);

        mEffectHolder.addView(l);

        return l;
    }

    private int getEffectImage(int type, double amount) {
        if (amount > 1d) {
            switch (type) {
                case Item.ANOMALY_MODIFIER:
                    return R.drawable.info_anomaly_negative;
                case Item.BURER_MODIFIER:
                    return R.drawable.info_burer_negative;
                case Item.CONTROLLER_MODIFIER:
                    return R.drawable.info_controller_negative;
                case Item.MENTAL_MODIFIER:
                    return R.drawable.info_psi_negative;
                case Item.MONOLITH_MODIFIER:
                    return R.drawable.info_monolith;
                case Item.HEALTH_MODIFIER:
                    return R.drawable.info_health_clear;
                case Item.RADIATION_MODIFIER:
                    return R.drawable.info_radiation_negative;
                case Item.RADIATION_EMMITER:
                    return R.drawable.info_radiation;
                case Item.RADIATION_INSTANT:
                    return R.drawable.info_radiation_negative;
                case Item.HEALTH_INSTANT:
                    return R.drawable.info_health;
            }
        } else {
            switch (type) {
                case Item.ANOMALY_MODIFIER:
                    return R.drawable.info_anomaly;
                case Item.BURER_MODIFIER:
                    return R.drawable.info_burer;
                case Item.CONTROLLER_MODIFIER:
                    return R.drawable.info_controller;
                case Item.MENTAL_MODIFIER:
                    return R.drawable.info_psi;
                case Item.MONOLITH_MODIFIER:
                    return R.drawable.info_monolith;
                case Item.HEALTH_MODIFIER:
                    return R.drawable.info_health_clear_negative;
                case Item.RADIATION_MODIFIER:
                    return R.drawable.info_radiation;
                case Item.RADIATION_EMMITER:
                    return R.drawable.info_radiation_negative;
                case Item.RADIATION_INSTANT:
                    return R.drawable.info_radiation;
                case Item.HEALTH_INSTANT:
                    return R.drawable.info_health_negative;
            }
        }

//        switch (type) {
//            case "duration":
//                return R.drawable.info_duration;
//            case "radiation_instant":
//                if (amount > 0d) {
//                    return R.drawable.info_radiation_negative;
//                } else {
//                    return R.drawable.info_radiation;
//                }
//            case "health_instant":
//                if (amount > 0d) {
//                    return R.drawable.info_health;
//                } else {
//                    return R.drawable.info_health_negative;
//                }
//            case "radiation_emitter":
//                return R.drawable.info_radiation_negative;
//        }

        return 0;
    }

    private String getEffectText(int type, double amount) {


        switch (type) {
            case Item.ANOMALY_MODIFIER:
            case Item.BURER_MODIFIER:
            case Item.CONTROLLER_MODIFIER:
            case Item.MENTAL_MODIFIER:
            case Item.MONOLITH_MODIFIER:
            case Item.HEALTH_MODIFIER:
            case Item.RADIATION_MODIFIER:
                return String.format(Locale.US, "%1.0f%%", amountToPercent(amount));

            case Item.RADIATION_INSTANT:
                return String.format(Locale.US, "%1.0f Sv", amount);

            case Item.RADIATION_EMMITER:
                return String.format(Locale.US, "%1.0fSv/h", amount);

            case Item.HEALTH_INSTANT:
                return String.format(Locale.US, "%1.0f%%", amount);

//            case "duration":
//                return String.format(Locale.US, "%1.0f m", (amount / 1000 / 60));
        }
        return "50%";
    }

    private double amountToPercent(double amount) {
        if (amount < 1d) {
            return (1 - amount) * 100;
        }

        return (amount - 1) * 100;
    }
}
