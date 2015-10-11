package com.iterson.lovefruits.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iterson.lovefruits.R;
import com.iterson.lovefruits.utils.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;



public class SplashActivity extends Activity {
	private static final String PATH = "http://171.94.204.137:8080/update.json";
	private String versionName;
	private int versonCode;
	private String versionDesc;
	private String dowloadUrl;
	private ProgressBar pbUpdata;
	private TextView tvUpdata;
	private TextView tvVersion;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 0:
				enterMainActivity();
				break;
			case 1:
				showUpdateDialog();
				break;
			case 2:
			pbUpdata.setVisibility(View.VISIBLE);
			tvUpdata.setVisibility(View.VISIBLE);
			ProgressUpdata updata = new ProgressUpdata();
			updata =(ProgressUpdata) msg.obj;
			pbUpdata.setMax(updata.total);
			pbUpdata.setProgress(updata.current);
			break;

			default:
				break;
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvUpdata = (TextView) findViewById(R.id.tv_splash_updata);
		pbUpdata = (ProgressBar) findViewById(R.id.pb_splash_updata);
		

		checkVersion();

	}

	/**
	 * 联网操作
	 */

	private void checkVersion() {
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				long startTime = System.currentTimeMillis();
				try {
					URL url = new URL(PATH);
					@SuppressWarnings("deprecation")
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setRequestMethod("GET");
					connection.setReadTimeout(2000);
					connection.setConnectTimeout(2000);
					int code = connection.getResponseCode();
					if (code == 200) {
						
						InputStream stream = connection.getInputStream();
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						byte[] buffer = new byte[1024];
						int len = 0;
						while ((len = stream.read(buffer)) != -1) {
							out.write(buffer, 0, len);
						}
						out.close();
						stream.close();
						String jsonData = out.toString();
						JSONObject json = new JSONObject(jsonData);
						versionName = json.getString("versionName");
						versonCode = json.getInt("versionCode");
						versionDesc = json.getString("description");
						dowloadUrl = json.getString("downloadUrl");
						
					}
					long endTime = System.currentTimeMillis();
					PackageManager pm = getPackageManager();
					PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
					int infoCode = info.versionCode;
					if (versonCode > infoCode) {//如果当前版本小于服务器版本
						mHandler.sendEmptyMessage(1);
					}else {//否则跳转主页面
						try {
							Thread.sleep(2000-startTime+startTime);
							mHandler.sendEmptyMessage(0);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
					
				} catch (MalformedURLException e) {
					// URL异常
					mHandler.sendEmptyMessage(0);//出现异常跳转主页面
					e.printStackTrace();
				} catch (IOException e) {
					// 连接异常
					mHandler.sendEmptyMessage(0);//出现异常跳转主页面
					e.printStackTrace();
				} catch (JSONException e) {
					// JSON读取异常
					mHandler.sendEmptyMessage(0);//出现异常跳转主页面
					e.printStackTrace();
				} catch (NameNotFoundException e) {
					// 获取包信息异常
					mHandler.sendEmptyMessage(0);//出现异常跳转主页面
					e.printStackTrace();
				}

			};
		}.start();
	}

	/*
	 * 更新
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("有新版本可以更新啦！");
		builder.setMessage("新版本内容：/n" + versionDesc);
		builder.setPositiveButton("立即更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				HttpUtils http = new HttpUtils();
				@SuppressWarnings("rawtypes")
				HttpHandler handler = http.download(dowloadUrl,
						Environment.getExternalStorageDirectory()
								+ "/lovefruits.apk",
						new RequestCallBack<File>() {
							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								ToastUtils.showShort(getApplicationContext(), "下载失败");
								
							}
							@Override
							public void onSuccess(ResponseInfo<File> responseInfo) {
								String apkPath = responseInfo.result.getPath();
								//安装apk
								Intent intent = new Intent();
								intent.setAction(Intent.ACTION_VIEW);
								intent.addCategory(Intent.CATEGORY_DEFAULT);
								intent.setDataAndType(Uri.fromFile(responseInfo.result),"application/vnd.android.package-archive" );
								startActivityForResult(intent, 0);
								
							}
							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								Message msg =Message.obtain();
								ProgressUpdata updata = new ProgressUpdata();
								updata.total =(int) total;
								updata.current = (int) current;
								msg.obj =updata;
								msg.what=2;
								mHandler.sendMessage(msg);
								super.onLoading(total, current, isUploading);
							}
						});
			}
		});
		
		builder.setNegativeButton("以后再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				mHandler.sendEmptyMessage(0);
			}
		});
		builder.show();

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		enterMainActivity();
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 跳转主页面
	 */
	private void enterMainActivity() {
		startActivity(new Intent(SplashActivity.this,MainActivity.class));
		finish();
	}
	class ProgressUpdata{
		public int total;
		public int current;
	}
}
