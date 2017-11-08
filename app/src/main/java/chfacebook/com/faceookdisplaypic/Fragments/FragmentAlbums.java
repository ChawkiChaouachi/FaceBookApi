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

import chfacebook.com.faceookdisplaypic.Adapters.AdapterAlbums;
import chfacebook.com.faceookdisplaypic.GalleryActivity;
import chfacebook.com.faceookdisplaypic.Models.Album;
import chfacebook.com.faceookdisplaypic.Models.PhotoAlbum;
import chfacebook.com.faceookdisplaypic.Models.ResponseAlbumls;
import chfacebook.com.faceookdisplaypic.Models.UiItemObject;
import chfacebook.com.faceookdisplaypic.R;
import chfacebook.com.faceookdisplaypic.WebService.APIServiceInterface;
import chfacebook.com.faceookdisplaypic.WebService.CreateNewClient;
import chfacebook.com.faceookdisplaypic.WebService.ServiceWeb;
import chfacebook.com.faceookdisplaypic.databinding.FragmentAlbumBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static chfacebook.com.faceookdisplaypic.Utils.Constant.BASE_URL;

/**
 * Created by chawki on 07/11/2017.
 */


/**
 * fragment albums use the list of album and the number of item for pagination .
 * it will send a request when you press previous or next to get the new list of albums and notify adapter of recycleview
 * NB : we can optimize by saving the old requset
 * use retrofit to get the next list of pictures.
 * Next or previous button will be hidden if the is no data available
 * every Album list have a field next and previous contain the url of the next request if there is a data
 * press an item of albums . the system will send a request to get the first list of photo and this fragment will be replaced by the photo fragment if
 * the request is not empty
 */


public class FragmentAlbums extends Fragment implements ServiceWeb.NotifyHome , AdapterAlbums.OnclickAlbum{

    FragmentAlbumBinding binding;
    boolean created = false;
    ResponseAlbumls responseAlbumls;
    UiItemObject uiItemObject;
    ServiceWeb serviceWeb;
    String TAG = FragmentAlbums.class.getSimpleName();
    Bundle bundle;
    Call<ResponseAlbumls> responseAlbumlsCall;
    APIServiceInterface apiServiceInterface;
    AdapterAlbums adapterAlbums;
    public static FragmentAlbums NewInstance(){
        return new FragmentAlbums();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceWeb = new ServiceWeb(FragmentAlbums.this);
        apiServiceInterface = CreateNewClient.getClient(BASE_URL).create(APIServiceInterface.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding= DataBindingUtil.inflate(inflater, R.layout.fragment_album,container,false);

     //  if(!created){
            bundle = this.getArguments();
           responseAlbumls=(ResponseAlbumls) bundle.getSerializable(ResponseAlbumls.class.getSimpleName());
           uiItemObject =(UiItemObject)bundle.getSerializable (UiItemObject.class.getSimpleName());

           if (responseAlbumls.getPaging().getNext()==null){
                binding.previous.setVisibility(View.INVISIBLE);
                binding.next.setVisibility(View.INVISIBLE);
           }else{
               binding.next.setVisibility(View.VISIBLE);
               binding.previous.setVisibility(View.INVISIBLE);
           }

           GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),uiItemObject.getNumbreItemColumns(),GridLayoutManager.VERTICAL,false);
           binding.gridAlbum.setLayoutManager(layoutManager);
           adapterAlbums =new AdapterAlbums(getActivity(),responseAlbumls.getListAlbum(),uiItemObject,this);
           binding.gridAlbum.setAdapter(adapterAlbums);
           binding.previous.setOnClickListener(onClickListener);
           binding.next.setOnClickListener(onClickListener);




       created=true;
       return binding.getRoot();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.previous:
                    if (responseAlbumls.getPaging().getPrevious()!= null && responseAlbumls.getPaging().getPrevious().length()>0){
                        callService(responseAlbumls.getPaging().getPrevious());
                    }else{
                        binding.previous.setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.id.next:
                    if (responseAlbumls.getPaging().getNext()!= null && responseAlbumls.getPaging().getNext().length()>0){
                        callService(responseAlbumls.getPaging().getNext());
                    }else{
                        binding.next.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    };


    public void callService(String URL){
        Log.d(TAG, "callService: "+URL);
        enableDisableButton(false);
        responseAlbumlsCall=apiServiceInterface.getAlbums(URL);
        responseAlbumlsCall.enqueue(callbackAlbums);
    }

    @Override
    public void notifyWebRequest(int actionCode, int statusResponse, Object object) {

        switch (statusResponse){
            case 200:
                if(((PhotoAlbum)object).getListphoto()!=null&&((PhotoAlbum)object).getListphoto().size()>0) {
                    FragmentPhotos fragmentPhotos = new FragmentPhotos();
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable(UiItemObject.class.getSimpleName(), uiItemObject);
                    bundle1.putSerializable(PhotoAlbum.class.getSimpleName(), (PhotoAlbum) object);
                    fragmentPhotos.setArguments(bundle1);
                    getFragmentManager().beginTransaction().addToBackStack(FragmentAlbums.class.getSimpleName()).replace(R.id.container, fragmentPhotos).commit();
                }else{
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.no_photo_found),Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                if(((GalleryActivity)getActivity()).publicFunction.isOnline()){
                    // we can handle it more
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.unknown_prob),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.check_network),Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    @Override
    public void albumClicked(Album album) {
        serviceWeb.getphoto(album,uiItemObject.getTotalnumberItem());

    }
    Callback<ResponseAlbumls> callbackAlbums = new Callback<ResponseAlbumls>() {
        @Override
        public void onResponse(Call<ResponseAlbumls> call, Response<ResponseAlbumls> response) {
            responseAlbumls = response.body();
            enableDisableButton(true);
            adapterAlbums.NotifyAdapter(responseAlbumls.getListAlbum());
            if(responseAlbumls.getPaging().getNext()==null&&responseAlbumls.getPaging().getPrevious()==null){
                binding.next.setVisibility(View.INVISIBLE);
                binding.previous.setVisibility(View.INVISIBLE);
                return;
            }
            if (responseAlbumls.getPaging().getNext()==null){
                binding.next.setVisibility(View.INVISIBLE);
                binding.previous.setVisibility(View.VISIBLE);
            }else{
                binding.next.setVisibility(View.VISIBLE);
                binding.previous.setVisibility(View.VISIBLE);
            }
            if (responseAlbumls.getPaging().getPrevious()==null){
                binding.previous.setVisibility(View.INVISIBLE);
            }else{
                binding.previous.setVisibility(View.VISIBLE);
            }



        }

        @Override
        public void onFailure(Call<ResponseAlbumls> call, Throwable t) {
            enableDisableButton(true);

                if(((GalleryActivity)getActivity()).publicFunction.isOnline()){
                    // we can handle it more
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.unknown_prob),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.check_network),Toast.LENGTH_SHORT).show();
                }
            enableDisableButton(true);
        }
    };

    public void enableDisableButton (boolean state){
        binding.next.setClickable(state);
        binding.previous.setClickable(state);
        if (state){
            binding.gridAlbum.setVisibility(View.VISIBLE);
            binding.prograssBar.setVisibility(View.INVISIBLE);
        }else{
            binding.gridAlbum.setVisibility(View.INVISIBLE);
            binding.prograssBar.setVisibility(View.VISIBLE);
        }
    }
}
