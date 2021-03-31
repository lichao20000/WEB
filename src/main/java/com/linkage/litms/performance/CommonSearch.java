/**
 * @(#)CommonSearch.java 2006-1-19
 *
 * Copyright 2005 联创科技.版权所有
 */

package com.linkage.litms.performance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.filter.SelectCityFilter;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.dbimpl.DbUserRes;

/**
 * <p>Title: 网络性能常用查询</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Linkage Corporation.</p>
 * @author Yanhj, Network Management Product Department, ID Card No.5126
 * @version 2.0
 */

public class CommonSearch {

  /**
   * Constractor.
   */
  public CommonSearch() {
  }

  /**
   * 获取性能表达式.
   * @return
   */
  public static Cursor getPMExpression() {

    String strSQL =
        "select expressionid,name,descr,unit,class1 from pm_expression where"
        +
        " class2=1 and expressionid in (select distinct expressionid from pm_map)";

    // 构造预处理SQL
    PrepareSQL pSQL = new PrepareSQL(strSQL);

    return DataSetBean.getCursor(pSQL.getSQL());

  }
  /**
   * 下拉列表device_model过滤. modify by xiaoxf
   * @param request
   * @return
   */
  public static String getStrDeviceModel(HttpServletRequest request) {
    String device_type_id = request.getParameter("device_type");
    String expression = request.getParameter("expression");
    String city_id = request.getParameter("city_id");//所要过滤的属地编号
    String ifcontainChild = request.getParameter("ifcontainChild");//是否包含所有下属属地

    String strSQL =
        "select distinct device_model from tab_deviceresource where"
        + " resource_type_id=" + device_type_id
        + " and device_id in (select distinct device_id from pm_map where"
        + " expressionid=" + expression + " and isok=1)"
        + " and city_id in(?)";
    
    PrepareSQL pSQL = new PrepareSQL(strSQL);
    
    if(ifcontainChild.equals("1")) {
        SelectCityFilter scf = new SelectCityFilter();
        pSQL.setStringExt(1,scf.getAllSubCityIds(city_id, true),false);
    }else {
        pSQL.setStringExt(1,city_id,true);
    }
    
    
    Cursor rsDevice = DataSetBean.getCursor(pSQL.getSQL());
//设备权限过滤
//    DeviceResDataFilter deviceFilter = new DeviceResDataFilter(rsDevice,
//        "device_id");
//    rsDevice = (Cursor) deviceFilter.doFilter(dbUserRes);

    String strDevice = FormUtil.createListBox(rsDevice,"device_model","device_model",true,"","");
    return strDevice;

  }
  /**
   * 下拉列表设备过滤. modify by YYS
   * @param request
   * @return
   */
  public static String getStrDevice(HttpServletRequest request) {
    String device_type_id = request.getParameter("device_type");
    String device_model = request.getParameter("device_model");
    String expression = request.getParameter("expression");
    String city_id = request.getParameter("city_id");//所要过滤的属地编号
    String ifcontainChild = request.getParameter("ifcontainChild");//是否包含所有下属属地
    HttpSession session = request.getSession();
    DbUserRes dbUserRes = (DbUserRes) session.getAttribute("curUser");
    List gather_id = dbUserRes.getUserProcesses();

    String strSQL =
        "select device_name,loopback_ip,device_id from tab_deviceresource where"
        + " resource_type_id=" + device_type_id
        + ((device_model==null||device_model.equals(""))?" ":" and device_model='"+device_model+"'")//如果为空则不添加device_model条件 江苏修改后为达到兼容性而修改
        + " and device_id in (select distinct device_id from pm_map where"
        + " expressionid=" + expression + " and isok=1)"
        + " and gather_id in (?) and city_id in(?)";
    
    PrepareSQL pSQL = new PrepareSQL(strSQL);
    pSQL.setStringExt(1, StringUtils.weave(gather_id), false);
    
    if(ifcontainChild.equals("1")) {
        SelectCityFilter scf = new SelectCityFilter();
        pSQL.setStringExt(2,scf.getAllSubCityIds(city_id, true),false);
    }else {
        pSQL.setStringExt(2,city_id,true);
    }
    
    
    Cursor rsDevice = DataSetBean.getCursor(pSQL.getSQL());
//设备权限过滤
//    DeviceResDataFilter deviceFilter = new DeviceResDataFilter(rsDevice,
//        "device_id");
//    rsDevice = (Cursor) deviceFilter.doFilter(dbUserRes);

    String strDevice = FormUtil.createListBox1(rsDevice, "loopback_ip",
                                               "device_name", true, "device_id",
                                               "dev_id", true);
    return strDevice;

  }

  /**
   * 索引
   * @param request
   * @return
   */
  public static Cursor getDeviceIndex(HttpServletRequest request) {
    String expression = request.getParameter("expression");
    String id = request.getParameter("ip");
    String strSQL =
        "select id,indexid,descr from pm_map_instance where expressionid=" +
        expression + " and device_id='" + id + "' and collect=1";
    PrepareSQL pSQL = new PrepareSQL(strSQL);
    
    return DataSetBean.getCursor(pSQL.getSQL());
  }

//获取表达式名字
  public static Map getExpressionMap(String expressionid) {
    Map expressionMap = new HashMap();
    String s;
    String sql="select name,unit from pm_expression where expressionid=" + expressionid;
    PrepareSQL psql = new PrepareSQL(sql);
	psql.getSQL();
    Map fields = DataSetBean.getRecord(sql);
    if (fields != null) {
      s = (String) fields.get("name") + "," + (String) fields.get("unit");
      expressionMap.put(expressionid, s);
    }

    return expressionMap;
  }

  /**
   * 设备MAP
   * @param device_id
   * @return
   */
  public static Map getDeviceMap(String device_id) {
    Map deviceMap = new HashMap();
    String sql = "select device_name from tab_gw_device where device_id='" +
        device_id + "'";
    PrepareSQL psql = new PrepareSQL(sql);
	psql.getSQL();
    Map fields = DataSetBean.getRecord(sql);
    if (fields != null) {
      deviceMap.put(device_id, (String) fields.get("device_name"));
    }

    return deviceMap;
  }

  /**
   * 索引map
   * @param device_id
   * @return
   */
  public static Map getIndexMap(String str_iid) {
    Map indexMap = new HashMap();
    String sql = "select id,expressionid,device_id,indexid,descr from pm_map_instance where id in (" +
                                          str_iid + ")";
    PrepareSQL pSQL = new PrepareSQL(sql);
    
    Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
    Map fields = cursor.getNext();
    String s = "";
    while (fields != null) {
      s = (String) fields.get("expressionid") + "," +
          (String) fields.get("device_id") + "," + (String) fields.get("indexid") +
          "," + (String) fields.get("descr");
      indexMap.put( (String) fields.get("id"), s);
      fields = cursor.getNext();
    }

    return indexMap;
  }

  /**
   *
   * @return
   */
  public static Cursor getResourceType() {
//    String strSQL = "select * from tab_resourcetype order by resource_type_id";
//    PrepareSQL pSQL = new PrepareSQL(strSQL);

//    return DataSetBean.getCursor(pSQL.getSQL());
	  return null;
  }

//  public static void main(String[] args) {
//    CommonSearch commonSearch1 = new CommonSearch();
//  }

}