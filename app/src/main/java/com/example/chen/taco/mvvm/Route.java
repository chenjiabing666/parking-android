package com.example.chen.taco.mvvm;


import android.app.Activity;
import android.content.Intent;

import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.tasktool.Taskpool;

import java.util.HashMap;

/**
 * <br> 该类用于进行activity的切换，总共定义了两种切换方式</br>
 * <p>
 * <br> 1.不包含网络请求的切换,仅是封装了普通的Intent跳转方式</br>
 * <br> 2.包含网络请求的切换,在Intent跳转的同时，另起线程进行网络请求</br>
 *
 * @author 龚雪寒
 */
public class Route {
    public static String ACTIVITY_TOKEN_KEY = "activity_token_key";
    private static Route sRoute;
    public static int WITHOUT_RESULTCODE = -1;

    public static int PERSONAL_INFO_USERICON_RESULTCODE = 1001;
    public static int PERSONAL_INFO_REQUESTCODE = 2001;

    public static Route route() {
        synchronized (Route.class) {
            if (sRoute == null) {
                sRoute = new Route();
            }
            return sRoute;
        }
    }

//	/**
//	 * 跳转到下一个activity（无网络请求）<br>
//	 * 只有在intent需要设置特殊参数时 适用
//	 * 
//	 * @param currentActivity
//	 *            当前的activity
//	 * @param viewModel
//	 *            待创建的activity相应的viewmodel
//	 * @param resultCode
//	 *            是否关心返回，默认为 WITHOUT_RESULTCODE = -1
//	 * @param intent
//	 *            跳转的intent
//	 */
//	public void nextControllerWithIntent(Activity currentActivity,
//			ViewModel viewModel, int resultCode, Intent intent) {
//		viewModel.showProgressBar = false;
//		intent.putExtra(ACTIVITY_TOKEN_KEY, viewModel.getViewModelId());
//		if (resultCode == WITHOUT_RESULTCODE) {
//			currentActivity.startActivity(intent);
//		} else {
//			currentActivity.startActivityForResult(intent, resultCode);
//		}
//	}
//
//	/**
//	 * 跳转到下一个activity（带网络请求）<br>
//	 * 只有在intent需要设置特殊参数时 适用
//	 * 
//	 * @param currentActivity
//	 *            当前的activity
//	 * @param viewModel
//	 *            待创建的activity相应的viewmodel
//	 * @param resultCode
//	 *            是否关心返回，默认为 WITHOUT_RESULTCODE = -1
//	 * @param method
//	 *            跳转需要的网络请求
//	 * @param intent
//	 *            跳转的intent
//	 */
//	public void nextControllerWithIntent(Activity currentActivity,
//			ViewModel viewModel, int resultCode, String method, Intent intent) {
//		if (method != null) {
//			TaskToken token = Taskpool.sharedInstance().doTask(viewModel,
//					method);
//			viewModel.setTaskToken(token);
//			viewModel.showProgressBar = true;
//		}
//		intent.putExtra(ACTIVITY_TOKEN_KEY, viewModel.getViewModelId());
//
//		if (resultCode == WITHOUT_RESULTCODE) {
//			currentActivity.startActivity(intent);
//		} else {
//			currentActivity.startActivityForResult(intent, resultCode);
//		}
//	}
//	
//	/**
//	 * 跳转到下一个activity（无网络请求）
//	 * 
//	 * @param viewModel
//	 *            待创建的activity相应的viewmodel
//	 * @param resultCode
//	 *            是否关心返回，默认为 WITHOUT_RESULTCODE = -1
//	 * @return 返回一个controller实例
//	 */
//	public void nextController(Activity currentActivity, ViewModel viewModel,
//			int resultCode) {
//
//		Intent intent;
//		try {
//			intent = new Intent(currentActivity, Class.forName(viewModel
//					.getActivityClass()));
//			viewModel.showProgressBar = false;
//			intent.putExtra(ACTIVITY_TOKEN_KEY, viewModel.getViewModelId());
//
//			if (resultCode == WITHOUT_RESULTCODE) {
//				currentActivity.startActivity(intent);
//			} else {
//				currentActivity.startActivityForResult(intent, resultCode);
//			}
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 跳转到下一个activity（带网络请求）
//	 * 
//	 * @param viewModel
//	 *            待创建的activity相应的viewmodel
//	 * @param method
//	 *            跳转需要的网络请求
//	 * @param resultCode
//	 *            是否关心返回，默认为 WITHOUT_RESULTCODE = -1
//	 * @return 返回一个controller实例
//	 */
//	public void nextController(Activity currentActivity, ViewModel viewModel,
//			int resultCode, String method) {
//		try {
//			if (method != null) {
//				TaskToken token = Taskpool.sharedInstance().doTask(viewModel, method);
//				viewModel.setTaskToken(token);
//				viewModel.showProgressBar = true;
//			}
//			
//			Intent intent = new Intent(currentActivity, Class.forName(viewModel
//					.getActivityClass()));
//			intent.putExtra(ACTIVITY_TOKEN_KEY, viewModel.getViewModelId());
//
//			if (resultCode == WITHOUT_RESULTCODE) {
//				currentActivity.startActivity(intent);
//			} else {
//				currentActivity.startActivityForResult(intent, resultCode);
//			}
//		} catch (ClassNotFoundException e1) {
//			e1.printStackTrace();
//		}
//	}

