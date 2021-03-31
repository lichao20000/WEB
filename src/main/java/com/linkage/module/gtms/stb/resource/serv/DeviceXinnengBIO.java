/**
 * 
 */
package com.linkage.module.gtms.stb.resource.serv;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.gtms.stb.resource.dao.DeviceXinnengDAO;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.corba.SuperGatherCorba;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.bio
 * 
 */
public class DeviceXinnengBIO {
	//日志记录
	private static Logger logger = LoggerFactory.getLogger(DeviceXinnengBIO.class);
	private SuperGatherCorba sgCorba = null;
	
	private DeviceXinnengDAO xinnengDao = null;
	
	/**
	 * 查询设备详细信息，根据权限过滤
	 * @param deviceId
	 * @param areaId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> queryDeviceXinneng(String deviceId){
		logger.debug("GwDeviceQueryBIO=>queryDeviceXinneng(deviceId:{})",deviceId);
		
		Map<String,String> rsMap = new HashMap<String,String>();
		
		int rs1 = sgCorba.getCpeParams(deviceId, 4, 1);
		int rs2 = sgCorba.getCpeParams(deviceId, 5, 1);
		int rs3 = sgCorba.getCpeParams(deviceId, 6, 1);
		
		// 失败
		if(rs1 <= 0)
		{	
			logger.debug("终端:{}采集失败！",deviceId);
			// 直接返回失败码
			rsMap.put("error", Global.G_Fault_Map.get(rs1).getFaultReason());
		}
		else if(rs2 <= 0)
		{
			logger.debug("终端:{}采集失败！",deviceId);
			// 直接返回失败码
			rsMap.put("error", Global.G_Fault_Map.get(rs2).getFaultReason());
		}
		else if(rs3 <= 0)
		{
			logger.debug("终端:{}采集失败！",deviceId);
			// 直接返回失败码
			rsMap.put("error", Global.G_Fault_Map.get(rs3).getFaultReason());
		}
		/**
		if((rs1+rs2+rs3)<1){
			logger.debug("终端:{}采集失败！",deviceId);
			rsMap.put("error", "信息采集失败,设备可能不在线！");
		}
		**/
		else{
			List stbQos = xinnengDao.getStbQos(deviceId);
			if(stbQos.size()>0){
				Map tempMap = (Map)stbQos.get(0);
				//参数统计重置
				String resetStatistics = String.valueOf(tempMap.get("reset_statistics"));
				//收到的包
				String packetsReceived = String.valueOf(tempMap.get("packets_received"));
				//丢包数
				String packetsLost = String.valueOf(tempMap.get("packets_lost"));
				//接收字节数
				String bytesReceived = String.valueOf(tempMap.get("bytes_received"));
				//丢包率
				String fractionLost = String.valueOf(tempMap.get("fraction_lost"));
				//码率
				String bitrate = String.valueOf(tempMap.get("bitrate"));
				if(null==resetStatistics || "null".equals(resetStatistics)){
					rsMap.put("reset_statistics", "");
				}else{
					rsMap.put("reset_statistics", resetStatistics);
				}
				if(null==packetsReceived || "null".equals(packetsReceived)){
					rsMap.put("packets_received", "");
				}else{
					rsMap.put("packets_received", packetsReceived);
				}
				if(null==packetsLost || "null".equals(packetsLost)){
					rsMap.put("packets_lost", "");
				}else{
					rsMap.put("packets_lost", packetsLost);
				}
				if(null==bytesReceived || "null".equals(bytesReceived)){
					rsMap.put("bytes_received", "");
				}else{
					rsMap.put("bytes_received", bytesReceived);
				}
				if(null==fractionLost || "null".equals(fractionLost)){
					rsMap.put("fraction_lost", "");
				}else{
					rsMap.put("fraction_lost", topercent(fractionLost));
				}
				if(null==bitrate || "null".equals(bitrate)){
					rsMap.put("bitrate", "");
				}else{
					rsMap.put("bitrate", bitrate);
				}
			}
			List stbXIptv = xinnengDao.getStbXIptv(deviceId);
			if(stbXIptv.size()>0){
				Map tempMap = (Map)stbXIptv.get(0);
				//机顶盒ID
				String stbId = String.valueOf(tempMap.get("stb_id"));
				//总RAM 大小
				String phymemSize = String.valueOf(tempMap.get("phy_mem_size"));
				//存储大小
				String storageSize = String.valueOf(tempMap.get("storage_size"));
				if(null==stbId || "null".equals(stbId)){
					rsMap.put("stb_id", "");
				}else{
					rsMap.put("stb_id", stbId);
				}
				if(null==phymemSize || "null".equals(phymemSize)){
					rsMap.put("phy_mem_size", "");
				}else{
					rsMap.put("phy_mem_size", phymemSize);
				}
				if(null==storageSize || "null".equals(storageSize)){
					rsMap.put("storage_size", "");
				}else{
					rsMap.put("storage_size", storageSize);
				}
				
			}
			List stbVideoPhone = xinnengDao.getStbVideoPhone(deviceId);
			if(stbVideoPhone.size()>0){
				Map tempMap = (Map)stbVideoPhone.get(0);
				//丢包数
				String pmLossPacketsNum = String.valueOf(tempMap.get("pm_loss_packets_num"));
				//比特率
				String pmBitRrate = String.valueOf(tempMap.get("pm_bit_rate"));
				//媒体丢包率
				String pmLostRate = String.valueOf(tempMap.get("pm_lost_rate"));
				//最小DF
				String minDf = String.valueOf(tempMap.get("min_df"));
				//平均DF
				String avgDf = String.valueOf(tempMap.get("avg_df"));
				//最大DF
				String maxDf = String.valueOf(tempMap.get("max_df"));
				//抖动，DF的最大和最小值之差
				String dithering = String.valueOf(tempMap.get("dithering"));

				if(null==pmLossPacketsNum || "null".equals(pmLossPacketsNum)){
					rsMap.put("pm_loss_packets_num", "");
				}else{
					rsMap.put("pm_loss_packets_num", pmLossPacketsNum);
				}
				if(null==pmBitRrate || "null".equals(pmBitRrate)){
					rsMap.put("pm_bit_rate", "");
				}else{
					rsMap.put("pm_bit_rate", pmBitRrate);
				}
				if(null==pmLostRate || "null".equals(pmLostRate)){
					rsMap.put("pm_lost_rate", "");
				}else{
					rsMap.put("pm_lost_rate", topercent(pmLostRate));
				}
				if(null==minDf || "null".equals(minDf)){
					rsMap.put("min_df", "");
				}else{
					rsMap.put("min_df", minDf);
				}
				if(null==avgDf || "null".equals(avgDf)){
					rsMap.put("avg_df", "");
				}else{
					rsMap.put("avg_df", avgDf);
				}
				if(null==maxDf || "null".equals(maxDf)){
					rsMap.put("max_df", "");
				}else{
					rsMap.put("max_df", maxDf);
				}
				if(null==dithering || "null".equals(dithering)){
					rsMap.put("dithering", "");
				}else{
					rsMap.put("dithering", dithering);
				}
			}
		}
		return rsMap;
	}
	
	public String topercent(String num)
	{
		double p = StringUtil.getDoubleValue(num);
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		String str = nf.format(p/100);
		return str;
	}
	
	/**
	 * @return the sgCorba
	 */
	public SuperGatherCorba getSgCorba() {
		return sgCorba;
	}

	/**
	 * @param sgCorba the sgCorba to set
	 */
	public void setSgCorba(SuperGatherCorba sgCorba) {
		this.sgCorba = sgCorba;
	}

	/**
	 * @return the xinnengDao
	 */
	public DeviceXinnengDAO getXinnengDao() {
		return xinnengDao;
	}

	/**
	 * @param xinnengDao the xinnengDao to set
	 */
	public void setXinnengDao(DeviceXinnengDAO xinnengDao) {
		this.xinnengDao = xinnengDao;
	}
	
	
}
