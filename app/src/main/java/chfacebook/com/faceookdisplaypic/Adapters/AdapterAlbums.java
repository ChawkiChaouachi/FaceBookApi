package chfacebook.com.faceookdisplaypic.Adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import java.util.List;
import chfacebook.com.faceookdisplaypic.Models.Album;
import chfacebook.com.faceookdisplaypic.Models.UiItemObject;
import chfacebook.com.faceookdisplaypic.R;
import chfacebook.com.faceookdisplaypic.Utils.Constant;

/**
 * Created by chawki on 07/11/2017.
 */



        /**
        *  adapter album list
        */

public class AdapterAlbums  extends RecyclerView.Adapter<AdapterAlbums.ViewHolder> {

    Context context;
    List<Album> albums;
    UiItemObject uiItemObject;
    String TAG = AdapterAlbums.class.getSimpleName();
    OnclickAlbum onclickAlbum;
    public AdapterAlbums(Context context, List<Album> albums,UiItemObject uiItemObject,OnclickAlbum onclickAlbum) {
        this.context = context;
        this.albums = albums;
        this.uiItemObject=uiItemObject;
        this.onclickAlbum=onclickAlbum;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_album,parent,false);
        Log.d(TAG, "onCreateViewHolder: "+uiItemObject.getWidthitem());

            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams)view.getLayoutParams();
        layoutParams.width = (int)(uiItemObject.getWidthitem()*context.getResources().getDisplayMetrics().density);

        layoutParams.height = (int)(uiItemObject.getHeightitem()*context.getResources().getDisplayMetrics().density);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // get cover photo from url
        Glide.with(context).load(Constant.BASE_URL+albums.get(position).getId()+Constant.URL_PHOTO+ AccessToken.getCurrentAccessToken().getToken()).fitCenter().into(holder.coverphot);
    }

    @Override
    public int getItemCount() {
        return (albums==null)?0:albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView coverphot;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            coverphot=(ImageView)itemView.findViewById(R.id.coverphot);
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   // notify FragmentAlbum to get the first part of picture
                    onclickAlbum.albumClicked(albums.get(getAdapterPosition()));
               }
           });

        }
    }

    public interface OnclickAlbum{
        void albumClicked(Album album);
    }

    // notify adapter when user switch the album page
    public void NotifyAdapter(List<Album> albumsupdated){
        this.albums.clear();
        this.albums.addAll(albumsupdated);
        notifyDataSetChanged();
    }
}
