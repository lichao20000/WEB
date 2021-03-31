/*
 * GeneralHostPerf.java
 *
 * Created on 2006年2月8日, 下午2:30
 */

package com.linkage.litms.host;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.core.GeneralData;
/**
 *
 * @author chenyue
 */
public class GeneralHostPerf implements GeneralData {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(GeneralHostPerf.class);
    /** 小时报表类型 */
    public final static int HOUR = 0;
    /** 日报表类型 */
    public final static int DAY = 1;
    /** 周报表类型 */
    public final static int WEEK = 2;
    /** 月报表类型 */
    public final static int MONTH = 3;
    /** 年报表类型 */
    public final static int YEAR = 4;

    /** 报表类型 */
    private int type;
    /** 开始时间(秒) */
    private long start;
    /** 结束时间(秒) */
    private long end;
    /** 被管对象编号 */
    private int dxbh;
    /** 属性编号 */
    private int sxbh;
    /** 实例编号列表，中间都逗号隔开 */
    //private String slmc;
    /** 实例名称列表，每个实例名称用单引号括起来，中间用逗号隔开。形如：'test1','test2' */
    private String slmc;

    public GeneralHostPerf(int type,long start,long end,int dxbh,int sxbh,String slmc){
        this.type = type;
        this.start = start;
        this.end = end;
        this.dxbh = dxbh;
        this.sxbh = sxbh;
        this.slmc = slmc;
    }

    public Cursor getGeneralTxtData(){
        String sql;
        if(type == HOUR)
            sql = "select slmc,cysj,csz,cyjg from "+getTableName()+" where dxbh="+dxbh+" and sxbh="+sxbh+" and slmc in ("+slmc+") and cysj>="+start+" and cysj<"+end;
        else
            sql = "select slmc,cysj,maxvalue,minvalue,avgvalue,totalcyjg from "+getTableName()+" where dxbh="+dxbh+" and sxbh="+sxbh+" and slmc in ("+slmc+") and cysj>="+start+" and cysj<"+end+" order by slmc,cysj";
        logger.debug("获取详细数据\n"+sql);
        PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
        return DataSetBean.getCursor(sql);
    }
    public Cursor getGeneralChartData(){
        return null;
    }

    public Cursor getStatisticData(){
        final String sql = "select slmc,cysj,maxvalue,minvalue,avgvalue,totalcyjg from "+getStatisticTableName()+" where dxbh="+dxbh+" and sxbh="+sxbh+" and slmc in ("+slmc+") and cysj>="+start+" and cysj<"+end+" order by slmc,cysj";
        logger.debug("获取统计数据\n"+sql);
        PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
        return DataSetBean.getCursor(sql);
    }
    /**
     * 获取主机性能的TopN报表
     * @return cursor
     * add by wangfeng 2007-1-23
     */
    public Cursor getStatisticDataTopN(HttpServletRequest request){
    	String [] ArributeInstance = request.getParameterValues("Attribute");
    	// 排名次序
    	String sortcolumn = request.getParameter("sortcolumn");
    	// 显示名次
    	String limit = request.getParameter("limit");
    	
    	String str_slbh = ""+ArributeInstance[0]+"";
    	for(int i=1;i<ArributeInstance.length;i++){
    	    str_slbh += "," + ArributeInstance[i]+"";
    	}
    	/** 通过类型编号获得属于该类型的的所有对象编号*/
    	String str_TopN = "select dxbh from object_def where dxlx = " + dxbh;
    	String str_dxbh_list = "";
    	PrepareSQL psql = new PrepareSQL(str_TopN);
    	psql.getSQL();
    	Cursor cursor = DataSetBean.getCursor(str_TopN);
    	Map filds = cursor.getNext();
    	while(filds!=null){
    		str_dxbh_list = str_dxbh_list + (String)filds.get("dxbh");
    		str_dxbh_list = str_dxbh_list + ",";
    		filds = cursor.getNext();
    	}
    	str_dxbh_list = str_dxbh_list.substring(0,str_dxbh_list.length()-1);
    	
        final String sql = "select dxbh,sxbh,slmc,cysj,maxvalue,minvalue,avgvalue,totalcyjg from "+getStatisticTableName()+" where dxbh in (" + str_dxbh_list + ") and sxbh in (" + str_slbh + ") and cysj>="+start+" and cysj<"+end+" order by " + sortcolumn;
        logger.debug("获取统计数据\n"+sql);
        PrepareSQL psql2 = new PrepareSQL(sql);
    	psql2.getSQL();
        return DataSetBean.getCursor(sql);
    }
    
    /**
     * 获取对象编号和对象名称的Map
     * @return map
     * add by wangfeng 2007-1-23
     */
    public Map getObjectMap(){
    	Map ObjectMap = new HashMap();
    	String strSQL = "select dxbh,dxmc,ipdz from object_def";
    	PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
    	Cursor cursor = DataSetBean.getCursor(strSQL);
    	Map result = cursor.getNext();
    	while(result!=null){
    		if(!ObjectMap.containsKey((String)result.get("dxbh"))){
    			ObjectMap.put((String)result.get("dxbh"),(String)result.get("dxmc")+ "/" + (String)result.get("ipdz"));
    		}
    		result = cursor.getNext();
    	}
    	return ObjectMap;
    }
    
