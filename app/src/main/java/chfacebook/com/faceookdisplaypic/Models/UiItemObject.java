package chfacebook.com.faceookdisplaypic.Models;

import java.io.Serializable;

/**
 * Created by chawki on 07/11/2017.
 */
// model UiItemObject use serializable and SerializedName for parsing data and to pass data from fragment to fragment or from acticity to ......
// this model will containt a usefull information : number of item used to request the api and also the width and height

public class UiItemObject implements Serializable {

    int numbreItemColumns;
    int numberItemLigne ;
    int totalnumberItem;
    int widthitem;
    int heightitem;


    public UiItemObject(int numbreItemColumns, int numberItemLigne, int totalnumberItem, int widthitem, int heightitem) {
        this.numbreItemColumns = numbreItemColumns;
        this.numberItemLigne = numberItemLigne;
        this.totalnumberItem = totalnumberItem;
        this.widthitem = widthitem;
        this.heightitem = heightitem;
    }

    public UiItemObject() {
    }

    public int getNumbreItemColumns() {
        return numbreItemColumns;
    }

    public void setNumbreItemColumns(int numbreItemColumns) {
        this.numbreItemColumns = numbreItemColumns;
    }

    public int getNumberItemLigne() {
        return numberItemLigne;
    }

    public void setNumberItemLigne(int numberItemLigne) {
        this.numberItemLigne = numberItemLigne;
    }

    public int getTotalnumberItem() {
        return totalnumberItem;
    }

    public void setTotalnumberItem(int totalnumberItem) {
        this.totalnumberItem = totalnumberItem;
    }

    public int getWidthitem() {
        return widthitem;
    }

    public void setWidthitem(int widthitem) {
        this.widthitem = widthitem;
    }

    public int getHeightitem() {
        return heightitem;
    }

    public void setHeightitem(int heightitem) {
        this.heightitem = heightitem;
    }
}
