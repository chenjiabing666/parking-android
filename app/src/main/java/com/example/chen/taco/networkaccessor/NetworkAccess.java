package com.example.chen.taco.networkaccessor;


import com.example.chen.taco.parser.MockHandler;
import com.example.chen.taco.parser.MockHandlerSucceedListener;
import com.example.chen.taco.utils.ENVConfig;
import com.example.chen.taco.utils.LogUtil;
import com.example.chen.taco.utils.TacoConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据请求类
 * 
 * @author kyson
 * @discussion 请求使用的类是android推荐的HttpURLConnection类
 *             分六种请求，http,https,http支持gzip，https支持gzip,http支持认证，https支持认证
 *             非单例类，因为HttpURLConnection使用完必须close(调用disconnect函数)
 */
public class NetworkAccess {
	final static String TAG = "NetworkAccess";
	public static String venusUrl = ENVConfig.xmlParser == null ? ""
			: ENVConfig.xmlParser.getVenusURL();

	private static NetworkResponse doHttpRequest(String requestURL,
                                                 String requestData, boolean isAuth, boolean isGzip) {
		// 判断有无挡板数据
		final NetworkResponse resultResponse = new NetworkResponse();
		if (ENVConfig.mockState) {
			MockHandler mockHandler = new MockHandler();
			mockHandler
					.setRequestFinishedListener(new MockHandlerSucceedListener() {
						@Override
						public void responseFetched(String response) {
							// TODO Auto-generated method stub
							resultResponse.setWithData(response,
									HttpURLConnection.HTTP_OK, null);
						}
					});
			mockHandler.getDataFormMock(requestURL, requestData);
			return resultResponse;
		}
		HttpURLConnection urlConn = null;
		try {
			urlConn = HttpURLConnectionHelper.getNewHttpURLConnectionInstance(
					requestURL, requestData,
					isGzip ? HttpURLConnectionHelper.RequestZipType.requestTypeGzip
							: HttpURLConnectionHelper.RequestZipType.requestTypeUnzip, isAuth);

			LogUtil.d(TAG, "Request:" + requestURL.toString());
			LogUtil.d(TAG, "URL:" + urlConn.getURL().toString());
			LogUtil.d(TAG, "Params:" + requestData);

			String session_value = urlConn.getHeaderField("Set-Cookie" );
			if (session_value != null) {
				String[] sessionId = session_value.split(";");
				if (sessionId.length > 0) {
					TacoConstants.cookie= sessionId[0];
				}
			}
			
			
			
			
			if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String resultString = HttpURLConnectionHelper
						.fetchHttpRequestData(urlConn);
				resultResponse.setWithData(resultString,
						HttpURLConnection.HTTP_OK, null);
				LogUtil.d(TAG, "Response:" + resultString);
			} else {
				LogUtil.d("HttpError", urlConn.getResponseCode() + ":"
						+ urlConn.getResponseMessage());
				resultResponse.setWithData("", urlConn.getResponseCode(),
						urlConn.getResponseMessage());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.e(TAG, "Connection Error"+ e.getMessage());
			e.printStackTrace();
		} finally {
			urlConn.disconnect();
		}
		return resultResponse;
	}

	private static NetworkResponse doHttpRequest(String requestURL,
                                                 String requestData, boolean isAuth, boolean isGzip, boolean isForm) {
		// 判断有无挡板数据
		final NetworkResponse resultResponse = new NetworkResponse();
		if (ENVConfig.mockState) {
			MockHandler mockHandler = new MockHandler();
			mockHandler
					.setRequestFinishedListener(new MockHandlerSucceedListener() {
						@Override
						public void responseFetched(String response) {
							// TODO Auto-generated method stub
							resultResponse.setWithData(response,
									HttpURLConnection.HTTP_OK, null);
						}
					});
			mockHandler.getDataFormMock(requestURL, requestData);
			return resultResponse;
		}
		HttpURLConnection urlConn = null;
		try {
			urlConn = HttpURLConnectionHelper.getNewHttpURLConnectionInstance(
					requestURL, requestData,
					isGzip ? HttpURLConnectionHelper.RequestZipType.requestTypeGzip
							: HttpURLConnectionHelper.RequestZipType.requestTypeUnzip, isAuth,isForm);

			LogUtil.d(TAG, "Request:" + requestURL.toString());
			LogUtil.d(TAG, "URL:" + urlConn.getURL().toString());
			LogUtil.d(TAG, "Params:" + requestData);

			if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String resultString = HttpURLConnectionHelper
						.fetchHttpRequestData(urlConn);
				resultResponse.setWithData(resultString,
						HttpURLConnection.HTTP_OK, null);
				LogUtil.d(TAG, "Response:" + resultString);
			} else {
				LogUtil.d("HttpError", urlConn.getResponseCode() + ":"
						+ urlConn.getResponseMessage());
				resultResponse.setWithData("", urlConn.getResponseCode(),
						urlConn.getResponseMessage());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.e(TAG, "Connection Error");
			e.printStackTrace();
		} finally {
			urlConn.disconnect();
		}
		return resultResponse;
	}

