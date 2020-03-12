package net.afterday.compas.core.serialization;

import com.google.gson.JsonObject;

/**
 * Created by spaka on 5/30/2018.
 */

public interface Jsonable
{
    JsonObject toJson();
}
