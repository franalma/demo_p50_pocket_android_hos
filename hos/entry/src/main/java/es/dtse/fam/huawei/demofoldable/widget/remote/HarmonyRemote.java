package es.dtse.fam.huawei.demofoldable.widget.remote;

import es.dtse.fam.huawei.demofoldable.widget.qtz.PlayItem;
import ohos.aafwk.ability.Ability;
import ohos.rpc.*;
import ohos.utils.zson.ZSONObject;

import java.util.HashMap;

public class HarmonyRemote extends RemoteObject implements IHarmonyController {
    Ability ability;

    public HarmonyRemote(Ability ability) {
        super("HarmonyRemote");
        this.ability = ability;
    }

    @Override
    public void onResult(String value) {

    }

    @Override
    public IRemoteObject asObject() {
        return this;
    }

    @Override
    public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) throws RemoteException {
        System.out.println("---remote request: "+code);
        String token = data.readInterfaceToken();
        System.out.println("----token: "+token);
        String value = data.readString();
        System.out.println("----value: "+value);
        ZSONObject input = ZSONObject.stringToZSON(value);
        PlayItem.getInstance().setName(input.getString("title"));
        PlayItem.getInstance().setTitle(input.getString("artist"));
        PlayItem.getInstance().setDuration(input.getString("duration"));
        PlayItem.getInstance().getDelegate().onPlayItemUpdate(input);
        return true;
    }
}
