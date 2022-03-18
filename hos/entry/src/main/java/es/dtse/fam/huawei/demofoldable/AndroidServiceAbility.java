package es.dtse.fam.huawei.demofoldable;


import es.dtse.fam.huawei.demofoldable.widget.remote.AndroidController;
import es.dtse.fam.huawei.demofoldable.widget.remote.HarmonyRemote;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.rpc.IRemoteObject;

public class AndroidServiceAbility extends Ability {
    private static final int INVALID_FORM_ID = -1;
    private  HarmonyRemote hosRemote ;
    private String topWidgetSlice;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
       // setUIContent(ResourceTable.Layout_ability_main);
        Logger.d("--->Starting AndroidServiceAbility",this);
       hosRemote = new HarmonyRemote(this);
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    private boolean intentFromWidget(Intent intent) {
        long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        return formId != INVALID_FORM_ID;
    }

    private String getRoutePageSlice(Intent intent) {
        long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        if (formId == INVALID_FORM_ID) {
            return null;
        }
        return null;
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (intentFromWidget(intent)) { // Only response to it when starting from a service widget.
            String newWidgetSlice = getRoutePageSlice(intent);
            if (topWidgetSlice == null || !topWidgetSlice.equals(newWidgetSlice)) {
                topWidgetSlice = newWidgetSlice;
                restart();
            }
        } else {
            if (topWidgetSlice != null) {
                topWidgetSlice = null;
                restart();
            }
        }
    }

    @Override
    public IRemoteObject onConnect(Intent intent) {
        return hosRemote.asObject();
    }

}
