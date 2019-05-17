package com.example.chen.taco.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.chen.taco.networkservice.ServiceMediator;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共工具
 */
public class CommonUtil {
	public final static String TAG = "CommonUtil";

	private static InputMethodManager inputmanger;

	/**
	 * 判断网络状态
	 * 
	 * @param context 应用上下文
	 * @return 网络可用与否
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {

			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			// wifi网
			NetworkInfo networkInfo = connMgr
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			// 3G网
			NetworkInfo mobilWorkInfo = connMgr
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if ((networkInfo != null && networkInfo.isConnected())
					|| (mobilWorkInfo != null && mobilWorkInfo.isConnected())) {
				System.out.println("isNetworkConnected true");
				return true;
			}

		}
		return false;
	}
	
	private static int CheckSecurity(String pwd)
	{
	    /**
	     密码字符包括：小写字母、大写字母、数字、符号等；
	     这个正则会得到五个捕获组，前四个捕获组会告诉我们这个字符串包含有多少种组合（返回多少个匹配代表多少种组合）
	     如果这个字符串小于6位的话,则会得到第五个捕获组,长度为1（即强度为1），如果没有输入，就连捕获组5都不会得到（强度为0)
	     */
	    String regexString       = "^(?:([a-z])|([A-Z])|([0-9])|(.)){6,}|(.)+$" ;
	    String replaceWithString = "$1$2$3$4$5" ;
	    String replacedString    = null;
	    Pattern pattern = Pattern.compile(regexString);
	    Matcher matcher = pattern.matcher(pwd);
	    if (matcher.matches())
	    	replacedString = matcher.replaceAll(replaceWithString).replace("null", "");
	    return replacedString.length();
	}

	/**
	 * 获取密码安全级别
	 * @param password 待判断的密码
	 * @return 密码级别
	 */
	public static String levelOfPassword(String password)
	{
	    /*
	     （ 密码形态类型包含数字，符号，小写字母，大写字母）
	     强：密码长度大于6位，并且包含三种或以上的形态；
	     中：不满足“强”规则，长度大于8位，并且包含两种形态组合
	     弱，不满足“中”规则，就为弱
	     */
	    ServiceMediator.SECURITY_TYPE securityType = ServiceMediator.SECURITY_TYPE.HIGH_SECURITY;
	    if ((password.length() > 6) &&
	        (CheckSecurity(password) > 2))
	    {
	        securityType = ServiceMediator.SECURITY_TYPE.HIGH_SECURITY;
	        LogUtil.i(TAG, "密码级别为 1 高");
	    }
	    else if ((password.length() > 8) &&
	               (CheckSecurity(password) > 1))
	    {
	        securityType = ServiceMediator.SECURITY_TYPE.MEDIUM_SECURITY;
	        LogUtil.i(TAG, "密码级别为 2 中");
	    }
	    else
	    {
	        securityType = ServiceMediator.SECURITY_TYPE.LOW_SECURITY;
	        LogUtil.i(TAG, "密码级别为 3 低");
	    }
	    
	    return securityType.toString();
	}
	
	/**
	 * MD5加密
	 * @param str 待加密字符串
	 * @return MD5加密后的字符串
	 */
	public static String MD5Encrypt(String str) {
		if (str != null && str.length() > 0)
			return MD5.encrypt32(str);
		else
			return "";
	}

	/**
	 * 获取manifest的application标签下的meta-data值（若为长数字类型需先在strings.xml定义）
	 * @param context
	 * @return
	 */
	public static String getAppMetaValue(Context context, String key) {
		ApplicationInfo appInfo;
		String metaValue = "";
		try {
			appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			metaValue = appInfo.metaData.getString(key);
			if (metaValue == null || metaValue.length() <= 0) {
				return "";
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e("VersionInfo", "Exception", e);
		}
		return metaValue;
	}

	/**
	 * 判断GPS是否开启
	 * 
	 * @return
	 */
	public static boolean isGPSEnabled(Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		boolean providerEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (providerEnabled) {
			System.out.println("isGPSEnabled true");
			return true;
		} else {
			System.out.println("isGPSEnabled false");
			return false;
		}
	}

	/**
	 * 格式化价格
	 * 
	 * @param str
	 * @return
	 */
	public static String formatPrice(String str) {
		return new DecimalFormat("0.00").format(Double.valueOf(str));
	}

	/**
	 * 隐藏虚拟键盘
	 * 
	 * @param context
	 */
	public static void KeyBoardCancel(final Activity context) {
		View view = context.getWindow().peekDecorView();
		if (view != null) {
			inputmanger = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}

	}

	/**
	 * 隐藏虚拟键盘
	 * 
	 * @param editText
	 */
	public static void KeyBoardCancel(EditText editText) {
		((InputMethodManager) editText.getContext().getSystemService(
				Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 弹出软键盘
	 * 
	 * @param editText
	 */
	public static void KeyBoardPop(final EditText editText) {
		new Handler() {
			public void handleMessage(android.os.Message msg) {
				InputMethodManager inputManager = (InputMethodManager) editText
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(editText, 0);
			}
		}.sendEmptyMessageDelayed(0, 200);

	}
	
	/**
	 * cookie拼接
	 * @param url
	 * @param cookie
	 * @param context
	 */
/*	@SuppressWarnings("unused")
	public static String synCookies(String url,Cookie cookie,Context context){
		
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		if(cookie != null){
			cookieManager.setAcceptCookie(true);
			cookieManager.removeSessionCookie();
			String cookieString = cookie.getName() + "="+cookie.getValue()+";domain="+cookie.getDomain();
			cookieManager.setCookie(url, cookieString);
			LogUtil.d(TAG, "cookieString:"+cookieString);
			CookieSyncManager.getInstance().sync();
			return cookieString;
		}
		return null;
	}*/
}

