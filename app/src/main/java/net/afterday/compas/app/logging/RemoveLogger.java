package net.afterday.compas.app.logging;

import com.google.gson.JsonObject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import net.afterday.compas.engine.core.EventBus;
import net.afterday.compas.engine.engine.system.influence.anomaly.AnomalyEvent;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class RemoveLogger {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    private String ToString(AnomalyEvent event) {
        JSONObject jsonObject = new JSONObject();
        try {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            long timeInMili = calendar.getTimeInMillis();

            jsonObject.put("aid", event.getId());
            jsonObject.put("type", event.getType());
            jsonObject.put("time", timeInMili);
            jsonObject.put("value", event.getValue());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private Disposable subscribe;
    public RemoveLogger(){
        subscribe = EventBus.INSTANCE.anomaly()
                //.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                //.observeOn()
                .doOnNext((s)->{
                    RequestBody body = RequestBody.create(ToString(s), JSON);
                    Request request = new Request.Builder()
                            .url("http://192.168.1.43:8080/events/anomaly")
                            .post(body)
                            .build();


                    /*try (Response response = client.newCall(request).execute()) {
                        String str = response.body().string();
                        if (str!= null) {

                        }
                    }
                    catch (Exception ex){
                        if (ex != null) {}
                    }*/
                    client.newCall(request).enqueue(new Callback() {
                        @Override public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body()) {
                                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

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
    }
}
