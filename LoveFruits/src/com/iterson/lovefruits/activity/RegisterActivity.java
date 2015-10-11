package com.iterson.lovefruits.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.iterson.lovefruits.R;

public class RegisterActivity extends Activity {
	private EditText etName;
	private EditText etPwd;
	private EditText etRePwd;
	private Button btnRegister;
	private Button btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initView();// 初始化界面

		btnOnclick();// 按键逻辑

	}

	/**
	 * 按键
	 */
	private void btnOnclick() {
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(){
					public void run() {
						
						
						
					};
				}.start();
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(RegisterActivity.this,
						LoginActivity.class));
				finish();
			}
		});
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		etName = (EditText) findViewById(R.id.et_register_name);
		etPwd = (EditText) findViewById(R.id.et_register_pwd);
		etRePwd = (EditText) findViewById(R.id.et_register_repwd);
		btnRegister = (Button) findViewById(R.id.btn_register_register);
		btnCancel = (Button) findViewById(R.id.btn_register_cancel);

	}

}
