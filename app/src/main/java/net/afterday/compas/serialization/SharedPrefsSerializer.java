package net.afterday.compas.serialization;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.afterday.compas.core.serialization.Jsonable;
import net.afterday.compas.core.serialization.Serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by spaka on 5/30/2018.
 */

public class SharedPrefsSerializer implements Serializer
{
    private Context context;
    private static SharedPrefsSerializer instance;
    private Map<String, SharedPreferences> prefs;
    private SharedPrefsSerializer(Context context)
    {
        this.context = context;
        prefs = new HashMap<>();
    }

    public static SharedPrefsSerializer instance(Context context)
    {
        instance = new SharedPrefsSerializer(context);
        return instance;
    }

    public static SharedPrefsSerializer instance()
    {
        if(instance == null)
        {
            throw new IllegalStateException("SharedPrefsSerializer not initialized");
        }
        return instance;
    }

    @Override
    public void serialize(String key, Jsonable object)
    {
        getSp(key).edit().putString(key, object.toJson().toString()).apply();
    }

    @Override
    public void serialize(String key, String id, Jsonable object)
    {
        getSp(key).edit().putString(id, object.toJson().toString()).apply();
    }


    @Override
    public void remove(String key, String id)
    {
        getSp(key).edit().remove(id).apply();
    }

    @Override
    public Jsonable deserialize(String key)
    {
        SharedPreferences sp = getSp(key);
        if(!sp.contains(key))
        {
            return null;
        }
        return new JsonableImpl(sp.getString(key, null));
    }

    public List<Jsonable> deserializeList(String key)
    {
        Map<String, ?> all = getSp(key).getAll();
        List<Jsonable> jsonables = new ArrayList<>();
        for (Map.Entry e : all.entrySet())
        {
            jsonables.add(new JsonableImpl((String) e.getValue()));
        }
        return jsonables;
    }

    private SharedPreferences getSp(String key)
    {
        SharedPreferences sp;
        if(prefs.containsKey(key))
        {
            sp = prefs.get(key);
        }else
        {
            sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
            prefs.put(key, sp);
        }
        return sp;
    }

    private static class JsonableImpl implements Jsonable
    {
        private String jsonString;
        private JsonObject o;
        JsonableImpl(String string)
        {
            jsonString = string;
        }

        @Override
        public JsonObject toJson()
        {
            if(o != null)
            {
                return o;
            }
            if(jsonString == null)
            {
                return null;
            }
            o = (new JsonParser()).parse(jsonString).getAsJsonObject();
            return o;

        }
    }
}
