package com.mdground.guest;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.graphics.Bitmap;

import com.mdground.guest.api.MdgAppliction;
import com.mdground.guest.bean.Clinic;
import com.mdground.guest.bean.Employee;
import com.mdground.guest.util.MdgConfig;
import com.mdground.guest.util.TencentXgPush;
import com.mdground.guest.util.YiDeGuanImageDownloader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class MedicalAppliction extends MdgAppliction {

    public static Employee mLoginEmployee;// 登陆用户

    public static Clinic mClinic; // 诊所信息

    public MedicalAppliction() {
    }

    public Employee getLoginEmployee() {
        return mLoginEmployee;
    }

    public void setLoginEmployee(Employee employee) {
        mLoginEmployee = employee;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initImageLoader();
        // 初始化信鸽推送
        initTencentXgPush();
    }

    /**
     * 初始化Image缓存
     */
    public void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().delayBeforeLoading(150).bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true).build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .imageDownloader(new YiDeGuanImageDownloader(getApplicationContext())).defaultDisplayImageOptions(options).build();

        ImageLoader.getInstance().init(configuration);
    }

    /**
     * 初始化信鸽推送
     */
    private void initTencentXgPush() {
        // TencentXgPush.config(this, MedicalConstant.XG_V2_ACCESS_ID,
        // MedicalConstant.XG_V2_ACCESS_KEY);
        TencentXgPush.registerPush(this);
//        TencentXgPush.customPushNotifyLayout(this);
    }

    public boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getDeviceId() {
        int deviceId = MdgConfig.getDeviceId();
        return deviceId;
    }

    @Override
    public String getDeviceToken() {
        return "";
    }

}
