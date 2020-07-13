package net.afterday.compas.app;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import net.afterday.compas.app.logging.RemoveLogger;
import net.afterday.compas.app.pda.geiger.Compass;
import net.afterday.compas.app.pda.geiger.Geiger;
import net.afterday.compas.app.util.PermissionsManager;
import net.afterday.compas.engine.core.gameState.Frame;
import net.afterday.compas.engine.core.influences.Influence;
import net.afterday.compas.engine.core.inventory.items.Events.ItemAdded;
import net.afterday.compas.engine.core.inventory.items.Item;
import net.afterday.compas.engine.core.player.Equipment;
import net.afterday.compas.engine.core.player.Player;
import net.afterday.compas.engine.core.player.PlayerProps;
import net.afterday.compas.engine.engine.events.*;
import net.afterday.compas.app.fragment.*;
import net.afterday.compas.app.settings.*;
import net.afterday.compas.app.settings.Constants;
import net.afterday.compas.app.view.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private LocalMainService stalkerApp;
    private static final int PRESS_DELAY = 500;
    private static final int PRESS_LONG_DELAY = 1000;
    private static final int PRESS_SUICIDE = 1000;
    //Views
    private ViewGroup mContentView;
    private Compass mCompass;
    private Geiger mGeiger;
    private Radbar mRadbar;
    private Healthbar mHealthbar;
    private Bar mArmorBar;
    private Bar mStaminaBar;
    private Bar mDeviceBar;
    private Clock mClock;
    private Battery mBattery;
    private ImageButton mQrButton;
    private RecyclerView logList;
    private Indicator mIndicator;
    private CountDownTimer countDownTimer;
    private LevelProgress levelProgress;
    private ViewGroup layout;

    private boolean hasActiveBooster,
                    hasActiveDevice,
                    hasActiveArmor;
    ///////////////////////////////////////////////////////
    //Streams
    private Disposable framesSubscribtion,
            userActionsSubscribtion,
            impactsSubsciption;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private Observable<Frame> framesStream;
    private Observable<Long> countDownStream;
    private Observable<Integer> playerLevelStream;
    private Observable<Player.STATE> playerStateStream;
    private Observable<ItemAdded> itemAddedStream;
    private Player.STATE currentState;
    ////////////////////////////////////////////////////////
    private long qrBtnPressTime = 0;
    private long hBarPressTime = 0;
    private long tubePressTime = 0;
    private long staminaPressTime = 0;
    private long devicePressTime = 0;
    private long armorPressTime = 0;
    private long rBarPressTime = 0;
    private long rClockPressTime = 0;
    private long lastTick = 0;
    private long duration = 0;
    private SmallLogListAdapter logAdapter;
    private boolean showArtifactsSignal = true;
    private boolean active = false;
    private SettingsListener settingsListener;

    ////////////////////////////////////////////////////////
    private final Subject<Integer> orientationChanges = BehaviorSubject.create();
    private final ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            lastTick = System.currentTimeMillis();

            // Hide top bar
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            setupListeners();
            //mIndicator.setVisibility(View.GONE);
            stalkerApp = ((LocalMainService.MainBinder)iBinder).getService();
            framesStream = stalkerApp.getFramesStream();
            countDownStream = stalkerApp.getCountDownStream();
            playerLevelStream = stalkerApp.getPlayerLevelStream();
            playerStateStream = stalkerApp.getPlayerStateStream();
            disposables.add(playerLevelStream.observeOn(AndroidSchedulers.mainThread()).subscribe((pl) -> {

                //mTube.setLevel(pl);
                //mGeiger.setLevel(pl);
                //mIndicator.setLevel(pl);
                if(pl >= 4)
                {
                    //mGeiger.setFingerPrint(true);
                }
                if(pl == 5)
                {
                    //mIndicator.setVisibility(View.VISIBLE);
                    //mGeiger.setBrokenGlass(true);
                }
            }));
            disposables.add(playerStateStream.observeOn(AndroidSchedulers.mainThread()).subscribe((s) -> {
                currentState = s;
                //mTube.setState(s);
                showArtifactsSignal = s.getCode() == Player.ALIVE;
                if(!showArtifactsSignal)
                {
                    //mIndicator.setStrength(0);
                }
            }));
            //disposables.add(countDownStream.observeOn(AndroidSchedulers.mainThread()).subscribe((t) -> countDownTimer.setSecondsLeft(t)));
            disposables.add(framesStream
                    //.doOnNext((i) -> {//Log.d(TAG, "ImpactsStream: " + Thread.currentThread().getName()); updateViews(null, null);})
                    .observeOn(AndroidSchedulers.mainThread())
                    //.doOnNext((i) -> //Log.d(TAG, "ImpactsStream: " + Thread.currentThread().getName()))
                    .subscribe((frame -> updateViews(frame))));
            itemAddedStream = stalkerApp.getItemAddedStream();
            levelProgress.addOnLevelChangedListener((l) -> ((LevelIndicator)mQrButton).setLevel(l));
            disposables.add(itemAddedStream.observeOn(AndroidSchedulers.mainThread()).subscribe((ia) -> levelProgress.setProgress(ia)));
            disposables.add(stalkerApp.getBatteryStatusStream().observeOn(AndroidSchedulers.mainThread())
                    .subscribe((b) -> {
                        mBattery.setStatus(b);
                    }));
            framesStream.take(1)
                    //.doOnNext((i) -> {//Log.d(TAG, "ImpactsStream: " + Thread.currentThread().getName()); updateViews(null, null);})
                    .observeOn(AndroidSchedulers.mainThread())
                    //.doOnNext((i) -> //Log.d(TAG, "ImpactsStream: " + Thread.currentThread().getName()))
                    .subscribe((frame -> {
                        if(frame.getPlayerProps().getLevel() == 5)
                        {
                            levelProgress.showMax(true);
                        }else
                        {
                            levelProgress.setProgress(frame.getPlayerProps().getLevelXp());
                        }
                        ((LevelIndicator)mQrButton).setLevel(frame.getPlayerProps().getLevel());
                    }));
            disposables.add(Observable.combineLatest(EmissionEventBus.instance().getEmissionStateStream(), PlayerEventBus.instance().getPlayerFractionStream(), (e, f) -> new Pair<Boolean, Player.FRACTION>(e, f))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((s) -> {
                                        /*mTube.setFraction(s.second);
                                        if(s.second == Player.FRACTION.MONOLITH)
                                        {
                                            mTube.setEmission(false);
                                        }else
                                        {
                                            mTube.setEmission(s.first);
                                        }*/
                                    }));

            //Orientation
            Log.d(TAG, "SERVICE CONNECTED!!!!");
            setupLog();
            active = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            Log.d(TAG, "SERVICE DISCONNECTED!!!!");
        }
    };

    @SuppressLint("SourceLockedOrientationActivity")
    private void setOrientation(int o)
    {
        Log.e(TAG, "SET ORIENTATION: " + (o == Constants.ORIENTATION_PORTRAIT ? "PORTRAIT" : (o == Constants.ORIENTATION_LANDSCAPE ? "LANDSCAPE" : "UNKNOWN")));
        if(o == Constants.ORIENTATION_PORTRAIT && getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else if(o == Constants.ORIENTATION_LANDSCAPE && getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        PermissionsManager.check(this);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
        }
        disposables.add(orientationChanges.skip(1).observeOn(AndroidSchedulers.mainThread()).subscribe((o) -> {setOrientation(o);}));
        orientationChanges.onNext(Settings.instance().getIntSetting(Constants.ORIENTATION));
        int o = Settings.instance().getIntSetting(Constants.ORIENTATION);
        setOrientation(o);
        setContentView(R.layout.activity_main);
        bindViews();
        setViewListeners();
        disposables.add(Observable.combineLatest(PlayerEventBus.instance().getPlayerFractionStream(), orientationChanges, (pf, x) -> new Pair<Player.FRACTION, Integer>(pf, x)).observeOn(AndroidSchedulers.mainThread()).subscribe((p) -> setBackground(p.first, p.second)));
        bindService(new Intent(MainActivity.this, LocalMainService.class), serviceConnection, Context.BIND_ABOVE_CLIENT);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void setupLog()
    {
        RecyclerView.LayoutManager logListManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        ((LinearLayoutManager)logListManager).setStackFromEnd(true);
        logList.setLayoutManager(logListManager);
        logAdapter = new SmallLogListAdapter(this, new ArrayList<>());
        logList.setAdapter(logAdapter);
        disposables.add(stalkerApp.getLogStream().subscribe((log) -> {logAdapter.setDataset(log); logListManager.scrollToPosition(log.size() - 1);}));
//        stalkerApp.registerLogAdapter(logAdapter);

        RemoveLogger l = new RemoveLogger();
        l.SendInfo();
    }

    private void updateViews(Frame frame)
    {
        PlayerProps pProps = frame.getPlayerProps();
        Equipment equipment = frame.getEquipment();
        if(pProps.getState().getCode() == Player.ALIVE)
        {
            if(pProps.isHit(Influence.EMISSION)
                    || pProps.isHit(Influence.ANOMALY)
                    || pProps.isHit(Influence.BURER)
                    || pProps.isHit(Influence.CONTROLLER)
                    || pProps.isHit(Influence.MENTAL))
            {
                showBlood();
            }
        }

        /*if(pProps.getState().getCode() == Player.ALIVE && pProps.getHealthImpact() <= 0)
        {
            mGeiger.setAnomaly((float) pProps.getAnomalyImpact());
            mGeiger.setMental((float) pProps.getMentalImpact());
            mGeiger.setMonolith((float) pProps.getMonolithImpact());
            ////Log.d(TAG, "UPDATE VIEWS: Impacts - radiation: " + impacts[Influence.RADIATION]);
            mGeiger.toSvh((float) pProps.getRadiationImpact(), 1000);

        }else {
            mGeiger.setAnomaly(0f);
            mGeiger.setMental(0f);
            mGeiger.setMonolith(0f);
            mGeiger.toSvh(0f, 750);
        }*/

        /*mTube.setParameters(
                pProps.getRadiationImpact(),
                pProps.getAnomalyImpact(),
                pProps.getMentalImpact(),
                pProps.getMonolithImpact(),
                pProps.getControllerImpact(),
                pProps.getBurerImpact(),
                pProps.getHealthImpact(),
                pProps.getState()
        );*/
        // Radbar

        /*mRadbar.setInfo(
                pProps.getHealth(),
                pProps.getRadiation(),
                pProps.getHealthImpact(),
                pProps.getController(),
                pProps.hasRadiationInstant()
        );*/

        mHealthbar.setInfo(
                pProps.getHealth(),
                pProps.getHealthImpact(),
                pProps.getController(),
                pProps.hasHealthInstant()
        );

        if(showArtifactsSignal)
        {
            if(frame.getPlayerProps().getHealthImpact() > 0)
            {
                //mIndicator.setStrength(0);
            }else
            {
                //mIndicator.setStrength((float) pProps.getArtefactImpact());
            }
        }
        mStaminaBar.setPercents(equipment.getBoosterPercents());
        mDeviceBar.setPercents(equipment.getDevicePercents());
        mArmorBar.setPercents(equipment.getArmorPercents());
        hasActiveArmor = equipment.getArmorPercents() > 0;
        hasActiveBooster = equipment.getBoosterPercents() > 0;
        hasActiveDevice = equipment.getDevicePercents() > 0;
    }

    private void setViewListeners()
    {
        mStaminaBar.setOnTouchListener((v, event) -> {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    staminaPressTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    if(System.currentTimeMillis() - staminaPressTime > PRESS_DELAY && isAlive())
                    {
                        openInventory(Item.BOOSTER);
                    }
            }
            return true;
        });
        mDeviceBar.setOnTouchListener((v, event) -> {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    devicePressTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    if(System.currentTimeMillis() - devicePressTime > PRESS_DELAY && isAlive())
                    {
                        openInventory(Item.DEVICES);
                    }
            }
            return true;
        });
        mArmorBar.setOnTouchListener((v, event) -> {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    armorPressTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    if(System.currentTimeMillis() - armorPressTime > PRESS_DELAY && isAlive())
                    {
                        openInventory(Item.ARMOR);
                    }
            }
            return true;
        });
        mQrButton.setOnTouchListener((v, event) -> {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    qrBtnPressTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    long delay = isAlive() ? PRESS_DELAY : PRESS_LONG_DELAY;
                    if(System.currentTimeMillis() - qrBtnPressTime > delay)
                    {
                        openScanner();
                    }
            }
            return true;
        });

        mRadbar.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    rBarPressTime = System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_UP:
                    if(System.currentTimeMillis() - rBarPressTime > PRESS_DELAY && isAlive())
                    {
                        openInventory(Item.ALL);
                    }
                    break;

            }
            return true;
        });
        mHealthbar.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    hBarPressTime = System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_UP:
                    if(System.currentTimeMillis() - hBarPressTime > PRESS_DELAY && isAlive())
                    {
                        openInventory(Item.ALL);
                    }
                    break;

            }
            return true;
        });

        /*mTube.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    tubePressTime = System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_UP:
                    if(currentState != null && currentState.getSuicideType() != Player.SUICIDE_NOT_ALLOWED && System.currentTimeMillis() - tubePressTime > PRESS_SUICIDE)
                    {
                        if(currentState == Player.STATE.W_ABDUCTED)
                        {
                            PlayerEventBus.instance().suicide();
                            return true;
                        }
                        openSuicideConfirmation();
                    }
                    break;

            }
            return true;
        });*/

        mClock.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    rClockPressTime = System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_UP:
                    if(System.currentTimeMillis() - rClockPressTime > PRESS_DELAY && isAlive())
                    {
                        openDebug();
                    }
                    break;

            }
            return true;
        });

    }

    private boolean isAlive()
    {
        return currentState != null && currentState.getCode() == Player.ALIVE;
    }

    private void openSuicideConfirmation()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("suicide");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dia//Log.
        DialogFragment newFragment = new SuicideConfirmationFragment();
        newFragment.show(ft, "scanner");
    }

    private void showBlood()
    {
        if(!active)
        {
            return;
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment bloodFragment = new BloodFragment();
        bloodFragment.show(ft, "blood");
    }

    private void bindViews()
    {
        mRadbar = findViewById(R.id.radbar);
        mHealthbar = findViewById(R.id.healthbar);
        mArmorBar = findViewById(R.id.armorbar);
        mStaminaBar = findViewById(R.id.staminabar);
        mDeviceBar = findViewById(R.id.devicebar);
        mClock = findViewById(R.id.clock);
        mBattery = findViewById(R.id.battery);
        mQrButton = findViewById(R.id.qrbutton);
        logList = findViewById(R.id.log_list);
        //mIndicator = findViewById(R.id.indicator);
        //countDownTimer = findViewById(R.id.countdown);
        levelProgress = findViewById(R.id.levelProgress);
        layout = findViewById(R.id.activity_main);
    }

    private void setBackground(Player.FRACTION pf, int orientation)
    {
        layout.setBackground(ContextCompat.getDrawable(this, getBackground(pf, orientation)));
    }

    private int getBackground(Player.FRACTION pf, int orientation)
    {
        switch (pf)
        {
            case MONOLITH: return orientation == Constants.ORIENTATION_LANDSCAPE ? R.drawable.background_h_monolith : R.drawable.background_v_monolith;
            case STALKER: return orientation == Constants.ORIENTATION_LANDSCAPE ? R.drawable.background_h_merged : R.drawable.background_v_merged;
            case GAMEMASTER:  return orientation == Constants.ORIENTATION_LANDSCAPE ? R.drawable.background_h_gamemaster : R.drawable.background_v_gamemaster;
            case DARKEN: return orientation == Constants.ORIENTATION_LANDSCAPE ? R.drawable.background_h_darken : R.drawable.background_v_darken;
            default: return -1;
        }
    }

    private void openDebug() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("debug");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dia//Log.
        DialogFragment newFragment = new DebugFragment();
        newFragment.show(ft, "debug");
    }

    private void openInventory(int type) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(type == Item.ALL)
        {
            Fragment prev = getFragmentManager().findFragmentByTag(InventoryFragment.TAG_INVENTORY);
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            Bundle b = new Bundle();
            b.putInt(InventoryFragment.TYPE, type);
            DialogFragment newFragment = new InventoryFragment();
            newFragment.setArguments(b);
            newFragment.show(ft, InventoryFragment.TAG_INVENTORY);
            return;
        }

        if((type == Item.ARMOR && hasActiveArmor) || (type == Item.BOOSTER && hasActiveBooster) || (type == Item.DEVICES && hasActiveDevice))
        {
            //TODO: uncommented and fix
            /*ItemEventsBus.instance().getUserItems().take(1).subscribe((Inventory inv) -> {
                switch (type)
                {
                    case Item.DEVICES: openItemInfo(inv.getActiveDevice()); break;
                    case Item.ARMOR: openItemInfo(inv.getActiveArmor()); break;
                    case Item.BOOSTER: openItemInfo(inv.getActiveBooster()); break;
                }
            });*/
            ItemEventsBus.instance().requestItems();
        }
    }



    private void openScanner()
    {
//        android.util.//Log.e(TAG, "OPEN SCANNER");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("scanner");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dia//Log.
        DialogFragment newFragment = ScannerFragment.newInstance();
        newFragment.show(ft, "scanner");
    }

    private void setupListeners()
    {
        ItemEventsBus ieBus = LocalMainService.getInstance().getItemEventBus();
        disposables.add(ieBus.getItemAddedEvents().observeOn(AndroidSchedulers.mainThread()).subscribe((i) -> itemAdded(i)));
        disposables.add(ieBus.getUnknownItemEvents().observeOn(AndroidSchedulers.mainThread()).subscribe((i) -> unknownItem(i)));
        settingsListener = (key, val) -> {
            switch (key)
            {
                case Constants.COMPASS:
                    try{
                        if(Boolean.parseBoolean(val))
                        {
                            mCompass.compassOn();
                        }else
                        {
                            mCompass.compassOff();
                        }
                    }catch (Exception e)
                    {
                    }
                    break;
                case Constants.ORIENTATION:
                    this.orientationChanges.onNext(Integer.parseInt(val));
            }
        };
        Settings.instance().addSettingsListener(settingsListener);
    }

    private void itemAdded(Item item)
    {
        ////Log.d("Item added: " + item.getName());
    }

    private void unknownItem(String item)
    {
        ////Log.e("Unknown item!");
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "ON DESTROY!!!!");
        super.onDestroy();
        if(!disposables.isDisposed())
        {
            disposables.dispose();
            settingsListener = null;
        }
        //mGeiger.setOnTouchListener(null);
        unbindService(serviceConnection);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        active = false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        active = true;

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.stopService(new Intent(this, LocalMainService.class));
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private long getUsedMemory() {
        long freeSize = 0L;
        long totalSize = 0L;
        long usedSize = -1L;
        try {
            Runtime info = Runtime.getRuntime();
            freeSize = info.freeMemory();
            totalSize = info.totalMemory();
            usedSize = totalSize - freeSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usedSize;
    }

    private void openItemInfo(Item item) {
        android.util.Log.d(TAG, "openItemInfo");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("item");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dia//Log.
        ItemInfoFragment newFragment = ItemInfoFragment.newInstance(item);
        newFragment.show(ft, "item");
    }
}
