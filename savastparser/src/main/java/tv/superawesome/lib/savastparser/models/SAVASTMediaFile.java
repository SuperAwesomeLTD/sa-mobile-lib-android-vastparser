package tv.superawesome.lib.savastparser.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.JSONSerializable;

/**
 * Created by gabriel.coman on 22/12/15.
 */
public class SAVASTMediaFile implements Parcelable, JSONSerializable {

    public String width;
    public String height;
    public String url;
    public String type;

    public SAVASTMediaFile() {

    }

    public SAVASTMediaFile(JSONObject json) throws JSONException {
        readFromJson(json);
    }

    protected SAVASTMediaFile(Parcel in) {
        width = in.readString();
        height = in.readString();
        url = in.readString();
        type = in.readString();
    }

    public static final Creator<SAVASTMediaFile> CREATOR = new Creator<SAVASTMediaFile>() {
        @Override
        public SAVASTMediaFile createFromParcel(Parcel in) {
            return new SAVASTMediaFile(in);
        }

        @Override
        public SAVASTMediaFile[] newArray(int size) {
            return new SAVASTMediaFile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(width);
        dest.writeString(height);
        dest.writeString(url);
        dest.writeString(type);
    }

    @Override
    public void readFromJson(JSONObject json) {
        if (!json.isNull("width")){
            width = json.optString("width");
        }
        if (!json.isNull("height")){
            height = json.optString("height");
        }
        if (!json.isNull("url")){
            url = json.optString("url");
        }
        if (!json.isNull("type")){
            type = json.optString("type");
        }
    }

    @Override
    public JSONObject writeToJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("width", width);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("height", height);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
