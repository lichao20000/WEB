/**
 * 
 */
package com.linkage.module.gtms.stb.resource.dao;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-12-25
 * @category com.linkage.module.stb.resource.dao
 * 
 */
public class DeviceXinnengDAO {
	
	 //日志记录
	private static Logger logger = LoggerFactory
				.getLogger(DeviceXinnengDAO.class);

	private JdbcTemplateExtend jt;

	/**
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}
	
	public List getStbQos(String deviceId){
		logger.debug("deviceId=>getStbQos({})",deviceId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(" select device_id,reset_statistics,packets_received," +
				" packets_lost,bytes_received,fraction_lost,bitrate " +
				" from stb_qos where device_id=?");
		pSQL.setString(1, deviceId);
		return jt.queryForList(pSQL.getSQL());		
	}
	
	public List getStbXIptv(String deviceId){
		logger.debug("deviceId=>getStbXIptv({})",deviceId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(" select device_id,stb_id,phy_mem_size," +
				" storage_size from stb_x_iptv where device_id=?");
		pSQL.setString(1, deviceId);
		return jt.queryForList(pSQL.getSQL());		
	}
	
	public List getStbVideoPhone(String deviceId){
		logger.debug("deviceId=>getStbXIptv({})",deviceId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(" select device_id,pm_loss_packets_num,pm_bit_rate," +
				" pm_lost_rate,min_df,avg_df,max_df,dithering from " +
				" stb_pminfo where device_id=? ");
		pSQL.setString(1, deviceId);
		return jt.queryForList(pSQL.getSQL());
	}
	
	public List getStbXServiceStat(String deviceId){
		logger.debug("deviceId=>getStbXServiceStat({})",deviceId);
		PrepareSQL pSQL = new PrepareSQL();
		pSQL.setSQL(" select device_id,start_point,end_point," +
				" auth_numbers ,auth_fail_numbers ,auth_fail_info ," +
				" multi_req_numbers ,multi_fail_numbers,vod_req_numbers," +
				" vod_fail_numbers,http_req_numbers,http_fail_numbers," +
				" muti_abend_numbers,vod_abend_numbers,play_error_numbers from " +
				" stb_x_service_stat where device_id=? ");
		pSQL.setString(1, deviceId);
		return jt.queryForList(pSQL.getSQL());
	}
}
