package com.demo.app.p50mobileappdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import es.dtse.fam.huawei.demofoldable.widget.remote.IAndroidController;

public class EntryPointService extends Service {
    IAndroidController iAndroidController = new IAndroidController.Stub() {

        @Override
        public void action(String deviceId, String action) throws RemoteException {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            switch(action){
                case "play":{
                    MusicPlayer.getInstance().play(getBaseContext());
                    break;
                }
                case "stop": {
                    MusicPlayer.getInstance().stop();
                    break;
                }
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MusicPlayer.getInstance().initHosCommunication(getBaseContext());
        return (IBinder) iAndroidController;
    }
}
