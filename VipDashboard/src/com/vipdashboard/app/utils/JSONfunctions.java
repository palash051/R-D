package com.vipdashboard.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;


import com.google.gson.Gson;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

/**
 * @author Tanvir Ahmed Chowdhury use for connecting to application server
 */

public class JSONfunctions {
	/**
	 * Retrieve json data string from a url request
	 * 
	 * @param url
	 * @return
	 */
	public static int TIMEOUT_MILLISEC = 10000; // = 10 seconds	\
	
	public static Object retrieveDataFromStream(String url, Class<?> dataClass) {
		InputStream inputStream = null;
		CommonValues.getInstance().ExceptionCode = CommonConstraints.NO_EXCEPTION;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_MILLISEC);
			HttpGet httpGet = new HttpGet(url);
			// Set accept header
			//httpGet.setHeader("Accept", "application/json");
			//httpGet.setHeader("Content-Type", "application/xml");
			HttpClient httpclient = new DefaultHttpClient(httpParameters);

			HttpResponse response;

			response = httpclient.execute(httpGet);

			// check response ok(200) code
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					inputStream = entity.getContent();
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(inputStream,"ISO-8859-1"), 8);
						StringBuilder sb = new StringBuilder();
						String line = null;
						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						Gson gson = new Gson();					
						return gson.fromJson(fixEncoding(sb.toString()), dataClass);
					} 
					catch (Exception e) {
						Log.e("log_tag", "Error converting result " + e.toString());
						CommonValues.getInstance().IsServerConnectionError = true;
					}
					
				}
			}

		} catch (ClientProtocolException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.CLIENT_PROTOCOL_EXCEPTION;
		} catch (IllegalStateException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.ILLEGAL_STATE_EXCEPTION;
		} catch (UnsupportedEncodingException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.UNSUPPORTED_ENCODING_EXCEPTION;
		} catch (IOException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.IO_EXCEPTION;
		} catch (Exception e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.GENERAL_EXCEPTION;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				Log.e("log_tag", "Error in " + e.toString());
				CommonValues.getInstance().ExceptionCode = CommonConstraints.IO_EXCEPTION;
			}
		}
		return null;
	}

	/*public static Object getJSONfromURL(String url, Class<?> dataClass) {
		InputStream is = null;
		String result = "";		
		CommonValues.getInstance().IsServerConnectionError = false;
		int TIMEOUT_MILLISEC = 10000; // = 10 seconds

		try {
			// code block for for server connection timeout..here it is set to 5
			// second..Tanvir
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_MILLISEC);
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Accept", "application/json");
			httpGet.setHeader("Content-Type", "application/xml");

			HttpPost httpPost = new HttpPost(url);
			// TimeOut Code Ends
			HttpClient httpclient = new DefaultHttpClient(httpParameters);

			HttpResponse response;

			response = httpclient.execute(httpGet);
			
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null)
					is = entity.getContent();
			}

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
			CommonValues.getInstance().IsServerConnectionError = true;
			if (context != null) {
				// CommonTask.ShowMessage(context,"Server kontakt fejl\nVare ikke fundet");
			}
		}
		if (is != null) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF8"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();
				if(result!="")
					return retrieveObjectFromXml(result,dataClass);
			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
				CommonValues.getInstance().IsServerConnectionError = true;
				if (context != null) {
					// CommonTask.ShowMessage(context,
					// "Server kontakt fejl\nVare ikke fundet");
				}
			}			
		}
		return null;
	}*/
	
	public static Object retrieveDataFromStream(String url) {
		InputStream inputStream = null;
		CommonValues.getInstance().ExceptionCode = CommonConstraints.NO_EXCEPTION;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_MILLISEC);
			HttpGet httpGet = new HttpGet(url);
			// Set accept header
			/*httpGet.setHeader("Accept", "application/json");
			httpGet.setHeader("Content-Type", "application/xml");*/
			HttpClient httpclient = new DefaultHttpClient(httpParameters);

			HttpResponse response;

			response = httpclient.execute(httpGet);

			// check response ok(200) code
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					inputStream = entity.getContent();
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(inputStream,"ISO-8859-1"), 8);
						StringBuilder sb = new StringBuilder();
						String line = null;
						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						return sb.toString();
					} 
					catch (Exception e) {
						Log.e("log_tag", "Error converting result " + e.toString());
						CommonValues.getInstance().IsServerConnectionError = true;
						/*
						 * if (context != null) { CommonTask.ShowMessage(context,
						 * "Server kontakt fejl\nVare ikke fundet"); }
						 */
					}
					
				}
			}

		} catch (ClientProtocolException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.CLIENT_PROTOCOL_EXCEPTION;
		} catch (IllegalStateException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.ILLEGAL_STATE_EXCEPTION;
		} catch (UnsupportedEncodingException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.UNSUPPORTED_ENCODING_EXCEPTION;
		} catch (IOException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.IO_EXCEPTION;
		} catch (Exception e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.GENERAL_EXCEPTION;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				Log.e("log_tag", "Error in " + e.toString());
				CommonValues.getInstance().ExceptionCode = CommonConstraints.IO_EXCEPTION;
			}
		}
		return null;
	}
	public static Object getJSONfromPostURL(String url,byte[] postData,String contentType) {
		return getJSONfromPostURL(url,postData,contentType,null);
	}
	public static Object getJSONfromPostURL(String url,byte[] postData,String contentType, Class<?> dataClass) {	
		
		CommonValues.getInstance().ExceptionCode = CommonConstraints.NO_EXCEPTION;
		InputStream inputStream = null;
		try {
			// code block for for server connection timeout..here it is set to 5
			// second..Tanvir
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_MILLISEC);
			HttpPost httpPost = new HttpPost(url);
			//httpPost.setHeader("Content-Type", contentType);
			//StringEntity se = new StringEntity(Base64.encodeToString(postData,Base64.NO_WRAP));
			String ba1=Base64.encodeToString(postData,Base64.NO_WRAP);

            ArrayList<NameValuePair> nameValuePairs = new

            ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("byteValue",ba1));
            
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            
			/*ByteArrayBody bab = new ByteArrayBody(postData, "picture.jpg");
			MultipartEntity  multipartEntity=new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			multipartEntity.addPart("byteValue",bab );
			httpPost.setEntity(multipartEntity);*/
			// TimeOut Code Ends
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpResponse response = httpclient.execute(httpPost);
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if(dataClass!=null){
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						inputStream = entity.getContent();
						if (inputStream != null) {
							BufferedReader reader = new BufferedReader(	new InputStreamReader(inputStream,CommonConstraints.EncodingCode), 8);
							Gson gson = new Gson();						
							return gson.fromJson(reader, dataClass);
						}	
					}
				}
			}						

		}  catch (ClientProtocolException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.CLIENT_PROTOCOL_EXCEPTION;
		} catch (IllegalStateException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.ILLEGAL_STATE_EXCEPTION;
		} catch (UnsupportedEncodingException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.UNSUPPORTED_ENCODING_EXCEPTION;
		} catch (IOException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.IO_EXCEPTION;
		} catch (Exception e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.GENERAL_EXCEPTION;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				Log.e("log_tag", "Error in " + e.toString());
				CommonValues.getInstance().ExceptionCode = CommonConstraints.IO_EXCEPTION;
			}
		}		
		return null;
	}
	
