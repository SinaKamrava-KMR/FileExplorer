package com.example.fileexplorer.Fragments;

import static android.util.Log.i;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fileexplorer.FileAdapter;
import com.example.fileexplorer.FileOpener;
import com.example.fileexplorer.OnFileSelectedListener;
import com.example.fileexplorer.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InternalFragment extends Fragment implements OnFileSelectedListener {

    private RecyclerView recyclerView;
    private List<File> fileList ;
    private TextView tv_pathHolder;
    private ImageView img_back;
    private FileAdapter fileAdapter;
    File storage;
    String data;
    String[] items={"Details","Rename","Delete","Share"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_internal,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_pathHolder=view.findViewById(R.id.tv_pathHolder);
        img_back=view.findViewById(R.id.img_back);



        //String internalStorage=System.getenv("EXTERNAL_STORAGE");
        String internalStorage=System.getenv("EXTERNAL_STORAGE");
        i("FilesList", "internalStorage : "+internalStorage);
        storage=new File(internalStorage);

        try {
            data=getArguments().getString("path");
            File file=new File(data);
            storage=file;
        }catch (Exception e){
            e.printStackTrace();
        }

        tv_pathHolder.setText(storage.getAbsolutePath());
        recyclerView=view.findViewById(R.id.recycler_internal);
        runTimePermission();


    }

    private void runTimePermission() {
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        displayFiles();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();

    }

    public ArrayList<File> findFiles(File file){
        ArrayList<File> arrayList =new ArrayList<>();
        File[] files=file.listFiles();
        i("FilesList", "findPdfs: "+file.listFiles());
        if (files !=null){
            for(File singleFile : files){
                if (singleFile.isDirectory() && !singleFile.isHidden()){
                    arrayList.add(singleFile);
                }
            }
            for (File singleFile:files){
                if (singleFile.getName().toLowerCase().endsWith(".jpeg") || singleFile.getName().toLowerCase().endsWith(".jpg") ||
                        singleFile.getName().toLowerCase().endsWith(".png")||singleFile.getName().toLowerCase().endsWith(".mp3")||
                        singleFile.getName().toLowerCase().endsWith(".wav")||singleFile.getName().toLowerCase().endsWith(".mp4")||
                        singleFile.getName().toLowerCase().endsWith(".pdf")||singleFile.getName().toLowerCase().endsWith(".doc")||
                        singleFile.getName().toLowerCase().endsWith(".apk")){
                    arrayList.add(singleFile);

                }
            }
        }else {
            Toast.makeText(getContext(), "files value is null", Toast.LENGTH_SHORT).show();
        }

        return arrayList;
    }
    private void displayFiles() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        fileList=new ArrayList<>();
        i("FilesList", "displayFiles: storage File : "+storage.listFiles());
        fileList.addAll(findFiles(storage));
        fileAdapter=new FileAdapter(getContext(),fileList,this);
        recyclerView.setAdapter(fileAdapter);
    }

    @Override
    public void onFileClicked(File file) {
        if (file.isDirectory()){
            Bundle bundle=new Bundle();
            bundle.putString("path",file.getAbsolutePath());
            InternalFragment internalFragment=new InternalFragment();
            internalFragment.setArguments(bundle);

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,internalFragment)
                    .addToBackStack(null)
                    .commit();

        }else {
            try {
                FileOpener.openFile(getContext(),file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onFileLongClicked(File file) {

    }
}