    /**
     * 获取对象类型编号和对象名称的Map
     * @return map
     * add by wangfeng 2007-1-23
     */
    public Map getTypeMap(){
    	Map ObjectMap = new HashMap();
    	String strSQL = "select lxbh,lxsm from object_type";
    	PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
    	Cursor cursor = DataSetBean.getCursor(strSQL);
    	Map result = cursor.getNext();
    	while(result!=null){
    		if(!ObjectMap.containsKey((String)result.get("lxbh"))){
    			ObjectMap.put((String)result.get("lxbh"),(String)result.get("lxsm"));
    		}
    		result = cursor.getNext();
    	}
    	return ObjectMap;
    }
    
    /**
     * 获取属性编号和属性名称的Map
     * @return map
     * add by wangfeng 2007-1-23
     */
    public Map getAttributeMap(){
    	Map ObjectMap = new HashMap();
    	String strSQL = "select sxbh,sxsm from attribute_list where dxlx = " + dxbh;
    	PrepareSQL psql = new PrepareSQL(strSQL);
    	psql.getSQL();
    	Cursor cursor = DataSetBean.getCursor(strSQL);
    	Map result = cursor.getNext();
    	while(result!=null){
    		if(!ObjectMap.containsKey((String)result.get("sxbh"))){
    			ObjectMap.put((String)result.get("sxbh"),(String)result.get("sxsm"));
    		}
    		result = cursor.getNext();
    	}
    	return ObjectMap;
    }
    
    public String getStatisticTableName(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(start*1000));
        int year = cal.get(Calendar.YEAR);

        String tableName;
        switch(type){
            case HOUR:
                int month = cal.get(Calendar.MONTH)+1;
                tableName = "attribute_hour_stats_"+year+"_"+month;
                break;
            case DAY:
                tableName = "attribute_day_stats_"+year;
                break;
            case WEEK:
                tableName = "attribute_week_stats_"+year;
                break;
            case MONTH:
                tableName = "attribute_month_stats_"+year;
                break;
            default:
                tableName = null;
        }
        return tableName;
    }

    public String getChartTableName(){
        Calendar cal = Calendar.getInstance();
        int year_today = cal.get(Calendar.YEAR);
        int month_today = cal.get(Calendar.MONTH);
        int day_today = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(new Date(start*1000));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String tableName;
        //除了小时报表读取原始数据外，其他报表都读取小时统计数据
        if(type==HOUR){
            //今天
            //if(year==year_today && month==month_today && day==day_today){
                int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                int res = dayOfYear%3-1;
                if(res==0)
                    tableName = "attribute_data_zero";
                else if(res==1)
                    tableName = "attribute_data_one";
                else
                    tableName = "attribute_data_two";
            //}
            //不是今天
            //else
            //    tableName = "attribute_data_"+year+"_"+month;
        }
        else
            tableName = "attribute_hour_stats_"+year+"_"+(month+1);

        return tableName;
    }

    public String getTableName(){
        Calendar cal = Calendar.getInstance();
        int year_today = cal.get(Calendar.YEAR);
        int month_today = cal.get(Calendar.MONTH);
        int day_today = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(new Date(start*1000));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String tableName;
        if(type==HOUR){
            //今天
            //if(year==year_today && month==month_today && day==day_today){
                int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
                int res = dayOfYear%3-1;
                if(res==0)
                    tableName = "attribute_data_zero";
                else if(res==1)
                    tableName = "attribute_data_one";
                else
                    tableName = "attribute_data_two";
            //}
            //不是今天
            //else
            //    tableName = "attribute_data_"+year+"_"+month;
        }
        else if(type==DAY)
            tableName = "attribute_hour_stats_"+year+"_"+(month+1);
        else if(type==WEEK || type==MONTH)
                tableName = "attribute_day_stats_"+year;
        else if(type==YEAR)
            tableName = "attribute_month_stats_"+year;
        else
            tableName = null;
        return tableName;
    }

    public static void main(String[] args){
        java.util.Date dt = new java.util.Date(106,2,3,0,0,0);
        String str = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);
        logger.debug(str);

        long time = dt.getTime()/1000;
        GeneralHostPerf hostPerf = new GeneralHostPerf(GeneralHostPerf.HOUR,time,time+3600,3,63,"1,2,3");
        //logger.debug(hostPerf.getTableName());
        Cursor cur = hostPerf.getGeneralTxtData();
        //logger.debug(cur.getRecordSize());
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getDxbh() {
        return dxbh;
    }

    public void setDxbh(int dxbh) {
        this.dxbh = dxbh;
    }

    public int getSxbh() {
        return sxbh;
    }

    public void setSxbh(int sxbh) {
        this.sxbh = sxbh;
    }

    public String getSlmc() {
        return slmc;
    }

    public void setSlmc(String slmc) {
        this.slmc = slmc;
    }
}
