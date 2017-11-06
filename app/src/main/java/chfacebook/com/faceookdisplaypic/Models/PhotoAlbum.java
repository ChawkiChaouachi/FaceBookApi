package chfacebook.com.faceookdisplaypic.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by macbook on 06/11/2017.
 */

public class PhotoAlbum implements Serializable {
    @SerializedName("data")
    List<Photo> listAlbum;
    @SerializedName("paging")
    Paging paging;

    public List<Photo> getListAlbum() {
        return listAlbum;
    }

    public void setListAlbum(List<Photo> listAlbum) {
        this.listAlbum = listAlbum;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
