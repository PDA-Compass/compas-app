package net.afterday.compas.engine.core.serialization;

import com.google.gson.JsonObject;

public interface Jsonable
{
    JsonObject toJson();
}
