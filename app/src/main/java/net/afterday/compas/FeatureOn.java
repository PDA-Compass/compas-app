package net.afterday.compas;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityManager;

import java.lang.reflect.Method;

public class  FeatureOn {
    private static  Context mContext;

    private static boolean isHighTextContrastInited;
    private static boolean isHighTextContrast;

    public static void setContext(Context context){
        mContext = context;
    }

    public static boolean isHighTextContrast(){
        if (!isHighTextContrastInited) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                isHighTextContrast = isHighTextContrastEnabled();
            }
            else {
                isHighTextContrast = false;
            }
            isHighTextContrastInited = true;
        }
        return isHighTextContrast;
    }

    @RequiresApi(28)
    @SuppressWarnings("JavaReflectionMemberAccess")
    private static boolean isHighTextContrastEnabled(){
        try {
            AccessibilityManager accessibility = (AccessibilityManager) mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
            Method m = accessibility.getClass().getMethod("isHighTextContrastEnabled");
            Object result = m.invoke(accessibility);
            if (result instanceof Boolean) {
                return (Boolean) result;
            }
        } catch (Exception exception) {
        }
        return false;
    }
}
