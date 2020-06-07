package net.afterday.compas.app.logging;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.afterday.compas.engine.core.EventBus;
import net.afterday.compas.engine.engine.system.damage.DamageEvent;
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RemoveLogger {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    UUID device = UUID.randomUUID();

    OkHttpClient client = new OkHttpClient();

    private String AnomalyToString(List<AnomalyEvent> events) {
        JSONObject result = new JSONObject();


        JSONArray list = new JSONArray();
        for (AnomalyEvent event : events) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("aid", event.getId());
                jsonObject.put("type", event.getType());
                jsonObject.put("time", event.getAt());
                jsonObject.put("value", event.getValue());
                list.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            result.put("device", device);
            result.put("data", list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private String DamageToString(List<DamageEvent> events) {
        JSONObject result = new JSONObject();

        JSONArray list = new JSONArray();
        for (DamageEvent event : events) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("aid", event.getId());
                jsonObject.put("type", event.getType());
                jsonObject.put("time", event.getAt());
                jsonObject.put("value", event.getValue());
                list.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            result.put("device", device);
            result.put("data", list);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public void SendInfo() {
        try {
            String ver = System.getProperty("os.version")      + "(" + android.os.Build.VERSION.INCREMENTAL + ")";

            String s = "Debug-infos:";
            s += "\n OS Version: "      + ver;
            s += "\n OS API Level: "    + android.os.Build.VERSION.SDK_INT;
            s += "\n Device: "          + android.os.Build.DEVICE;
            s += "\n Model (and Product): " + android.os.Build.MODEL            + " ("+ android.os.Build.PRODUCT + ")";

            s += "\n RELEASE: "         + android.os.Build.VERSION.RELEASE;
            s += "\n BRAND: "           + android.os.Build.BRAND;
            s += "\n DISPLAY: "         + android.os.Build.DISPLAY;
            s += "\n CPU_ABI: "         + android.os.Build.CPU_ABI;
            s += "\n CPU_ABI2: "        + android.os.Build.CPU_ABI2;
            s += "\n UNKNOWN: "         + android.os.Build.UNKNOWN;
            s += "\n HARDWARE: "        + android.os.Build.HARDWARE;
            s += "\n Build ID: "        + android.os.Build.ID;
            s += "\n MANUFACTURER: "    + android.os.Build.MANUFACTURER;
            s += "\n SERIAL: "          + android.os.Build.SERIAL;
            s += "\n USER: "            + android.os.Build.USER;
            s += "\n HOST: "            + android.os.Build.HOST;

            JSONObject json = new JSONObject();
            json.put("name", android.os.Build.BRAND + " " + android.os.Build.MODEL);
            json.put("version", android.os.Build.VERSION.RELEASE);
            json.put("info", s);

            RequestBody body = RequestBody.create(json.toString(), JSON);
            Request request = new Request.Builder()
                    .url("http://188.242.194.9:9090/devices")
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful()) return;  //throw new IOException("Unexpected code " + response);
                        System.out.println(responseBody.string());
                    }
                }
            });

        } catch (Exception e) {
        }
    }

    private Disposable subscribe;
    public RemoveLogger() {
        subscribe = EventBus.INSTANCE.anomaly()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .buffer(15, TimeUnit.SECONDS, 50)
                .doOnNext((s) -> {
                    RequestBody body = RequestBody.create(AnomalyToString(s), JSON);
                    Request request = new Request.Builder()
                            .url("http://188.242.194.9:9090/events/anomaly/bucket")
                            .post(body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body()) {
                                if (!response.isSuccessful())
                                    return;  //throw new IOException("Unexpected code " + response);

                                Headers responseHeaders = response.headers();
                                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                                }

                                System.out.println(responseBody.string());
                            }
                        }
                    });

                })
                .subscribe();

        EventBus.INSTANCE.damage()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .buffer(15, TimeUnit.SECONDS, 100)
                .doOnNext((s) -> {
                    RequestBody body = RequestBody.create(DamageToString(s), JSON);
                    Request request = new Request.Builder()
                            .url("http://188.242.194.9:9090/events/damage/bucket")
                            .post(body)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body()) {
                                if (!response.isSuccessful())
                                    return;  //throw new IOException("Unexpected code " + response);

                                Headers responseHeaders = response.headers();
                                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                                }

                                System.out.println(responseBody.string());
                            }
                        }
                    });
                }).subscribe();
    }
}
