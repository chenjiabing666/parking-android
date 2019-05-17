package com.example.chen.taco.mvvm;



import com.example.chen.taco.tasktool.TaskToken;

import java.util.HashMap;

public interface Controller {

	/**
	 * 设置viewModle
	 */
	public void setViewModel(ViewModel viewModel);

	/**
	 * 获得viewModle
	 */
	public ViewModel getViewModel();

	/**
	 * 网络请求成功，通知界面刷新
	 */
	public void refreshData(TaskToken token);

	/**
	 * 网络请求失败，将错误码和错误信息返回自行处理
	 */
	public void requestFailedHandle(TaskToken token, int errorCode,
                                    String errorMsg);

	/**
	 * 网络请求，实现还是调用的SCTaskPool的逻辑，重载的目的是将每个请求绑定在对应的controller上面，
	 * 方便在退出activity的时候能够统一的取消该controller中所有的网络请求。
	 */
//	public TaskToken doTask(ViewModel viewModel, String method);
	public TaskToken doTask(String method, HashMap<String, ?> parameters) ;
	public TaskToken doTask(String method, HashMap<String, ?> parameters, String flag) ;

	/**
	 * activity已经绑定了viewmodel，可以直接调用baseviewmodel获取viewmodel
	 */
	public void alreadyBindBaseViewModel();
	
	
	public void showToast(String text);
	
	public void showToast(String text, int duration);
	
	 public void cancelToast();

}