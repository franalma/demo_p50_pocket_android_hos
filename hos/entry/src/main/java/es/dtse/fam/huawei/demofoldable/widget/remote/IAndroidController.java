package es.dtse.fam.huawei.demofoldable.widget.remote;

import ohos.rpc.IRemoteBroker;

public interface IAndroidController extends IRemoteBroker {
    void action(String deviceId, String action);
}


