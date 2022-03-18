package com.demo.app.p50mobileappdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;

import com.huawei.ohos.localability.AbilityUtils;

class HarmonyServiceConnection {
    IBinder binder;
    Context context;
    private String DESCRIPTOR = " es.dtse.fam.huawei.demofoldable.widget.remote.IHarmonyController";
    private String HARMONY_BUNDLE_NAME = "es.dtse.fam.huawei.demofoldable";
    private String HARMONY_ABILITY_NAME = "es.dtse.fam.huawei.demofoldable.AndroidServiceAbility";

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            HarmonyServiceConnection.this.binder = iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
    public HarmonyServiceConnection(Context context){
        this.context = context;
    }

    public void connect(){
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(HARMONY_BUNDLE_NAME, HARMONY_ABILITY_NAME);
        intent.setComponent(componentName);
        AbilityUtils.connectAbility(this.context, intent, serviceConnection);
    }

    public void sendData(String info){
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken(DESCRIPTOR);
        data.writeString(info);
        try {
            binder.transact(10000, data, reply, 0);
            reply.writeNoException();
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            data.recycle();
            reply.recycle();
        }
    }

}
