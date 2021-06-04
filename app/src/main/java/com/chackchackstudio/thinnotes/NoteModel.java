package com.chackchackstudio.thinnotes;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteModel implements Parcelable {

    private final String id;
    private final String title;
    private final String date;
    private final String body;

    public NoteModel(String id, String title, String date, String body) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.body = body;
    }

    public NoteModel(Parcel in) {
        String[] data = new String[4];
        in.readStringArray(data);
        id = data[0];
        title = data[1];
        date = data[2];
        body = data[3];
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{id, title, date, body});
    }

    public static final Parcelable.Creator<NoteModel> CREATOR = new Parcelable.Creator<NoteModel>() {

        @Override
        public NoteModel createFromParcel(Parcel source) {
            return new NoteModel(source);
        }

        @Override
        public NoteModel[] newArray(int size) {
            return new NoteModel[size];
        }
    };
}
