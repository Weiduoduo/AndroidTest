package com.check.iptv;

import java.util.List;
import android.R.string;
import android.app.Activity;
import android.app.DevInfoManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	private TextView mTitle,mModel,mMunf, mTextView1,mTextView2,mTextView3;
	private Button btn_exit,btn_start;
	private Context mContext;
	private static String amtTtitle="中间件所属公司：成都卓影科技";
	private static boolean isAMT=false;
	//新版本IPTV数据模块AIDL服务
	private static final String AmtService="com.amt.amtdata.dataI.AmtDataAIdlService";
	private static final String AmtServiceAction="com.amt.dataservice";
	//老版本IPTV数据模块AIDL服务
	private static final String SyService="com.amt.amtdata.dataI.ServiceCfgService";
	private static final String SyServiceAction="com.android.smart.terminal.iptv.aidl.ServiceCfg";
	private static final String packageName="com.android.smart.terminal.iptv";
	public static final String TAG="Deng";
	//test git
	Handler mhandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Log.i("Deng", "deal handle -->"+isAMT);
			
			if (isAMT) {
				mTitle.setText(amtTtitle);
			}
			mTextView1.setVisibility(View.VISIBLE);
			mTextView2.setVisibility(View.VISIBLE);
			mTextView3.setVisibility(View.VISIBLE);
			mTitle.setVisibility(View.VISIBLE);
			mModel.setVisibility(View.VISIBLE);
			mMunf.setVisibility(View.VISIBLE);
			btn_start.setVisibility(View.INVISIBLE);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		
		mContext =this;
		mTitle =(TextView) findViewById(R.id.zhuoyingTile);
		mModel=(TextView) findViewById(R.id.model);
		mMunf =(TextView) findViewById(R.id.maunfac);
		btn_exit=(Button) findViewById(R.id.btn_exit);
		mTextView1=(TextView) findViewById(R.id.textView1);
		mTextView2=(TextView) findViewById(R.id.textView2);
		mTextView3=(TextView) findViewById(R.id.textView3);
		btn_start=(Button) findViewById(R.id.btn_start);
		btn_exit.setOnClickListener(this);
		btn_start.setOnClickListener(this);
		initView();
		init();
		//设置默认光标
		btn_start.requestFocus();
		
	}

	/**
	 * 初始化View add djf 20180111
	 */
	private void initView() {
		// TODO Auto-generated method stub
		mTextView1.setVisibility(View.INVISIBLE);
		mTextView2.setVisibility(View.INVISIBLE);
		mTextView3.setVisibility(View.INVISIBLE);
		mTitle.setVisibility(View.INVISIBLE);
		mModel.setVisibility(View.INVISIBLE);
		mMunf.setVisibility(View.INVISIBLE);
		
	}

	private void init() {
		Log.i(TAG,"inti " );
		// TODO Auto-generated method stub
		DevInfoManager mDevInfoManager=(DevInfoManager) getSystemService("devinfo_data");
		String model=android.os.Build.MODEL;
		String munf=android.os.Build.MANUFACTURER;
		if (mDevInfoManager ==null) {
			Log.i(TAG,"mDevInfoManager is null" );
			return;
		}
	//	model=mDevInfoManager.getValue("DevInfoManager.MODEL");
		Log.i(TAG,"1807 model-->"+model+"   &&munf  -->"+munf );
		mModel.setText(model);
		mMunf.setText(munf);
	}

	/***
	 * APK是否存在
	 * 
	 * @param packageName
	 * @return
	 */
	public boolean isAppInstalled(String packageName) {
		// packageName = "com.android.browser";
		boolean isInstall = false;
		if (TextUtils.isEmpty(packageName))
			isInstall = false;
		try {
			ApplicationInfo info = mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			isInstall = true;
			Log.i("Deng","ApplicationInfo-->" + info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("Deng","isAppInstalled-->" + packageName + ":" + isInstall);
		return isInstall;
	}

	/**
	 * 判断AIDL是否存在
	 * 
	 * @param context
	 * @param flag
	 *            PackageManager.GET_SERVICES
	 * @param action
	 * @return
	 */
	public static boolean isExisting(Context context, int flags, String action) {
		Log.i("Deng", "isExisting");
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent(action);
		List<ResolveInfo> resolveInfo = null;
		switch (flags) {
		case PackageManager.GET_ACTIVITIES:
			resolveInfo = packageManager.queryIntentActivities(intent, flags);
			break;

		case PackageManager.GET_SERVICES:
			resolveInfo = packageManager.queryIntentServices(intent, flags);
			break;
		case PackageManager.GET_RECEIVERS:
			resolveInfo = packageManager.queryBroadcastReceivers(intent, flags);
			break;
		}

		if (resolveInfo != null && resolveInfo.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_exit:
			finish();
			break;
		case R.id.btn_start:
		//	mhandler.sendEmptyMessage(1);
			if ((isExisting(mContext, PackageManager.GET_SERVICES, AmtServiceAction)
					||isExisting(mContext, PackageManager.GET_SERVICES, SyServiceAction))
					&& isAppInstalled( packageName) ){
				Log.i("Deng", "this is  AMT IPTV");
				isAMT =true;				
			}
			Log.i("Deng", "this is not AMT IPTV");
				mhandler.sendEmptyMessage(1);
			break;
		default:
			break;
		}
	}

}
