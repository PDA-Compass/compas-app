package net.afterday.compass.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.afterday.compass.R;
import net.afterday.compass.logging.LogLine;

import java.util.ArrayList;
import java.util.TimeZone;

public class SmallLogListAdapter extends RecyclerView.Adapter
{
    private ArrayList<LogLine> mDataset;
    private Typeface mTypeface;
    private TimeZone mTimezone;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
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

    // Provide a suitable constructor (depends on the kind of dataset)
    public SmallLogListAdapter(Context ctx, ArrayList<LogLine> dataset) {
        mDataset = dataset;
        this.context = ctx;
        try {
            mTypeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/console.ttf");
        }
        catch (RuntimeException e) {
            //Log.e("SmallLogListAdapter", "Cannot create typeface");
        }

        mTimezone = TimeZone.getTimeZone("GMT+03:00");
    }

    public void setDataset(ArrayList<LogLine> dataset) {
        mDataset = dataset;
    }

    // Create new views (invoked by the layout manager)
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

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        LogLine line = mDataset.get(position);

        // Get clock
        vh.mTime.setText(line.getDate());
        if(line.hasResId())
        {
            vh.mText.setText(context.getText(line.getResId()));
        }else
        {
            vh.mText.setText(line.getText());
        }
        //vh.mText.setText(line.getText());

        if (line.getType() == 0) {
            vh.mTime.setTextColor(0xFFFF8800);
            vh.mText.setTextColor(0xFFFF8800);
        } else if (line.getType() == 1) {
            vh.mTime.setTextColor(0xFFFF0000);
            vh.mText.setTextColor(0xFFFF0000);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //return 2;
        return mDataset.size();
    }
}
