package com.linkage.litms.midware;

import java.io.PrintWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLBuilder {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(XMLBuilder.class);
	private PrintWriter out;
	private String id;
	private Map map;
	private InterID interID;

	public XMLBuilder(Map map) {
		Object id_ = map.get("id");
		Object pram_ = map.get("pram");
		if (id_ != null && pram_ != null) {
			this.id = (String) id_;
			this.map = (Map) pram_;
			interID = InterID.valueOf(this.id);
		} else {
			logger.warn("[中间件管理][XMLBuilder] 参数不完整！");
		}
	}

	public String getXML() {
		if (interID != null) {
			switch (interID) {
				case D1 :
					return deviceAdd();
				case D2 :
					return deviceChangeInfo();
				case D3 :
					return deviceDelete();
				case B1 :
					return bussinessNew();
				case B2 :
					return bussinessLock();
				case B3 :
					return bussinessUnlock();
				case B4 :
					return bussinessUpdateParm();
				case B5 :
					return bussinessClose();
				case B6 :
					return bussinessUpgrade();
				case C1 :
					return areaInfoUpdate();
				case C2 :
					return csrUpdate();
				case C3 :
					return areaInfoDelete();
				default :
					return "未知功能";
			}
		}
		return "";
	}

	public int getRETcode(String xml) {
		if (xml != null && !xml.equals("")) {
			String str = "RETcode";
			int len = str.length();
			int i = xml.indexOf(str);
			int j = xml.lastIndexOf(str);
			if (i != -1 && j != -1 && i < j) {
				String rst = xml.substring(i + len + 1, j - 2);
				return Integer.parseInt(rst);
			}
		}
		return -999;
	}
		
	private String deviceAdd() {
		String s = "";
		s += head();
		s += node("OUI", (String) map.get("vendor_type"));
		s += node("SN", (String) map.get("device_serial"));
		//s += node("TYPE", (String) map.get("device_type"));
		s += node("TYPE", "e8");
		s += node("AREA", (String) map.get("device_area"));
		s += node("GROUP", (String) map.get("device_group_attribute"));
		s += node("PHONE", (String) map.get("phone_number"));
		s += node("STATUS", (String) map.get("status"));
		s += foot();
		return s;
	}

	private String deviceChangeInfo() {
		return deviceAdd();
	}

	private String deviceDelete() {
		String s = "";
		s += head();
		s += node("OUI", (String) map.get("vendor_type"));
		s += node("SN", (String) map.get("device_serial"));
		s += foot();
		return s;
	}

	private String bussinessNew() {
		String s = "";
		s += head();
		s += tag("DEV");
		s += node("OUI", (String) map.get("vendor_type"));
		s += node("SN", (String) map.get("device_serial"));
		s += tag_("DEV");
		s += node("MODNAME", (String) map.get("mod_name"));
		s += node("MODID", (String) map.get("mod_id"));
		s += tag("ACCOUNT");
		s += node("USERNAME", (String) map.get("username"));
		s += node("PASSWORD", (String) map.get("password"));
		s += tag_("ACCOUNT");
		s += tag("PARMS");
		Map<String, String[]> params = (Map) map.get("parms");
		for (Map.Entry<String, String[]> m : params.entrySet()) {
			s += node(m.getKey(), m.getValue()[0], m.getValue()[1]);
		}
		s += tag_("PARMS");
		s += foot();
		return s;
	}

	private String bussinessLock() {
		String s = "";
		s += head();
		s += tag("DEV");
		s += node("OUI", (String) map.get("vendor_type"));
		s += node("SN", (String) map.get("device_serial"));
		s += tag_("DEV");
		s += node("MODNAME", (String) map.get("mod_name"));
		s += node("MODID", (String) map.get("mod_id"));
		s += foot();
		return s;
	}

	private String bussinessUnlock() {
		return bussinessLock();
	}

	private String bussinessUpdateParm() {
		return bussinessNew();
	}

	private String bussinessClose() {
		return bussinessLock();
	}

	private String bussinessUpgrade() {
		String s = "";
		s += head();
		s += tag("DEV");
		s += node("OUI", (String) map.get("vendor_type"));
		s += node("SN", (String) map.get("device_serial"));
		s += tag_("DEV");
		s += node("MODNAME", (String) map.get("mod_name"));
		s += node("MODID", (String) map.get("mod_id"));
		s += tag("PARMS");
		Map<String, String[]> params = (Map) map.get("parms");
		for (Map.Entry<String, String[]> m : params.entrySet()) {
			s += node(m.getKey(), m.getValue()[0], m.getValue()[1]);
		}
		s += tag_("PARMS");
		s += foot();
		return s;
	}

	private String areaInfoUpdate() {
		String s = "";
		s += head();
		s += node("area", (String) map.get("area"));
		s += node("list", (String) map.get("device_list"));
		s += foot();
		return s;
	}

	private String csrUpdate() {
		String s = "";
		s += head();
		s += node("login", (String) map.get("username"));
		s += node("password", (String) map.get("password"));
		s += node("permit", (String) map.get("operation_list"));
		s += node("area", (String) map.get("area"));
		s += node("status", (String) map.get("status"));
		s += foot();
		return s;
	}

	private String areaInfoDelete() {
		return areaInfoUpdate();
	}

	private String head() {
		return xml() + tag("DOC") + node("ID", id) + node("DES", interID.getDescription()) + tag("PARM");
	}

	private String foot() {
		return tag_("PARM") + tag_("DOC");
	}

	private String xml() {
		return "<?xml version=\"1.0\"?>";
	}

	private String tag(String tag) {
		return "<" + tag + ">";
	}

	private String tag_(String tag) {
		return "</" + tag + ">";
	}

	private String attr(String attributeType) {
		return " AttributeType=\"" + attributeType + "\"";
	}
	private String node(String tag, String content) {
		return "<" + tag + ">" + content + "</" + tag + ">";
	}

	private String node(String tag, String attr, String content) {
		return "<" + tag + attr(attr) + ">" + content + "</" + tag + ">";
	}

}
