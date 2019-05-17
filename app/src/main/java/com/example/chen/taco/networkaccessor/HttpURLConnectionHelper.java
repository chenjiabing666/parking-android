package com.example.chen.taco.networkaccessor;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.example.chen.taco.networkservice.ServiceMediator;
import com.example.chen.taco.parser.JsonHandler;
import com.example.chen.taco.utils.LogUtil;
import com.example.chen.taco.utils.StreamUtil;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * HttpURLConnectionHelper
 * 
 * @author kyson
 * @discussion 新建一个HttpURLConnection实例，可实现gzip压缩
 */
@SuppressLint("TrulyRandom")
public class HttpURLConnectionHelper {
	/**
	 * 请求压缩类型
	 */
	enum RequestZipType {
		requestTypeUnzip, // 不压缩
		requestTypeGzip// gzip压缩
	};

	private static int CONNECTION_TIMEOUT = 20 * 1000;
	private static int READ_TIMEOUT = 20 * 1000;
	private final static int BUFFER_SIZE = 1024;

	public static final String BOUNDARY = java.util.UUID.randomUUID()
			.toString();
	public static final String BOUNDARY1 = "******";
	public static final String PREFIX = "--", LINEND = "\r\n";
	public static final String MULTIPART_FROM_DATA = "multipart/form-data";

	private static void setHttpRequestType(HttpURLConnection connection,
			RequestZipType type) {
		if (connection == null) {
			LogUtil.d("setHttpRequestType", "connection为空！");
			return;
		}

		switch (type) {
		case requestTypeUnzip: {
			connection.setRequestProperty("Accept-Encoding", "*");
		}
			break;
		case requestTypeGzip: {
			connection.setRequestProperty("Accept-Encoding", "gzip");
		}
			break;
		default:
			break;
		}
	}

	// private static void httpRequestAddAuthHeader(String method,String
	// requestDataString,HttpURLConnection connection)
	// {
	// ServiceAuthHead authHeader = new ServiceAuthHead();
	// authHeader.createSignature(method,requestDataString);
	// connection.setRequestProperty(ServiceAuthHead.kAppKey,
	// authHeader.appKey);
	// connection.setRequestProperty(ServiceAuthHead.kVersion,
	// authHeader.appPermission);
	// connection.setRequestProperty(ServiceAuthHead.kFormat,
	// authHeader.format);
	// connection.setRequestProperty(ServiceAuthHead.kTimestamp,
	// authHeader.timestamp);
	// connection.setRequestProperty(ServiceAuthHead.kSignatureMethod,
	// authHeader.signatureMethod);
	// connection.setRequestProperty(ServiceAuthHead.kSignature,
	// authHeader.signature);
	// LogUtil.d("Core", authHeader.toString());
	// }

	// 名酒项目修改的header
	private static void httpRequestAddAuthHeader(String method,
                                                 String requestDataString, HttpURLConnection connection) {

		/*User user = UserCenter.getInstance().getUser();*/
//		if (user != null && user.getToken() != null) {
//			connection.setRequestProperty("token", user.getToken());
//			LogUtil.d("Http_Request_Header_Token", user.getToken());
//		}
		// 获取短信验证码的时候服务器的短信验证码放在了session中，这里取出来放注册的cookie里
/*		if (method.equals(SoftwareStoreRequestUrl.DO_REGISTER)) {
			connection.setRequestProperty("Cookie", CreditConst.cookie);
		}*/
		// String s = FamousWineRequestUrl.DO_REGISTER;
		// if (method.equals(FamousWineRequestUrl.DO_REGISTER)) {
		// int userNameIndex = requestDataString.indexOf("userName");
		// int userNameEndIndex = requestDataString.indexOf("&",userNameIndex);
		// if (userNameEndIndex == -1) {
		// userNameEndIndex = requestDataString.length();
		// }
		// String userName =
		// requestDataString.substring(userNameIndex,userNameEndIndex);
		//
		// int authIndex = requestDataString.indexOf("authcode");
		// int authEndIndex = requestDataString.indexOf("&",authIndex);
		// if (authEndIndex == -1) {
		// authEndIndex = requestDataString.length();
		// }
		// String autchdoe =
		// requestDataString.substring(authIndex,authEndIndex);
		// connection.setRequestProperty(userName, autchdoe);
		// }

	}

