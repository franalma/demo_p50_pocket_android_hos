package es.dtse.fam.huawei.demofoldable;

import es.dtse.fam.huawei.demofoldable.widget.controller.FormController;
import es.dtse.fam.huawei.demofoldable.widget.controller.FormControllerManager;
import es.dtse.fam.huawei.demofoldable.widget.remote.AndroidController;
import es.dtse.fam.huawei.demofoldable.widget.remote.HarmonyRemote;
import ohos.aafwk.ability.*;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.utils.zson.ZSONObject;

/**
 * Ability deals with service widgets, handles form's create/update/delete/click event.
 * Each kind of form may extends {@link FormController} to provide different data and different ability slice
 * according to the click event triggered by this form. Each kind of form may be created multiple times, but
 * they should share the same data.
 * When adding a new service widget's code, WidgetAbility's code no need to change, a derived class inherited from
 * FormController will be added and reflected by FormControllerManager.
 */
public class WidgetAbility extends Ability {
    private static AndroidController controllerRemote;
    private HarmonyRemote hosRemote;
    /**
     * The service widget default dimension 2*2
     */
    public static final int DEFAULT_DIMENSION_2X2 = 2;
    /**
     * Service widget dimension 1*2
     */
    public static final int DIMENSION_1X2 = 1;
    /**
     * Service widget dimension 2*4
     */
    public static final int DIMENSION_2X4 = 3;
    /**
     * The invalid service widget form id
     */
    public static final int INVALID_FORM_ID = -1;
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, WidgetAbility.class.getName());
    private String topWidgetSlice;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        System.out.println("----widgeability starts");
        // Only response to it when starting from a service widget.
        if (intentFromWidget(intent)) {
            topWidgetSlice = getRoutePageSlice(intent);
            if (topWidgetSlice != null) {
                setMainRoute(topWidgetSlice);
            }
        }
//        initAndroidController();
    }

//    private void initAndroidController(){
//        androidController = new AndroidController(WidgetAbility.this);
//    }



    @Override
    protected ProviderFormInfo onCreateForm(Intent intent) {
        HiLog.info(TAG, "onCreateForm");

        controllerRemote = new AndroidController(this);
        hosRemote = new HarmonyRemote(this);


        long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        String formName = intent.getStringParam(AbilitySlice.PARAM_FORM_NAME_KEY);
        int dimension = intent.getIntParam(AbilitySlice.PARAM_FORM_DIMENSION_KEY, DEFAULT_DIMENSION_2X2);
        HiLog.info(TAG, "onCreateForm: formId=" + formId + ",formName=" + formName);

        FormControllerManager formControllerManager = FormControllerManager.getInstance(this);
        FormController formController = formControllerManager.getController(formId);
        formController = (formController == null) ? formControllerManager.createFormController(formId,
                formName, dimension) : formController;
        if (formController == null) {
            HiLog.error(TAG, "Get null controller. formId: " + formId + ", formName: " + formName);
            return null;
        }
        IntentParams intentParams = intent.getParam(AbilitySlice.PARAM_FORM_CUSTOMIZE_KEY);
        if (intentParams.getParam("EXTENED_FA_CARD")
                .equals("TYPE_BALI")) {
            ZSONObject zsonObject = new ZSONObject();
            boolean isQTZ = true;
            zsonObject.put("isQtzType", isQTZ);
            try {
                updateForm(formId, new FormBindingData(zsonObject));
            } catch (FormException e) {
                HiLog.error(TAG, e.getMessage());
            }
        }
        return formController.bindFormData(formId);
    }

    @Override
    protected void onUpdateForm(long formId) {
        HiLog.info(TAG, "onUpdateForm");
        super.onUpdateForm(formId);
        FormControllerManager formControllerManager = FormControllerManager.getInstance(this);
        FormController formController = formControllerManager.getController(formId);
        formController.onUpdateFormData(formId);
    }

    @Override
    protected void onDeleteForm(long formId) {
        HiLog.info(TAG, "onDeleteForm: formId=" + formId);
        super.onDeleteForm(formId);
        FormControllerManager formControllerManager = FormControllerManager.getInstance(this);
        FormController formController = formControllerManager.getController(formId);
        formController.onDeleteForm(formId);
        formControllerManager.deleteFormController(formId);
    }

    @Override
    protected void onTriggerFormEvent(long formId, String message) {
        HiLog.info(TAG, "onTriggerFormEvent: " + message);
        super.onTriggerFormEvent(formId, message);
        FormControllerManager formControllerManager = FormControllerManager.getInstance(this);
        FormController formController = formControllerManager.getController(formId);
        formController.onTriggerFormEvent(formId, message);
    }

    @Override
    public void onNewIntent(Intent intent) {
        // Only response to it when starting from a service widget.
        if (intentFromWidget(intent)) {
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

    private boolean intentFromWidget(Intent intent) {
        long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        return formId != INVALID_FORM_ID;
    }

    private String getRoutePageSlice(Intent intent) {
        long formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        if (formId == INVALID_FORM_ID) {
            return null;
        }
        FormControllerManager formControllerManager = FormControllerManager.getInstance(this);
        FormController formController = formControllerManager.getController(formId);
        if (formController == null) {
            return null;
        }
        Class<? extends AbilitySlice> clazz = formController.getRoutePageSlice(intent);
        if (clazz == null) {
            return null;
        }
        return clazz.getName();
    }

    @Override
    public IRemoteObject onConnect(Intent intent) {
        return hosRemote.asObject();
    }

    public static void doRemoteAction(String action) {
        controllerRemote.sendAction(action);
    }

}
