package com.linkage.module.itms.resource.act;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ailk.tr069.acsalive.obj.conf.AcsAliveConfMapOBJ;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.resource.bio.QueryCurrentAcsBIO;

/**
 * 小工具获取调用的ACS
 * @author wangyan10
 * @since 2017-1-6 下午2:54:18
 */
public class QueryCurrentAcsACT {
	private static Logger logger = LoggerFactory
			.getLogger(QueryCurrentAcsACT.class);
	private String deviceId;
	private String devSn;
	private String systemKey;
	private String ajax = "";
	private QueryCurrentAcsBIO bio;

	/**
	 * 获取gatherId
	 * @author wangyan10
	 * @return
	 * @since 2017-1-6 下午2:57:21
	 */
	public String getGatherId() {
		logger.warn("deviceId-------------" + deviceId);
		logger.warn("devSn-------------" + devSn);
		systemKey ="ITMS_ACS";
		logger.warn("systemKey-------------" + systemKey);
		if (null == AcsAliveConfMapOBJ.getInstance().getObjByKey(systemKey)
				|| null == AcsAliveConfMapOBJ.getInstance()
						.getObjByKey(systemKey).getAcsGatherIdList()
				|| AcsAliveConfMapOBJ.getInstance().getObjByKey(systemKey)
						.getAcsGatherIdList().isEmpty()) {
			logger.warn("The paramter: acsList is empty or null");
			ajax = "acsList is empty or null";
		} else {
			// 获取deviceId
			if ((null != devSn) && (!"".equals(devSn))) {
				deviceId = bio.getDeviceId(devSn);
				if (deviceId == null || "".equals(deviceId)){
					ajax = "根据devSn找不到相应的设备";
					return "ajax";
				}
			} else {
				List<HashMap<String, String>> list = bio
						.checkDeviceId(deviceId);
				// 根据入参的deviceId去设备表去查，如果查不到的话，就报设备不存在
				if (list == null || list.isEmpty()) {
					ajax = "根据deviceId找不到相应的设备";
					return "ajax";
				}

			}
			int index = StringUtil.getIntegerValue(hash(AcsAliveConfMapOBJ
					.getInstance().getObjByKey(systemKey).getAcsGatherIdList(),
					deviceId));
			logger.debug("[{}]getACS==>index[{}]", deviceId, index);
			String gatherId = AcsAliveConfMapOBJ.getInstance()
					.getObjByKey(systemKey).getAcsGatherIdList().get(index);
			if (gatherId == null || StringUtil.IsEmpty(gatherId)) {
				logger.error("no ACS alive");
				ajax = "no ACS alive";
			} else {
				ajax = systemKey+"_"+gatherId;
			}
		}
		logger.warn("ajax-------------" + ajax);
		return "ajax";
	}

	public static long hash(List<String> servNodes, String servKey) {
		return StringUtil.getLongValue(servKey) % servNodes.size();
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getSystemKey() {
		return systemKey;
	}

	public void setSystemKey(String systemKey) {
		this.systemKey = systemKey;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public QueryCurrentAcsBIO getBio() {
		return bio;
	}

	public void setBio(QueryCurrentAcsBIO bio) {
		this.bio = bio;
	}

	public String getDevSn() {
		return devSn;
	}

	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}

}
