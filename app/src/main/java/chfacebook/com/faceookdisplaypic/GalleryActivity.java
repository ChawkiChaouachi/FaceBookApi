package chfacebook.com.faceookdisplaypic;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import chfacebook.com.faceookdisplaypic.Adapters.AdapterPictures;
import chfacebook.com.faceookdisplaypic.Control.PublicFunction;
import chfacebook.com.faceookdisplaypic.Fragments.FragmentAlbums;
import chfacebook.com.faceookdisplaypic.Models.Photo;
import chfacebook.com.faceookdisplaypic.WebService.APIServiceInterface;
import chfacebook.com.faceookdisplaypic.WebService.CreateNewClient;
import chfacebook.com.faceookdisplaypic.databinding.ActivityGalleryBinding;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static chfacebook.com.faceookdisplaypic.Utils.Constant.BASE_URL;
import static chfacebook.com.faceookdisplaypic.Utils.Constant.PATH_FONT_FACEBOOK;

// Activity containt 2 fragment album and photo
// responsable of download and upload photo

public class GalleryActivity extends AppCompatActivity implements AdapterPictures.OnclickPicture{
    ActivityGalleryBinding binding;
    public PublicFunction publicFunction;
    private StorageReference mStorageRef;
    String TAG =GalleryActivity.class.getSimpleName();
    APIServiceInterface apiServiceInterface;
    Photo currentPhoto;
    FirebaseAuth mAuth;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        apiServiceInterface = CreateNewClient.getClient(BASE_URL).create(APIServiceInterface.class);
         mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }
        publicFunction = new PublicFunction(this);
        binding.toolbarTitle.setTypeface(publicFunction.getTypeFace(PATH_FONT_FACEBOOK));
        binding.uplad.setOnClickListener(onClickListener);
        binding.back.setOnClickListener(onClickListener);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FragmentAlbums fragmentAlbums =new FragmentAlbums();
        fragmentAlbums.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().replace(R.id.container,fragmentAlbums,FragmentAlbums.class.getSimpleName()).commit();

    }


    @Override
    public void pictureClicked(Photo photo) {
        currentPhoto=photo;
        binding.fullscreeninfo.setVisibility(View.VISIBLE);
        Glide.with(this).load(photo.getPhotoSizes().get(0).getSource()).centerCrop().into(binding.fullscreenphoto);

    }

    @Override
    public void onBackPressed() {
        //hide  full screen photo if is showing
        if (binding.fullscreeninfo.getVisibility()==View.VISIBLE){
            binding.fullscreeninfo.setVisibility(View.INVISIBLE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //upload the photo to FireBase
    private void uploadImageToFireBase(String path,String name){
        Uri file = Uri.fromFile(new File(path));
        StorageReference riversRef = mStorageRef.child("images/"+name);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(GalleryActivity.this,GalleryActivity.this.getResources().getString(R.string.photo_uploaded),Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        dialog.dismiss();
                        Toast.makeText(GalleryActivity.this,GalleryActivity.this.getResources().getString(R.string.unknown_prob),Toast.LENGTH_SHORT).show();

                        // ...
                    }
                });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.uplad:
                    Log.d(TAG, "onClick: ");
                    showChangeLangDialog();
                    break;
                case R.id.back:
                    binding.fullscreeninfo.setVisibility(View.INVISIBLE);
            }
        }
    };

    // download a photo to sdcard

    public void downloadFile(String path, final String name){

        Call<ResponseBody> call = apiServiceInterface.downloadImage(path);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");

                    String absoulutefile = publicFunction.writeResponseBodyToDisk(response.body(),name);
                    uploadImageToFireBase(absoulutefile,name);
                    Log.d(TAG, "file download was a success? " + absoulutefile);
                } else {
                    Toast.makeText(GalleryActivity.this,GalleryActivity.this.getResources().getString(R.string.unknown_prob),Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });
    }

    // sign in to FireBase
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d(TAG, "onSuccess: ");
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    // dialog to enter the name of photo before upload it to FireBase
    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_name, null);
        dialogBuilder.setView(dialogView);

        final EditText name = (EditText) dialogView.findViewById(R.id.name);

        dialogBuilder.setTitle(getResources().getString(R.string.enter_name));
        //
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(name.getText().toString()==null || name.getText().toString().length()<2){
                    Toast.makeText(GalleryActivity.this,getResources().getString(R.string.enter_name_plz),Toast.LENGTH_SHORT).show();

                }else{
                    showdialog();
                    downloadFile(currentPhoto.getPhotoSizes().get(0).getSource(),name.getText().toString()+".png");
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
// dialog progress it will be showin when you upluod or download a photo
    public void showdialog(){
        dialog = new ProgressDialog(GalleryActivity.this);
        dialog.setMessage("Upload Photo Please wait..");
        dialog.show();
    }

}
