package tv.superawesome.lib.savastparser.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.style.ParagraphStyle;

/**
 * Created by gabriel.coman on 22/12/15.
 */
public enum SAVASTAdType implements Parcelable {
    Invalid {
        @Override
        public String toString() {
            return "Invalid";
        }
    },
    InLine {
        @Override
        public String toString() {
            return "InLine";
        }
    },
    Wrapper {
        @Override
        public String toString() {
            return "Wrapper";
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

    public static final Creator<SAVASTAdType> CREATOR = new Creator<SAVASTAdType>() {
        @Override
        public SAVASTAdType createFromParcel(final Parcel source) {
            return SAVASTAdType.values()[source.readInt()];
        }

        @Override
        public SAVASTAdType[] newArray(final int size) {
            return new SAVASTAdType[size];
        }
    };
}
