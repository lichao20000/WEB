package com.linkage.module.gwms.util;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jason(3412)
 * @date 2009-9-28
 */
public class FormTagUtil {

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(FormTagUtil.class);

	/**
	 * 获取List<Map<name,value>>的下拉列表Select标签；
	 * 
	 * @param name为
	 *            <select name; value
	 * @author Jason(3412)
	 * @date 2009-9-28
	 * @return String
	 */
	public static String getHtmlSelect(List someList, String name, String value,
			String text, String onclickMethod) {
		logger.debug("getHtmlSelect({},{},{},{},{})", new Object[] {
				someList, name, value, text, onclickMethod });
		StringBuffer htmlStr = new StringBuffer();
		if (null == onclickMethod) {
			// 表示不触发任何方法
			htmlStr.append("<SELECT NAME=" + name + " CLASS=bk >");
		} else {
			htmlStr.append("<SELECT NAME=" + name
					+ " CLASS=bk onchange=showChild('" + onclickMethod + "')>");
		}
		htmlStr.append("<OPTION VALUE=-1>==请选择==</OPTION>");
		if (someList == null || someList.isEmpty()) {
			logger.debug("someList is empty");
		} else {
			int size = someList.size();
			for (int i = 0; i < size; i++) {
				Map tmap = (Map) someList.get(i);
				if(tmap != null && false == tmap.isEmpty()) {
					String tmp = StringUtil.getStringValue(tmap.get(value));
					String txt = StringUtil.getStringValue(tmap.get(text));
					htmlStr.append("<OPTION VALUE='" + tmp + "'>--" + txt
							+ "--</OPTION>");
				}
			}
		}

		htmlStr.append("</SELECT>");
		return htmlStr.toString();
	}
}
