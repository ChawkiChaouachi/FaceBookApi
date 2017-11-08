package chfacebook.com.faceookdisplaypic.Adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.util.List;
import chfacebook.com.faceookdisplaypic.Models.Photo;
import chfacebook.com.faceookdisplaypic.Models.UiItemObject;
import chfacebook.com.faceookdisplaypic.R;

/**
 * Created by chawki on 07/11/2017.
 */


/**
 * adapter for picture
 */
public class AdapterPictures extends RecyclerView.Adapter<AdapterPictures.ViewHolder> {

    Context context;
    List<Photo> photos;
    UiItemObject uiItemObject;
    String TAG = AdapterAlbums.class.getSimpleName();
    OnclickPicture onclickPicture;
    public AdapterPictures(Context context, List<Photo> photos, UiItemObject uiItemObject, OnclickPicture onclickPicture) {
        this.context = context;
        this.photos = photos;
        this.uiItemObject=uiItemObject;
        this.onclickPicture=onclickPicture;
    }

    @Override
    public AdapterPictures.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_album,parent,false);
        Log.d(TAG, "onCreateViewHolder: "+uiItemObject.getWidthitem());

        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams)view.getLayoutParams();
        layoutParams.width = (int)(uiItemObject.getWidthitem()*context.getResources().getDisplayMetrics().density);

        layoutParams.height = (int)(uiItemObject.getHeightitem()*context.getResources().getDisplayMetrics().density);

        AdapterPictures.ViewHolder vh = new AdapterPictures.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterPictures.ViewHolder holder, int position) {

        //use glid to download picture because it saves the photo into the cache
        Glide.with(context).load(photos.get(position).getPhotoSizes().get(0).getSource()).centerCrop().into(holder.coverphot);

    }

    @Override
    public int getItemCount() {
        return (photos==null)?0:photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView coverphot;


        public ViewHolder(View itemView) {
            super(itemView);
            coverphot=(ImageView)itemView.findViewById(R.id.coverphot);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // notify the GalleryActivity to put the photo to full screen from there you can upload the photo to Firebase
                    onclickPicture.pictureClicked(photos.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface OnclickPicture{
        void pictureClicked(Photo photo);
    }


    // notify adapter when the user switch the page
    public void NotifyAdapter(List<Photo> photosnew){
        this.photos.clear();
        this.photos.addAll(photosnew);
        notifyDataSetChanged();
    }
}
