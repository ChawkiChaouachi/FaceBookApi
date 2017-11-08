package chfacebook.com.faceookdisplaypic.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chawki on 07/11/2017.
 */

public class PhotoSize implements Serializable {


    @SerializedName("source")
    String source;
    @SerializedName("height")
    int  height;
    @SerializedName("width")
    int  width;


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
