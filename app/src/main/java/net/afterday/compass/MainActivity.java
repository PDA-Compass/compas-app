package net.afterday.compass;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import net.afterday.compass.core.gameState.Frame;
import net.afterday.compass.core.inventory.Inventory;
import net.afterday.compass.core.inventory.items.Events.ItemAdded;
import net.afterday.compass.core.inventory.items.Item;
import net.afterday.compass.core.player.Player;
import net.afterday.compass.core.player.PlayerProps;
import net.afterday.compass.core.userActions.UserActionsPack;
import net.afterday.compass.engine.events.ItemEventsBus;
import net.afterday.compass.engine.events.PlayerEventBus;
import net.afterday.compass.fragment.InventoryFragment;
import net.afterday.compass.fragment.ItemInfoFragment;
import net.afterday.compass.fragment.ScannerFragment;
import net.afterday.compass.fragment.SuicideConfirmationFragment;
import net.afterday.compass.view.ArmorBar;
import net.afterday.compass.view.Battery;
import net.afterday.compass.view.Clock;
import net.afterday.compass.view.CountDownTimer;
import net.afterday.compass.view.Geiger;
import net.afterday.compass.view.Healthbar;
import net.afterday.compass.view.Indicator;
import net.afterday.compass.view.LevelIndicator;
import net.afterday.compass.view.LevelProgress;
import net.afterday.compass.view.Radbar;
import net.afterday.compass.view.SmallLogListAdapter;
import net.afterday.compass.view.StaminaBar;
import net.afterday.compass.view.Tube;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

//import net.afterday.compass.logging.Log;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private StalkerApp stalkerApp;
    private static final int PRESS_DELAY = 1000;
    private static final int PRESS_SUICIDE = 2000;
    //Views
    private ViewGroup mContentView;
    private Geiger mGeiger;
    private Radbar mRadbar;
    private Healthbar mHealthbar;
    private ArmorBar mArmorBar;
    private StaminaBar mStaminaBar;
    private Clock mClock;
    private Battery mBattery;
    private Tube mTube;
    private ImageButton mQrButton;
    private RecyclerView logList;
    private Indicator mIndicator;
    private CountDownTimer countDownTimer;
    private LevelProgress levelProgress;
    private boolean hasActiveBooster,
                    hasActiveArmor;
    ///////////////////////////////////////////////////////
    //Streams
    private Disposable framesSubscribtion,
                       userActionsSubscribtion,
                       impactsSubsciption;
    private Observable<UserActionsPack> userActionsStream;
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
    private long armorPressTime = 0;
    private long rBarPressTime = 0;
    private long lastTick = 0;
    private long duration = 0;
    private SmallLogListAdapter logAdapter;
    private boolean showArtifactsSignal = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        lastTick = System.currentTimeMillis();
        //Log.d(TAG, "onCreate " + Thread.currentThread().getName());
