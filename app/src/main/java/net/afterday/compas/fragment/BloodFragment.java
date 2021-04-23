package net.afterday.compas.fragment;

import android.animation.ValueAnimator;
import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.afterday.compas.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class BloodFragment extends DialogFragment
{
    private CompositeDisposable subscriptions = new CompositeDisposable();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();

        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Observable.timer(300, TimeUnit.SECONDS).take(1).subscribe((t) -> closePopup(v));
        View v = inflater.inflate(R.layout.blood_fragment, container, false);
        ValueAnimator vAnim = ValueAnimator.ofFloat(1,0);
        vAnim.setDuration(3000);
        vAnim.addUpdateListener((a) -> {
            v.setAlpha((float)a.getAnimatedValue());
        });
        vAnim.start();
        subscriptions.add(Observable.timer(3000, TimeUnit.MILLISECONDS).subscribe((t) -> close()));
        return v;
    }

    public void close() {
        dismiss();
    }


    @Override
    public void onPause()
    {
        super.onPause();
        if(subscriptions != null && !subscriptions.isDisposed())
        {
            subscriptions.dispose();
            subscriptions = null;
            try
            {
                close();
            }catch (Exception e)
            {
            }
        }
    }

}
