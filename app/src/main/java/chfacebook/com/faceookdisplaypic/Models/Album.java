package chfacebook.com.faceookdisplaypic.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chawki on 06/11/2017.
 */


// model album use serializable and SerializedName for parsing data and to pass data from fragment to fragment or from acticity to ......
public class Album implements Serializable {

    @SerializedName("created_time")
    String createdTime ;

    @SerializedName("name")
    String name ;

    @SerializedName("id")
    String id ;

    List<PhotoAlbum> photoAlbum;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
