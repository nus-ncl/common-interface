package sg.ncl.adapter.deterlab;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import sg.ncl.adapter.deterlab.dtos.entities.DeterlabUserEntity;

/**
 * Created by Te Ye on 21-Jun-16.
 */
public class Util {

    public static JSONObject getUserAdapterJSONObject() {
        JSONObject userObject = new JSONObject();
        userObject.put("firstName", RandomStringUtils.randomAlphabetic(20));
        userObject.put("lastName", RandomStringUtils.randomAlphabetic(20));
        userObject.put("jobTitle", RandomStringUtils.randomAlphabetic(20));
        userObject.put("password", RandomStringUtils.randomAlphabetic(20));
        userObject.put("email", RandomStringUtils.randomAlphanumeric(8) + "@nus.edu.sg");
        userObject.put("phone", RandomStringUtils.randomNumeric(8));
        userObject.put("institution", RandomStringUtils.randomAlphabetic(20));
        // max institution abbreviation is 15 chars only
        userObject.put("institutionAbbreviation", RandomStringUtils.randomAlphabetic(15));
        userObject.put("institutionWeb", "http://" + RandomStringUtils.randomAlphabetic(20) + ".com");

        userObject.put("address1", RandomStringUtils.randomAlphabetic(20));
        userObject.put("address2", RandomStringUtils.randomAlphabetic(20));
        userObject.put("country", RandomStringUtils.randomAlphabetic(20));
        userObject.put("region", RandomStringUtils.randomAlphabetic(20));
        userObject.put("city", RandomStringUtils.randomAlphabetic(20));
        userObject.put("zipCode", RandomStringUtils.randomNumeric(8));

        return userObject;
    }

    public static DeterlabUserEntity getDeterlabUserEntity() {
        final DeterlabUserEntity deterlabUserEntity = new DeterlabUserEntity();
        deterlabUserEntity.setNclUserId(RandomStringUtils.randomAlphabetic(20));
        deterlabUserEntity.setDeterUserId(RandomStringUtils.randomAlphabetic(8));
        return deterlabUserEntity;
    }

    public static JSONObject getExperimentAdapterJsonObject() {
        JSONObject experimentObject = new JSONObject();
        experimentObject.put("id", RandomStringUtils.randomAlphanumeric(20));
        experimentObject.put("userId", "ncl");
        experimentObject.put("teamId", "testbed");
        experimentObject.put("name", RandomStringUtils.randomAlphanumeric(19));
        experimentObject.put("description", RandomStringUtils.randomAlphanumeric(20));
        experimentObject.put("nsFile", "test.ns");
        experimentObject.put("idleSwap", "240");
        experimentObject.put("maxDuration", "960");

        return experimentObject;
    }
}
