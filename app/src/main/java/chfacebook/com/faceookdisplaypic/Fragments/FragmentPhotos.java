package chfacebook.com.faceookdisplaypic.Fragments;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import chfacebook.com.faceookdisplaypic.Adapters.AdapterPictures;
import chfacebook.com.faceookdisplaypic.GalleryActivity;
import chfacebook.com.faceookdisplaypic.Models.PhotoAlbum;
import chfacebook.com.faceookdisplaypic.Models.UiItemObject;
import chfacebook.com.faceookdisplaypic.R;
import chfacebook.com.faceookdisplaypic.WebService.APIServiceInterface;
import chfacebook.com.faceookdisplaypic.WebService.CreateNewClient;
import chfacebook.com.faceookdisplaypic.databinding.FragmentPicturesBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static chfacebook.com.faceookdisplaypic.Utils.Constant.BASE_URL;

/**
 * Created by macbook on 07/11/2017.
 */


/**
 * fragment photos use the list of photo and the number of item for pagination .
 * it will send a request when you press previous or next to get the new list of photo and notify adapter of recycleview
 * NB : we can optimize by saving the old requset
 * use retrofit to get the next list of pictures.
 * Next or previous button will be hidden if the is no data available
 * every photo list have a field next and previous contain the url of the next request if there is a data
 * when you press an item you will have a full screen image and you can upload it
 *
 */

public class FragmentPhotos extends Fragment  {
    boolean created = false;
    FragmentPicturesBinding binding;
    PhotoAlbum photoAlbum;
    UiItemObject uiItemObject;
    AdapterPictures adapterPictures;
    APIServiceInterface apiServiceInterface;
    Call<PhotoAlbum> photoAlbumCall;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiServiceInterface = CreateNewClient.getClient(BASE_URL).create(APIServiceInterface.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_pictures,container,false);

        if(!created){
            Bundle bundle = this.getArguments();
            photoAlbum=(PhotoAlbum) bundle.getSerializable(PhotoAlbum.class.getSimpleName());
            uiItemObject =(UiItemObject)bundle.getSerializable (UiItemObject.class.getSimpleName());

            if (photoAlbum.getPaging().getNext()==null){
                binding.next.setVisibility(View.INVISIBLE);
                binding.previous.setVisibility(View.INVISIBLE);
            }else{
                binding.next.setVisibility(View.VISIBLE);
                binding.previous.setVisibility(View.INVISIBLE);
            }

            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),uiItemObject.getNumbreItemColumns(),GridLayoutManager.VERTICAL,false);
            binding.gridPhoto.setLayoutManager(layoutManager);
            adapterPictures =new AdapterPictures(getActivity(),photoAlbum.getListphoto(),uiItemObject,(AdapterPictures.OnclickPicture)getActivity());
            binding.gridPhoto.setAdapter(adapterPictures);
            binding.previous.setOnClickListener(onClickListener);
            binding.next.setOnClickListener(onClickListener);

        }


        created=true;
        return binding.getRoot();
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.previous:
                    if (photoAlbum.getPaging().getPrevious()!=null&&photoAlbum.getPaging().getPrevious().length()>0){
                        callService(photoAlbum.getPaging().getPrevious());
                    }else{
                        binding.previous.setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.id.next:
                    if (photoAlbum.getPaging().getNext()!=null&&photoAlbum.getPaging().getNext().length()>0){
                        callService(photoAlbum.getPaging().getNext());
                    }else{
                        binding.next.setVisibility(View.INVISIBLE);
                    }

                    break;
            }
        }
    };



    Callback<PhotoAlbum> callbackPhoto = new Callback<PhotoAlbum>() {
        @Override
        public void onResponse(Call<PhotoAlbum> call, Response<PhotoAlbum> response) {
            photoAlbum = response.body();
            enableDisableButton(true);
            adapterPictures.NotifyAdapter(photoAlbum.getListphoto());
            if(photoAlbum.getPaging().getNext()==null&&photoAlbum.getPaging().getPrevious()==null){
                binding.next.setVisibility(View.INVISIBLE);
                binding.previous.setVisibility(View.INVISIBLE);
                return;
            }

            binding.paginatLayout.setVisibility(View.VISIBLE);
            if (photoAlbum.getPaging().getNext()==null){
                binding.next.setVisibility(View.INVISIBLE);
                binding.previous.setVisibility(View.VISIBLE);
            }else{
                binding.next.setVisibility(View.VISIBLE);
                binding.previous.setVisibility(View.VISIBLE);
            }
            if (photoAlbum.getPaging().getPrevious()==null){
                binding.previous.setVisibility(View.INVISIBLE);
            }else{
                binding.previous.setVisibility(View.VISIBLE);
            }



        }

        @Override
        public void onFailure(Call<PhotoAlbum> call, Throwable t) {
            enableDisableButton(true);
            if(((GalleryActivity)getActivity()).publicFunction.isOnline()){
                // we can handle it more
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.unknown_prob),Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.check_network),Toast.LENGTH_SHORT).show();
            }

        }
    };

    public void callService(String URL){

        enableDisableButton(false);
        photoAlbumCall=apiServiceInterface.getPhotoAlbum(URL);
        photoAlbumCall.enqueue(callbackPhoto);
    }


    public void enableDisableButton (boolean state){
        binding.next.setClickable(state);
        binding.previous.setClickable(state);
        if (state){
            binding.gridPhoto.setVisibility(View.VISIBLE);
            binding.prograssBar.setVisibility(View.INVISIBLE);
        }else{
            binding.gridPhoto.setVisibility(View.INVISIBLE);
            binding.prograssBar.setVisibility(View.VISIBLE);
        }
    }



}
