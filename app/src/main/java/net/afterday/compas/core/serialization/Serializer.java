package net.afterday.compas.core.serialization;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by spaka on 5/30/2018.
 */

public interface Serializer
{
    void serialize(String key, Jsonable object);
    void serialize(String key, String id, Jsonable object);
    void remove(String key, String id);
    Jsonable deserialize(String key);
    List<Jsonable> deserializeList(String key);
}
