package net.afterday.compas.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Switch;

import net.afterday.compas.R;
import net.afterday.compas.core.inventory.items.Item;
import net.afterday.compas.core.player.Player;
import net.afterday.compas.engine.events.ItemEventsBus;
import net.afterday.compas.engine.events.PlayerEventBus;
import net.afterday.compas.settings.Constants;
import net.afterday.compas.settings.Settings;

import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by spaka on 7/21/2018.
 */

public class SettingsFragment extends DialogFragment
{
    private Settings settings;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
        settings = Settings.instance(getActivity().getApplication());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Observable.timer(300, TimeUnit.SECONDS).take(1).subscribe((t) -> closePopup(v));
        View v = inflater.inflate(R.layout.settings_fragment, container, false);
        ((Switch)v.findViewById(R.id.vibroSwitch)).setChecked(settings.getBoolSetting(Constants.VIBRATION));
        ((Switch)v.findViewById(R.id.compassSwitch)).setChecked(settings.getBoolSetting(Constants.COMPASS));
        ((Switch)v.findViewById(R.id.vibroSwitch)).setOnCheckedChangeListener((btn, on) -> {
            settings.setBoolSetting(Constants.VIBRATION, on);
        });
        ((Switch)v.findViewById(R.id.compassSwitch)).setOnCheckedChangeListener((btn, on) -> {
            settings.setBoolSetting(Constants.COMPASS, on);
        });
        v.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    dismiss();
                }catch (Exception e)
                {}

            }
        });
        RadioButton p = (RadioButton)v.findViewById(R.id.orientationPort);//.setOnClickListener((e) -> orientationPort());
        RadioButton h = (RadioButton)v.findViewById(R.id.orientationLand);//.setOnClickListener((e) -> orientationLand());
        int o = settings.getIntSetting(Constants.ORIENTATION);
        if(o == Constants.ORIENTATION_PORTRAIT)
        {
            p.setChecked(true);
        }else if(o == Constants.ORIENTATION_LANDSCAPE)
        {
            h.setChecked(true);
        }
        p.setOnClickListener((e) -> orientationPort());
        h.setOnClickListener((e) -> orientationLand());
        return v;
    }

    public void orientationPort()
    {
        settings.setIntSetting(Constants.ORIENTATION, Constants.ORIENTATION_PORTRAIT);
    }

    public void orientationLand()
    {
        settings.setIntSetting(Constants.ORIENTATION, Constants.ORIENTATION_LANDSCAPE);
    }
}
