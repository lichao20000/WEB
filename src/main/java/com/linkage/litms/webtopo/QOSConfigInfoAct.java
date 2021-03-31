/*
 * 
 * 创建日期 2007-3-5
 * Administrator yys
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.webtopo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.QOSManager;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.tree.TreeMap;

/**
 * 实现对QOS配置信息的展示
 * 
 * @author yys
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class QOSConfigInfoAct{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(QOSConfigInfoAct.class);
    /**
     * 获取设备QOS配置实例列表
     */
	private String m_QOSConfig_List_SQL = "select * FROM tab_qosifindex where device_id=?";

    /**
     * 获取设备QOS配置对象
     */
	private String m_QOSConfigObject_SQL = "select * FROM tab_qosobject where device_id=? and qosindex=? order by parentobjectsindex,qosobjectsindex";
	
    /**
     * 获取设备QOS配置对象类型及配置索引
     */
	private String m_QOSConfigObjectIndex_SQL = "select * FROM tab_qosobject where device_id=? and qosindex=? and qosobjectsindex=?";
	
    /**
     * 获取设备QOS配置对象实例详细信息
     */
	private String m_QOSConfigObjectDetail_SQL = "select * FROM ? where device_id=? and qosconfigindex=?";
	
    /**
     * QOS设备配置表查询
     */
	private String m_QOSDeviceConfig_SQL = "select * FROM tab_qosresourceconfig where device_id=?";
	
    /**
     * QOS设备配置表更新
     */
	private String m_QOSDeviceConfigUpdate_SQL = "update tab_qosresourceconfig set flag=1 where device_id=?";
	
	/**
     * QOS设备配置表插入
     */
	private String m_QOSDeviceConfigInsert_SQL = "insert into tab_qosresourceconfig values(?,1,'')";

    private Cursor cursor = null;

    private String str_device_id = null;
    
    private String str_qosindex = null;

    private PrepareSQL pSQL;
    
    private static QOSManager qosManager = null;
    
    /**
     * 树状结构缩进符
     */
    public String starSeg = "    ";
    
    /**
     * 树状结构换行符
     */
    public String endSeg = "\\n";
    
    /**
     * QOS对象类型与表的对应关系
     */
    public static HashMap QOSObject_table = new HashMap();
    
    /**
     * QOS对象类型与类型名称的对应关系
     */
    public static LinkedHashMap QOSObject_type = new LinkedHashMap();
    
    static {
    	if(QOSObject_table.isEmpty()) {
        	//初始化QOS对象类型与表的对应关系
        	QOSObject_table.put("1","tab_policymapname");
        	QOSObject_table.put("2","tab_qoscmname");
        	QOSObject_table.put("3","tab_qosmatchname");
        	QOSObject_table.put("4","tab_qosqueueingcfg");
        	QOSObject_table.put("6","tab_qostscfg");
        	QOSObject_table.put("7","tab_qospolicecfg");
        	QOSObject_table.put("8","tab_qossetentry");
    	}
    	
    	if(QOSObject_type.isEmpty()) {
        	//初始化QOS对象类型与类型名称对应关系
    		QOSObject_type.put("1","policymap");
    		QOSObject_type.put("2","classmap");
    		QOSObject_type.put("3","matchStatement");
    		QOSObject_type.put("4","queueing");
    		QOSObject_type.put("6","trafficShaping");
    		QOSObject_type.put("7","police");
    		QOSObject_type.put("8","set");
    	}
    }
    
    public QOSConfigInfoAct(HttpServletRequest request) {
        pSQL = new PrepareSQL();        
        
        str_device_id = request.getParameter("device_id");
        str_qosindex = request.getParameter("qosindex");
    }
    
    /**
     * 获得设备所有配置实例列表
     * 
     * @return Cursor
     */
    public Cursor getQOSConfigCursor() {
    	pSQL.setSQL(m_QOSConfig_List_SQL);
    	pSQL.setString(1,str_device_id);
    	
    	cursor = DataSetBean.getCursor(pSQL.getSQL());
    	
    	
        return cursor;
    }
    
    /**
     * 配置设备
     * 
     * @return ArrayList index 0 标识位：1 已配置采集，0未配置采集；
     * 					  index 1 Cursor；
     */
    public ArrayList getQOSDeviceConfig() {
    	ArrayList array = new ArrayList();
    	array.clear();
    	
    	array.add(0, "0");
    	array.add(1, null);
    	
    	pSQL.setSQL(m_QOSDeviceConfig_SQL);
    	pSQL.setString(1,str_device_id);    	
    	cursor = DataSetBean.getCursor(pSQL.getSQL());
    	
    	//未配置
    	if(cursor==null||cursor.getRecordSize()==0) {
    		//插入设备配置表
    		pSQL.setSQL(m_QOSDeviceConfigInsert_SQL);
    		pSQL.setString(1,str_device_id);
    		DataSetBean.executeUpdate(pSQL.getSQL());
    		
    		
    	//已配置
    	}else {
    		//判断采集状态
    		boolean flag = cursor.getNext().get("flag").equals("1");
    		//已采集
    		if(flag) {
    			cursor = getQOSConfigCursor();
    	    	array.add(0, "1");
    	    	array.add(1, cursor);
    	    	
    	    	//logger.debug("array 0："+array.get(0));
    	    	//logger.debug("array 1："+array.get(1));
    		//未采集
    		}else{
    			//更新配置字段
    			pSQL.setSQL(m_QOSDeviceConfigUpdate_SQL);
    			pSQL.setString(1,str_device_id);
    			DataSetBean.executeUpdate(pSQL.getSQL());
    			
    			
    		}
    	}
    	
    	//通知后台对新增的配置设备进行采集
    	if(array.get(0).equals("0")) {
    		this.I_InformGather();
    	}    		
    	
    	return array;
    		
    }
    
    /**
     * 获得设备某QOS配实例对象结果集
     * 
     * @return Cursor
     */
    public Cursor getQOSConfigObjectInfo() {
    	pSQL.setSQL(m_QOSConfigObject_SQL);
    	pSQL.setString(1,str_device_id);
    	pSQL.setString(2,str_qosindex);
    	
    	cursor = DataSetBean.getCursor(pSQL.getSQL());
    	
    	
        return cursor;
    }
    
    /**
     * 获得设备所有QOS配置对象详细信息
     * 
     * @return Cursor[]
     */
    public Cursor[] getAllQOSConfigObjectInfo() {
    	String Sql = "select ? from ? where device_id=?";
    	String tem_table = "";
    	String sqlColum = "strdesc";
    	String qosobjectstype = "";
    	cursor = null;
    	Cursor[] myCursor = new Cursor[9];
    	
    	java.util.Iterator keyValuePairs = QOSObject_table.entrySet().iterator();
    	for(int j =0;j<QOSObject_table.size();j++){
    		Map.Entry entry = (Map.Entry) keyValuePairs.next();
    		
    		qosobjectstype = (String)entry.getKey();
    		tem_table = (String)entry.getValue();
    		if(tem_table.equals("tab_qoscmname")) 
    			sqlColum += ",strdesc1";
    		else
    			sqlColum = "strdesc";
    		
    		pSQL.setSQL(Sql);
    		pSQL.setStringExt(1,sqlColum,false);
    		pSQL.setStringExt(2,tem_table,false);
    		pSQL.setString(3,str_device_id);
    		
    		cursor = DataSetBean.getCursor(pSQL.getSQL());    		
    		myCursor[Integer.parseInt(qosobjectstype)-1] = cursor;    		
    		
    		cursor = null;
    	}

        return myCursor;
    }
    
    /**
     * 获取某一具体配置实例的命令描述字符串
     * @return String 树状结构的命令描述字符串
     */
    public String getQOSDetail() {
    	String QOSDetail = "";
    	String[] _s = {"",""};
    	LinkedHashMap _map = new LinkedHashMap();
    	
    	//获得格式化结果集
    	cursor = getQOSConfigObjectInfo();
    	//获取树状结构关系Map
    	_map = TreeMap.getTreeFormateMap(cursor,"qosobjectsindex","parentobjectsindex","0",false);
    	//获得最终树状结构的拼接string
    	_s = getFormateTreeStringByInterative(_map,"");
    	
    	//特殊对象描述放在上面
    	//QOSDetail = _s[1] + "\\n" + _s[0];
    	QOSDetail = _s[0];
    	
    	//logger.debug("	拼接字符串结束："+ QOSDetail);
    	
		return QOSDetail;
    }
    
    /**
     * 将_m的层次结构根据特定seg以缩进形式呈现
     * @param _m		需展示层次关系的Map
     * @param starwith	层次关系起始符
     * @return	_s[]	QOS对象实例描述信息
     * 					index 0 常规对象描述
     * 						  1 特殊对象描述
     */    
    public String[] getFormateTreeStringByInterative(LinkedHashMap _m,String starwith) {    	
    	
    	String _s[]= {"",""};
    	
    	String tem_k = "";
    	String[] tem_s = {"",""};
    	LinkedHashMap tem_v = new LinkedHashMap();
    	java.util.Iterator keyValuePairs = _m.entrySet().iterator();
    	for(int j =0;j<_m.size();j++){
    		Map.Entry entry = (Map.Entry) keyValuePairs.next();
    		
    		tem_k = (String)entry.getKey();
    		tem_v = (LinkedHashMap)entry.getValue(); 
    		
//    		logger.debug("	树状结构字符串解析开始 with "+tem_k);
    		
    		//拼接当前层
    		tem_s = getTreeDesc(tem_k);
    		_s[0] += starwith+tem_s[0]+endSeg;
    		_s[1] += starwith+tem_s[1]+endSeg;
    		
    		//拼接此分支的下一层
    		tem_s = getFormateTreeStringByInterative(tem_v,starwith+starSeg);
    		_s[0] += tem_s[0];
    		_s[1] += tem_s[1];
//    		logger.debug("	树状结构字符串解析结束 with "+tem_k);
    	}
    	
    	return _s;
    }
    
    /**
     * 根据对象编号 查找对象实例描述信息 供树状结构展现使用
     * @param _id		QOS对象索引
     * @return	_s[]	QOS对象实例描述信息
     * 					index 0 常规对象描述
     * 						  1 特殊对象描述
     */
    public String[] getTreeDesc(String _id) {
    	//logger.debug("		开始取得 "+_id+" 的信息");
    	
    	String[] _s = {"",""};
    	String _type = "";
    	String _index = "";
    	String _tableName = "";
    	HashMap _m = new HashMap();
    	
    	pSQL.setSQL(m_QOSConfigObjectIndex_SQL);
    	pSQL.setString(1,str_device_id);
    	pSQL.setString(2,str_qosindex);
    	pSQL.setString(3,_id);
    	_m = DataSetBean.getRecord(pSQL.getSQL());
//    	logger.debug("		获得 "+_id+" 的类型及索引 "+pSQL.getSQL());
    	
    	_type = (String)_m.get("qosobjectstype");
    	_index = (String)_m.get("qosconfigindex");
    	_m = null;    	
    	_tableName = (String)QOSObject_table.get(_type);
    	
    	if(_tableName!=null) {
    		pSQL.setSQL(m_QOSConfigObjectDetail_SQL);
        	pSQL.setStringExt(1,_tableName, false);
        	pSQL.setString(2,str_device_id);
        	pSQL.setString(3,_index);        	
        	_m = DataSetBean.getRecord(pSQL.getSQL());
        	//logger.debug("		!!!!获得 "+_id+" 的sql "+pSQL.getSQL());
        	
        	if(_m!=null) {
            	//对classmap对象特殊处理
            	//做特殊标记@@
            	//if(_type.equals("2")){
        		//	_s[1] = "$"+_id+"$"+_m.get("strdesc")+"\\n "+_m.get("strdesc1")+"\\n";
        		//}else{
        		//	_s[0] = "$"+_id+"$"+(String)_m.get("strdesc");
        		//}
            	_s[0] = (String)_m.get("strdesc");
        	}

    	}
    	
    	return _s;
    }
    
    /**
     * 实现通知后台从数据库中读取设备配置信息的功能
     */
    public void I_InformGather() {    	
        if(qosManager == null){
            qosManager = new VpnScheduler().getQosManager();
        }
        
        try{
            qosManager.I_InformGather(str_device_id);
        }catch(Exception e){
            e.printStackTrace();
            qosManager = new VpnScheduler().getQosManager();
            qosManager.I_InformGather(str_device_id);
        }
        
        logger.debug("通知后台重新读取设备" + str_device_id + "的QOS配置");
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO 自动生成方法存根
    	
    }

	/**
	 * @return 返回 qOSObject_type。
	 */
	public static LinkedHashMap getQOSObject_type() {
		return QOSObject_type;
	}

	/**
	 * @param endSeg 要设置的 endSeg。
	 */
	public void setEndSeg(String endSeg) {
		this.endSeg = endSeg;
	}

	/**
	 * @param starSeg 要设置的 starSeg。
	 */
	public void setStarSeg(String starSeg) {
		this.starSeg = starSeg;
	}
}
