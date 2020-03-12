package net.afterday.compas.fragment;

import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.afterday.compas.R;
import net.afterday.compas.engine.events.PlayerEventBus;
import net.afterday.compas.util.Fonts;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;


/**
 * Created by spaka on 5/13/2018.
 */

public class SuicideConfirmationFragment extends DialogFragment
{
    private View v;
    private Button cancelBtn;
    private Button confirmBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Observable.timer(15, TimeUnit.SECONDS).take(1).subscribe((t) -> closePopup(v));
        v = inflater.inflate(R.layout.suicide_confirmation, container, false);
        Typeface defaultTf = Fonts.instance().getDefaultTypeFace();
        TextView title = (TextView) v.findViewById(R.id.name);
        title.setTypeface(defaultTf);
        title.setTextSize(35);
        // Assign close button
        Button confirmBtn = (Button) v.findViewById(R.id.confirm_suicide);
        Button cancelBtn = (Button) v.findViewById(R.id.cancel_suicide);
        confirmBtn.setTypeface(defaultTf);
        confirmBtn.setTextSize(25);
        cancelBtn.setTypeface(defaultTf);
        cancelBtn.setTextSize(25);

        v.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopup(v);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                closePopup(v);
            }
        });

        confirmBtn.findViewById(R.id.confirm_suicide).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PlayerEventBus.instance().suicide();
                closePopup(v);
            }
        });

        //Log.d("SCANNER", "onCreateView");

        return v;
    }

    public void closePopup(View view) {
        try
        {
            if(!this.isDetached())
                dismiss();
        }catch (Exception e)
        {
        }
    }
}
