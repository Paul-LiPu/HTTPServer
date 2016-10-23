package me.lipu.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import me.lipu.util.CloseUtil;

public class Request {
	private InputStream is;
	private String method;
	private String url;
	private Map<String, List<String>> parameterMap;
	private String requestInfo;

	public static final String CRLF = "\r\n";

	public Request() {
		super();
		this.method = "";
		this.url = "";
		this.parameterMap = new HashMap<String, List<String>>();
		this.requestInfo = "";
	}

	public Request(InputStream is) {
		this();
		this.is = is;
		try {
			byte[] data = new byte[20480];
			int len = is.read(data);
			requestInfo = new String(data, 0, len);
		} catch (IOException e) {
			return;
		}
		parseRequestInfo();
	}

	private void parseRequestInfo() {
		if (requestInfo == null
				|| (requestInfo = requestInfo.trim()).equals("")) {
			return;
		}

		String paramString = ""; 

		String firstLine = requestInfo.substring(0, requestInfo.indexOf(CRLF));
		int idx = requestInfo.indexOf("/");
		this.method = firstLine.substring(0, idx).trim();
		String urlStr = firstLine.substring(idx, firstLine.indexOf("HTTP/"))
				.trim();
		if (this.method.equalsIgnoreCase("post")) {
			this.url = urlStr;
			paramString = requestInfo.substring(requestInfo.lastIndexOf(CRLF))
					.trim();
		} else if (this.method.equalsIgnoreCase("get")) {
			if (urlStr.contains("?")) {
				String[] urlArray = urlStr.split("\\?");
				this.url = urlArray[0];
				paramString = urlArray[1];
			} else {
				this.url = urlStr;
			}
		}

		if (paramString.equals("")) {
			return;
		}
		
		parseParams(paramString);
	}

	private void parseParams(String paramString) {
		StringTokenizer token = new StringTokenizer(paramString, "&");
		while (token.hasMoreTokens()) {
			String keyValue = token.nextToken();
			String[] keyValues = keyValue.split("=");
			if (keyValues.length == 1) {
				keyValues = Arrays.copyOf(keyValues, 2);
				keyValues[1] = null;
			}

			String key = keyValues[0].trim();
			String value = null == keyValues[1] ? null : decode(
					keyValues[1].trim(), "gbk");
			// 转换成Map 分拣
			if (!parameterMap.containsKey(key)) {
				parameterMap.put(key, new ArrayList<String>());
			}

			List<String> values = parameterMap.get(key);
			values.add(value);
		}
	}

	private String decode(String value, String code) {
		try {
			return java.net.URLDecoder.decode(value, code);
		} catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据页面的name 获取对应的多个值
	 * 
	 * @param args
	 */
	public String[] getParameterValues(String name) {
		List<String> values = null;
		if ((values = parameterMap.get(name)) == null) {
			return null;
		} else {
			return values.toArray(new String[0]);
		}
	}

	/**
	 * 根据页面的name 获取对应的单个值
	 * 
	 * @param args
	 */
	public String getParameter(String name) {
		String[] values = getParameterValues(name);
		if (null == values) {
			return null;
		}
		return values[0];
	}

	public String getUrl() {
		return url;
	}

	public void close() {
		CloseUtil.closeIO(is);
	}
	
	public String getMethod(){
		return this.method;
	}

}
