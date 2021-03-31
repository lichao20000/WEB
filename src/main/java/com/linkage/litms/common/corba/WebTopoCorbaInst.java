package com.linkage.litms.common.corba;

import org.omg.CORBA.Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.DB;

import com.linkage.litms.LipossGlobals;

public class WebTopoCorbaInst extends CorbaInst {
	// private WebTopoManager wtInstance = null;
	private DB dbInstance = null;

	private static Logger log = LoggerFactory.getLogger(WebTopoCorbaInst.class);

	private String WEBTOPO_OBJECT_NAME = "RemoteDbServant";

	private String WEBTOPO_OBJECT_POA = "lmgDataSource_poaNoILOG3";

	public static Boolean isReBind = new Boolean(false);

	public WebTopoCorbaInst() {
		WEBTOPO_OBJECT_NAME = LipossGlobals
				.getLipossProperty("webtopo.MC_NAME");
		WEBTOPO_OBJECT_POA = LipossGlobals.getLipossProperty("webtopo.MC_POA");
		bind();
	}

	public boolean bind() {
		boolean result = true;
		dbInstance = null;
		synchronized (isReBind) {
			log.warn("绑定MasterControl,isReBind置为true");
			isReBind = new Boolean(true);
		}
		try {
//			log.debug(WEBTOPO_OBJECT_NAME + ":" + WEBTOPO_OBJECT_POA);
			String ior = getNameIor(WEBTOPO_OBJECT_NAME, WEBTOPO_OBJECT_POA);
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init((String[]) null,
					null);
			org.omg.CORBA.Object obj = orb.string_to_object(ior);
			DB remoteSvr = RemoteDB.DBHelper.narrow(obj);
			// String passStr = "";
			// wtInstance = remoteSvr..createWebTopoManager(passStr);

			dbInstance = remoteSvr;
			// 在绑定成功之后将ｓｅｓｓｉｏｎ的变量清楚掉

		} catch (Exception e) {
			log.error("获取WebTopo Corba接口失败：" + e.getMessage());
			result = false;
		}
		return result;
	}

	public Object getInstance() {
		return dbInstance;
	}
}
