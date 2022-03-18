package es.dtse.fam.huawei.demofoldable;


import es.dtse.fam.huawei.demofoldable.widget.remote.AndroidController;
import es.dtse.fam.huawei.demofoldable.widget.remote.HarmonyRemote;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.rpc.IRemoteObject;

public class MainAbility extends Ability {
    private static final int INVALID_FORM_ID = -1;
    private static AndroidController controllerRemote;
    private HarmonyRemote hosRemote;
    private String topWidgetSlice;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_main);
        controllerRemote = new AndroidController(this);
        hosRemote = new HarmonyRemote(this);
    }

    @Override
    protected void onActive() {
        super.onActive();
    }


    @Override
    public IRemoteObject onConnect(Intent intent) {
        return hosRemote.asObject();
    }

    public static void doRemoteAction(String action) {
        controllerRemote.sendAction(action);
    }
}
