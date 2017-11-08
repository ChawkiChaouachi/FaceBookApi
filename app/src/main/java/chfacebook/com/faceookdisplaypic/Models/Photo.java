package chfacebook.com.faceookdisplaypic.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chawki on 06/11/2017.
 */
// model photo use serializable and SerializedName for parsing data and to pass data from fragment to fragment or from acticity to ......

public class Photo implements Serializable {

   @SerializedName("images")
    List<PhotoSize> photoSizes;

    public List<PhotoSize> getPhotoSizes() {
        return photoSizes;
    }

    public void setPhotoSizes(List<PhotoSize> photoSizes) {
        this.photoSizes = photoSizes;
    }
}
