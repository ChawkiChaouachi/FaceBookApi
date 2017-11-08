package chfacebook.com.faceookdisplaypic.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chawki on 06/11/2017.
 */

// model PhotoAlbum use serializable and SerializedName for parsing data and to pass data from fragment to fragment or from acticity to ......

public class PhotoAlbum implements Serializable {


    @SerializedName("data")
    List<Photo> listphoto;
    @SerializedName("paging")
    Paging paging;

    public List<Photo> getListphoto() {
        return listphoto;
    }

    public void setListphoto(List<Photo> listphoto) {
        this.listphoto = listphoto;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
