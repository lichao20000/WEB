package com.linkage.litms.common.util;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * <p>Title: 报表制作</p>
 * <p>Description: 联创报表</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 联创集团</p>
 * @author 苏智荣
 * @version 1.0
 */

public class StringSeries {

	public StringSeries() {
	}

	/**
	 * 替换字符串stringSplit(String source,String replace)
	 */
	public String replace(String parentStr,String ch,String rep) {
		int i = parentStr.indexOf(ch);
		StringBuffer sb = new StringBuffer();
		if (i == -1){
			return parentStr;
		}
		sb.append(parentStr.substring(0,i) + rep);
		if (i+ch.length() < parentStr.length()){
			sb.append(replace(parentStr.substring(i+ch.length(),parentStr.length()),ch,rep));
		}
		return sb.toString();
	}

	//获取源字符串和解析字符串，并解析分割后输出
	public Vector splitString(String source,String ch) throws Exception{
		Vector v=new Vector();
		v.removeAllElements();

		String elementSelectedNameValues=source;
		StringTokenizer st=new StringTokenizer(elementSelectedNameValues,ch);
		while(st.hasMoreTokens()){
			v.add(st.nextToken());
		}

		return v;
	}
}