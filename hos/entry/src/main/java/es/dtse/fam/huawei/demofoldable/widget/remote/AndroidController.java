package es.dtse.fam.huawei.demofoldable.widget.remote;

import es.dtse.fam.huawei.demofoldable.widget.log.LogUtil;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.bundle.ElementName;
import ohos.rpc.*;

import static ohos.data.search.schema.PhotoItem.TAG;

public class AndroidController extends RemoteObject implements IRemoteBroker {

    private IAndroidController remoteService;
    private String firstDeviceId;
    private boolean isConnected;
    private final Ability ability;


    public AndroidController(Ability ability) {
        super("");
        this.ability = ability;
        connectToAndroidService();

    }

    private final IAbilityConnection connection = new IAbilityConnection() {
        @Override
        public void onAbilityConnectDone(ElementName elementName, IRemoteObject remote, int resultCode) {
            System.out.println("-----onAbilityConnectDone:"+resultCode);
            remoteService = AndroidControllerStub.asInterface(remote);
        }

        @Override
        public void onAbilityDisconnectDone(ElementName elementName, int i) {
            LogUtil.info(TAG, "Android service disconnect done!");
            isConnected = false;
            ability.disconnectAbility(connection);
        }
    };

    @Override
    public IRemoteObject asObject() {
        return this;
    }

    private void startAndroidApp() {
        Intent intent = new Intent();
        System.out.println("-----starting android app");
        Operation operation = new Intent.OperationBuilder()
                .withBundleName("com.demo.app.p50mobileappdemo")
                .withAbilityName("com.demo.app.p50mobileappdemo.MainActivity")
                .withFlags(Intent.FLAG_NOT_OHOS_COMPONENT)
                .build();
        intent.setOperation(operation);
        ability.startAbility(intent);
    }

    private void connectToAndroidService() {
        Intent intent = new Intent();
        System.out.println("-----connecting to android service");
        Operation operation = new Intent.OperationBuilder()
                .withBundleName("com.demo.app.p50mobileappdemo")
                .withAbilityName("com.demo.app.p50mobileappdemo.EntryPointService")
                .withFlags(Intent.FLAG_NOT_OHOS_COMPONENT)
                .build();
        intent.setOperation(operation);
        isConnected = ability.connectAbility(intent, connection);
    }
    @Override
    public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) throws RemoteException{
        return super.onRemoteRequest(code, data, reply, option);
    }

    public void sendAction(String action){
        System.out.println("---->sendAction: "+action);
        System.out.println("-----remoteService: "+remoteService);
        remoteService.action("000000", action);

    }

}
