package chfacebook.com.faceookdisplaypic.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by macbook on 06/11/2017.
 */

public class ResponseAlbumls implements Serializable {

    @SerializedName("data")
    List<Album> listAlbum;
    @SerializedName("paging")
    Paging paging;

    public List<Album> getListAlbum() {
        return listAlbum;
    }

    public void setListAlbum(List<Album> listAlbum) {
        this.listAlbum = listAlbum;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}

