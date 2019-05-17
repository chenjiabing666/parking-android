package com.example.chen.taco.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * stream工具
 */
public class StreamUtil {
	/**
	 * 读取输入流并转换成字符串
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String convertStreamToString(InputStream inputStream)
			throws IOException {
		if (inputStream != null) {
			InputStreamReader insr = new InputStreamReader(inputStream);
			Writer writer = new StringWriter();
			int length = 1024;
			char[] buffer = new char[length];
			try {
				Reader reader = new BufferedReader(insr, length);
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
				reader.close();
			} finally {
				writer.close();
				insr.close();
			}
			return writer.toString();
		} else {
			return null;
		}
	}

	/**
	 * 读取stream并转换到图片
	 * 
	 * @param inputStream
	 * @return
	 */
	public static Bitmap convertStreamToImage(InputStream inputStream) {
		// BufferedInputStream bufferedInputStream = new
		// BufferedInputStream(inputStream);
		Bitmap bmp = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
		if (bmp == null) {
			System.out.println("转换失败，可能是因为您提供的输入流有误");
		}
		return bmp;
	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	/**
	 * 读取inputStream并写到文件中去
	 * @param inputStream
	 * @param file
	 * @return
	 */
	public static boolean writeStreamToFile(InputStream inputStream, File file) {
		boolean writeSucceed = false;
		if (inputStream == null || file == null) {
			Log.d("writeStreamToFile", "输入流或者文件不能为空");
			return false;
		}
		if (file.isDirectory()) {
			Log.d("writeStreamToFile", "您输入的是文件名有误");
			return false;
		}
		
		FileOutputStream fileOutputStream = null;

		// 将输入流is写入文件输出流fos中  
	    int ch = 0;  
		try {  
	        fileOutputStream = new FileOutputStream(file);

	        while((ch=inputStream.read()) != -1){  
	        	fileOutputStream.write(ch);  
	        }  
	    } catch (IOException e1) {
	        e1.printStackTrace();  
	    } finally{  
	                  //关闭输入流等（略）  
	        try {
				fileOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	    } 
		return writeSucceed;
	}
}