	private static void httpRequestAddSaikeMobileHeader(
			HttpURLConnection connection) {
		if (connection == null) {
			LogUtil.d("setHttpRequestType", "connection为空！");
			return;
		}

		String saikeHead = JsonHandler.objectToJson(ServiceMediator.httpHeader);
		connection.setRequestProperty("saikemobilehead", saikeHead);
		LogUtil.d("Core", ServiceMediator.httpHeader.toString());
	}

	/**
	 * 取到HttpURLConnection实例
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static HttpURLConnection getNewHttpURLConnectionInstance(
            String type, String params, RequestZipType zipType, boolean isAuth)
			throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException {
		if (type == null) {
			LogUtil.d("getNewHttpURLConnectionInstance", "urlString为空！");
			return null;
		}
		HttpURLConnection httpURLConnection = null;
		try {
			URL url = new URL(type);
			// 创建请求连接
			httpURLConnection = (HttpURLConnection) url.openConnection();
			// 支持HTTPS请求，信任所有的服务器证书
			if (httpURLConnection instanceof HttpsURLConnection) {
				// Trust all certificates
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(new KeyManager[0],
						new TrustManager[] { new EasyX509TrustManager(null) },
						new SecureRandom());
				SSLSocketFactory socketFactory = context.getSocketFactory();
				((HttpsURLConnection) httpURLConnection)
						.setSSLSocketFactory(socketFactory);

				// Allow all hostnames
				((HttpsURLConnection) httpURLConnection)
						.setHostnameVerifier(new AllowAllHostnameVerifier());

			}
			// 设置类型：是否压缩
			setHttpRequestType(httpURLConnection, zipType);
			// 是否需要增加鉴权头
			if (isAuth) {
				httpRequestAddAuthHeader(
						type.replace(NetworkAccess.venusUrl + "/services", ""),
						params, httpURLConnection);
			} else {
				httpRequestAddSaikeMobileHeader(httpURLConnection);
			}
			setHttpUrlConnectionProperty(httpURLConnection);
			if (params != null) {
				setParams(httpURLConnection, params);
			}

			return httpURLConnection;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpURLConnection;
	}

	public static HttpURLConnection getNewHttpURLConnectionInstance(
            String type, String params, RequestZipType zipType, boolean isAuth,
            boolean isForm) throws NoSuchAlgorithmException,
            KeyManagementException, KeyStoreException {
		if (type == null) {
			LogUtil.d("getNewHttpURLConnectionInstance", "urlString为空！");
			return null;
		}
		HttpURLConnection httpURLConnection = null;
		try {
			URL url = new URL(type);
			// 创建请求连接
			httpURLConnection = (HttpURLConnection) url.openConnection();
			// 支持HTTPS请求，信任所有的服务器证书
			if (httpURLConnection instanceof HttpsURLConnection) {
				// Trust all certificates
				SSLContext context = SSLContext.getInstance("TLS");
				context.init(new KeyManager[0],
						new TrustManager[] { new EasyX509TrustManager(null) },
						new SecureRandom());
				SSLSocketFactory socketFactory = context.getSocketFactory();
				((HttpsURLConnection) httpURLConnection)
						.setSSLSocketFactory(socketFactory);

				// Allow all hostnames
				((HttpsURLConnection) httpURLConnection)
						.setHostnameVerifier(new AllowAllHostnameVerifier());

			}
			// 设置类型：是否压缩
			setHttpRequestType(httpURLConnection, zipType);
			// 是否需要增加鉴权头
			if (isAuth) {
				httpRequestAddAuthHeader(
						type.replace(NetworkAccess.venusUrl + "/services", ""),
						params, httpURLConnection);
			} else {
				httpRequestAddSaikeMobileHeader(httpURLConnection);
			}
			setHttpUrlConnectionProperty(httpURLConnection, isForm);
			if (params != null) {
				setParams(httpURLConnection, params);
			}

			return httpURLConnection;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpURLConnection;
	}

	/**
	 * 取到HttpsURLConnection实例
	 */
	// Provider provider = new Provider("",1.0,"");
	// KeyStoreSpi stroreSpi = new KeyStoreSpi();
	// KeyStore keyStore = new KeyStore(stroreSpi, provider, "default");
	// String algorithm = TrustManagerFactory.getDefaultAlgorithm();
	// TrustManagerFactory tmf =
	// TrustManagerFactory.getInstance(algorithm);
	// tmf.init(keyStore);
	//
	// SSLContext context = SSLContext.getInstance("TLS");
	// context.init(null, tmf.getTrustManagers(), null);

