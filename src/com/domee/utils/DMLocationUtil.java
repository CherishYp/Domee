package com.domee.utils;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by duyuan on 13-5-24.
 */
public class DMLocationUtil {

    private Activity mActivity;

    private LocationManager mLoctionManager;
    private Location mLocation;
    private String mProvider;
    private double mLat;
    private double mLon;

    public DMLocationUtil(Activity mActivity) {
        this.mActivity = mActivity;
        init();
    }

    public void init() {
        mLoctionManager = (LocationManager) mActivity.getSystemService(mActivity.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
        //从可用的位置提供器中，匹配以上标准的最佳提供器
        mProvider = mLoctionManager.getBestProvider(criteria, true);
        //获得最后一次变化的位置
        mLocation = mLoctionManager.getLastKnownLocation(mProvider);

        if(mLoctionManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            mLocation = mLoctionManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(mLocation != null){
                mLat = mLocation.getLatitude();
                mLon = mLocation.getLongitude();
            }
        }else{
            LocationListener locationListener = new LocationListener() {

                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                // Provider被enable时触发此函数，比如GPS被打开
                @Override
                public void onProviderEnabled(String provider) {

                }

                // Provider被disable时触发此函数，比如GPS被关闭
                @Override
                public void onProviderDisabled(String provider) {

                }

                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        Log.e("Map", "Location changed : Lat: "
                                + location.getLatitude() + " Lng: "
                                + location.getLongitude());
                    }
                }
            };
            mLoctionManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);
            Location location = mLoctionManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                mLat = location.getLatitude(); //经度
                mLon = location.getLongitude(); //纬度
            }
        }
    }

    public double getmLat() {
        return mLat;
    }

    public double getmLon() {
        return mLon;
    }
}