	/**
	 * 发送http请求,请求数据无压缩
	 * 
	 * @param requestData
	 *            请求数据为JSON格式字符串
	 * @param requestURL
	 *            接口地址
	 */
	public static NetworkResponse httpRequest(String requestURL,
			String requestData) {
		return doHttpRequest(requestURL, requestData, false, false);
	}

	/**
	 * 发送http请求,请求数据GZip压缩
	 * 
	 * @param requestData
	 *            请求数据为JSON格式字符串
	 * @param requestURL
	 *            接口地址
	 */
	public static NetworkResponse httpRequestByGZip(String requestURL,
			String requestData) {
		return doHttpRequest(requestURL, requestData, false, true);
	}

	/**
	 * 发送http请求,用于OpenAPI鉴权
	 * 
	 * @param requestData
	 *            请求数据为JSON格式字符串
	 * @param requestURL
	 *            接口地址
	 * 
	 * @return 请求结果
	 */
	public static NetworkResponse httpRequestAuth(String requestURL,
			String requestData) {
		LogUtil.d("httpRequestAuth","httpRequestAuth");
		return doHttpRequest(requestURL, requestData, true, false);
	}
	public static NetworkResponse httpRequestAuth(String requestURL,
                                                  String requestData, boolean isForm) {
		return doHttpRequest(requestURL, requestData, true, false,isForm);
	}

	/**
	 * 发送https请求,请求数据无压缩
	 * 
	 * @param requestData
	 *            请求数据为JSON格式字符串
	 */
	public static NetworkResponse httpsRequest(String requestURL,
			String requestData) {
		return doHttpRequest(requestURL, requestData, false, false);
	}

	/**
	 * 发送https请求,请求数据GZip压缩
	 * 
	 * @param requestData
	 *            请求数据为JSON格式字符串
	 */
	public static NetworkResponse httpsRequestByGZip(String requestURL,
			String requestData) {
		return doHttpRequest(requestURL, requestData, false, true);
	}

	/**
	 * 发送https请求,用于OpenAPI鉴权
	 * 
	 * @param requestData
	 *            参数
	 *            接口名
	 * 
	 * @return 请求结果
	 */
	public static NetworkResponse httpsRequestAuth(String requestData,
			String requestURL) {
		return doHttpRequest(requestURL, requestData, true, false);
	}

	/**
	 * 表单提交
	 * 
	 * @requestURL 路径
	 * @param file
	 *            图片
	 */
	public static NetworkResponse httpRequestByForm(String requestURL,
                                                    HashMap<String, String> params, File file, boolean isGzip) {
		final NetworkResponse resultResponse = new NetworkResponse();
		HttpURLConnection urlConn = null;
		try {
			// HeadImg headImg = new Gson().fromJson(params, HeadImg.class);
			// HashMap<String, String> pars = new HashMap<String, String>();
			// pars.put("userId", headImg.userId);
			// pars.put("file", headImg.file);
			urlConn = HttpURLConnectionHelper
					.getNewHttpURLFormConnectionInstance(requestURL, params,
							isGzip ? HttpURLConnectionHelper.RequestZipType.requestTypeGzip
									: HttpURLConnectionHelper.RequestZipType.requestTypeUnzip, file);
			LogUtil.d(TAG, "Request:" + requestURL.toString());
			LogUtil.d(TAG, "URL:" + urlConn.getURL().toString());
			LogUtil.d(TAG, "Params:" + params.toString());
			if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String resultString = HttpURLConnectionHelper
						.fetchHttpRequestData(urlConn);
				resultResponse.setWithData(resultString,
						HttpURLConnection.HTTP_OK, null);
				LogUtil.d(TAG, "Response:" + resultString);
			} else {
				LogUtil.d("HttpError", urlConn.getResponseCode() + ":"
						+ urlConn.getResponseMessage());
				resultResponse.setWithData("", urlConn.getResponseCode(),
						urlConn.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			urlConn.disconnect();
		}
		return resultResponse;
	}



	/**
	 * HttpPost方式请求,返回cookie
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static NetworkResponse requestByHttpPostWithCookie(String url,
			Map<String, String> params) throws Exception {
		final NetworkResponse resultResponse = new NetworkResponse();
		String result = null;
		// 新建HttpPost对象
		HttpPost httpPost = new HttpPost(url);
		// Post参数
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		for (Map.Entry entry : params.entrySet()) {
			parameters.add(new BasicNameValuePair(entry.getKey().toString(),
					entry.getValue().toString()));
		}
		// 设置字符集
		HttpEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
		// 设置参数实体
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		// 获取HttpClient对象
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// 获取HttpResponse实例
		HttpResponse httpResp = httpClient.execute(httpPost);
		// 判断是够请求成功
		if (httpResp.getStatusLine().getStatusCode() == 200) {
			// 获取返回的数据
			result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
			List<Cookie> cookies = httpClient.getCookieStore().getCookies();

			resultResponse.setWithData(result, HttpURLConnection.HTTP_OK, null,
					cookies);
			LogUtil.d(TAG, "HttpPost方式请求成功，返回数据如下：" + result);
		} else {
			LogUtil.d(TAG, "HttpPost方式请求失败");
		}
		return resultResponse;
	}
}