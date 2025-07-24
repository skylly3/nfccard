package com.sinpo.xnfc.nfc.reader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Contacts;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Build;
//import android.os.Build.VERSION;
/**
 * 主要功能:手机管理工具类
 */

public class AppPhoneMgr {
    private static AppPhoneMgr phoneUtil;

    public static AppPhoneMgr getInstance() {
        if (phoneUtil == null) {
            synchronized (AppPhoneMgr.class) {
                if (phoneUtil == null) {
                    phoneUtil = new AppPhoneMgr();
                }
            }
        }
        return phoneUtil;
    }

    /**
     * 获取手机系统版本号
     *
     * @return
     */
    public Integer getSDKVersionNumber() {
        Integer sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            sdkVersion = Integer.valueOf(0);
        }
        return sdkVersion;
    }
    public   String getOsVersion()
    {
      return Build.VERSION.RELEASE;
    }
    
    public static String getPhoneBrand()
    {
      return Build.BRAND;
    }
    
    /**
     * 获取手机型号
     */
    public String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机宽度
     */
    @SuppressWarnings("deprecation")
    public int getPhoneWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取手机高度
     *
     * @param context
     */
    @SuppressWarnings("deprecation")
    public int getPhoneHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 获取手机imei串号 ,GSM手机的 IMEI 和 CDMA手机的 MEID.
     *
     * @param context
     */
    public String getPhoneImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null)
            return null;
        return tm.getDeviceId();
    }

    /**
     * 获取手机sim卡号
     *
     * @param context
     */
    public String getPhoneSim(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null)
            return null;
        return tm.getSubscriberId();
    }

    /**
     * 获取手机号
     *
     * @param context
     */
    public String getPhoneNum(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null)
            return null;
        String strRet =  tm.getLine1Number();
        int len = strRet.length();
        if (len > 11) {
        	strRet = strRet.substring(len - 11);
        } 
        return strRet;
    }
    
    //获取iccid
    public String getIccid(Context context)
    {
    	  TelephonyManager tm = (TelephonyManager) context
                  .getSystemService(Context.TELEPHONY_SERVICE);
          if (tm == null)
              return null;
      return tm.getSimSerialNumber();
    }

    /**
     * 判断sd卡是否挂载
     */
    public boolean isSDCardMount() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取sd卡剩余空间的大小
     */
    @SuppressWarnings("deprecation")
    public long getSDFreeSize() {
        File path = Environment.getExternalStorageDirectory(); // 取得SD卡文件路径
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize(); // 获取单个数据块的大小(Byte)
        long freeBlocks = sf.getAvailableBlocks();// 空闲的数据块的数量
        // 返回SD卡空闲大小
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 获取sd卡空间的总大小
     */
    @SuppressWarnings("deprecation")
    public long getSDAllSize() {
        File path = Environment.getExternalStorageDirectory(); // 取得SD卡文件路径
        StatFs sf = new StatFs(path.getPath());
        long blockSize = sf.getBlockSize(); // 获取单个数据块的大小(Byte)
        long allBlocks = sf.getBlockCount(); // 获取所有数据块数
        // 返回SD卡大小
        return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 判断是否是平板
     */
    public boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 判断一个apk是否安装
     *
     * @param context
     * @param packageName
     */
    public boolean isApkInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            return false;
        }
        return true;
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phoneNum
     */
    public void call(Context context, String phoneNum) throws Exception {
        if (phoneNum != null && !phoneNum.equals("")) {
            Uri uri = Uri.parse("tel:" + phoneNum);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转到联系人界面
     */

    public void toChooseContactsList(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Contacts.People.CONTENT_URI);
        context.startActivity(intent);
    }

    /**
     * 发送短信界面  ,现在好像不行了
     */
    public void toSendMessageActivity(Context context, String number) {
        try {
            Uri uri = Uri.parse("smsto:" + number);
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 打开网页
     */
    public void openWeb(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取应用权限 名称列表
     */
    public String[] getAppPermissions(Context context)
            throws NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(),
                PackageManager.GET_PERMISSIONS);
        return getAppPermissions(packageInfo);
    }

    public String[] getAppPermissions(PackageInfo packageInfo)
            throws NameNotFoundException {
        return packageInfo.requestedPermissions;
    }

    /**
     * 获取手机内安装的应用
     */
    public List<PackageInfo> getInstalledApp(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.getInstalledPackages(0);
    }
    
    public   int getVersionCode(Context paramContext)
    {
      // 假装鹏淘最新版3.06
      return 306;
//      try
//      {
//        int i = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0).versionCode;
//        return i;
//      }
//      catch (PackageManager.NameNotFoundException ee)
//      {
//        ee.printStackTrace();
//      }
 //     return 0;
    }
    /**
     * 获取手机安装非系统应用
     */
    @SuppressWarnings("static-access")
    public List<PackageInfo> getUserInstalledApp(Context context) {
        List<PackageInfo> infos = getInstalledApp(context);
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        for (PackageInfo info : infos) {
            if ((info.applicationInfo.flags & info.applicationInfo.FLAG_SYSTEM) <= 0) {
                apps.add(info);
            }
        }
        infos.clear();
        infos = null;
        return apps;
    }

    /**
     * 获取安装应用的信息
     */
    public Map<String, Object> getInstalledAppInfo(Context context,
                                                   PackageInfo info) {
        Map<String, Object> appInfos = new HashMap<String, Object>();
        PackageManager pm = context.getPackageManager();
        ApplicationInfo aif = info.applicationInfo;
        appInfos.put("icon", pm.getApplicationIcon(aif));
        appInfos.put("lable", pm.getApplicationLabel(aif));
        appInfos.put("packageName", aif.packageName);
        return appInfos;
    }

    /**
     * 打开指定包名的应用
     */
    public void startAppPkg(Context context, String pkg) {
        Intent startIntent = context.getPackageManager()
                .getLaunchIntentForPackage(pkg);
        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startIntent);
    }

    /**
     * 卸载指定包名的应用
     */
    public void unInstallApk(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 手机号判断
     */
    public static boolean isMobileNO(String mobile) {
        Pattern p = Pattern
                .compile("^((145|147)|(15[^4])|(17[0-9])|((13|18)[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }
}
