package tv.superawesome.lib.savastparser.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tv.superawesome.lib.sajsonparser.JSONSerializable;
import tv.superawesome.lib.sautils.SAUtils;

/**
 * Created by gabriel.coman on 22/12/15.
 */
public class SAVASTCreative implements Parcelable, JSONSerializable {

    public SAVASTCreativeType type;
    public String id;
    public String sequence;
    public String duration;
    public String clickThrough;
    public String playableMediaUrl;
    public String playableDiskUrl;
    public boolean isOnDisk = false;
    public List<SAVASTMediaFile> mediaFiles;
    public List<SAVASTTracking> trackingEvents;
    public List<String> clickTracking;
    public List<String> customClicks;

    public SAVASTCreative() {

    }

    public SAVASTCreative(JSONObject json) throws JSONException{
        readFromJson(json);
    }

    protected SAVASTCreative(Parcel in) {
        type = in.readParcelable(SAVASTCreativeType.class.getClassLoader());
        id = in.readString();
        sequence = in.readString();
        duration = in.readString();
        clickThrough = in.readString();
        playableMediaUrl = in.readString();
        playableDiskUrl = in.readString();
        isOnDisk = in.readByte() != 0;
        mediaFiles = in.createTypedArrayList(SAVASTMediaFile.CREATOR);
        trackingEvents = in.createTypedArrayList(SAVASTTracking.CREATOR);
        clickTracking = in.createStringArrayList();
        customClicks = in.createStringArrayList();
    }

    public static final Creator<SAVASTCreative> CREATOR = new Creator<SAVASTCreative>() {
        @Override
        public SAVASTCreative createFromParcel(Parcel in) {
            return new SAVASTCreative(in);
        }

        @Override
        public SAVASTCreative[] newArray(int size) {
            return new SAVASTCreative[size];
        }
    };

    public void sumCreative(SAVASTCreative creative) {
        // call super

        this.id = creative.id;
        this.sequence = creative.sequence;
        this.duration = creative.duration;

        if (SAUtils.isValidURL(creative.clickThrough)) {
            this.clickThrough = creative.clickThrough;
        }
        if (SAUtils.isValidURL(creative.playableMediaUrl)) {
            this.playableMediaUrl = creative.playableMediaUrl;
        }
        if (creative.playableDiskUrl != null) {
            this.playableDiskUrl = creative.playableDiskUrl;
        }

        /** now add all other things */
        for (Iterator<SAVASTMediaFile> i = creative.mediaFiles.iterator(); i.hasNext(); ) {
            this.mediaFiles.add(i.next());
        }
        for (Iterator<SAVASTTracking> i = creative.trackingEvents.iterator(); i.hasNext(); ) {
            this.trackingEvents.add(i.next());
        }
        for (Iterator<String> i = creative.clickTracking.iterator(); i.hasNext(); ) {
            this.clickTracking.add(i.next());
        }
        for (Iterator<String> i = creative.customClicks.iterator(); i.hasNext(); ) {
            this.customClicks.add(i.next());
        }
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
        dest.writeString(duration);
        dest.writeString(clickThrough);
        dest.writeString(playableMediaUrl);
        dest.writeString(playableDiskUrl);
        dest.writeByte((byte) (isOnDisk ? 1 : 0));
        dest.writeTypedList(mediaFiles);
        dest.writeTypedList(trackingEvents);
        dest.writeStringList(clickTracking);
        dest.writeStringList(customClicks);
    }


    @Override
    public void readFromJson(JSONObject json) {
        if (!json.isNull("id")) {
            id = json.optString("id");
        }
        if (!json.isNull("sequence")) {
            sequence = json.optString("sequence");
        }
        if (!json.isNull("duration")) {
            duration = json.optString("duration");
        }
        if (!json.isNull("clickThrough")) {
            clickThrough = json.optString("clickThrough");
        }
        if (!json.isNull("playableMediaUrl")) {
            playableMediaUrl = json.optString("playableMediaUrl");
        }
        if (!json.isNull("playableDiskUrl")) {
            playableDiskUrl = json.optString("playableDiskUrl");
        }
        if (!json.isNull("isOnDisk")){
            isOnDisk = json.optBoolean("isOnDisk");
        }
        if (!json.isNull("mediaFiles")) {
            mediaFiles = new ArrayList<SAVASTMediaFile>();
            JSONArray jsonArray = json.optJSONArray("mediaFiles");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.optJSONObject(i);
                try {
                    SAVASTMediaFile mediaFile = new SAVASTMediaFile(obj);
                    mediaFiles.add(mediaFile);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!json.isNull("trackingEvents")) {
            trackingEvents = new ArrayList<SAVASTTracking>();
            JSONArray jsonArray = json.optJSONArray("trackingEvents");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.optJSONObject(i);
                try {
                    SAVASTTracking tracking = new SAVASTTracking(obj);
                    trackingEvents.add(tracking);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!json.isNull("clickTracking")){
            clickTracking = new ArrayList<String>();
            JSONArray jsonArray = json.optJSONArray("clickTracking");
            for (int i = 0; i < jsonArray.length(); i++){
                String obj = jsonArray.optString(i);
                if (obj != null) {
                    clickTracking.add(obj);
                }
            }
        }
        if (!json.isNull("customClicks")){
            customClicks = new ArrayList<String>();
            JSONArray jsonArray = json.optJSONArray("customClicks");
            for (int i = 0; i < jsonArray.length(); i++) {
                String obj = jsonArray.optString(i);
                if (obj != null) {
                    customClicks.add(obj);
                }
            }
        }
        if (!json.isNull("type")){
            String obj = json.optString("type");
            if (obj != null) {
                if (obj.equals(SAVASTCreativeType.Linear.toString())) {
                    type = SAVASTCreativeType.Linear;
                }
                if (obj.equals(SAVASTCreativeType.NonLinear.toString())) {
                    type = SAVASTCreativeType.NonLinear;
                }
                if (obj.equals(SAVASTCreativeType.CompanionAds.toString())) {
                    type = SAVASTCreativeType.CompanionAds;
                }
            }
        }
    }

    @Override
    public JSONObject writeToJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("sequence", sequence);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("duration", duration);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("clickThrough", clickThrough);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("playableMediaUrl", playableMediaUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("playableDiskUrl", playableDiskUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("isOnDisk", isOnDisk);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (mediaFiles != null) {
            JSONArray mediaFileJsonArray = new JSONArray();
            for (SAVASTMediaFile mediaFile : mediaFiles) {
                mediaFileJsonArray.put(mediaFile.writeToJson());
            }
            try {
                json.put("mediaFiles", mediaFileJsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (trackingEvents != null) {
            JSONArray trackingEventsJsonArray = new JSONArray();
            for (SAVASTTracking tracking : trackingEvents) {
                trackingEventsJsonArray.put(tracking.writeToJson());
            }
            try {
                json.put("trackingEvents", trackingEventsJsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (clickTracking != null) {
            JSONArray clickTrackingJsonArray = new JSONArray();
            for (String click : clickTracking) {
                clickTrackingJsonArray.put(click);
            }
            try {
                json.put("clickTracking", clickTrackingJsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (customClicks != null) {
            JSONArray customClicksJsonArray = new JSONArray();
            for (String click : customClicks) {
                customClicksJsonArray.put(click);
            }
            try {
                json.put("customClicks", customClicksJsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return json;
    }


}
