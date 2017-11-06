package chfacebook.com.faceookdisplaypic.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by macbook on 06/11/2017.
 */

public class Photo implements Serializable {

    @SerializedName("created_time")
    String createdTime;

    @SerializedName("id")
    String id;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
