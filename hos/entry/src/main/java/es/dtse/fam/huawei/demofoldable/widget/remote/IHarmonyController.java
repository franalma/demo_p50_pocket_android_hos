package es.dtse.fam.huawei.demofoldable.widget.remote;

import ohos.rpc.IRemoteBroker;

public interface IHarmonyController extends IRemoteBroker {
   void onResult(String value);
}