//        Observable.interval(10000, TimeUnit.MILLISECONDS).subscribe(((k) -> {
//            //Log.d("TICK", "TICKED");
//        }));
        // Hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide toolbar
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            //
        }
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        bindViews();
        mIndicator.setVisibility(View.GONE);
        stalkerApp = StalkerApp.getInstance();
        framesStream = stalkerApp.getFramesStream();
        countDownStream = stalkerApp.getCountDownStream();
        playerLevelStream = stalkerApp.getPlayerLevelStream();
        playerStateStream = stalkerApp.getPlayerStateStream();
        playerLevelStream.observeOn(AndroidSchedulers.mainThread()).subscribe((pl) -> {

                                                                                        //((LevelIndicator)mQrButton).setLevel(pl);
                                                                                        mTube.setLevel(pl);
                                                                                        mGeiger.setLevel(pl);
                                                                                        mIndicator.setLevel(pl);
                                                                                        if(pl >= 4)
                                                                                        {
                                                                                            mGeiger.setFingerPrint(true);
                                                                                        }
                                                                                        if(pl == 5)
                                                                                        {
                                                                                            mIndicator.setVisibility(View.VISIBLE);
                                                                                            mGeiger.setBrokenGlass(true);
                                                                                        }
                                                                                      });
        playerStateStream.observeOn(AndroidSchedulers.mainThread()).subscribe((s) -> {
            currentState = s;
            mTube.setState(s);
            showArtifactsSignal = s.getCode() == Player.ALIVE;
            if(!showArtifactsSignal)
            {
                mIndicator.setStrength(0);
            }
        });
        countDownStream.observeOn(AndroidSchedulers.mainThread()).subscribe((t) -> countDownTimer.setSecondsLeft(t));
        framesSubscribtion = framesStream
                    //.doOnNext((i) -> {//Log.d(TAG, "ImpactsStream: " + Thread.currentThread().getName()); updateViews(null, null);})
                    .observeOn(AndroidSchedulers.mainThread())
                    //.doOnNext((i) -> //Log.d(TAG, "ImpactsStream: " + Thread.currentThread().getName()))
                    .subscribe((frame -> updateViews(frame)));
        userActionsStream = BehaviorSubject.create();
