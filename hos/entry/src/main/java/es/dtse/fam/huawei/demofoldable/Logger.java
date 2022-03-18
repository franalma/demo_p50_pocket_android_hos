package es.dtse.fam.huawei.demofoldable;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class Logger {

    public static void d(String message, Object obj){
        HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 1,obj != null?obj.getClass().getCanonicalName():"");
        HiLog.debug(TAG,message);

    }
}
