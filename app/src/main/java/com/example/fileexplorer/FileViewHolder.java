package com.example.fileexplorer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FileViewHolder extends RecyclerView.ViewHolder {
    public TextView fileName,fileSize;
    public CardView container;
    public ImageView imgFile;
    public FileViewHolder(@NonNull View itemView) {
        super(itemView);
        fileName=itemView.findViewById(R.id.tv_fileName);
        fileSize=itemView.findViewById(R.id.tv_fileSize);
        imgFile=itemView.findViewById(R.id.img_fileType);
        container=itemView.findViewById(R.id.card_container);
    }
}
