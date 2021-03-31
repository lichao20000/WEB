/*
 * HostCommon.java
 *
 * Created on 2006年1月11日, 下午5:04
 */

package com.linkage.litms.host;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;

/**
 * 定义主机管理相关操作
 * @author chenyue
 */
public final class HostCommon {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(HostCommon.class);
    /**
     * 获取所有对象类型的列表。列表中的每个元素是一个ObjectType对象。
     * @return 对象类型列表
     * @see ObjectType
     */
    public static ArrayList getObjectTypeList(){
        final String SQL = "select lxbh,lxmc,lxsm from object_type";
        
        ArrayList list = new ArrayList();
        PrepareSQL psql = new PrepareSQL(SQL);
    	psql.getSQL();
        Cursor cursor = DataSetBean.getCursor(SQL);
        Map map =cursor.getNext();
        while(map!=null){
            ObjectType objectType = new ObjectType();
            objectType.lxbh = Integer.parseInt((String)map.get("lxbh"));
            objectType.lxmc = (String)map.get("lxmc");
            objectType.lxsm = (String)map.get("lxsm");
            
            list.add(objectType);
            
            map =cursor.getNext();
        }
        return list;
    }
    
    /**
     * 根据对象类型编号获取被管对象的列表。列表中的每个元素是一个ObjectDef对象。
     * @param dxlx 对象类型编号
     * @return 被管对象列表
     * @see ObjectDef
     */
    public static ArrayList getObjectDefList(int dxlx){
        String SQL = "select * from object_def where dxlx=" + dxlx;
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            SQL = "select dxbh, fdxbh, dxmc, dxsm, ipdz, dkh, cyjg, lxbz from object_def where dxlx=" + dxlx;
        }
        PrepareSQL psql = new PrepareSQL(SQL);
    	psql.getSQL();
        ArrayList list = new ArrayList();
        Cursor cursor = DataSetBean.getCursor(SQL);
        logger.debug("cursor="+cursor);
        Map map = cursor.getNext();
        logger.debug("map="+map);
        while(map!=null){
            ObjectDef objectDef = new ObjectDef();
            objectDef.dxbh = Integer.parseInt((String)map.get("dxbh"));
            objectDef.fdxbh = Integer.parseInt((String)map.get("fdxbh"));
            objectDef.dxlx = dxlx;
            objectDef.dxmc = (String)map.get("dxmc");
            objectDef.dxsm = (String)map.get("dxsm");
            objectDef.ipdz = (String)map.get("ipdz");
            objectDef.dkh = Integer.parseInt((String)map.get("dkh"));
            objectDef.cyjg = Integer.parseInt((String)map.get("cyjg"));
            objectDef.lxbz = Integer.parseInt((String)map.get("lxbz"));
            
            list.add(objectDef);
            
            map = cursor.getNext();
        }
        return list;
    }
    
    /**
     * 根据对象类型编号获取属性组的列表。列表中的每个元素是一个AttributeGroup对象。
     * @param dxlx 对象类型编号
     * @return 属性组列表
     * @see AttributeGroup
     */
    public static ArrayList getAttributeGroupList(int dxlx){
        final String SQL = "select zbh,zmc,zsm from attrgroup_list where dxlx=" + dxlx;
        PrepareSQL psql = new PrepareSQL(SQL);
    	psql.getSQL();
        ArrayList list = new ArrayList();
        Cursor cursor = DataSetBean.getCursor(SQL);
        Map map =cursor.getNext();
        while(map!=null){
            AttributeGroup attributeGroup = new AttributeGroup();
            attributeGroup.dxlx = dxlx;
            attributeGroup.zbh = Integer.parseInt((String)map.get("zbh"));
            attributeGroup.zmc = (String)map.get("zmc");
            attributeGroup.zsm = (String)map.get("zsm");
            
            list.add(attributeGroup);
            
            map =cursor.getNext();
        }
        return list;
    }
    
    /**
     * 根据对象类型编号，被管对象编号，属性组名称获取属性的列表。列表中的每个元素是一个Attribute对象。
     * @param dxlx 对象类型编号
     * @param dxbh 被管对象编号
     * @param zmc 属性组名称
     * @return 属性列表
     * @see Attribute
     */
    public static ArrayList getAttributeList (int dxlx,int dxbh,String zmc){
        final String SQL = "select a.dxlx,a.sxbh,a.zmc,a.sxmc,a.sxsm,a.sxlx,a.sllx,a.dwmc,a.xsbl,a.cyzq from attribute_list a,attribute_conf b where b.dxbh=" + dxbh + " and a.dxlx=" + dxlx + " and a.zmc='" + zmc + "' and a.sxbh=b.sxbh";
        PrepareSQL psql = new PrepareSQL(SQL);
    	psql.getSQL();
        ArrayList list = new ArrayList();
        Cursor cursor = DataSetBean.getCursor(SQL);
        Map map =cursor.getNext();
        while(map!=null){
            Attribute attribute = new Attribute();
            attribute.dxlx = dxlx;
            attribute.sxbh = Integer.parseInt((String)map.get("sxbh"));
            attribute.zmc = zmc;
            attribute.sxmc = (String)map.get("sxmc");
            attribute.sxsm = (String)map.get("sxsm");
            attribute.xsbl = Double.parseDouble((String)map.get("xsbl"));
            attribute.dwmc = (String)map.get("dwmc");
            attribute.sllx = Integer.parseInt((String)map.get("sllx"));
            
            list.add(attribute);
            
            map =cursor.getNext();
        }
        return list;
    }
    
    /**
     * 根据被管对象编号，属性编号获取实例的列表。列表中的每个元素是一个AttributeInstance对象。
     * @param dxbh 被管对象编号
     * @param sxbh 属性编号
     * @return 实例列表
     * @see AttributeInstance
     */
    public static ArrayList getAttributeInstanceList (int dxbh,int sxbh){
        final String SQL = "select dxbh,sxbh,slbh,slmc from attribute_slmc where dxbh=" + dxbh + " and sxbh=" + sxbh;
        PrepareSQL psql = new PrepareSQL(SQL);
    	psql.getSQL();
        ArrayList list = new ArrayList();
        Cursor cursor = DataSetBean.getCursor(SQL);
        Map map =cursor.getNext();
        while(map!=null){
            AttributeInstance attributeInstance = new AttributeInstance();
            attributeInstance.dxbh = dxbh;
            attributeInstance.sxbh = sxbh;
            attributeInstance.slbh = Integer.parseInt((String)map.get("slbh"));
            attributeInstance.slmc = (String)map.get("slmc");
            
            list.add(attributeInstance);
            
            map =cursor.getNext();
        }
        return list;
    }
    
    /**
     * 在链表1中去除不在链表2中的元素。最终，链表1中的元素都是链表链表2中的元素。
     * @param listNeedToFiter 链表1。需要过滤的链表
     * @param list 链表2
     * @return 过滤后的链表
     */
    public static ArrayList fiter(ArrayList listNeedToFiter,List list){
        for(int i=0;i<listNeedToFiter.size();i++){
            if(!list.contains(listNeedToFiter.get(i))){
                listNeedToFiter.remove(i);
                i--;
            }
        }
        return listNeedToFiter;
    }
    /**
     * 
     * @param listID
     * @param dateTimeUtil
     * @param param[0]=sxbh(属性编号) param[1]=dxbh(对象编号)
     * @return
     */
    public Cursor[] getCursorReport(List listSLMC,String[] param,DateTimeUtil dateTimeUtil){
        int size = listSLMC.size();
        Cursor[] cursor = new Cursor[size];
        String tmpSLMC = null;
        rowKeys = new String[size];
        String tmpSQL = null;
        String strSQL = null;
        String tablenameA = null;
        String tablenameB = null;
        // 根据报表类型 时间向前推
        if(this.reportType == 1)
        {
            strTime_end = dateTimeUtil.getDate() + " " + dateTimeUtil.getTime();
            tablenameB = this.tableFirst + dateTimeUtil.getYear() + "_"
                    + dateTimeUtil.getMonth();
            dateTimeUtil.getNextDate(-1);
            tablenameA = this.tableFirst + dateTimeUtil.getYear() + "_"
                    + dateTimeUtil.getMonth();
            strTime_start = dateTimeUtil.getDate() + " "
                    + dateTimeUtil.getTime();
        }else if(this.reportType == 2)
        {
            DateTimeUtil tmpTimeUtil = new DateTimeUtil(dateTimeUtil
                    .getLongTime()*1000);
            // 周报表暂时从原始数据表中取数据
            strTime_end = dateTimeUtil.getDate() + " " + dateTimeUtil.getTime();
            tablenameB = this.tableFirst + dateTimeUtil.getYear() + "_"
                    + dateTimeUtil.getMonth();
            dateTimeUtil.getNextDate(-7);
            tmpTimeUtil.getNextDate(-7);
            tablenameA = this.tableFirst + dateTimeUtil.getYear() + "_"
                    + dateTimeUtil.getMonth();
            strTime_start = dateTimeUtil.getDate() + " "
                    + dateTimeUtil.getTime();
            // 遍历时间 再检验某个日期是否为星期一
            for(int i = 1;i <= 8;i++)
            {
                // 是否是星期一
                if(tmpTimeUtil.getNoDayOfWeek("CN") == 1)
                {
                    logger.debug(tmpTimeUtil.getDate() + "是星期一");
                    marker = tmpTimeUtil.getLongTime();
                }
                // 向前加一天
                tmpTimeUtil.getNextDate(1);
            }
        }else
        {
            DateTimeUtil tmpTimeUtil = new DateTimeUtil(dateTimeUtil
                    .getLongTime()*1000);
            // 月报表暂时从原始数据表中取数据
            strTime_end = dateTimeUtil.getDate() + " " + dateTimeUtil.getTime();
            long _end = dateTimeUtil.getLongTime();
            tablenameB = this.tableFirst + dateTimeUtil.getYear() + "_"
                    + dateTimeUtil.getMonth();
            dateTimeUtil.getNextMonth(-1);
            tmpTimeUtil.getNextMonth(-1);
            long _start = dateTimeUtil.getLongTime();
            tablenameA = this.tableFirst + dateTimeUtil.getYear() + "_"
                    + dateTimeUtil.getMonth();
            strTime_start = dateTimeUtil.getDate() + " "
                    + dateTimeUtil.getTime();
            long days = ( _end - _start ) / ( 24 * 3600 );
            days += 1;
            // 遍历时间 再检验某个日期是否为月初第一天
            for(int i = 1;i <= days;i++)
            {
                // 是否是是月初第一天
                if(tmpTimeUtil.getDay() == 1)
                {
                    logger.debug(tmpTimeUtil.getDate() + "是月初第一天");
                    marker = tmpTimeUtil.getLongTime();
                }
                // 向前加一天
                tmpTimeUtil.getNextDate(1);
            }
        }

        // 得到当前月份
        long start = dateTimeUtil.getLongTime();
        // 判断昨天和今天是否在同一个月内
        // 如果相等，则只需要查询一张表
        if(tablenameA.equals(tablenameB))
        {
            strSQL = "select avgvalue,cysj from " + tablenameB
                    + " where cysj >= " + start + " and slmc=? and sxbh=? and dxbh=?";
        }else
        {
            strSQL = "select avgvalue,cysj from " + tablenameA
                    + " where cysj >= " + start + " and slmc=? and sxbh=? and dxbh=?"
                    + " union " + "select avgvalue,cysj from " + tablenameB
                    + " where slmc=? and sxbh=? and dxbh=?";
        }
        PrepareSQL pSQL = new PrepareSQL(strSQL);
        pSQL.setStringExt(2,param[0],false);
        pSQL.setStringExt(3,param[1],false);
        pSQL.setStringExt(5,param[0],false);
        pSQL.setStringExt(6,param[1],false);
        for(int i = 0;i < size;i++){
            tmpSLMC = (String) listSLMC.get(i);
            pSQL.setStringExt(1,tmpSLMC,true);
            pSQL.setStringExt(4,tmpSLMC,true);
            tmpSQL = pSQL.getSQL();
            cursor[i] = DataSetBean.getCursor(tmpSQL);
            this.rowKeys[i] = tmpSLMC;
        }
        pSQL = null;
        tablenameA = null;
        tablenameB = null;
        dateTimeUtil = null;
        return cursor;
    }

    public String[] getRowKeys(){
        return this.rowKeys;
    }
    public long getMarker()
    {
        return this.marker;
    }
    public String getStrTime_end()
    {
        return strTime_end;
    }

    public String getStrTime_start()
    {
        return strTime_start;
    }
    public void setReportType(int type)
    {
        switch(type)
        {
            case 1:
                this.reportType = 1;
                break;
            case 2:
                this.reportType = 2;
                break;
            case 3:
                this.reportType = 3;
                break;
        }
    }
    /**
     * 报表类型 1：日 2：周 3:月
     */
    private int         reportType  = 1;
    private String[]    rowKeys;
    private String  strTime_start   = null;
    /**
     * 用于构造表明
     * 日报表为：attribute_hour_stats_2007_1、周：attribute_hour_stats_2007_1、月:attribute_hour_stats_2007_1
     */
    private String      tableFirst  = "attribute_hour_stats_";
    private String  strTime_end     = null;

    /**
     * 用于chart图标的标尺
     */
    private long    marker          = 0L;
}
