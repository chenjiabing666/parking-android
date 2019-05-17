package com.example.chen.taco.mvvm;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.example.chen.taco.networkservice.ServiceMediator;
import com.example.chen.taco.networkservice.ServiceResponse;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.tasktool.Taskpool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * <br>
 * 统一管理 viewmodel的创建，与activity关联,以及doTask请求返回的handel处理</br>
 * 
 * @author 龚雪寒
 */
@SuppressLint("HandlerLeak")
@SuppressWarnings({"rawtypes" })
public class ViewModelManager {
	private static ViewModelManager vmManager;
	private HashMap<String, ViewModel> viewModelMap;
	private HashMap<String, String> viewModelPlist;

	public static ViewModelManager manager() {
		synchronized (Route.class) {
			if (vmManager == null) {
				vmManager = new ViewModelManager();
			}
			return vmManager;
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			handlerMessage(msg);
		}
	};

	public Handler getHandler() {
		return handler;
	}

	private ViewModelManager() {
		viewModelMap = new HashMap<String, ViewModel>();
	}

	private void handlerMessage(Message msg) {
		ServiceResponse response = (ServiceResponse) msg.obj;
		TaskToken token = (TaskToken) response.getTokenObj();
		String viewModelId = token.viewModelId;

		ViewModel model = viewModelForKey(viewModelId);
		if (null != model) {
			switch (msg.what) {
			case ServiceMediator.Service_Return_Success:
				model.setResponse(response);
				break;
			default:
				model.requestFailed(response);
				break;
			}
		}
	}

	/**
	 * 传入viewmodelplist中实现的，activity和viewmodel对应关系的hashmp
	 */
	public void addViewModelPlist(HashMap<String, String> map) {
		if (viewModelPlist == null) {
			viewModelPlist = map;
		} else {
			viewModelPlist.putAll(map);
		}
	}

	/**
	 * 根据activity类名，从view model plist 中获取对应的viewmodel类名，并实例化
	 * 
	 * @param classString
	 *            需要的viewmodel的activity类名
	 * @return 实例化的view model对象
	 */
	public ViewModel newViewModel(String classString) {
		String viewModelId = classString + "@"
				+ Taskpool.sharedInstance().getActivityCounter();

		ViewModel resultViewModel = viewModelMap.get(viewModelId);
		if (null == resultViewModel) {
			String strClass = viewModelPlist.get(classString);
			try {
				resultViewModel = (ViewModel) Class.forName(strClass)
						.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			resultViewModel.setViewModelId(viewModelId);
			resultViewModel.setActivityClass(classString);
			viewModelMap.put(viewModelId, resultViewModel);
		}
		return resultViewModel;
	}

	/**
	 * 根据viewModelId 获取manager中已存在对应的viewModel
	 * 
	 * @param viewModelId
	 *            一个activity的唯一标示符
	 * 
	 * @return 实例化的view model对象
	 */
	public ViewModel viewModelForKey(String viewModelId) {
		ViewModel resultViewModel = viewModelMap.get(viewModelId);
		return resultViewModel;
	}

	/**
	 * 销毁viewmodel
	 */
	public void destoryViewModel(String viewModelId) {
		viewModelMap.remove(viewModelId);
		Set<String> keys = viewModelMap.keySet();
		try {
			String startStr = viewModelId.substring(0,
					viewModelId.indexOf("@") + 1);
			for (Iterator it = keys.iterator(); it.hasNext();) {
				String key = (String) it.next();
				if (key.startsWith(startStr)) {
					viewModelMap.remove(viewModelId);
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}



