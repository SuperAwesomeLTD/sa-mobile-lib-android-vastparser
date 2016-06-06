package tv.superawesome.lib.savastparser.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.JSONSerializable;

/**
 * Created by gabriel.coman on 22/12/15.
 */
public class SAVASTTracking implements Parcelable, JSONSerializable {

    public String event;
    public String url;

    public SAVASTTracking() {

    }

    public SAVASTTracking(JSONObject json) throws JSONException{
        readFromJson(json);
    }

    protected SAVASTTracking(Parcel in) {
        event = in.readString();
        url = in.readString();
    }

    public static final Creator<SAVASTTracking> CREATOR = new Creator<SAVASTTracking>() {
        @Override
        public SAVASTTracking createFromParcel(Parcel in) {
            return new SAVASTTracking(in);
        }

        @Override
        public SAVASTTracking[] newArray(int size) {
            return new SAVASTTracking[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(event);
        dest.writeString(url);
    }

    @Override
    public void readFromJson(JSONObject json){
        if (!json.isNull("event")){
            event = json.optString("event");
        }
        if (!json.isNull("url")){
            url = json.optString("url");
        }
    }

    @Override
    public JSONObject writeToJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("event", event);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
