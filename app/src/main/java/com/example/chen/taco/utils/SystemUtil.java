package com.example.chen.taco.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class SystemUtil {
	private static String sdcarddircache;

	/** ��ȡ�洢��·�� */
	public static String getSdCardDir() {
		if (sdcarddircache != null) {
			return sdcarddircache;
		}
		String rt = "/sdcard/";
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File dir = Environment.getExternalStorageDirectory();
				if (dir != null) {
					String dirstr = dir.getAbsolutePath();
					if (dirstr.endsWith("/")) {
						rt = dirstr;
					} else {
						rt = dirstr + "/";
					}
				}
			}
		} catch (Exception ex) {
		}
		sdcarddircache = rt;
		return rt;
	}

	/** �жϴ洢���Ƿ���� */
	public static boolean isSdCardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * @return ����sdcard��·��
	 */
	public static final String getDatabasePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public static float DpTopixel(Context context, float val) {
		float density = context.getResources().getDisplayMetrics().density;
		return val * density;
	}


	/** �鿴�����Ƿ���� */
	public static boolean isConnect(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean checkAliPayInstalled(Context context) {

		Uri uri = Uri.parse("alipays://platformapi/startApp");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		ComponentName componentName = intent.resolveActivity(context.getPackageManager());
		return componentName != null;
	}


	/** get the current app_version */
	public static double getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double version = packInfo.versionCode;
		return version;
	}

	/**
	 * ��ȡ�汾����
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String pkName = null, versionName = null;
		int versionCode;
		try {
			// ����
			pkName = context.getPackageName();
			// �汾��Ϣ
			versionName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;
			// �汾��
			versionCode = context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
			// return pkName + "   " + versionName + "  " + versionCode;
			Log.i("", "----pkName=" + pkName + "///versionName=" + versionName
					+ "///versionCode=" + versionCode);
			return versionName;
		} catch (Exception e) {
		}
		return versionName;
	}
	
	/**
	 * ��ȡӦ�ð���
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppPackageName(Context context) {
		String pkName = null, versionName = null;
		int versionCode;
		try {
			// ����
			pkName = context.getPackageName();
			// �汾��Ϣ
			versionName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;
			// �汾��
			versionCode = context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
			// return pkName + "   " + versionName + "  " + versionCode;
			Log.i("", "----pkName=" + pkName + "///versionName=" + versionName
					+ "///versionCode=" + versionCode);
			return pkName;
		} catch (Exception e) {
		}
		return pkName;
	}

	// ѹ��ͼƬ������
	public static String compressBmpToFile(Bitmap bmp, File file) {
		String photoCode = "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		bmp.compress(Bitmap.CompressFormat.PNG, options, baos);
		byte[] b = baos.toByteArray(); // ��ͼƬ�����ַ�����ʽ�洢����
		// while (baos.toByteArray().length / 1024 > 200)
		// {
		// baos.reset();
		// options -= 10;
		// bmp.compress(Bitmap.CompressFormat.PNG, options, baos);
		// }
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return photoCode;
	}

	// ѹ��ͼƬ���ڴ�
	public static Bitmap compressImageFromFile(String srcPath, Context context) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true; // ֻ����,��������
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float ww = 100 * (int) context.getResources().getDisplayMetrics().density;
		float hh = ww;
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0) {
			be = 1;
		}
		newOpts.inSampleSize = be; // ���ò�����

		newOpts.inPreferredConfig = Config.ARGB_8888; // ��ģʽ��Ĭ�ϵ�,�ɲ���
		newOpts.inPurgeable = true; // ͬʱ���òŻ���Ч
		newOpts.inInputShareable = true; // ��ϵͳ�ڴ治��ʱ��ͼƬ�Զ�������
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	/**
	 * * ��ȡͼƬ���ԣ���ת�ĽǶ� *
	 * 
	 * @param path
	 *            ͼƬ����·�� *
	 * @return degree��ת�ĽǶ�
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/*
	 * ��תͼƬ
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// ��תͼƬ ����
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// �����µ�ͼƬ
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public static void toastCode(Context context, int code) {
		switch (code) {

		default:
			break;
		}
	}

	public static int getScreenWidth(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		float density = dm.density;
		int width = dm.widthPixels;
		return width;
	}

	public static int getScreenHeight(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		float density = dm.density;
		int height = dm.heightPixels;
		return height;
	}

	private static boolean checkDeviceHasNavigationBar(Context context) {

		boolean hasNavigationBar = false;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
		if (id > 0) {
			hasNavigationBar = rs.getBoolean(id);
		}
		try {
			Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
			Method m = systemPropertiesClass.getMethod("get", String.class);
			String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
			if ("1".equals(navBarOverride)) {
				hasNavigationBar = false;
			} else if ("0".equals(navBarOverride)) {
				hasNavigationBar = true;
			}
		} catch (Exception e) {
			Log.w("hasNavigationBar", e);
		}
		return hasNavigationBar;
	}

	public static int getNavigationBarHeight(Context context) {
		int navigationBarHeight = 0;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
		if (id > 0 && checkDeviceHasNavigationBar(context)) {
			navigationBarHeight = rs.getDimensionPixelSize(id);
		}
		return navigationBarHeight;
	}


	/**
	 * 跳转到权限设置界面
	 */
	public static void toAppSetting(Context context) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= 9) {
			intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			intent.setData(Uri.fromParts("package", context.getPackageName(), null));
		} else if (Build.VERSION.SDK_INT <= 8) {
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
			intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
		}
		context.startActivity(intent);
	}


}