	public static HttpsURLConnection getNewHttpsURLConnectionInstance(
            String type, String params, RequestZipType zipType) {
		if (type == null) {
			LogUtil.d("getNewHttpsURLConnectionInstance", "urlString为空！");
			return null;
		}
		HttpsURLConnection httpsURLConnection = null;
		try {
			URL url = new URL(type);
			httpsURLConnection = (HttpsURLConnection) url.openConnection();
			setHttpRequestType(httpsURLConnection, zipType);
			setHttpUrlConnectionProperty(httpsURLConnection, false);

			/**
			 * 对https请求的安全性进行设置
			 */
			if (params != null) {
				setParams(httpsURLConnection, params);
			}
			return httpsURLConnection;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 表单提交
	 * 
	 * @param type
	 * @param params
	 * @param zipType
	 * @param file
	 * @return
	 */
	public static HttpURLConnection getNewHttpURLFormConnectionInstance(
            String type, HashMap<String, String> params,
            RequestZipType zipType, File file) {
		if (type == null) {
			LogUtil.d("getNewHttpURLConnectionInstance", "urlString为空！");
			return null;
		}
		HttpURLConnection httpURLConnection = null;
		try {
			URL url = new URL(type);
			// 创建请求连接
			httpURLConnection = (HttpURLConnection) url.openConnection();
			// 设置类型：是否压缩
			setHttpRequestType(httpURLConnection, zipType);
			// 是否需要增加鉴权头
			httpRequestAddSaikeMobileHeader(httpURLConnection);
			setHttpUrlConnectionFormProperty(httpURLConnection);
			params = params == null ? new HashMap<String, String>() : params;
			HashMap<String, File> fileMap = new HashMap<String, File>();
			fileMap.put("icon", file);

			DataOutputStream outStream = null;
			try {
				StringBuilder sb = new StringBuilder();
				StringBuilder sb0 = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (entry.getKey().equals("userId")) {
						// userId需要放在第一位
						sb.append(PREFIX);
						sb.append(BOUNDARY);
						sb.append(LINEND);
						sb.append("Content-Disposition: form-data; name=\""
								+ entry.getKey() + "\"" + LINEND);
						sb.append("Content-Type: application/x-www-form-urlencoded; charset=UTF-8"
								+ LINEND);
						sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
						sb.append(LINEND);
						sb.append(entry.getValue());
						sb.append(LINEND);
					} else {
						sb0.append(PREFIX);
						sb0.append(BOUNDARY);
						sb0.append(LINEND);
						sb0.append("Content-Disposition: form-data; name=\""
								+ entry.getKey() + "\"" + LINEND);
						sb0.append("Content-Type: application/x-www-form-urlencoded; charset=UTF-8"
								+ LINEND);
						sb0.append("Content-Transfer-Encoding: 8bit" + LINEND);
						sb0.append(LINEND);
						sb0.append(entry.getValue());
						sb0.append(LINEND);
					}
				}
				sb.append(sb0);
				outStream = new DataOutputStream(
						httpURLConnection.getOutputStream());
				if (sb.length() > 0) {
					outStream.write(sb.toString().getBytes());
				}
				if (fileMap != null) {
					for (Map.Entry<String, File> f : fileMap.entrySet()) {

						outStream.writeBytes(PREFIX + BOUNDARY + LINEND);
						// outStream
						// .writeBytes("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
						// + f.getValue()
						// .toString()
						// .substring(
						// f.getValue().toString()
						// .lastIndexOf("/") + 1)
						// + "\"" + LINEND);
						// famouswine项目改
						outStream
								.writeBytes("Content-Disposition: form-data; name=\"userIcon\"; filename=\""
										+ f.getValue()
												.toString()
												.substring(
														f.getValue()
																.toString()
																.lastIndexOf(
																		"/") + 1)
										+ "\"" + LINEND);
						outStream.writeBytes(LINEND);
						FileInputStream fis = new FileInputStream(f.getValue());
						byte[] buffer = new byte[1024]; // 8k
						int count = -1;
						// 读取<strong>文件</strong>
						while ((count = fis.read(buffer)) != -1) {
							outStream.write(buffer, 0, count);
						}
						fis.close();
						outStream.write(LINEND.getBytes());
					}

					// 请求结束标志
					byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND)
							.getBytes();
					outStream.write(end_data);
					outStream.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (outStream != null) {
					try {
						outStream.close();
					} catch (IOException e) {
						Log.e("HttpURLConnectionHelper",
								"OutPutStream Close Error");
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpURLConnection;
	}

	/**
	 * 设置请求头信息，设置请求属性
	 * 
	 * @param connection
	 */
	private static void setHttpUrlConnectionProperty(
			HttpURLConnection connection) {
		/**
		 * 设置通讯头信息
		 */
		ProtocolHandler.handleRequest(connection);
		/**
		 * 默认不缓存
		 */
		connection.setUseCaches(false);
		/**
		 * 设置连接主机超时（单位：毫秒）
		 */
		connection.setConnectTimeout(CONNECTION_TIMEOUT);
		/**
		 * 设置从主机读取数据超时（单位：毫秒）
		 */
		connection.setReadTimeout(READ_TIMEOUT);
		/**
		 * 使用 URL 连接进行输入
		 */
		connection.setDoOutput(true);
		/**
		 * 使用 URL 连接进行输出
		 */
		connection.setDoInput(true);
	}

	private static void setHttpUrlConnectionProperty(
            HttpURLConnection connection, boolean isForm) {
		/**
		 * 设置通讯头信息
		 */
		ProtocolHandler.handleRequest(connection, isForm);
		/**
		 * 默认不缓存
		 */
		connection.setUseCaches(false);
		/**
		 * 设置连接主机超时（单位：毫秒）
		 */
		connection.setConnectTimeout(CONNECTION_TIMEOUT);
		/**
		 * 设置从主机读取数据超时（单位：毫秒）
		 */
		connection.setReadTimeout(READ_TIMEOUT);
		/**
		 * 使用 URL 连接进行输入
		 */
		connection.setDoOutput(true);
		/**
		 * 使用 URL 连接进行输出
		 */
		connection.setDoInput(true);
	}

	/**
	 * 设置表单请求头信息，设置请求属性
	 * 
	 * @param connection
	 */
	private static void setHttpUrlConnectionFormProperty(
			HttpURLConnection connection) {
		/**
		 * 设置通讯头信息
		 */
		ProtocolHandler.handleRequestForm(connection);
		/**
		 * 默认不缓存
		 */
		connection.setUseCaches(false);
		/**
		 * 使用 URL 连接进行输入
		 */
		connection.setDoOutput(true);
		/**
		 * 使用 URL 连接进行输出
		 */
		connection.setDoInput(true);

	}

	/**
	 * 设置请求参数
	 * 
	 * @param httpURLConnection
	 * @param params
	 */
	private static void setParams(HttpURLConnection httpURLConnection,
			String params) {
		ByteArrayInputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(params.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LogUtil.e("Helper", "Params InPutStream Close Error");
					e.printStackTrace();
				}
			}
		}

		readData(httpURLConnection, inputStream);
	}

	/**
	 * 获取请求的结果
	 * 
	 * @param httpURLConnection
	 * @param inputStream
	 */
	private static void readData(HttpURLConnection httpURLConnection,
			ByteArrayInputStream inputStream) {
		int length = 0;
		OutputStream outputStream = null;
		byte buffer[] = new byte[BUFFER_SIZE];

		// 该参数设置大数量分包上传，此处不应该设置
		// httpURLConnection.setChunkedStreamingMode(0);

		try {
			outputStream = new BufferedOutputStream(
					httpURLConnection.getOutputStream());
			while ((length = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					LogUtil.e("Helper", "Params OutPutStream Close Error");
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取请求数据
	 * 
	 * @param urlConn
	 *            连接请求
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String fetchHttpRequestData(HttpURLConnection urlConn) {
		try {
			// 请求的contentencode不为空
			if (!TextUtils.isEmpty(urlConn.getContentEncoding())) {
				String encode = urlConn.getContentEncoding().toLowerCase();
				InputStream inputStream = urlConn.getInputStream();
				// 判断服务端返回的头信息是否支持gzip压缩
				String resultString = null;
				if (!TextUtils.isEmpty(encode) && encode.indexOf("gzip") >= 0) {
					LogUtil.d("gzip", "即将进行gzip解压缩数据");
					InputStream inputStream2 = new GZIPInputStream(inputStream);
					resultString = StreamUtil
							.convertStreamToString(inputStream2);
					inputStream2.close();
				} else {
					LogUtil.d("gzip", "服务器不支持gzip，自动请求无压缩类型数据");
					resultString = StreamUtil
							.convertStreamToString(inputStream);
				}
				return resultString;
			}
			// 请求的contenttype为空
			else {
				String resultString = null;
				InputStream inputStream = urlConn.getInputStream();
				resultString = StreamUtil.convertStreamToString(inputStream);
				inputStream.close();
				return resultString;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			urlConn.disconnect();
			urlConn = null;
		}
		return null;
	}

}