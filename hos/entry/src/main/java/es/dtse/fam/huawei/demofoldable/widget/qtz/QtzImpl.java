package es.dtse.fam.huawei.demofoldable.widget.qtz;

import es.dtse.fam.huawei.demofoldable.MainAbility;
import es.dtse.fam.huawei.demofoldable.WidgetAbility;
import es.dtse.fam.huawei.demofoldable.widget.controller.FormController;
import ohos.aafwk.ability.*;
import ohos.aafwk.content.Intent;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONObject;

/**
 * Form controller implementation.
 */
public class QtzImpl extends FormController implements PlayerDelegate{
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, QtzImpl.class.getName());

    private static final String BTN_NEXT = "btn_next";

    private static final String BTN_PAUSE = "btn_pause";
    private final Context mContext;
    private long formId;

    public QtzImpl(Context context, String formName, Integer dimension) {
        super(context, formName, dimension);
        mContext = context;
    }

    @Override
    public ProviderFormInfo bindFormData(long formId) {
        return null;
    }

    @Override
    public void updateFormData(long formId, Object... vars) {
    }

    @Override
    public void onTriggerFormEvent(long formId, String message) {
        PlayItem.getInstance().setDelegate(this);
        ZSONObject zsonObject = ZSONObject.stringToZSON(message);
        this.formId = formId;
        System.out.println("----onTriggerFormEvent");
        switch (zsonObject.getString("mAction")) {
            case BTN_NEXT:
                MainAbility.doRemoteAction("play");
                break;
            case BTN_PAUSE:
                MainAbility.doRemoteAction("stop");
                break;
            default:
                break;
        }
    }

    @Override
    public Class<? extends AbilitySlice> getRoutePageSlice(Intent intent) {
        return null;
    }

    @Override
    public void onPlayItemUpdate(ZSONObject joc) {
        System.out.println("----onPlayItemUpdate delegate. "+ mContext.getClass());
        // Update js card
        try {
            if (mContext instanceof Ability)
            {
                System.out.println("-----updating form with binding data: "+joc.toString()+ " formId: "+formId);

                ((Ability) mContext).updateForm(formId, new FormBindingData(joc));
            }
        } catch (FormException e) {
            HiLog.error(TAG, e.getMessage());
        }
    }
}
