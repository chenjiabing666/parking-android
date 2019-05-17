package com.example.chen.taco.mvvm;

import android.app.Activity;

import com.example.chen.taco.networkservice.ServiceMediator;
import com.example.chen.taco.networkservice.ServiceResponse;
import com.example.chen.taco.tasktool.TaskToken;
import com.example.chen.taco.utils.LogUtil;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <br>
 * 每一个activity绑定一个viewmodel，将界面和数据分离， 所有的数据处理和传递都通过activity之间的viewmodel完成。</br>
 * 
 * <br>
 * 每个项目中的BaseViewModel都会将所有的数据请求的参数和返回值定义为属性</br>
 * 
 * @author 龚雪寒
 * 
 */	
@SuppressWarnings("rawtypes")
public abstract class ViewModel {
	private String viewModelId;
	private String activityClassName;
	private TaskToken taskToken;
	public Boolean showProgressBar;
	private Activity activity;
	protected ServiceResponse response;
	public AtomicBoolean isDataReady = new AtomicBoolean(false); //数据是否准备
	public AtomicBoolean isRefreshed = new AtomicBoolean(false); //是否已经进行过刷新操作，防止重复操作
	public AtomicBoolean isFailed = new AtomicBoolean(false);
	public HashMap parameters;
	
	public ServiceResponse getResponse() {
		return response;
	}

	public void setResponse(ServiceResponse response) {
		this.response = response;
		LogUtil.e("whx","set response");
		isDataReady.set(true);
		paddingResult((TaskToken)response.getTokenObj());
		if (null != this.activity) {
				((Controller) activity).refreshData((TaskToken)response
						.getTokenObj());
		} else {
			LogUtil.e("whx","Activity not bind for viewmodel " + this.viewModelId + " or the binded activity is not started!");
		}
	}
	
	/**
	 * 根据方法名，回填返回值数据。如果不需要activity，可以在这里处理返回值，例如填UserCenter可放在BaseViewModel中
	 * 
	 * @param token 
	 */
	public abstract void paddingResult(TaskToken token);
	
	/**
	 * called when requestFailed, before activity requestFailedHandler()
	 * 
	 * @param token 
	 */
	public void preFailedHandle(TaskToken token){
		
	}
	
	/**
	 * 设置ServiceMediator
	 * 
//	 * @param token
	 */
	public abstract ServiceMediator getServiceMediator();
	

	public void requestFailed(ServiceResponse response) {
		this.response = response;
		isFailed.set(true);
		preFailedHandle((TaskToken) response.getTokenObj());
		if (null != this.activity) {
				((Controller) activity).requestFailedHandle(
						(TaskToken)response.getTokenObj(), response.getReturnCode(),
						response.getReturnDesc());
		}else{
			LogUtil.e("whx", "requestFailed,but activity is null");
		}
	}

	public void setViewModelId(String viewModelId) {
		this.viewModelId = viewModelId;
	}

	public String getViewModelId() {
		return viewModelId;
	}

	public void setActivityClass(String className) {
		activityClassName = className;
	}

	public String getActivityClass() {
		return activityClassName;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setTaskToken(TaskToken token) {
		this.taskToken = token;
	}

	public TaskToken getTaskToken() {
		return this.taskToken;
	}
}