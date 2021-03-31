package com.linkage.litms.uss;

public class AllTrTds {
	/**
	 * td
	 * 
	 * @param text
	 * @return
	 */
	protected String td(String text) {
		return BuildHTML.td(text);
	}
	
	protected String td_h(String text) {
		return BuildHTML.td_h(text);
	}

	protected String td_B(String text) {
		return BuildHTML.td_B(text);
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
	protected String td(String text, String align, String cols, String width) {
		return BuildHTML.td(text, align, cols, width);
	}

	/**
	 * td(带格式控制参数)，可自定义CSS样式
	 * 
	 * @param text
	 * @param align
	 * @param cols
	 * @param width
	 * @param css_class
	 * @return
	 */
	protected String td(String text, String align, String cols, String width, String css_class) {
		return BuildHTML.td(text, align, cols, width, css_class);
	}
	
	/**
	 * tr(接受可变参数)
	 * 
	 * @param tds
	 * @return
	 */
	protected String tr(String... tds) {
		return BuildHTML.tr(tds);
	}

	/**
	 * th
	 * @param tds
	 * @return
	 */
	protected String th(String... tds) {
		return BuildHTML.th(tds);
	}

	
	/**
	 * table(接受可变参数)
	 * 
	 * @param trs
	 * @return
	 */
	protected String table(String... trs) {
		return BuildHTML.table(trs);
	}

	/**
	 * span(接受可变参数)
	 * 
	 * @param objs
	 * @return
	 */
	protected String span(String... objs) {
		return BuildHTML.span(objs);
	}

	protected String select(String name, String[] options, String[] values, String onselected) {
		return BuildHTML.select(name, options, values, onselected);
	}
	
	/**
	 * html(生成数据报表页面)
	 * 
	 * @param dataHtml
	 * @return
	 */
	protected String html(String dataHtml) {
		return BuildHTML.html(dataHtml);
	}
}
