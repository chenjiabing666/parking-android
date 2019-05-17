package com.example.chen.taco.parser;

import android.content.res.Resources;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * 挡板数据xml解析类，主要用于解析挡板数据
 * @author kyson
 * @discussion 挡板数据位于目录/res/raw目录下,如果要更改，请重设属性mockResourceDir
 * 考虑到与iOS的兼容性，xml的结构如下：
 * ==========================================================
 * <?xml appPermission="1.0" encoding="UTF-8"?>
 * <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
 * <plist appPermission="1.0">
 * <dict>
 * <key>RequestDataTypeLoginout</key>
 * <string>{"result":"success"}</string>
 * <string>{"result":"success"}</string>
 * </dict>
 * </plist>
 * ==========================================================
 *其中key对应的是请求类型，string是该请求类型对应的返回的response（json类型）
 */
public class  MockHandler{
	private SAXParser parser = null;
	private ParserHandler handler = new ParserHandler();
	InputStream inputStream = null;
	private String requestTypeString = null;
	private MockHandlerSucceedListener mListener = null;

	/**
	 * 重写构造器方法
	 */
	public MockHandler() {
		/**
		 * 初始化XML解析类
		 */
		SAXParserFactory factory = SAXParserFactory.newInstance();
		XMLReader reader = null;
		try {
			parser = factory.newSAXParser();
			reader = parser.getXMLReader();
			handler = new ParserHandler();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader.setContentHandler(new ParserHandler());
	}
	
	/**
	 * 从资源池中读取挡板数据
	 * @param mockData  挡板数据资源路径
	 * @param resources app的资源池
	 */
	public void getMockData(int mockData, Resources resources){
		inputStream = resources.openRawResource(mockData);
	}
	
	/**
	 * 根据请求key读取
	 * @param type 
	 */
	public void getDataFormMock(String type, String params) {
		this.requestTypeString = type + params;
		try {
			parser.parse(inputStream, handler);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置监听器
	 * @param listener 监听器
	 */
	public void setRequestFinishedListener(MockHandlerSucceedListener listener) {
		this.mListener = listener;
	}
	
	/**
	 * 解析处理类
	 * @author kyson
	 *
	 */
	public class ParserHandler extends DefaultHandler {
		//有三种类型，一种是当前在“键”处，一种是在“值”处，还有一种这两种都不在
		//用于判断当前是否是“key”，比如说当前请求的key是登录，并且正在请求key，则置为true
		boolean isKey = false;
		//用于判断当前是不是“string”，比如说当前请求的是string，则置为true
		boolean isString = false;
		//用于判断当前的string是否是请求的key对应的string：
		//如果当前key是登录的，并且请求的也是登录的，而且现在解析的是key，
		//那么下次解析的一定是当前key所对应的string，这时就应该将isCurrentString置为true，
		//用于标记下次即将获取挡板数据
		boolean isCurrentString = false;
		//用于判断解析是否成功
		private boolean parseSucceed = false;

		
//		StringBuilder stringBuilder = new StringBuilder();
		//请求的类型
		String requestString = MockHandler.this.requestTypeString;
		
		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			// super.startDocument();
//			System.out.println("start");
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			// super.endDocument();
			if (!parseSucceed) {
				System.out.println("解析失败！您要请求的"+ MockHandler.this.requestTypeString + "在挡板数据中未找到，请与管理员联系");
			}
			parseSucceed = false;
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
			// TODO Auto-generated method stub
			//判断解析器的解析位置，如果是key处则置Key为true，如果是string则置string为true，
			//string里面包含了数据的返回值
			if (localName.equals("key")) {
//				stringBuilder.setLength(0);	
				isKey = true;
			}
			if (localName.equals("string")) {
//				stringBuilder.setLength(0);	
				isString = true;
			}
//			super.startElement(uri, localName, qName, attributes);
			
			//取得属性
//			if (localName.equals("key")) {
//				for (int i = 0; i < attributes.getLength(); i++) {
//					System.out.println(attributes.getLocalName(i) + "===" + attributes.getValue(i));
//				}
//			}
			
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			// TODO Auto-generated method stub
			
//			//如果当前是键值对中的值那么就获取数据并通知监听器
//			if (isString && shouldGetResponse) {
//				String response = stringBuilder.toString();
//				XMLParserUtils.this.mListener.responseFetched(response);
//				
//				shouldGetResponse = false;
//			}
//			//每次停止当前标签的解析都要将stringBuilder清空！
//			stringBuilder.setLength(0);	
			isString = false;
			isKey = false;
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			// TODO Auto-generated method stub
//			String keyString = String.valueOf(ch);
//			stringBuilder.replace(0, length, String.valueOf(ch));
			String resultString = new String(ch, start, length);
			if (isKey) {
				//判断类型如果相等,则置isCurrentString为真，即将获取数据
				String requestString = MockHandler.this.requestTypeString;
				if (!parseSucceed) {
					System.out.println("您需要请求的数据类型为:"+requestString);
					System.out.println("您正在请求的数据类型为:"+resultString);
				}
				//当前请求的类型跟挡板类型相同
				if (requestString.equals(resultString)) {
//					valueString = String.valueOf(ch);
					//清空stringBuilder数据
					System.out.println("您请求的数据类型已经在挡板文件中匹配到，即将为您返回数据");
//					stringBuilder.setLength(0);
					isCurrentString = true;
				}else {
					isCurrentString = false;
				}
			}
			//如果当前是string，那么当前就不可能是key
			if (isString) {
				if (isCurrentString) {
					//数据取得成功，通知给监听器
					System.out.println("解析成功！您请求的数据的返回值为：" + resultString + "即将为您回调");

					MockHandler.this.mListener.responseFetched(resultString);
					
					isCurrentString = false;
					parseSucceed = true;
					return;
				}
				isKey = false;
			}

		}

	}

}
