package com.iterson.lovefruits.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	public static void showLong(Context context , String string){
		Toast.makeText(context, string, Toast.LENGTH_LONG).show();
	}
	public static void showShort(Context context , String string){
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}
}
