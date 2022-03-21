package es.dtse.fam.huawei.demofoldable.widget.remote;

import ohos.rpc.*;

public abstract  class AndroidControllerStub extends RemoteObject implements IAndroidController {

    static final int ACTION_COMMAND = IRemoteObject.MIN_TRANSACTION_ID;

    public AndroidControllerStub(String descriptor) {
        super(descriptor);
    }

    static final String DESCRIPTOR = "es.dtse.fam.huawei.demofoldable.widget.remote.IAndroidController";

    public static IAndroidController asInterface(IRemoteObject remoteObject) {
        System.out.println("---->remoteObj:"+remoteObject);
        if (remoteObject == null) {
            System.out.println("---->remoteObj is null");
            return null;
        }
        System.out.println("---isdead: "+remoteObject.isObjectDead());
        IRemoteBroker rpc = remoteObject.queryLocalInterface(DESCRIPTOR);
        if (rpc != null){
            return (IAndroidController) rpc;
        }else{
            return new AndroidControllerProxy(remoteObject);
        }
    }

    public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) throws RemoteException {
        switch (code) {
            case ACTION_COMMAND:
                action(data.readString(), data.readString());
                return true;
        }
        return false;
    }


    @Override
    public IRemoteObject asObject() {
        return this;
    }
}
