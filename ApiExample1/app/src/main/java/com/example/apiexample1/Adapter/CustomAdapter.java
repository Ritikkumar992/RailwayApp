package com.example.apiexample1.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apiexample1.Model.RetroPhotoResponse;
import com.example.apiexample1.R;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    Context context;
    private List<RetroPhotoResponse> retroPhotoList;

    public CustomAdapter(Context context, List<RetroPhotoResponse> retroPhotoList){
        this.context = context;
        this.retroPhotoList = retroPhotoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.retrofit_img_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.text.setText(retroPhotoList.get(position).getTitle());
        holder.id.setText("Id :"+String.valueOf(retroPhotoList.get(position).getId()));
        holder.albumId.setText("Album Id: "+String.valueOf(retroPhotoList.get(position).getAlbumId()));


        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(retroPhotoList.get(position).getUrl())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return retroPhotoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView text, id, albumId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgView);
            text = itemView.findViewById(R.id.textView);
            id = itemView.findViewById(R.id.idView);
            albumId = itemView.findViewById(R.id.albumIdView);

        }
    }
}
