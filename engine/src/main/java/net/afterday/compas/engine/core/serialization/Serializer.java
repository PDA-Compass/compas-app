package net.afterday.compas.engine.core.serialization;

import java.util.List;

public interface Serializer
{
    void serialize(String key, Jsonable object);
    void serialize(String key, String id, Jsonable object);
    void remove(String key, String id);
    Jsonable deserialize(String key);
    List<Jsonable> deserializeList(String key);
}