//        framesSubscribtion = framesStream.observeOn(AndroidSchedulers.mainThread()).subscribe((frame -> processFrame(frame)));
        userActionsSubscribtion = stalkerApp.setUserActionsStream(userActionsStream);
        itemAddedStream = stalkerApp.getItemAddedStream();
        levelProgress.addOnLevelChangedListener((l) -> ((LevelIndicator)mQrButton).setLevel(l));
        itemAddedStream.observeOn(AndroidSchedulers.mainThread()).subscribe((ia) -> levelProgress.setProgress(ia));

        setupLog();
        stalkerApp.getBatteryLevelStream().observeOn(AndroidSchedulers.mainThread())
                .subscribe((b) -> {
                    mBattery.setLevel(b);
                });
        setViewListeners();
        setupListeners();
    }

    private void setupLog()
    {
        RecyclerView.LayoutManager logListManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        logList.setLayoutManager(logListManager);
        logAdapter = new SmallLogListAdapter(this, new ArrayList<>());
        logList.setAdapter(logAdapter);
        stalkerApp.registerLogAdapter(logAdapter);
    }


    private void updateViews(Frame frame)
    {
//        long current = System.currentTimeMillis();
//        duration += current - lastTick;
//        lastTick = current;
        //Log.d(TAG, "updateViews " + Thread.currentThread().getName() + " ---- " + getUsedMemory() + " ---- " + duration);
//        ////Log.d(TAG, Thread.currentThread().getName());
        PlayerProps pProps = frame.getPlayerProps();
        if(pProps.getState().getCode() == Player.ALIVE && pProps.getHealthImpact() <= 0)
        {
            mGeiger.setAnomaly((float) pProps.getAnomalyImpact());
            mGeiger.setMental((float) pProps.getMentalImpact());
            ////Log.d(TAG, "UPDATE VIEWS: Impacts - radiation: " + impacts[Influence.RADIATION]);
            mGeiger.toSvh((float) pProps.getRadiationImpact(), 1000);

        }else {
            mGeiger.setAnomaly(0f);
            mGeiger.setMental(0f);
            mGeiger.toSvh(0f, 750);
        }
        mTube.setParameters(
                pProps.getRadiationImpact(),
                pProps.getAnomalyImpact(),
                pProps.getMentalImpact(),
                pProps.getControllerImpact(),
                pProps.getBurerImpact(),
                pProps.getHealthImpact(),
                pProps.getState(),
                false
        );
        // Radbar
        mRadbar.setInfo(
                pProps.getHealth(),
                pProps.getRadiation(),
                pProps.getHealthImpact(),
                pProps.getController(),
                pProps.hasRadiationInstant()
        );

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
                mIndicator.setStrength(0);
            }else
            {
                mIndicator.setStrength((float) pProps.getArtefactImpact());
            }
        }
        mStaminaBar.setPercents(pProps.getBoosterPercents());
        mArmorBar.setPercents(pProps.getArmorPercents());
        hasActiveArmor = pProps.getArmorPercents() > 0;
        hasActiveBooster = pProps.getBoosterPercents() > 0;
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
                    if(System.currentTimeMillis() - qrBtnPressTime > PRESS_DELAY && isAlive())
                    {
                        openScanner();
                    }
            }
            return true;
        });

        mRadbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
            }
        });
        mHealthbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
            }
        });

        mTube.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
            }
        });
    }

    private boolean isAlive()
    {
        int currentCode = currentState.getCode();
        return currentState != null && (currentCode == Player.ALIVE);
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

    private void bindViews()
    {
        mGeiger = (Geiger) findViewById(R.id.geiger);
        mRadbar = (Radbar) findViewById(R.id.radbar);
        mHealthbar = (Healthbar) findViewById(R.id.healthbar);
        mArmorBar = (ArmorBar) findViewById(R.id.armorbar);
        mStaminaBar = (StaminaBar) findViewById(R.id.staminabar);
        //mClock = (Clock) findViewById(R.id.clock);
        mBattery = (Battery) findViewById(R.id.battery);
        mTube = (Tube) findViewById(R.id.tube);
        mQrButton = (ImageButton) findViewById(R.id.qrbutton);
        logList = (RecyclerView) findViewById(R.id.log_list);
        mIndicator = (Indicator) findViewById(R.id.indicator);
        countDownTimer = (CountDownTimer) findViewById(R.id.countdown);
        levelProgress = (LevelProgress) findViewById(R.id.levelProgress);
    }

    private void openInventory(int type) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if(type == Item.ALL)
        {
            Fragment prev = getFragmentManager().findFragmentByTag("inventory");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            // Create and show the dia//Log.
            DialogFragment newFragment = InventoryFragment.newInstance(type);
            newFragment.show(ft, "inventory");
            return;
        }
        if((type == Item.ARMOR && !hasActiveArmor) || (type == Item.BOOSTER && !hasActiveBooster))
        {
            Fragment prev = getFragmentManager().findFragmentByTag("inventory");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            // Create and show the dia//Log.
            DialogFragment newFragment = InventoryFragment.newInstance(type);
            newFragment.show(ft, "inventory");
            return;
        }
        if((type == Item.ARMOR && hasActiveArmor) || (type == Item.BOOSTER && hasActiveBooster))
        {
            ItemEventsBus.instance().getUserItems().take(1).subscribe((Inventory inv) -> {
                switch (type)
                {
                    case Item.BOOSTER: openItemInfo(inv.getActiveBooster()); break;
                    case Item.ARMOR: openItemInfo(inv.getActiveArmor()); break;
                }
            });
            ItemEventsBus.instance().requestItems();
        }
    }



    private void openScanner()
    {
//
//        mScannerView = new ZXingScannerView(this);
//        QCscanner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                /*Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                startActivityForResult(intent, 0);*/
//                    mScannerView = new ZXingScannerView(MainActivity.this);   // Programmatically initialize the scanner view<br />
//                    setContentView(mScannerView);
//                    mScannerView.setResultHandler(MainActivity.this); // Register ourselves as a handler for scan results.<br />
//                    mScannerView.startCamera();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
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
        ItemEventsBus ieBus = StalkerApp.getInstance().getItemEventBus();
        ieBus.getItemAddedEvents().observeOn(AndroidSchedulers.mainThread()).subscribe((i) -> itemAdded(i));
        ieBus.getUnknownItemEvents().observeOn(AndroidSchedulers.mainThread()).subscribe((i) -> unknownItem(i));
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
        super.onDestroy();
        //Log.d(TAG, "OnDestroy");
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
//        newFragment.setCallback(new ItemInfoClosed() {
//            @Override
//            public void onItemInfoClosed() {
//                newFragment.dismiss();
//                ItemInfoFragment.newInstance(null);
//            }
//        });
    }
}
