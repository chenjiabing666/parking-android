package com.example.chen.taco.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

	private static Handler handler = new Handler(Looper.getMainLooper());
	private static Toast toast = null;
	private static Object synObj = new Object();

	public static void show(final Context act, final String msg) {
		showMessage(act, msg, Toast.LENGTH_SHORT);
	}

	public static void show(final Context act, final int msg) {
		showMessage(act, msg, Toast.LENGTH_SHORT);
	}

	public static void showMessage(final Context act, final String msg,
                                   final int len) {
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						synchronized (synObj) {
							if (toast != null) {
								toast.cancel();
							}
							toast = Toast.makeText(act, msg, len);
							// 设置toast居中显示
							toast.setGravity(Gravity.CENTER, 0, 0);

							toast.show();
						}
					}
				});
			}
		}).start();
	}

	public static void showMessage(final Context act, final int msg,
                                   final int len) {
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						// synchronized (synObj) {
						// if (toast != null) {
						// toast.cancel();
						// toast.setText(msg);
						// toast.setDuration(len);
						// } else {
						toast = Toast.makeText(act, msg, len);

						// 设置toast居中显示
						toast.setGravity(Gravity.CENTER, 0, 0);
						// }
						toast.show();
						// }
					}
				});
			}
		}).start();
	}

}
