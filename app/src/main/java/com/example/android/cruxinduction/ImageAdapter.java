package com.example.android.cruxinduction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    String[] FileName = ImagesActivity.getfileName();
    Context context;
    File path = new File(Environment.getExternalStorageDirectory(),"/fav_images");

    public ImageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.image,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position) {
        Bitmap bitmap = BitmapFactory.decodeFile(path.getPath()+"/"+ FileName[position]);
        int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
        if(scaled != null){
        holder.imageview.setImageBitmap(scaled);}


    }

    @Override
    public int getItemCount() {
        return FileName.length;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview ;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageview  = (ImageView)itemView.findViewById(R.id.image);

        }
    }

}
