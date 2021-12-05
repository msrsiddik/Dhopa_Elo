package com.robotechvalley.dhopaelo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BannerService extends Service {
    private static final String TAG = "BannerService";

    public BannerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("banner");
        storageRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                Task<byte[]> bytes = item.getBytes(Long.MAX_VALUE);
                Log.d(TAG, "onStartCommand: "+item.getName());
                bytes.addOnCompleteListener(task -> {
                    try {
                        File file = new File(getCacheDir(), item.getName());
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(task.getResult());
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Stop banner service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}