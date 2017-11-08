package chfacebook.com.faceookdisplaypic.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chawki on 06/11/2017.
 */
// model ResponseAlbumls use serializable and SerializedName for parsing data and to pass data from fragment to fragment or from acticity to ......

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

