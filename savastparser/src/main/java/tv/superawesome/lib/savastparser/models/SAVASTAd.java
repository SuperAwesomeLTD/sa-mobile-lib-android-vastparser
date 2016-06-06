package tv.superawesome.lib.savastparser.models;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tv.superawesome.lib.sajsonparser.JSONSerializable;

public class SAVASTAd implements Parcelable, JSONSerializable {

    public SAVASTAdType type;
    public String id;
    public String sequence;
    public String redirectUri;
    public boolean isImpressionSent;
    public List<String> errors;
    public List<String> impressions;
    public SAVASTCreative creative;

    public SAVASTAd(){

    }

    public SAVASTAd(JSONObject json)  throws JSONException {
        readFromJson(json);
    }

    protected SAVASTAd(Parcel in) {
        type = in.readParcelable(SAVASTAdType.class.getClassLoader());
        id = in.readString();
        sequence = in.readString();
        redirectUri = in.readString();
        isImpressionSent = in.readByte() != 0;
        errors = in.createStringArrayList();
        impressions = in.createStringArrayList();
        creative = in.readParcelable(SAVASTCreative.class.getClassLoader());
    }

    public static final Creator<SAVASTAd> CREATOR = new Creator<SAVASTAd>() {
        @Override
        public SAVASTAd createFromParcel(Parcel in) {
            return new SAVASTAd(in);
        }

        @Override
        public SAVASTAd[] newArray(int size) {
            return new SAVASTAd[size];
        }
    };

    public void sumAd(SAVASTAd ad){
        this.id = ad.id;
        this.sequence = ad.sequence;

        // add errors
        for (Iterator<String> i = ad.errors.iterator(); i.hasNext(); ){
            this.errors.add(i.next());
        }

        // add impressions
        for (String impression : ad.impressions) {
            this.impressions.add(impression);
        }

        this.creative.sumCreative(ad.creative);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(type, flags);
        dest.writeString(id);
        dest.writeString(sequence);
        dest.writeString(redirectUri);
        dest.writeByte((byte) (isImpressionSent ? 1 : 0));
        dest.writeStringList(errors);
        dest.writeStringList(impressions);
        dest.writeParcelable(creative, flags);
    }

    @Override
    public void readFromJson(JSONObject json) {
        if (!json.isNull("id")){
            id = json.optString("id");
        }
        if (!json.isNull("sequence")){
            sequence = json.optString("sequence");
        }
        if (!json.isNull("redirectUri")) {
            redirectUri = json.optString("redirectUri");
        }
        if (!json.isNull("isImpressionSent")){
            isImpressionSent = json.optBoolean("isImpressionSent");
        }
        if (!json.isNull("type")){
            String obj = json.optString("type");
            if (obj != null) {
                if (obj.equals(SAVASTAdType.Invalid.toString())) {
                    type = SAVASTAdType.Invalid;
                }
                if (obj.equals(SAVASTAdType.InLine.toString())){
                    type = SAVASTAdType.InLine;
                }
                if (obj.equals(SAVASTAdType.Wrapper.toString())){
                    type = SAVASTAdType.Wrapper;
                }
            }
        }
        if (!json.isNull("errors")){
            errors = new ArrayList<String>();
            JSONArray jsonArray = json.optJSONArray("errors");
            for (int i = 0; i < jsonArray.length(); i++) {
                String obj = jsonArray.optString(i);
                if (obj != null) {
                    errors.add(obj);
                }
            }
        }
        if (!json.isNull("impressions")){
            impressions = new ArrayList<String>();
            JSONArray jsonArray = json.optJSONArray("impressions");
            for (int i = 0; i < jsonArray.length(); i++) {
                String obj = jsonArray.optString(i);
                if (obj != null) {
                    impressions.add(obj);
                }
            }
        }
        if (!json.isNull("creative")) {
            JSONObject obj = json.optJSONObject("creative");
            try {
                creative = new SAVASTCreative(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public JSONObject writeToJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("redirectUri", redirectUri);
        } catch (JSONException e){
            e.printStackTrace();
        }
        try {
            json.put("sequence", sequence);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("isImpressionSent", isImpressionSent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (errors != null) {
            JSONArray errorsJsonArray = new JSONArray();
            for (String error : errors) {
                errorsJsonArray.put(error);
            }
            try {
                json.put("errors", errorsJsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (impressions != null) {
            JSONArray impressionsJsonArray = new JSONArray();
            for (String impression : impressions) {
                impressionsJsonArray.put(impression);
            }
            try {
                json.put("impressions", impressionsJsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            json.put("creative", creative.writeToJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
