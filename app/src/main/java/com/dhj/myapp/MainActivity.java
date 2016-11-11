package com.dhj.myapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.dhj.myapp.fragment.fragment1;
import com.dhj.myapp.fragment.fragment2;
import com.dhj.myapp.fragment.fragment3;
import com.dhj.myapp.fragment.fragment4;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.rb_itme1)
    RadioButton rbItme1;
    @Bind(R.id.rb_itme2)
    RadioButton rbItme2;
    @Bind(R.id.rb_itme3)
    RadioButton rbItme3;
    @Bind(R.id.rb_itme4)
    RadioButton rbItme4;
    @Bind(R.id.fl_content)
    FrameLayout flContent;
    @Bind(R.id.bg_bottom)
    RadioGroup bgBottom;
    private long firstTime = 0;

    private Fragment f1, f2, f3, f4;
    private Fragment mFragment;

    @Override
    protected void afterCreat(Bundle savedInstanceState) {
        initFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fl_content, f1).commit();
        mFragment = f1;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void initFragment() {
        bgBottom.setOnCheckedChangeListener(this);
        f1 = new fragment1();
        f2 = new fragment2();
        f3 = new fragment3();
        f4 = new fragment4();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_itme1:
                rbItme1.setChecked(true);
                rbItme2.setChecked(false);
                rbItme3.setChecked(false);
                rbItme4.setChecked(false);
                switchFragment(f1);
                break;
            case R.id.rb_itme2:
                rbItme1.setChecked(false);
                rbItme2.setChecked(true);
                rbItme3.setChecked(false);
                rbItme4.setChecked(false);
                switchFragment(f2);
                break;
            case R.id.rb_itme3:
                rbItme1.setChecked(false);
                rbItme2.setChecked(false);
                rbItme3.setChecked(true);
                rbItme4.setChecked(false);
                switchFragment(f3);
                break;
            case R.id.rb_itme4:
                rbItme1.setChecked(false);
                rbItme2.setChecked(false);
                rbItme3.setChecked(false);
                rbItme4.setChecked(true);
                switchFragment(f4);
                break;
        }
    }

    private void switchFragment(Fragment fragment) {
        //判断当前显示的Fragment是不是切换的Fragment
        if (mFragment != fragment) {
            //判断切换的Fragment是否已经添加过
            if (!fragment.isAdded()) {
                //如果没有，则先把当前的Fragment隐藏，把切换的Fragment添加上
                getSupportFragmentManager().beginTransaction().hide(mFragment)
                        .add(R.id.fl_content, fragment).commit();
            } else {
                //如果已经添加过，则先把当前的Fragment隐藏，把切换的Fragment显示出来
                getSupportFragmentManager().beginTransaction().hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于800毫秒，则不退出
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;// 更新firstTime
                return true;
            } else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
