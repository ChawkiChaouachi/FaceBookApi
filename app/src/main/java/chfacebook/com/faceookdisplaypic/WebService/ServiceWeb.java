package chfacebook.com.faceookdisplaypic.WebService;
import android.os.Bundle;
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
import chfacebook.com.faceookdisplaypic.Utils.Constant;

/**
 * Created by chawki on 06/11/2017.
 */


//GraphRequest facebook api to get the first list . we define a limit wich is the number of item supported by screen
 // we notify the parent activity by interface

public class ServiceWeb {
    Gson gson;
    String TAG = ServiceWeb.class.getSimpleName();
    NotifyHome notifyHome;

    public ServiceWeb(NotifyHome notifyHome) {
        gson = new GsonBuilder().create();
        this.notifyHome=notifyHome;
    }

    public  void getalbums (int limit){
        Bundle paramaters = new Bundle();
        paramaters.putString("limit",limit+"");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+ AccessToken.getCurrentAccessToken().getUserId()+"/albums",
                paramaters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "onCompleted: "+response.toString());
                        if (response.getError()==null) {
                            ResponseAlbumls responseAlbumls = gson.fromJson(response.getJSONObject().toString(), ResponseAlbumls.class);
                            notifyHome.notifyWebRequest(Constant.ACTION_GET_ALBUMS,200,responseAlbumls);
                        }else{
                            notifyHome.notifyWebRequest(Constant.ACTION_GET_ALBUMS,response.getError().getErrorCode(),null);
                        }
                    }
                }
        ).executeAsync();
    }

    public void getphoto(final Album album,int limit) {
        Log.d(TAG, "getphoto: "+album.getId());
        Bundle paramaters = new Bundle();
        paramaters.putString("fields","images");
        paramaters.putString("limit",limit+"");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + album.getId()+"/photos",
                paramaters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d(TAG, "onCompleted: "+response.getJSONObject());
                        if (response.getError()==null) {
                            PhotoAlbum photoAlbum = gson.fromJson(response.getJSONObject().toString(), PhotoAlbum.class);
                            notifyHome.notifyWebRequest(Constant.ACTION_GET_PHOTO,200,photoAlbum);
                        }else {
                            notifyHome.notifyWebRequest(Constant.ACTION_GET_PHOTO,200,response.getError().getErrorCode());
                        }
                    }
                }
        ).executeAsync();
    }


    public interface NotifyHome {
        void notifyWebRequest(int actionCode, int statusResponse ,Object object);
    }




}
