package com.iterson.lovefruits.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.iterson.lovefruits.R;
import com.iterson.lovefruits.utils.MD5Utils;
import com.lidroid.xutils.cache.MD5FileNameGenerator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText etUserName;
	private EditText etPassWord;
	private CheckBox cbBuyer;
	private CheckBox cbSeller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();// 初始化布局

	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		etUserName = (EditText) findViewById(R.id.et_login_username);
		etPassWord = (EditText) findViewById(R.id.et_login_password);
		cbBuyer = (CheckBox) findViewById(R.id.cb_login_buyer);
		cbSeller = (CheckBox) findViewById(R.id.cb_login_seller);

	}

	/**
	 * 按键的功能
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login://登录
			String userName = etUserName.getText().toString().trim();
			String passWord = etPassWord.getText().toString().trim();
			String md5PassWord = MD5Utils.parse(passWord);
			
			break;

		case R.id.register://注册
			startActivity(new Intent(this,RegisterActivity.class));
			
			finish();
			break;
		case R.id.traveler://游客访问
			startActivity(new Intent(this,MainActivity.class));
			finish();
			break;

		default:
			break;
		}

	}

}
