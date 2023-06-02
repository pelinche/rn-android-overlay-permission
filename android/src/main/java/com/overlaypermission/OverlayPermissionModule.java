
package com.overlaypermission;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class OverlayPermissionModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public OverlayPermissionModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "OverlayPermissionModule";
    }

    @ReactMethod
    public void requestOverlayPermission(Promise promise) {
        /**
         * Before android 6.0 Marshmallow you dont need to ask for canDrawOverlays
         * permission,
         * but in newer android versions this is mandatory
         */

        // try {
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        // if(!Settings.canDrawOverlays(this.reactContext)) {
        // Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        // Uri.parse("package:" + this.reactContext.getPackageName()));
        // this.reactContext.startActivityForResult(intent, 0, null);
        // }
        // } else {
        // promise.resolve(true);
        // }
        // }
        // catch (Error e) {
        // promise.reject(e);
        // }

        String device = Build.MANUFACTURER;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this.reactContext)) {
                    if (device.equals("Xiaomi")) {
                        // final Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                        // intent.setClassName("com.miui.securitycenter",
                        // "com.miui.permcenter.permissions.PermissionsEditorActivity");
                        // intent.putExtra("extra_pkgname", this.reactContext.getPackageName());
                        // reactContext.startActivity(intent);
                        // // new AlertDialog.Builder(this.reactContext.getActivity())
                        // // .setTitle("Please Enable the additional permissions")
                        // // .setMessage(
                        // // "You will not receive notifications while the app is in background if you
                        // disable these permissions")
                        // // .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener()
                        // {
                        // // public void onClick(DialogInterface dialog, int which) {
                        // // reactContext.startActivity(intent);
                        // // }
                        // // })
                        // // .setIcon(android.R.drawable.ic_dialog_info)
                        // // .setCancelable(false)
                        // // .show();

                        try {
                            // MIUI 8
                            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                            localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            localIntent.setData(Uri.fromParts("package", this.reactContext.getPackageName(), null));
                            localIntent.setClassName("com.miui.securitycenter",
                                    "com.miui.permcenter.permissions.PermissionsEditorActivity");
                            localIntent.putExtra("extra_pkgname", this.reactContext.getPackageName());
                            this.reactContext.startActivity(localIntent);
                            return;
                        } catch (Exception ignore) {
                        }
                        try {
                            // MIUI 5/6/7
                            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                            localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            localIntent.setData(Uri.fromParts("package", this.reactContext.getPackageName(), null));
                            localIntent.setClassName("com.miui.securitycenter",
                                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                            localIntent.putExtra("extra_pkgname", this.reactContext.getPackageName());
                            this.reactContext.startActivity(localIntent);
                            return;
                        } catch (Exception ignore) {
                        }
                        // Otherwise jump to application details
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", this.reactContext.getPackageName(), null);
                        intent.setData(uri);
                        this.reactContext.startActivity(intent);
                    } else {
                        Intent overlaySettings = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + this.reactContext.getPackageName()));
                        // reactContext.startActivityForResult(overlaySettings, 5469);
                        this.reactContext.startActivityForResult(overlaySettings, 0, null);
                    }
                }
            } else {
                promise.resolve(true);
            }
        } catch (Error e) {
            promise.reject(e);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @ReactMethod
    public void isRequestOverlayPermissionGranted(Callback booleanCallback) {
        boolean equal = !Settings.canDrawOverlays(this.reactContext);
        booleanCallback.invoke(equal);
    }

}
