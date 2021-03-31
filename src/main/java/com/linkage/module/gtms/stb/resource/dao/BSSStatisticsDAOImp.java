package com.linkage.module.gtms.stb.resource.dao;

/**
 * 零配件BSS工单统计dao类
 *
 * @author 田启明
 * @version 1.0
 * @since 2011-12-02
 * @category com.linkage.module.itv.zeroconf.dao<br>
 *           版权：南京联创科技 网管科技部
 */
import java.util.List;
import java.util.Map;

import com.linkage.litms.common.database.PrepareSQL;
import org.apache.log4j.Logger;

import com.linkage.module.gtms.stb.resource.dto.BSSStatisticsDTO;
import com.linkage.module.gtms.stb.utils.ResTool;
import com.linkage.module.liposs.system.basesupport.BaseSupportDAO;

public class BSSStatisticsDAOImp extends BaseSupportDAO implements BSSStatisticsDAO{

	public static Logger log =Logger.getLogger(BSSStatisticsDAOImp.class);

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.lims.itv.zeroconf.dao.BSSStatisticsDAO#getSQL(com.linkage.module.lims.itv.zeroconf.dto.BSSStatisticsDTO)
	 */
	public String getSQL(BSSStatisticsDTO dto){

		StringBuilder sql=new StringBuilder(100);
		sql.append("select t.city_name,").
		append("  r.serv_account,r.oper_type,r.prod_id,r.bss_date , r.addressing_type from  ")
		.append("  tab_city as t ,stb_bss_report as r where   ")
		.append("  t.city_id= r.city_id   ");

		//属地
		if(dto.getSubordinate()!=null && dto.getSubordinate()==1){

			//获取所有子属地
			List<Map> cities= ResTool.getSubCityList(dto.getCityId(), -1, true);
			String ids="";

			for(Map map : cities){
				if(map.get("city_id")!=null){
					ids+="'"+map.get("city_id").toString()+"',";
				}
			}

			if(ids.length()>0){
				sql.append("   and t.city_id in("+ids.substring(0,ids.length()-1)+")  ");
			}
		}else if( dto.getSubordinate()!=null && dto.getSubordinate()==2){
			sql.append("  and  t.city_id='"+dto.getCityId()+"'  ");
		}


		//业务账号
		if(dto.getServAccount()!=null && dto.getServAccount().length()>0){
			sql.append("  and r.serv_account='"+dto.getServAccount()+"' ");
		}

		//产品id
		if(dto.getProdId()!=null && dto.getProdId().length()>0){
			sql.append("  and r.prod_id='"+dto.getProdId()+"' ");
		}

		//操作类型
		if(dto.getOpera()!=null && dto.getOpera().length()>0){
			sql.append("  and r.oper_type in("+dto.getOpera()+") ");
		}

		//开始时间和结束时间
		if(dto.getBeginTime()>0 && dto.getEndTime()>0){
			sql.append(" and ( r.bss_date >= "+dto.getBeginTime()+" ");
			sql.append(" and  r.bss_date <  "+dto.getEndTime()+" ) ");
		}else if(dto.getBeginTime()>0){
			sql.append(" and  r.bss_date>"+dto.getBeginTime()+" ");
		}else if(dto.getEndTime()>0){
			sql.append(" and r.bss_date<"+dto.getEndTime()+" ");
		}

		return sql.toString();

	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.lims.itv.zeroconf.dao.BSSStatisticsDAO#queryTotalNum(com.linkage.module.lims.itv.zeroconf.dto.BSSStatisticsDTO)
	 */
	public int queryTotalNum(BSSStatisticsDTO dto){
		String sql="select count(*) from  ("+getSQL(dto)+") num";
		log.info("获取总有多少务零配置BSS工单"+sql);
		PrepareSQL psql = new PrepareSQL(sql);
		return jt.queryForInt(psql.getSQL());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkage.module.lims.itv.zeroconf.dao.BSSStatisticsDAO#queryPageData(com.linkage.module.lims.itv.zeroconf.dto.BSSStatisticsDTO, int, int)
	 */
	public List<Map> queryPageData(BSSStatisticsDTO dto,int firstResult,int pageSize){
		String sql = getSQL(dto) + " order by r.bss_date desc ";
		log.info("获取零配置BSS工单分页数据"+sql);
		log.info("firstResult===>"+firstResult+",pageSize===>"+pageSize);
		PrepareSQL psql = new PrepareSQL(sql);
		return jte.querySP(psql.getSQL(), firstResult, pageSize);

	}


}
