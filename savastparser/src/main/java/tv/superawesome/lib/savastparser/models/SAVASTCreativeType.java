package tv.superawesome.lib.savastparser.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.JSONSerializable;

/**
 * Created by gabriel.coman on 22/12/15.
 */
public enum SAVASTCreativeType implements Parcelable {
    Linear {
        @Override
        public String toString() {
            return "Linear";
        }
    },
    NonLinear {
        @Override
        public String toString() {
            return "NonLinear";
        }
    },
    CompanionAds {
        @Override
        public String toString() {
            return "CompanionAds";
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(ordinal());
    }

    public static final Creator<SAVASTCreativeType> CREATOR = new Creator<SAVASTCreativeType>() {
        @Override
        public SAVASTCreativeType createFromParcel(final Parcel source) {
            return SAVASTCreativeType.values()[source.readInt()];
        }

        @Override
        public SAVASTCreativeType[] newArray(final int size) {
            return new SAVASTCreativeType[size];
        }
    };
}
