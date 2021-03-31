package com.linkage.litms.uss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildHTML {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(BuildHTML.class);
	public static String html = "";
	public static String head = "";
	public static String body = "";
	public static String body_ = "";
	public static String html_ = "";
	public static String BLANK = "";
	static {
		BLANK = "<span></span>";

		html += "<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.0 Transitional//EN'>";
		html += "<link href='../css/tags.css' rel='stylesheet' type='text/css'>";
		html += "<HTML>";
		head += "<HEAD>";
		head += "<TITLE></TITLE>";
//		head += "<style type='text/css'>";
//		head += "<!--";
//		head += "BODY.bd {BACKGROUND-COLOR: #ffffff; COLOR: #000000; FONT-FAMILY: '宋体','Arial'; FONT-SIZE: 12px; MARGIN: 0px}";
//		head += "TH.thh {background-color: #B0E0E6;font-size: 9pt;color: #000000;text-decoration: none;font-weight: bold;line-height: 22px;text-align: center;}";
//		head += "TR.trr {background-color:'#FFFFFF';}";
//		head += "TD.tdd {FONT-FAMILY: '宋体', 'Tahoma'; FONT-SIZE: 12px;background-color:'#FFFFFF';}";
//		head += "TD.hd {background-color:'#EEE8AA';}";
//		head += "-->";
//		head += "</style>";
		head += "</HEAD>";
		
		//为“客户相关信息”增加“changParentHeight()”
		body += "<BODY class='bd'>";
		
		body += "<table width='100%' cellspacing='0' cellpadding='0'>";

		// body += "<P class=topTit>报表选项</P>";
		// body += "<P class=topC0>终端</P>";
		// body += "<P class=topC0>用户</P>";
		// body += "<P class=topC0>客户</P>";
		// body += "<P class=topC0>在线情况</P>";
		// body += "<P class=topC0>WLAN/LAN</P>";
		// body += "<P class=topC0>告警</P>";
		// body += "<P class=topC0>流量</P>";
		// body += "<P class=topC0>病毒</P>";

		body_ += "</table>";
		body_ += "</BODY>";

		html_ += "</HTML>";

	}

	public static String getBlank() {
		return BLANK;
	}

	public static String getBlank(String s) {
		return "<span>" + s + "</span>";
	}

	public static String getNonCustomize(String title) {
		String html = "<tr><td>";
		html += "<table width='99%' border=0 align='left' cellpadding='1' cellspacing='1' bgcolor='#999999'";
		html += "<th class='thh'>" + title + "</th>";
		html += "<tr class='trr'>";
		html += "<td align='center'>" + "(未定制)" + "</td>";
		html += "</tr></table>";
		html += "</td></tr>";
		return html;
	}

	public static String getComplete(String title, int cols, String content) {
		String html = "<tr><td>";
		html += "<table width=775 border=0 align='center' cellpadding='1' cellspacing='1' bgcolor='#FFFFFF'>";
		html += "<th class='thh' colspan='" + cols + "'>" + title + "</th>";
		html += content;
		html += "</table>";
		html += "</td></tr>";
		return html;
	}

	/**
	 * td
	 * 
	 * @param text
	 * @return
	 */
	public static String td(String text) {
		return "<td class='tdd'>" + (null == text ? "" : text) + "</td>";
	}

	/**
	 * td class=?
	 * 
	 * @param text
	 * @param style
	 * @return
	 */
	public static String td_h(String text) {
		return "<td class='hd'>" + (null == text ? "" : text) + "</td>";
	}

	/**
	 * 对应于样式表中的tdd_B
	 * 
	 * @param text
	 * @return
	 */
	public static String td_B(String text) {
		return "<td class='tdd_B'>" + (null == text ? "" : text) + "</td>";
	}
	
	/**
	 * td(带格式控制参数)
	 * 
	 * @param text
	 * @param align
	 * @param cols
	 * @param width
	 * @return
	 */
	public static String td(String text, String align, String cols, String width) {
		return "<td class='tdd'"
				+ ("".equals(align) ? "" : " align='" + align + "'")
				+ ("".equals(cols) ? "" : " colspan='" + cols + "'")
				+ ("".equals(width) ? "" : " width='" + width + "'") + ">"
				+ text + "</td>";
	}

	/**
	 * td(带格式控制参数)
	 * 
	 * @param text
	 * @param align
	 * @param cols
	 * @param width
	 * @return
	 */
	public static String td(String text, String align, String cols, String width, String css_class) {
		return "<td class='"+css_class+"'"
				+ ("".equals(align) ? "" : " align='" + align + "'")
				+ ("".equals(cols) ? "" : " colspan='" + cols + "'")
				+ ("".equals(width) ? "" : " width='" + width + "'") + ">"
				+ text + "</td>";
	}
	
	/**
	 * TH
	 * @param tds
	 * @return
	 */
	public static String th(String... tds) {
		String html = "<tr class='trh'>";
		if (tds != null && 0 < tds.length) {
			for (String td : tds) {
				html += td;
			}
		}
		html += "</tr>";
		return html;
	}
	/**
	 * tr(接受可变参数)
	 * 
	 * @param tds
	 * @return
	 */
	public static String tr(String... tds) {
		String html = "<tr class='trr'>";
		if (tds != null && 0 < tds.length) {
			for (String td : tds) {
				html += td;
			}
		}
		html += "</tr>";
		return html;
	}

	/**
	 * table(接受可变参数)
	 * 
	 * @param trs
	 * @return
	 */
	public static String table(String... trs) {
		String html = "<table width=775 border=0 align='center' cellpadding='1' cellspacing='1' bgcolor='#999999'>";
		if (trs != null && 0 < trs.length) {
			for (String tr : trs) {
				html += tr;
			}
		}
		html += "</table>";
		return html;
	}

	/**
	 * span(接受可变参数)
	 * 
	 * @param objs
	 * @return
	 */
	public static String span(String... objs) {
		String html = "<span>";
		if (objs != null && 0 < objs.length) {
			for (String obj : objs) {
				html += obj;
			}
		}
		html += "</span>";
		return html;
	}

	/**
	 * 取得一个下拉框
	 * @param name
	 * @param options
	 * @return
	 */
	public static String select(String name, String[] options, String[] values, String onselected) {
		String html = "<select name='"+name+"' onchange='"+onselected+"()'>";
		if (null != options && options.length > 0) {
			for (int i=0; i<options.length; i++) {
				html += "<option value='"+values[i]+"'>"+options[i]+"</option>";
			}
		}
		html += "</select>";
		return html;
	}
	/**
	 * html(生成数据报表页面)
	 * 
	 * @param dataHtml
	 * @return
	 */
	public static String html(String dataHtml) {
		return html + head + body + dataHtml + body_ + html_;
	}

	public static void main(String[] args) {
		logger.debug(html + head + body + body_ + html_);
	}
}
