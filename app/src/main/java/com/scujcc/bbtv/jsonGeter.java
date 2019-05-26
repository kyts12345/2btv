package com.scujcc.bbtv;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class jsonGeter {
    private  Context context;

    public jsonGeter(Context context) {
        this.context = context;
    }

    public String loadJson(String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);
            byte[] buffer = new byte[is.available ()];
            is.read(buffer);
            json = new String(buffer, "UTF-8");
            is.close ();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    public List<jsonObject> getJson(String filename) {
        List<jsonObject> result = new ArrayList<>();
        String json = loadJson (filename);
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray data = obj.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                jsonObject jo = new jsonObject ();
                JSONObject item = data.getJSONObject(i);
                jo.setTitle(item.getString("title"));
                jo.setQuality(item.getString("quality"));
                jo.setUrl(item.getString("url"));
                jo.setImg ( item.getString ( "img" ) );
                result.add(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
