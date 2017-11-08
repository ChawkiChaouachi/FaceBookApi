package chfacebook.com.faceookdisplaypic.WebService;

import chfacebook.com.faceookdisplaypic.Models.PhotoAlbum;
import chfacebook.com.faceookdisplaypic.Models.ResponseAlbumls;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by chawki on 08/11/2017.
 */

/**
 * interface for retrofit define getAlbums next or previous list of albums.
 * getphotoAlbums next or previous list of photos
 * downloadImage download image before we upload it to FireBase
 */

public interface APIServiceInterface {

    @GET
    Call<ResponseAlbumls> getAlbums(@Url String url);

    @GET
    Call<PhotoAlbum> getPhotoAlbum(@Url String url);

    @GET
    Call<ResponseBody> downloadImage(@Url String fileUrl);

}
