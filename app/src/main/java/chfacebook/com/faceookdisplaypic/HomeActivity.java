package chfacebook.com.faceookdisplaypic;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import chfacebook.com.faceookdisplaypic.Control.PublicFunction;
import chfacebook.com.faceookdisplaypic.Models.ResponseAlbumls;
import chfacebook.com.faceookdisplaypic.Models.UiItemObject;
import chfacebook.com.faceookdisplaypic.Utils.Constant;
import chfacebook.com.faceookdisplaypic.WebService.ServiceWeb;
import chfacebook.com.faceookdisplaypic.databinding.ActivityHomeBinding;
public class HomeActivity extends Activity implements ServiceWeb.NotifyHome{
    CallbackManager callbackManager;
    String TAG = HomeActivity.class.getSimpleName();
    ActivityHomeBinding binding;
    ServiceWeb serviceWeb;
    PublicFunction publicFunction ;
    Gson gson;
    UiItemObject uiItemObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        serviceWeb = new ServiceWeb(this);
        publicFunction = new PublicFunction(this);


        // get the uiItemobject to khnow how many item supported by the screen we will use this to get the limit of the album and photo
        uiItemObject=publicFunction.setupsUi();
        gson = new GsonBuilder().create();
        binding.loginButton.setReadPermissions(Constant.PERMISSION_EMAIL);
        binding.loginButton.setReadPermissions(Constant.PERMISSION_USER_PHOTO);
        binding.tryAgain.setOnClickListener(onClickListener);

        // if facebook token not null we will send the first request to get the first list of albums
        if(AccessToken.getCurrentAccessToken()!=null){
            onPreexcute();
            serviceWeb.getalbums(uiItemObject.getTotalnumberItem());
            return;
        }


        // register call back check if the user is authenticate if yess we will send the first request to get the first list of albums
        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                onPreexcute();
                serviceWeb.getalbums(uiItemObject.getTotalnumberItem());
            }

            @Override
            public void onCancel() {
                Toast.makeText(HomeActivity.this,"Canceled",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(HomeActivity.this,exception.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



    public void onPreexcute (){
        binding.loginButton.setVisibility(View.INVISIBLE);
        binding.tryAgain.setVisibility(View.INVISIBLE);
        binding.prograssBar.setVisibility(View.VISIBLE);
    }



    // response of the first album list if resonse == 200 we will go to category Activity and see the albums
    @Override
    public void notifyWebRequest(int actionCode, int statusResponse, Object object) {

        switch (statusResponse) {
            case 200:
                if(((ResponseAlbumls)object).getListAlbum()!=null && ((ResponseAlbumls)object).getListAlbum().size()>0 ) {
                    Intent goToGallery = new Intent(HomeActivity.this, GalleryActivity.class);
                    goToGallery.putExtra(UiItemObject.class.getSimpleName(), uiItemObject);
                    goToGallery.putExtra(ResponseAlbumls.class.getSimpleName(), (ResponseAlbumls) object);
                    startActivity(goToGallery);
                    HomeActivity.this.finish();
                }else{
                    Toast.makeText(this,getResources().getString(R.string.no_album_found),Toast.LENGTH_SHORT).show();

                }
                break;
            case -1:
                binding.prograssBar.setVisibility(View.INVISIBLE);
                binding.tryAgain.setVisibility(View.VISIBLE);
                Toast.makeText(this,getResources().getString(R.string.check_network),Toast.LENGTH_SHORT).show();
                break;
            default:
                // need to handle all the state error code
                Toast.makeText(this,getResources().getString(R.string.unknown_prob),Toast.LENGTH_SHORT).show();
                break;
        }

    }


    // if there no internet connection you will be notifyed and a try again button will appear

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.try_again :
                    onPreexcute();
                    serviceWeb.getalbums(uiItemObject.getTotalnumberItem());
            }
        }
    };





}
