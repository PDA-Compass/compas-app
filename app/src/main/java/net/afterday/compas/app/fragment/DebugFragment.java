package net.afterday.compas.app.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.afterday.compas.app.R;
import net.afterday.compas.engine.core.EventBus;
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DebugFragment extends DialogFragment {
    private Disposable subscribe;
    private HashMap<String,Item> list;
    private Observable<Long> ticks;

    class Item {
        TableRow row;
        TextView value;
        TextView time;
        Integer tt;
    }

    TableLayout tableLayout;
    private void anomalyHandler(AnomalyEvent anomalyEvent){
        String key = anomalyEvent.getId();
        if (list.containsKey(key)){
            Item item = list.get(key);
            item.tt = 0;
            item.time.setText("0");
            item.value.setText(Long.toString(anomalyEvent.getValue()));
            list.put(key, item);
        }
        else
        {
            draw(anomalyEvent);
        }
    }

    public void update() {
        for (String key: list.keySet()){
            Item item = list.get(key);
            if (item.tt > 100) {
                tableLayout.removeView(item.row);
                break;
            }
            item.tt++;
            item.time.setText(Integer.toString(item.tt));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        list = new HashMap<>();

        View view = inflater.inflate(R.layout.debug, container, false);
        tableLayout = view.findViewById(R.id.tableLayout);

        subscribe = EventBus.INSTANCE.anomaly()
                //.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext((s)->{
                        anomalyHandler(s);
                    })
                .subscribe();

        ticks = Observable.interval(0,1, TimeUnit.SECONDS);
        ticks.observeOn(AndroidSchedulers.mainThread())
                .doOnNext((s)->{update();})
                .subscribe();
        return view;
    }

    public void draw(AnomalyEvent anomalyEvent){
        TableRow row = new TableRow(tableLayout.getContext());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        row.setLayoutParams(lp);

        TextView textView1 = new TextView(tableLayout.getContext());
        textView1.setText("");
        textView1.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100, 1.0f));
        row.addView(textView1);

        TextView textView2 = new TextView(tableLayout.getContext());
        textView2.setText(anomalyEvent.getId());
        textView2.setTextSize(8.0f);
        textView2.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100, 1.0f));
        row.addView(textView2);

        TextView textView3 = new TextView(tableLayout.getContext());
        textView3.setText(Long.toString(anomalyEvent.getValue()));
        textView3.setTextSize(12.0f);
        textView3.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100, 1.0f));
        row.addView(textView3);

        TextView textView4 = new TextView(tableLayout.getContext());
        textView4.setText("0");
        textView4.setTextSize(12.0f);
        textView4.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100, 1.0f));
        row.addView(textView4);
        tableLayout.addView(row);

        Item item = new Item();
        item.row = row;
        item.value = textView3;
        item.time = textView4;
        item.tt = 0;

        list.put(anomalyEvent.getId(), item);

    }

    @Override
    public void onResume() {
        super.onResume();

        /*if (subscribe!= null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }*/
    }
}
