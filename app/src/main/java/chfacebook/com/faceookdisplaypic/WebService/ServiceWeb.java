package chfacebook.com.faceookdisplaypic.WebService;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import chfacebook.com.faceookdisplaypic.Models.Album;
import chfacebook.com.faceookdisplaypic.Models.PhotoAlbum;
import chfacebook.com.faceookdisplaypic.Models.ResponseAlbumls;

/**
 * Created by chawki on 06/11/2017.
 */

public class ServiceWeb {
    Gson gson;
    String TAG = ServiceWeb.class.getSimpleName();
    public ServiceWeb() {
        gson = new GsonBuilder().create();
    }

    public  void getalbums (){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+ AccessToken.getCurrentAccessToken().getUserId()+"/albums",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "onCompleted: "+response.toString());
                        if (response.getError()==null) {
                            ResponseAlbumls responseAlbumls = gson.fromJson(response.getJSONObject().toString(), ResponseAlbumls.class);
                            getphoto(responseAlbumls.getListAlbum().get(4));
                        }
                    }
                }
        ).executeAsync();
    }

    public void getphoto(final Album album) {
        Log.d(TAG, "getphoto: "+album.getId());
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + album.getId()+"/photos",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "onCompleted: "+response.getJSONObject());
                        if (response.getError()==null) {
                            PhotoAlbum photoAlbum = gson.fromJson(response.getJSONObject().toString(), PhotoAlbum.class);
                            album.setPhotoAlbum(photoAlbum);

                        }
                    }
                }
        ).executeAsync();
    }


    public interface NotifyHome {
        //void notifyWebRequest(int actionCode,)
    }




}
