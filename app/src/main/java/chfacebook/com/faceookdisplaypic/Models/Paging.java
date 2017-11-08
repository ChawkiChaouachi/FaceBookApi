package chfacebook.com.faceookdisplaypic.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chawki on 06/11/2017.
 */
// model paging use serializable and SerializedName for parsing data and to pass data from fragment to fragment or from acticity to ......
// this class is very useful to khnow if there is a data available .We can khnow this if next or previous are not null
public class Paging implements Serializable {

    @SerializedName("cursors")
    Cursor cursor;
    @SerializedName("next")
    String next;
    @SerializedName("previous")
    String previous;

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }
}
