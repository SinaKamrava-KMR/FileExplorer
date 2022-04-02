package com.example.fileexplorer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileViewHolder> {

    private Context context;
    private List<File> files;
    private OnFileSelectedListener listener;

    public FileAdapter(Context context, List<File> files,OnFileSelectedListener onFileSelectedListener) {
        this.context = context;
        this.listener= onFileSelectedListener;
        this.files = files;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(context).inflate(R.layout.file_continer,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.fileName.setText(files.get(position).getName());
        holder.fileName.setSelected(true);

        int items=0;
        if (files.get(position).isDirectory()){
            File[] files1 =files.get(position).listFiles();
            for (File singleFile:files1){
                if (!singleFile.isHidden()){
                    items+=1;
                }
            }

            holder.fileSize.setText(String.valueOf(items)+" Files");
        }else {
            holder.fileSize.setText(Formatter.formatShortFileSize(context,files.get(position).length()));
        }
        if (files.get(position).getName().toLowerCase().endsWith(".jpeg")){
            holder.imgFile.setImageResource(R.drawable.ic_image);
        }else if (files.get(position).getName().toLowerCase().endsWith("jpg")){
            holder.imgFile.setImageResource(R.drawable.ic_image);
        }else if (files.get(position).getName().toLowerCase().endsWith("png")){
            holder.imgFile.setImageResource(R.drawable.ic_image);
        }else if (files.get(position).getName().toLowerCase().endsWith(".pdf")){
            holder.imgFile.setImageResource(R.drawable.ic_pdf);
        }else if (files.get(position).getName().toLowerCase().endsWith(".doc")){
            holder.imgFile.setImageResource(R.drawable.ic_docs);
        }else if (files.get(position).getName().toLowerCase().endsWith(".mp3")){
            holder.imgFile.setImageResource(R.drawable.ic_music);
        }else if (files.get(position).getName().toLowerCase().endsWith(".wav")){
            holder.imgFile.setImageResource(R.drawable.ic_image);
        }else if (files.get(position).getName().toLowerCase().endsWith(".mp4")){
            holder.imgFile.setImageResource(R.drawable.ic_play);
        }else if (files.get(position).getName().toLowerCase().endsWith(".apk")){
            holder.imgFile.setImageResource(R.drawable.ic_android);
        }else {
            holder.imgFile.setImageResource(R.drawable.ic_folder);
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFileClicked(files.get(position));
            }
        });

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onFileLongClicked(files.get(position),position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }
}
