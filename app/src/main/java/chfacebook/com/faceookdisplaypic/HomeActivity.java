package chfacebook.com.faceookdisplaypic;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import chfacebook.com.faceookdisplaypic.Models.Album;
import chfacebook.com.faceookdisplaypic.Models.ResponseAlbumls;
import chfacebook.com.faceookdisplaypic.WebService.ServiceWeb;
import chfacebook.com.faceookdisplaypic.databinding.ActivityHomeBinding;

public class HomeActivity extends Activity {
    CallbackManager callbackManager;
    String TAG = HomeActivity.class.getSimpleName();
    ActivityHomeBinding binding;
    ServiceWeb serviceWeb;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        serviceWeb = new ServiceWeb();
        gson = new GsonBuilder().create();
        binding.loginButton.setReadPermissions("email");
        binding.loginButton.setReadPermissions("user_photos");
        binding.loginButton.setReadPermissions("user_posts");
        serviceWeb.getalbums();
        Log.d(TAG, "onCreate: "+AccessToken.getCurrentAccessToken().getToken());

        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                serviceWeb.getalbums();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }





}
