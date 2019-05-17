package com.example.chen.taco.tasktool;

import android.annotation.SuppressLint;
import android.os.Message;

import com.example.chen.taco.mvvm.ViewModel;
import com.example.chen.taco.mvvm.ViewModelManager;
import com.example.chen.taco.networkservice.Field_Method_Parameter_Annotation;
import com.example.chen.taco.networkservice.ServiceMediator;
import com.example.chen.taco.networkservice.ServiceResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <br>
 * 网络请求的基本类，所有网络请求都统一调用该类中的 doTask(final ViewModel viewModel, final String
 * method)方法。</br>
 * 
 * <br>
 * 请求方法通过传入的viewmodel映射获取参数， 所以调用doTask之前确保viewmodel中请求相应的参数赋值正确。</br>
 * 
 * <br>
 * method是需要进行请求的方法名,方法名在ServiceMediator中会有定义 </br>
 * 
 * <br>
 * doTask的返回值是唯一的，用方法名+自增长的编号 </br>
 * 
 * <br>
 * removeTask(String token) 方法手动取消某个请求的时候，务必传入doTask返回的token，否则无效 </br>
 * 
 * <br>
 * 请求成功会调用refreshData(String method)方法。返回值会回填到activity的对应返回值属性中</br>
 * 
 * <br>
 * 请求失败会调用 requestFailedHandle(String method, int errorCode,String errorMsg)方法。
 * BaseActivity中会做统一错误处理，如果需要自行处理，请Override该方法</br>
 * 
 * @author 龚雪寒
 * 
 */