    /**
     * 跳转到下一个activity（无网络请求）
     * <p>
     * 待创建的activity相应的viewmodel
     *
     * @param resultCode 是否关心返回，默认为 WITHOUT_RESULTCODE = -1
     * @return 返回一个controller实例
     */
    public void nextController(Activity currentActivity, String nextClassName,
                               int resultCode) {
        ViewModel viewModel = ViewModelManager.manager().newViewModel(nextClassName);
        Intent intent;
        try {
            intent = new Intent(currentActivity, Class.forName(nextClassName));
            viewModel.showProgressBar = false;
            intent.putExtra(ACTIVITY_TOKEN_KEY, viewModel.getViewModelId());
            if (resultCode == WITHOUT_RESULTCODE) {
                currentActivity.startActivity(intent);
            } else {
                currentActivity.startActivityForResult(intent, resultCode);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转到下一个activity（带网络请求）
     * <p>
     * 待创建的activity相应的viewmodel
     *
     * @param method     跳转需要的网络请求
     * @param resultCode 是否关心返回，默认为 WITHOUT_RESULTCODE = -1
     * @return 返回一个controller实例
     */
    public void nextController(Activity currentActivity, String nextClassName, HashMap<String, ?> parameters,
                               int resultCode, String method, String flag) {
        ViewModel viewModel = ViewModelManager.manager().newViewModel(nextClassName);
        viewModel.parameters = parameters;
        try {
            if (method != null) {
                TaskToken token = Taskpool.sharedInstance().doTask(viewModel, method, parameters, flag);
                viewModel.setTaskToken(token);
                viewModel.showProgressBar = true;
            }
            Intent intent = new Intent(currentActivity, Class.forName(nextClassName));
            intent.putExtra(ACTIVITY_TOKEN_KEY, viewModel.getViewModelId());
            if (resultCode == WITHOUT_RESULTCODE) {
                currentActivity.startActivity(intent);
            } else {
                currentActivity.startActivityForResult(intent, resultCode);
            }
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * 跳转到下一个activity（无网络请求）<br>
     * 只有在intent需要设置特殊参数时 适用
     *
     * @param currentActivity 当前的activity
     *                        待创建的activity相应的viewmodel
     * @param resultCode      是否关心返回，默认为 WITHOUT_RESULTCODE = -1
     * @param intent          跳转的intent
     */
    public void nextControllerWithIntent(Activity currentActivity,
                                         String nextClassName, int resultCode, Intent intent) {
        ViewModel viewModel = ViewModelManager.manager().newViewModel(
                nextClassName);
        viewModel.showProgressBar = false;
        intent.putExtra(ACTIVITY_TOKEN_KEY, viewModel.getViewModelId());
        if (resultCode == WITHOUT_RESULTCODE) {
            currentActivity.startActivity(intent);
        } else {
            currentActivity.startActivityForResult(intent, resultCode);
        }
    }


    /**
     * 跳转到下一个activity（带网络请求）<br>
     * 只有在intent需要设置特殊参数时 适用
     *
     * @param currentActivity 当前的activity
     *                        待创建的activity相应的viewmodel
     * @param resultCode      是否关心返回，默认为 WITHOUT_RESULTCODE = -1
     * @param method          跳转需要的网络请求
     * @param intent          跳转的intent
     */
    public void nextControllerWithIntent(Activity currentActivity,
                                         String nextClassName, HashMap<String, ?> parameters,
                                         int resultCode, String method, Intent intent, String flag) {

        ViewModel viewModel = ViewModelManager.manager().newViewModel(
                nextClassName);
        viewModel.parameters = parameters;
        if (method != null) {
            TaskToken token = Taskpool.sharedInstance().doTask(viewModel,
                    method, parameters, flag);
            viewModel.setTaskToken(token);
            viewModel.showProgressBar = true;
        }

        intent.putExtra(ACTIVITY_TOKEN_KEY, viewModel.getViewModelId());
        if (resultCode == WITHOUT_RESULTCODE) {
            currentActivity.startActivity(intent);
        } else {
            currentActivity.startActivityForResult(intent, resultCode);
        }
    }


}