public static Object retrieveDataFromJsonPost(String url,JSONObject jsonObject, Class<?> dataClass) {	
		
		CommonValues.getInstance().ExceptionCode = CommonConstraints.NO_EXCEPTION;
		InputStream inputStream = null;
		try {
			// code block for for server connection timeout..here it is set to 5
			// second..Tanvir
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_MILLISEC);
			HttpPost httpPost = new HttpPost(url);			
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			
			httpPost.setEntity(new StringEntity(jsonObject.toString(), CommonConstraints.EncodingCode));
			
			HttpResponse response = httpclient.execute(httpPost);
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if(dataClass!=null){
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						inputStream = entity.getContent();
						if (inputStream != null) {
							BufferedReader reader = new BufferedReader(	new InputStreamReader(inputStream,CommonConstraints.EncodingCode), 8);
							Gson gson = new Gson();						
							return gson.fromJson(reader, dataClass);
						}	
					}
				}
			}						

		}  catch (ClientProtocolException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.CLIENT_PROTOCOL_EXCEPTION;
		} catch (IllegalStateException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.ILLEGAL_STATE_EXCEPTION;
		} catch (UnsupportedEncodingException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.UNSUPPORTED_ENCODING_EXCEPTION;
		} catch (IOException e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.IO_EXCEPTION;
		} catch (Exception e) {
			Log.e("log_tag", "Error in " + e.toString());
			CommonValues.getInstance().ExceptionCode = CommonConstraints.GENERAL_EXCEPTION;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				Log.e("log_tag", "Error in " + e.toString());
				CommonValues.getInstance().ExceptionCode = CommonConstraints.IO_EXCEPTION;
			}
		}		
		return null;
	}
	
	public static Bitmap LoadChart(String urlRqs) {
		Bitmap bm = null;
		InputStream inputStream = null;

		try {
			inputStream = OpenHttpConnection(urlRqs);
			bm = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return bm;
	}

	private static InputStream OpenHttpConnection(String strURL) throws IOException {
		InputStream is = null;
		URL url = new URL(strURL);
		URLConnection urlConnection = url.openConnection();

		try {
			HttpURLConnection httpConn = (HttpURLConnection) urlConnection;
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				is = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			String ss = ex.getMessage();
			ss = ss + ss;
		}

		return is;
	}
	
	/**
	 * Using for Convert object to xml
	 * here we use SimpleXML library for this conversion and the below link explain why we are use this library
	 * http://stackoverflow.com/questions/6166862/best-practices-for-parsing-xml
	 * For MIME type we use application/xml for this conversion.Below link explain why MIME type is application/xml
	 * http://www.grauw.nl/blog/entry/489  
	 * @param xmlConvertingObj
	 * @return
	 */
	/*public static String retrieveXmlFromObject(Object xmlConvertingObj){
		String strXml="";
		try{
			Serializer serializer=new Persister(new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>"));
			StringWriter result = new StringWriter();
			serializer.write(xmlConvertingObj, result);
			strXml=result.toString();
			result=null;
			serializer=null;
		}catch (Exception e) {
			strXml="";
		}
		
		return strXml;
	}
	
	public static Object retrieveObjectFromXml(String strXml, Class<?> dataClass){	
		
		try{			
			Serializer serializer=new Persister(new Format("<?xml version=\"1.0\" encoding= \"UTF-8\" ?>"));			
			return serializer.read(dataClass,strXml);
			
		}catch (Exception e) {
			return null;
		}
	}*/
	
	
	public static String fixEncoding(String latin1) {
		try {
			byte[] bytes = latin1.getBytes("ISO-8859-1");
			if (!validUTF8(bytes))
				return latin1;
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Impossible, throw unchecked
			throw new IllegalStateException("No Latin1 or UTF-8: "
					+ e.getMessage());
		}

	}

	public static boolean validUTF8(byte[] input) {
		int i = 0;
		// Check for BOM
		if (input.length >= 3 && (input[0] & 0xFF) == 0xEF
				&& (input[1] & 0xFF) == 0xBB & (input[2] & 0xFF) == 0xBF) {
			i = 3;
		}

		int end;
		for (int j = input.length; i < j; ++i) {
			int octet = input[i];
			if ((octet & 0x80) == 0) {
				continue; // ASCII
			}

			// Check for UTF-8 leading byte
			if ((octet & 0xE0) == 0xC0) {
				end = i + 1;
			} else if ((octet & 0xF0) == 0xE0) {
				end = i + 2;
			} else if ((octet & 0xF8) == 0xF0) {
				end = i + 3;
			} else {
				// Java only supports BMP so 3 is max
				return false;
			}

			while (i < end) {
				i++;
				octet = input[i];
				if ((octet & 0xC0) != 0x80) {
					// Not a valid trailing byte
					return false;
				}
			}
		}
		return true;
	}
}
