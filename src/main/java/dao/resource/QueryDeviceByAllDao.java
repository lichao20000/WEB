/**
 * 
 */
package dao.resource;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.util.JdbcTemplateExtend;

/**
 * @author zxj E-mail：qixq@lianchuang.com
 * @version $Revision$ 
 * @since 2009-4-9
 * @category dao.resource
 * 
 */
public class QueryDeviceByAllDao {

	private JdbcTemplateExtend jt;
	private static final Logger LOG = LoggerFactory.getLogger(QueryDeviceByAllDao.class);
	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}
	
	/**
	 * 根据传入的至少六位参数查询所有属地的设备并返回
	 * 
	 * @param device_serialnumber
	 * @return
	 */
	public List getDeviceByAllCityInBBMS(String device_serialnumber){
		
		if(device_serialnumber==null||"".equals(device_serialnumber)){
			LOG.debug("QueryDeviceByAllDao=>getDeviceByAllCityInBBMS=>device_serialnumber为空");
			return new ArrayList();
		}
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.device_serialnumber,a.city_name,a.x_com_passwd, b.username from "
				+ "(select aa.device_id,aa.device_serialnumber,bb.city_name,aa.oui,aa.x_com_passwd from tab_gw_device aa,tab_city bb where aa.city_id=bb.city_id ");
		if(device_serialnumber.length()>5){
			sql.append(" and aa.dev_sub_sn ='").append(device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length())).append("'");
		}
		sql.append(" and aa.device_serialnumber like '%");
		sql.append(device_serialnumber);
		sql.append("') a left join tab_egwcustomer b on a.device_id = b.device_id and b.user_state in ('1','2')");
		
		return jt.queryForList(sql.toString());
	}
}
