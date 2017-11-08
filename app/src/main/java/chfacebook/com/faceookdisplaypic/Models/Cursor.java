package chfacebook.com.faceookdisplaypic.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chawki on 06/11/2017.
 */
// model cursor use serializable and SerializedName for parsing data and to pass data from fragment to fragment or from acticity to ......


public class Cursor implements Serializable {

    @SerializedName("before")
    String before;
    @SerializedName("after")
    String after;

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
