package org.json;

import java.util.AbstractList;
import java.util.NoSuchElementException;

public class JSONList extends AbstractList<JSONObject> {

    private JSONArray array;

    public JSONList(JSONArray array) {
        this.array = array;
    }

    @Override
    public JSONObject get(int index) {
        try {
            return array.getJSONObject(index);
        } catch (JSONException e) {
            throw new NoSuchElementException();
        }
    }



    @Override
    public int size() {
        return array.length();
    }
}
