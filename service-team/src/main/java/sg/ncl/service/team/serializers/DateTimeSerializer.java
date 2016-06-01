package sg.ncl.service.team.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

/**
 * Created by Desmond / Te Ye
 */
public class DateTimeSerializer implements JsonSerializer<ZonedDateTime> {
    public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        String appDate = src.toEpochSecond() + "." + src.getNano();
        return new JsonPrimitive(appDate);
    }
}
