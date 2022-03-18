package es.dtse.fam.huawei.demofoldable.widget.remote;

import ohos.rpc.*;

public class AndroidControllerProxy implements IAndroidController {
    private final IRemoteObject remote;

    public AndroidControllerProxy(IRemoteObject remote) {
        this.remote = remote;
    }

    @Override
    public void action(String deviceId, String action) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(AndroidControllerStub.DESCRIPTOR);
        data.writeString(deviceId);
        data.writeString(action);

        try {
            remote.sendRequest(AndroidControllerStub.ACTION_COMMAND, data, reply, option);
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    @Override
    public IRemoteObject asObject() {
        return remote;
    }
}



