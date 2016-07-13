package sg.ncl.service.registration.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by Desmond / Te Ye
 */
public class DateTimeDeserializer implements JsonDeserializer<ZonedDateTime> {
    public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        String timeAsString = json.getAsJsonPrimitive().getAsString();
        String[] myStrArray = timeAsString.split("\\.");
        long milliSeconds = Long.parseLong(myStrArray[0]);
        long nanoSeconds = Long.parseLong(myStrArray[1]);
        Instant instant = Instant.ofEpochSecond(milliSeconds, nanoSeconds);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Singapore"));
        return zonedDateTime;
    }
}
