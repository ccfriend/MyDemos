package com.example.mydemos.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;

public class HttpConnection extends Activity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Thread thread = new Thread(httpConn);
		thread.start();
		

	}
	
	Runnable httpConn = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				String path = "/data/test.txt";
				URL url = new URL("http://www.baidu.com");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(6*1000);
				
				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("请求url失败");
				} else {
					String str = "";
					InputStream is = conn.getInputStream();
					//InputStreamReader reader = new InputStreamReader(is);
					BufferedReader reader = new BufferedReader(new InputStreamReader(
					         is));
					while((str = reader.readLine()) != null) {
						System.out.println(str);
						writeFromBuffer(path,str);
					}

							   
					//String result = readData(is, "GBK");
					
					conn.disconnect();
				}
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	
	public void writeFromBuffer(String filePath, String sb)throws IOException {
		File file = new File(filePath);
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			if (sb.toString() != null && !"".equals(sb.toString())) {
				fw.write(sb.toString());
			}
			fw.close();
		} catch (IOException e) {
			throw new IOException("文件写入异常！请检查路径名是否正确!");
		}
		
		
	}

}
