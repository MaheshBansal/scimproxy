package info.simplecloud.core.coding.handlers;

import info.simplecloud.core.execeptions.FailedToGetValue;
import info.simplecloud.core.execeptions.FailedToSetValue;
import info.simplecloud.core.execeptions.UnhandledAttributeType;
import info.simplecloud.core.execeptions.UnknownType;
import info.simplecloud.core.types.PluralType;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PluralSimpleListTypeHandler implements ITypeHandler {

    @Override
    public Object decode(JSONObject scimUserJson, String attributeId) throws JSONException, UnhandledAttributeType, FailedToSetValue,
            UnknownType, InstantiationException, IllegalAccessException {
        List<PluralType<String>> result = new ArrayList<PluralType<String>>();

        JSONArray plural = scimUserJson.getJSONArray(attributeId);
        for (int i = 0; i < plural.length(); i++) {
            boolean primary = false;
            String type = null;
            JSONObject sigular = plural.getJSONObject(i);
            String value = sigular.getString("value");

            try {
                type = sigular.getString("type");
            } catch (JSONException e) {
                // ignore, type is not mandatory
            }
            try {
                primary = sigular.getBoolean("primary");
            } catch (JSONException e) {
                // ignore, primary is not mandatory
            }

            result.add(new PluralType<String>(value, type, primary));
        }
        return result;
    }

    @Override
    public void encode(JSONObject scimUserJson, String attributeId, Object object) throws JSONException, UnhandledAttributeType,
            FailedToSetValue, UnknownType, InstantiationException, IllegalAccessException, FailedToGetValue {
        JSONArray plural = new JSONArray();
        List<PluralType<String>> data = (List<PluralType<String>>) object;

        for (PluralType<String> singular : data) {
            JSONObject jsonSingular = new JSONObject();
            jsonSingular.put("value", singular.getValue());
            jsonSingular.put("type", singular.getType());
            jsonSingular.put("primary", singular.getPrimary());

            plural.put(jsonSingular);
        }

        scimUserJson.put(attributeId, plural);
        
    }
}
