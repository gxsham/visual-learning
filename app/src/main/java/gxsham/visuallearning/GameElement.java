package gxsham.visuallearning;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gxsha on 5/5/2018.
 */

public class GameElement implements Parcelable
{

    public String Tags;
    public String ImageUrl;

    protected GameElement(Parcel in) {
        Tags = in.readString();
        ImageUrl = in.readString();
    }

    public static final Creator<GameElement> CREATOR = new Creator<GameElement>() {
        @Override
        public GameElement createFromParcel(Parcel in) {
            return new GameElement(in);
        }

        @Override
        public GameElement[] newArray(int size) {
            return new GameElement[size];
        }
    };

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public GameElement(String tags, String imageUrl){
        Tags = tags;
        ImageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getTags());
        parcel.writeString(this.getImageUrl());
    }
}