@SuppressLint({ "DefaultLocale", "rawtypes" })
public class Taskpool {
	private static Taskpool instance = new Taskpool();
	// private ServiceMediator serviceMediator;
	private static final int corePoolSize = 5;
	private static final int maximumPoolSize = 50;
	private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(
			maximumPoolSize);
	private static ThreadFactory factory = new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			return t;
		}
	};

	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
			corePoolSize, maximumPoolSize, 60, TimeUnit.SECONDS, workQueue,
			factory);

	public static synchronized Taskpool sharedInstance() {
		synchronized (Taskpool.class) {
			if (instance == null) {
				instance = new Taskpool();
			}
			return instance;
		}
	}

	// /**
	// * 传入继承自ServiceMeditor的类，实现所有网络请求方法，用于映射。
	// *
	// * @param mediator
	// * 继承自ServiceMeditor的类
	// */
	// private void setServiceMediator(ServiceMediator mediator) {
	// if(mediator!=null){
	// serviceMediator = mediator;
	// }
	// }

	// /**
	// * 线程开启任务 供UI调用
	// *
	// * @param method
	// * 目标对象需要调用的函数名称
	// */
	// @SuppressWarnings("unchecked")
	// public <T> TaskToken doTask(final ViewModel viewModel, final String
	// method) {
	// final TaskToken token = getTaskToken(method, viewModel);
	// if (isContains(token)) {
	// return null;
	// }
	//
	// tokenManager.put(token.method, token);
	//
	// // 异步完成任务
	// Runnable runnable = new Runnable() {
	// public void run() {
	// // 获取方法映射
	// Method mmethod = serviceMediator.getMethodByName(method);
	// Object[] objects = null;
	// // 获取参数注解
	// if (mmethod
	// .isAnnotationPresent(Field_Method_Parameter_Annotation.class)) {
	// Field_Method_Parameter_Annotation annotation = mmethod
	// .getAnnotation(Field_Method_Parameter_Annotation.class);
	// if (annotation.args() != null) {
	// objects = new Object[annotation.args().length];
	// for (int i = 0; i < annotation.args().length; i++) {
	// String strValue = annotation.args()[i];
	// try {
	// Field fd = viewModel.getClass().getField(
	// strValue);
	// Object valueObject = fd.get(viewModel);
	// objects[i] = valueObject;
	// } catch (NoSuchFieldException e) {
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// }
	// }
	// } else {
	// objects = new Object[0];
	// }
	// } else {
	// objects = new Object[0];
	// }
	//
	// // 调用方法，获取返回值
	// ServiceResponse<T> response = null;
	// try {
	// setServiceMediator(viewModel.getServiceMediator());
	// response = (ServiceResponse<T>) mmethod.invoke(
	// serviceMediator, objects);
	// response.setTokenObj((T) token);
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// taskFailedHandle(token);
	// removeTask(token);
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// taskFailedHandle(token);
	// removeTask(token);
	// } catch (InvocationTargetException e) {
	// e.printStackTrace();
	// taskFailedHandle(token);
	// removeTask(token);
	// }
	//
	// Message message = new Message();
	// if (isContains(token)) {
	// removeTask(token);
	// message.what = response.getReturnCode();
	// message.obj = response;
	// ViewModelManager.manager().getHandler()
	// .sendMessage(message);
	// }
	// }
	// };
	//
	// executor.execute(runnable);
	// return token;
	// }

	/**
	 * 线程开启任务 供UI调用
	 * 
	 * @param method
	 *            目标对象需要调用的函数名称
	 */
	@SuppressWarnings("unchecked")
	public <T> TaskToken doTask(final ViewModel viewModel, final String method,
								final HashMap<String, ?> parameters, String flag) {
		final TaskToken token = getTaskToken(method, viewModel,flag);

		if (isContains(token)) {
			return null;
		}

		tokenManager.put(token.method, token);

		// 异步完成任务
		Runnable runnable = new Runnable() {
			public void run() {
				// 获取方法映射
				ServiceResponse<T> response = null;
				ServiceMediator serviceMediator = viewModel
						.getServiceMediator();
				try {
					if (serviceMediator == null) {
						throw new IllegalArgumentException(
								"doTask() serviceMediator can not be null , check your BaseViewModel.getServiceMediator() !");
					}
					Method mmethod = serviceMediator.getMethodByName(method);
					Object[] objects = null;
					// 获取参数注解
					if (mmethod.isAnnotationPresent(Field_Method_Parameter_Annotation.class)) {
						Field_Method_Parameter_Annotation annotation = mmethod
								.getAnnotation(Field_Method_Parameter_Annotation.class);
						if (annotation.args() != null) {
							objects = new Object[annotation.args().length];
							for (int i = 0; i < annotation.args().length; i++) {
								String strValue = annotation.args()[i];
								objects[i] = parameters.get(strValue);
							}
						} else {
							objects = new Object[0];
						}
					} else {
						objects = new Object[0];
					}
					// 调用方法，获取返回值
					response = (ServiceResponse<T>) mmethod.invoke(
							serviceMediator, objects);
					response.setTokenObj((T) token);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					taskFailedHandle(token);
					removeTask(token);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					taskFailedHandle(token);
					removeTask(token);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					taskFailedHandle(token);
					removeTask(token);
				}
				Message message = new Message();
				if (isContains(token)) {
					removeTask(token);
					message.what = response.getReturnCode();
					message.obj = response;
					ViewModelManager.manager().getHandler()
							.sendMessage(message);
				}
			}
		};

		executor.execute(runnable);
		return token;
	}

	/**
	 * 取消请求返回
	 * 
	 * @param <T>
	 * */
	@SuppressWarnings({ "unchecked" })
	private <T> void cancelTaskHandle(TaskToken token) {
		ServiceResponse<T> response = new ServiceResponse<T>();
		response.setTokenObj((T) token);
		response.setReturnCode(ServiceMediator.Service_Return_CancelRequest);
		response.setReturnDesc("取消请求");

		Message message = new Message();
		message.what = response.getReturnCode();
		message.obj = response;

		ViewModelManager.manager().getHandler().sendMessage(message);
	}

	/**
	 * 请求失败返回
	 * */
	@SuppressWarnings({ "unchecked" })
	private <T> void taskFailedHandle(TaskToken token) {
		ServiceResponse<T> response = new ServiceResponse<T>();
		response.setTokenObj((T) token);
		response.setReturnCode(ServiceMediator.Service_Return_Error);
		response.setReturnDesc("请求失败");

		Message message = new Message();
		message.what = response.getReturnCode();
		message.obj = response;

		ViewModelManager.manager().getHandler().sendMessage(message);
	}

	public Boolean isContains(TaskToken token) {
		return tokenManager.get(token.method) == null ? false : true;
	}

	/**
	 * 取消Task任务 如果需要则需记录调用Task时返回的Token 根据此Token取消Task
	 * 
	 * @param token
	 *            开启任务时返回的Token
	 */
	private void removeTask(TaskToken token) {
		tokenManager.remove(token.method);
	}

	/**
	 * 对外的取消task任务方法
	 * */
	public void cancelTask(TaskToken token) {
		if (isContains(token)) {
			cancelTaskHandle(token);
			removeTask(token);
		}
	}

	/**
	 * 正在进行中的Task的Token集合
	 */
	private HashMap<String, TaskToken> tokenManager = new HashMap<String, TaskToken>();

	/**
	 * Token生成器 token累加器
	 */
	// private static long counter = 0;

	private <T> TaskToken getTaskToken(String method, ViewModel vModel, String flag) {
		TaskToken token = new TaskToken();
		token.method = method;
		// token.activityClass = vModel.getActivityClass();
		token.viewModelId = vModel.getViewModelId();
		token.flag = flag;

		// token.identifier = "" + counter;
		// token.requestToken = method + "@" + counter;
		// counter += 1;
		return token;
	}

	// 给activity编号，保证唯一性
	private static long activityCounter = 0;

	public long getActivityCounter() {
		activityCounter += 1;
		return activityCounter;
	}
}