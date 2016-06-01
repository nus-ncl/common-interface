package sg.ncl.service.team.serializers;

import com.google.gson.*;
import sg.ncl.service.team.data.jpa.entities.TeamEntity;

import java.lang.reflect.Type;

/**
 * Created by Desmond / Te Ye
 */
public class TeamEntitySerializer implements JsonSerializer<TeamEntity> {

    public JsonElement serialize(final TeamEntity teamEntity, final Type type, final JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("teyeName", new JsonPrimitive(teamEntity.getName()));
        result.add("teyeDesc", new JsonPrimitive(teamEntity.getDescription()));

        String appDate = teamEntity.getApplicationDate().toEpochSecond() + "." + teamEntity.getApplicationDate().getNano();

        result.add("teyeDate", new JsonPrimitive(appDate));
        return result;
    }

}
