package com.linkage.module.itms.report.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.dao.SuperDAO;
import com.linkage.module.gwms.dao.tabquery.CityDAO;


public class VoipXIPBSSReportJLDAO extends SuperDAO 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VoipXIPBSSReportJLDAO.class);

    /**
     * 根据cityId统计工单接收情况：成功数，失败数
     * cityId为空 则统计所有省市的工单接收情况
     * @param cityId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String, String>> getXVoipBSSReportByCityDB(String cityId, String startTime, 
    		String endTime,List<String> cityIdList)
    {
        LOGGER.debug("begin getXVoipBSSReportByCityDB with cityId:{},startTime:{},endTime:{}",
        		cityId,startTime,endTime);
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("select result,count(bss_sheet_id) as count,city_id ");
        sqlBuild.append("from tab_bss_sheet where product_spec_id=14 and type=4");
        if(!StringUtil.IsEmpty(cityId)){
            if (!"00".equals(cityId)){
                sqlBuild.append(" and city_id in ("+StringUtils.weave(cityIdList)+")");
            }else {
                sqlBuild.append("and city_id = '00'");
            }
        }

        if(!StringUtil.IsEmpty(startTime)){
            sqlBuild.append(" and receive_date >=").append(startTime);
        }
        if(!StringUtil.IsEmpty(endTime)){
            sqlBuild.append(" and receive_date <=").append(endTime);
        }
        sqlBuild.append(" group by result,city_id");
        PrepareSQL psql = new PrepareSQL(sqlBuild.toString());
        List<Map<String,String>> result =  jt.queryForList(psql.getSQL());
        LOGGER.debug("end getXVoipBSSReportByCityDB with result:{}",result);
        return result;
    }

    /**
     * 根据cityId统计工单下发情况：成功数，失败数
     * cityId为空 则统计所有省市的工单下发情况
     * @param cityId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String, String>> getXVoipBSSDownByCityDB(String cityId, String startTime, 
    		String endTime,List<String> cityIdList)
    {
        LOGGER.debug("begin getXVoipBSSDownByCityDB with cityId:{},startTIme:{},endTime:{}",
        		cityId,startTime,endTime);
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("select s.result_id,count(s.id) as count,d.city_id ");
        sqlBuild.append("from gw_serv_strategy_batch s,tab_hgwcustomer d ");
        sqlBuild.append("where s.device_id=d.device_id and s.service_id=1431");
        if(!StringUtil.IsEmpty(cityId)){
            if (!"00".equals(cityId)){
                sqlBuild.append(" and d.city_id in ("+StringUtils.weave(cityIdList)+")");
            }else {
                sqlBuild.append(" and d.city_id = '00'");
            }
        }

        if(!StringUtil.IsEmpty(startTime)){
            sqlBuild.append(" and s.time >=").append(startTime);
        }
        if(!StringUtil.IsEmpty(endTime)){
            sqlBuild.append(" and s.time <=").append(endTime);
        }
        sqlBuild.append(" group by s.result_id,d.city_id");
        PrepareSQL psql = new PrepareSQL(sqlBuild.toString());
        List<Map<String,String>> result =  jt.queryForList(psql.getSQL());
        LOGGER.debug("end getXVoipBSSDownByCityDB with result:{}",result);
        return result;

    }

    /**
     * 获取voip修改IP工单接收统计详情分页列表
     * @param cityId
     * @param startTime
     * @param endTime
     * @param type  工单接收状态 0-成功，1-失败，2-未绑定设备
     * @param startIndex
     * @param endIndex
     * @return
     */
    public List<Map<String, String>> getXVoipBSSInfoDB(String cityId, String startTime, 
    		String endTime,String type,int startIndex, int endIndex)
    {
        LOGGER.debug("begin getXVoipBSSInfoDB with cityId:{},startTIme:{},endTime:{}",
        				cityId,startTime,endTime);
        StringBuilder sqlBuild = new StringBuilder();
        if(DBUtil.GetDB()==3){
        	sqlBuild.append("select username,city_id,returnt_context,sheet_context,rn from ");
        }else{
        	sqlBuild.append("select * from ");
        }
        
        sqlBuild.append("(select username,city_id,returnt_context,sheet_context,rownum as rn ");
        sqlBuild.append("from tab_bss_sheet where product_spec_id=14 and type=4 ");
        List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
        sqlBuild.append(" and city_id in ("+StringUtils.weave(cityIdList)+")");

        if(!type.equals("-1") && type.equals("0")){
            //未绑定设备的情况也属于工单接收成功
            sqlBuild.append(" and (result = 0 or result = 2)");
        }else if(!type.equals("-1")){
            sqlBuild.append(" and result = ").append(type);
        }

        if(!StringUtil.IsEmpty(startTime)){
            sqlBuild.append(" and receive_date >=").append(startTime);
        }
        if(!StringUtil.IsEmpty(endTime)){
            sqlBuild.append(" and receive_date <=").append(endTime);
        }
        if(endIndex != -1){
            sqlBuild.append(" and rownum <=").append(endIndex);
        }
        sqlBuild.append(")re where 1=1");
        if(startIndex != -1){
            sqlBuild.append(" and re.rn >").append(startIndex);
        }
        PrepareSQL psql = new PrepareSQL(sqlBuild.toString());
        List<Map<String,String>> result =  jt.queryForList(psql.getSQL());
        LOGGER.debug("end getXVoipBSSInfoDB with result:{}",result);
        return result;
    }

    /**
     * 获取voip修改IP工单下发统计详情分页列表
     * @param cityId
     * @param startTime
     * @param endTime
     * @param type 下发结果 1-成功 其他-失败
     * @param startIndex
     * @param endIndex
     * @return
     */
    public List<Map<String, String>> getXVoipBSSDownInfoDB(String cityId, 
    		String startTime, String endTime,String type,int startIndex, int endIndex)
    {
        LOGGER.debug("begin getXVoipBSSDownInfoDB with cityId:{},startTIme:{},endTime:{}",
        		cityId,startTime,endTime);
        StringBuilder sqlBuild = new StringBuilder();
        if(DBUtil.GetDB()==3){
        	sqlBuild.append("select username,city_id,sheet_para,rn from ");
        }else{
        	sqlBuild.append("select * from ");
        }
        
        sqlBuild.append("(select h.username,h.city_id,s.sheet_para,rownum as rn ");
        sqlBuild.append("from gw_serv_strategy_batch s,tab_hgwcustomer h ");
        sqlBuild.append("where s.device_id=h.device_id and s.service_id=1431 ");
        List<String> cityIdList = CityDAO.getAllNextCityIdsByCityPid(cityId);
        sqlBuild.append(" and h.city_id in ("+StringUtils.weave(cityIdList)+")");
        if(type.equals("1")){
            sqlBuild.append(" and s.result_id = 1");
        }else {
            sqlBuild.append(" and s.result_id != 1");
        }
        if(!StringUtil.IsEmpty(startTime)){
            sqlBuild.append(" and s.time >=").append(startTime);
        }
        if(!StringUtil.IsEmpty(endTime)){
            sqlBuild.append(" and s.time <=").append(endTime);
        }
        if(endIndex != -1){
            sqlBuild.append(" and rownum <=").append(endIndex);
        }
        sqlBuild.append(")re where 1=1");
        if(startIndex != -1){
            sqlBuild.append(" and re.rn >").append(startIndex);
        }
        PrepareSQL psql = new PrepareSQL(sqlBuild.toString());
        List<Map<String,String>> result =  jt.queryForList(psql.getSQL());
        LOGGER.debug("end getXVoipBSSDownInfoDB with result:{}",result);
        return result;
    }


    /**
     * 根据loid查询voip修改IP最新工单
     * @param loid
     * @return
     */
    public Map<String, String> getXVoipBSSByLoidDB(String loid)
    {
        LOGGER.debug("begin getXVoipBSSByLoidDB with loid:{}",loid);
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	 psql.append("select city_id,result,sheet_context from ");
        }else{
        	 psql.append("select * from ");
        }
       
        psql.append("(select city_id,result,sheet_context ");
        psql.append("from tab_bss_sheet where product_spec_id=14 and type=4 ");
        psql.append("and username='"+loid+"' order by receive_date desc) ");
        psql.append("where rownum=1 ");
        List<Map<String,String>> result =  jt.queryForList(psql.getSQL());
        LOGGER.debug("end getXVoipBSSByLoidDB with result:{}",result);
        if(result == null || result.size() == 0){
            return null;
        }
        return result.get(0);
    }

    /**
     * 根据loid查询最新一条voip修改IP工单策略
     * @param loid
     * @return
     */
    public Map<String,String> getXVoipBSSDownByLoidDB(String loid)
    {
        LOGGER.debug("begin getXVoipBSSDownByLoidDB with loid:{}",loid);
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	 psql.append("select result_id from ");
        }else{
        	 psql.append("select * from ");
        }
       
        psql.append("(select s.result_id from gw_serv_strategy_batch s,tab_hgwcustomer h ");
        psql.append("where s.device_id=h.device_id and h.username='" + loid + "' order by s.time desc) ");
        psql.append("where rownum=1 ");
        List<Map<String,String>> result =  jt.queryForList(psql.getSQL());
        LOGGER.debug("end getXVoipBSSDownByLoidDB with result:{}",result);
        if(result == null || result.size() == 0){
            return null;
        }
        return result.get(0);
    }

    public Map<String,String> getUserInfoByloidDB(String loid)
    {
        LOGGER.debug("begin getUserInfoByloidDB with loid:{}",loid);
        PrepareSQL psql = new PrepareSQL();
        if(DBUtil.GetDB()==3){
        	psql.append("select city_id,reg_id from ");
        }else{
        	psql.append("select * from ");
        }
        
        psql.append("(select h.city_id,v.reg_id from tab_hgwcustomer h,tab_voip_serv_param v ");
        psql.append("where h.user_id=v.user_id" +" and h.username='" + loid + "') ");
        psql.append("where rownum=1");
        List<Map<String,String>> result =  jt.queryForList(psql.getSQL());
        LOGGER.debug("end getUserInfoByloidDB with result:{}",result);
        if(result == null || result.size() == 0){
            return null;
        }
        return result.get(0);
    }


}
