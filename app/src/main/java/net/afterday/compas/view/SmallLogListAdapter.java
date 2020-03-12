package net.afterday.compas.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.afterday.compas.R;
import net.afterday.compas.logging.LogLine;
import net.afterday.compas.logging.LogLine;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class SmallLogListAdapter extends RecyclerView.Adapter
{
    private List<LogLine> mDataset;
    private Typeface mTypeface;
    private TimeZone mTimezone;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTime;
        public TextView mText;
        public ViewHolder(View container) {
            super(container);
            mTime = (TextView) container.findViewById(R.id.time);
            mText = (TextView) container.findViewById(R.id.text);
        }
    }

    public SmallLogListAdapter(Context ctx, ArrayList<LogLine> dataset) {
        mDataset = dataset;
        this.context = ctx;
        try {
            mTypeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/console.ttf");
        }
        catch (RuntimeException e) {
            //Log.e("SmallLogListAdapter", "Cannot create typeface");
        }

        mTimezone = TimeZone.getTimeZone("GMT+02:00");
    }

    public void setDataset(List<LogLine> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    @Override
    public SmallLogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.small_log_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //
        ((TextView) v.findViewById(R.id.time)).setTypeface(mTypeface);
        ((TextView) v.findViewById(R.id.text)).setTypeface(mTypeface);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        LogLine line = mDataset.get(position);

        // Get clock
        vh.mTime.setText(line.getDate());
        vh.mText.setText(line.getText());
        //vh.mText.setText(line.getText());
        int color = line.getColor();
        vh.mTime.setTextColor(color);
        vh.mText.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        //return 2;
        return mDataset.size();
    }
}
