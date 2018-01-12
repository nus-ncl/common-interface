package sg.ncl.adapter.openstack;
import jdk.nashorn.api.scripting.JSObject;
import org.json.JSONObject;


/**
 * Author: Tran Ly Vu
 */

public class AdapterOpenStack {

    public void requestToken (String name, String password) {
        JSONObject domainObject = new JSONObject();
        domainObject.put("name", "Default");

        JSONObject userObject = new JSONObject();
        userObject.put("name", name);
        userObject.put("domain", domainObject);
        userObject.put("password", password);

        JSONObject passwordObject = new JSONObject();
        userObject.put("user", userObject);

        JSONObject identityObject = new JSONObject();
        userObject.put("methods", "password");
        userObject.put("password", passwordObject);

        JSONObject



    }
}
