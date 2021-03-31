package com.linkage.litms.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class HttpUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	public static SSLSocketFactory init() throws Exception {  
        
        class MyX509TrustManager implements X509TrustManager {  
              
            public MyX509TrustManager() throws Exception {  
                // do nothing  
            }  
    
            @Override  
            public void checkClientTrusted(X509Certificate[] chain,  
                    String authType) throws CertificateException {                    
            }  
    
            @Override  
            public void checkServerTrusted(X509Certificate[] chain,  
                    String authType) throws CertificateException {  
            }  
    
            @Override  
            public X509Certificate[] getAcceptedIssuers() {  
                return new X509Certificate[] {};  
            }  
        }  
        TrustManager[] tm = { new MyX509TrustManager() };  
          
        System.setProperty("https.protocols", "TLSv1");  
        SSLContext sslContext = SSLContext.getInstance("SSL");   
        sslContext.init(null, tm, new java.security.SecureRandom());  
        SSLSocketFactory ssf = sslContext.getSocketFactory();  
          
        return ssf;  
    }
	
	public static String sendHttpRequest(String Requrl , String Reqcontent) throws Exception {
		URL url = new URL(Requrl);
		
		/**
		 * 此处的urlConnection对象实际上是根据URL的请求协�?此处是http)生成的URLConnection�?
		 * 的子类HttpURLConnection,故此处最好将其转�? 为HttpURLConnection类型的对�?以便用到
		 */
		HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
		
		HostnameVerifier hostNameVerfier = new HostnameVerifier() {
			
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		
		connection.setHostnameVerifier(hostNameVerfier);
		
		try {
			connection.setSSLSocketFactory(init());
		} catch (Exception e1) {
			throw new IOException(e1);
		}
		//设置是否向httpUrlconnection输出，因为这个是post请求，参数要放到正文里面，因此需要设置true，默认情况下是false;
		connection.setDoOutput(true);
		
		//post 请求不能使用缓存
		connection.setUseCaches(false);
		
		//设定传�?的内容类�?如果不是基本类型，需要设�?
		//connection.setRequestProperty("Content-Type", "application/plain");
		connection.setRequestProperty("Charset", "UTF-8");
		//设定请求的方法为“post”，默认是get
		connection.setRequestMethod("POST");
		
		connection.setInstanceFollowRedirects(true);
		
		// 此处getOutputStream 会隐藏的进行connect();
		byte[] bytes = Reqcontent.toString().getBytes("UTF-8");
		OutputStream outstream = connection.getOutputStream();
		outstream.write(bytes);
		
		outstream.flush();
		outstream.close();
		
		/**
		 * 调用httpurlconnection连接对象的getinputStream()函数，将内存缓冲区中封装好的完整的http请求电文发�?到服务器�?
		 */
	    StringBuffer content = new StringBuffer();
	    BufferedReader reader = null;
	    
	    try {
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			
			String lines;
			while ((lines = reader.readLine()) != null){
				content.append(lines);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
			try {
				if(reader != null)
					reader.close();
				connection.disconnect();
			} catch (Exception fe) {
				System.out.println(fe);
			}
		}
	    
	    return content.toString();
	}
	public static String sendHttpRequestByJson(String Requrl , String Reqcontent) throws Exception {
		URL url = new URL(Requrl);
		
		/**
		 * 此处的urlConnection对象实际上是根据URL的请求协�?此处是http)生成的URLConnection�?
		 * 的子类HttpURLConnection,故此处最好将其转�? 为HttpURLConnection类型的对�?以便用到
		 */
		HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
		
		HostnameVerifier hostNameVerfier = new HostnameVerifier() {
			
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		
		connection.setHostnameVerifier(hostNameVerfier);
		
		try {
			connection.setSSLSocketFactory(init());
		} catch (Exception e1) {
			throw new IOException(e1);
		}
		//设置是否向httpUrlconnection输出，因为这个是post请求，参数要放到正文里面，因此需要设置true，默认情况下是false;
		connection.setDoOutput(true);
		
		//post 请求不能使用缓存
		connection.setUseCaches(false);
		
		//设定传�?的内容类�?如果不是基本类型，需要设�?
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Charset", "UTF-8");
		//设定请求的方法为“post”，默认是get
		connection.setRequestMethod("POST");
		
		connection.setInstanceFollowRedirects(true);
		
		// 此处getOutputStream 会隐藏的进行connect();
		byte[] bytes = Reqcontent.toString().getBytes("UTF-8");
		OutputStream outstream = connection.getOutputStream();
		outstream.write(bytes);
		
		outstream.flush();
		outstream.close();
		
		/**
		 * 调用httpurlconnection连接对象的getinputStream()函数，将内存缓冲区中封装好的完整的http请求电文发�?到服务器�?
		 */
		StringBuffer content = new StringBuffer();
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			
			String lines;
			while ((lines = reader.readLine()) != null){
				content.append(lines);
			}
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
			try {
				if(reader != null)
					reader.close();
				connection.disconnect();
			} catch (Exception fe) {
				System.out.println(fe);
			}
		}
		
		return content.toString();
	}
	
	


	 public static String filterNull(Object str) {
			String result = "";
			if(null != str)
				result = str.toString() ;
			return  result;
		}
	 
	    /**
	     * httpClient的get请求方式
	     * 使用GetMethod来访问一个URL对应的网页实现步骤：
	     * 1.生成一个HttpClient对象并设置相应的参数；
	     * 2.生成一个GetMethod对象并设置响应的参数；
	     * 3.用HttpClient生成的对象来执行GetMethod生成的Get方法；
	     * 4.处理响应状态码；
	     * 5.若响应正常，处理HTTP响应内容；
	     * 6.释放连接。
	     * @param url
	     * @param charset
	     * @return
	     */
	    public static String doGet(String url,Map<String, String> headers, String charset){
	        /**
	         * 1.生成HttpClient对象并设置参数
	         */
	        // 获取输入流
	        InputStream is = null;
	        BufferedReader br = null;
	        HttpClient httpClient = new HttpClient();
	        //设置Http连接超时为5秒
	        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);

	        /**
	         * 2.生成GetMethod对象并设置参数
	         */
	        GetMethod getMethod = new GetMethod(url);
	        //设置get请求超时为5秒
	        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 20000);
	        getMethod.addRequestHeader("Content-Type", "application/json;charset=utf-8");
	        if (headers!=null) {
				for (Map.Entry<String, String> e : headers.entrySet()) {
					getMethod.setRequestHeader(e.getKey(), e.getValue());
				}
			}
	        
	        //设置请求重试处理，用的是默认的重试处理：请求三次
	        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

	        StringBuffer response = new StringBuffer();
	        /**
	         * 3.执行HTTP GET 请求
	         */
	        try {
	            int statusCode = httpClient.executeMethod(getMethod);

	            /**
	             * 4.判断访问的状态码
	             */
	            if (statusCode != HttpStatus.SC_OK){
	            	logger.error("请求出错：" + getMethod.getStatusLine());
	            }

				 is = getMethod.getResponseBodyAsStream();
				 br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String str = "";
				while ((str = br.readLine()) != null) {
					response.append(str);
				}

	        } catch (HttpException e) {
	            //发生致命的异常，可能是协议不对或者返回的内容有问题
	        	logger.error("请检查输入的URL!");
	            e.printStackTrace();
	        } catch (IOException e){
	            //发生网络异常
	        	logger.error("发生网络异常!");
	        }finally {
	            /**
	             * 6.释放连接
	             */
	        	// 关闭资源
	            if (null != br) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            if (null != is) {
	                try {
	                    is.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            getMethod.releaseConnection();
	        }
	        return response.toString();
	    }

	    /**
	     * post请求
	     * @param url
	     * @param json
	     * @return
	     * @throws UnsupportedEncodingException 
	     */
		public static String doPost(String url, JSONObject paramMap, Map<String, String> headers) {
			// 获取输入流
			InputStream is = null;
			BufferedReader br = null;
			String result = null;
			// 创建httpClient实例对象
			HttpClient httpClient = new HttpClient();
			// 设置httpClient连接主机服务器超时时间：15000毫秒
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
			// 创建post请求方法实例对象
			PostMethod postMethod = new PostMethod(url);
			// 设置post请求超时时间
			postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 20000);
			postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			postMethod.addRequestHeader("accept", "*/*");
			postMethod.addRequestHeader("connection", "Keep-Alive");
			// 设置json格式传送
			postMethod.addRequestHeader("Content-Type", "application/json;charset=utf-8");
			// 必须设置下面这个Header
			postMethod.addRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");



			if (headers!=null) {
				for (Map.Entry<String, String> e : headers.entrySet()) {
					postMethod.setRequestHeader(e.getKey(), e.getValue());
				}
			}

			try {
				String toJson = paramMap.toString();
				RequestEntity se = new StringRequestEntity(toJson, "application/json", "UTF-8");
				postMethod.setRequestEntity(se);
				int statusCode = httpClient.executeMethod(postMethod);
				// 判断是否成功
				if (statusCode != HttpStatus.SC_OK) {
					logger.error("请求出错：" + postMethod.getStatusLine());
				}
				// 获取远程返回的数据
				is = postMethod.getResponseBodyAsStream();
				// 封装输入流
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

				StringBuffer sbf = new StringBuffer();
				String temp = null;
				while ((temp = br.readLine()) != null) {
					sbf.append(temp).append("\r\n");
				}
				result = sbf.toString();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 关闭资源
				if (null != br) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (null != is) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// 释放连接
				postMethod.releaseConnection();
			}
			return result;
		}
}
