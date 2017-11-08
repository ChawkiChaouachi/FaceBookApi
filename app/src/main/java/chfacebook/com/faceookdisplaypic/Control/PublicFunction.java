package chfacebook.com.faceookdisplaypic.Control;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import chfacebook.com.faceookdisplaypic.HomeActivity;
import chfacebook.com.faceookdisplaypic.Models.UiItemObject;
import chfacebook.com.faceookdisplaypic.Utils.Constant;
import okhttp3.ResponseBody;

/**
 * Created by chawki on 07/11/2017.
 */

public class PublicFunction {

    Context context;
    String TAG = PublicFunction.class.getSimpleName();

    public PublicFunction(Context context) {
        this.context = context;
    }

    // this method calculate the number of item supported on the screen . the size of item depend of the size of the screen
    // when you have the number of item we will use it to request the albums and photo with limit = number item from FaceBookApi .
    // pagination dynimacally
    public UiItemObject setupsUi(){
        Display display = ((HomeActivity)context).getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float density = context.getResources().getDisplayMetrics().density;
        float dpheight = displayMetrics.heightPixels/density ;
        float dpwidth = displayMetrics.widthPixels/density ;

        if(dpwidth <= Constant.WIDTH_LOW_SCREEN){
            int numberItemColumn = (int)(dpwidth )/Constant.WIDTH_LOW_ITEM;
            int numberItemLigne= (int)(dpheight - 70)/Constant.WIDTH_LOW_ITEM;
            return new UiItemObject(numberItemColumn,numberItemLigne,numberItemColumn*numberItemLigne,Constant.WIDTH_LOW_ITEM,Constant.WIDTH_LOW_ITEM);
        }else if(dpwidth<=Constant.WIDTH_MEDIUM_SCREEN){
            int numberItemColumn = (int)(dpwidth)/Constant.WIDTH_MEDIUM_ITEM;
            int numberItemLigne= (int)(dpheight - 85)/Constant.WIDTH_MEDIUM_ITEM;
            return new UiItemObject(numberItemColumn,numberItemLigne,numberItemColumn*numberItemLigne,Constant.WIDTH_MEDIUM_ITEM,Constant.WIDTH_MEDIUM_ITEM);
        }else if (dpwidth<=Constant.WIDTH_HIGH_SCREEN){
            int numberItemColumn = (int)(dpwidth )/Constant.WIDTH_HIGH_ITEM;
            int numberItemLigne= (int)(dpheight - 100)/Constant.WIDTH_HIGH_ITEM;
            return new UiItemObject(numberItemColumn,numberItemLigne,numberItemColumn*numberItemLigne,Constant.WIDTH_MEDIUM_ITEM,Constant.WIDTH_MEDIUM_ITEM);

        }else{
            int numberItemColumn = (int)(dpwidth)/Constant.WIDTH_HIGH_ITEM;
            int numberItemLigne= (int)(dpheight - 100)/Constant.WIDTH_HIGH_ITEM;
            return new UiItemObject(numberItemColumn,numberItemLigne,numberItemColumn*numberItemLigne,Constant.WIDTH_MEDIUM_ITEM,Constant.WIDTH_MEDIUM_ITEM);

        }

    }
    public Typeface getTypeFace(String path){
        return Typeface.createFromAsset(context.getAssets(),path);
    }

    //save file
    public String writeResponseBodyToDisk(ResponseBody body,String filename) {

        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(context.getExternalFilesDir(null) + File.separator + filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return futureStudioIconFile.getAbsolutePath();
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }
    // check internet connection
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
