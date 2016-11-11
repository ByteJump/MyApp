package com.dhj.myapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.dhj.myapp.BaseFragment;
import com.dhj.myapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zzh on 2016/11/9.
 */

public class fragment1 extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.bmapView)
    MapView mapView;
    @Bind(R.id.btn1)
    RadioButton btn1;
    @Bind(R.id.btn2)
    RadioButton btn2;
    @Bind(R.id.btn3)
    RadioButton btn3;
    @Bind(R.id.btn4)
    RadioButton btn4;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    LocationClient mLocClient;
    BitmapDescriptor mCurrentMarker;
    public MyLocationListenner myListener = new MyLocationListenner();

    private MyLocationConfiguration.LocationMode mCurrentMode;
    boolean isFirstLoc = true; // 是否首次定位
    BaiduMap mBaiduMap;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment1;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mBaiduMap = mapView.getMap();
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn1.setChecked(true);
        tvLocation.setText("");
        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        mBaiduMap.setMyLocationEnabled(true);// 开启定位图层
        mLocClient = new LocationClient(getActivity()); // 定位初始化
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            tvLocation.setText(location.getAddrStr());
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mapView = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//普通地图
                break;
            case R.id.btn2:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);//卫星地图
                break;
            case R.id.btn3:
                mBaiduMap.setTrafficEnabled(true);//交通
                break;
            case R.id.btn4:
                mBaiduMap.setBaiduHeatMapEnabled(true);//热力
                break;
        }
    }
}
