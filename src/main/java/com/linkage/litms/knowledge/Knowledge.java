/*
 * Knowledge.java
 * Created on 2006年2月14日, 下午3:09
 * Copyright 2006 联创科技.版权所有
 */

package com.linkage.litms.knowledge;

import java.util.Date;


import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;

/**
 * 
 * @author yanhj
 */
public class Knowledge {
    /** 编号 */
    //public long id;
	/**
	 * 序列号
	 */
    public String serialno = null;
    /**
     * 子序列号,写死,0
     */
    private int subserialno = 0;
    /**属地*/
    public String gather_id;
    /** 主题 */
    public String subject;
    /** 内容 */
    public String content;
    /** 创建人 */
    public String creator;
    /** 创建时间（秒），自1970年1月1日零时到现在的秒数 */
    public long createtime;
    /**告警的原因*/
    public String warnReason;
    /**解决方法*/
    public String warnResove;

	 //以下是 Knowledge 新增的 属性
    /**告警创建时间*/
    public long ack_CreateTime;
    /**设备类型*/
    public long deviceType;
    /**设备IP*/
    public String  sourceIp;
    /**告警创建者类型*/
    public int  creatorType;
    /**告警设备名称*/
    public String sourceName;
    
    /**
     * Creates a new instance of Knowledge
     */
    public Knowledge() {
    }
    
    /**
     * Creates a new instance of Knowledge
     * @param id 编号
     * @param subject 主题
     * @param content 内容
     * @param creator 创建人
     * @param createtime 创建时间
     */
    public Knowledge(String serialno,String gather_id,String subject,String content,String creator,long createtime,String warnReason,String warnResove) {
        this.serialno = serialno;
        this.gather_id = gather_id;
        this.subject = subject;
        this.content = content;
        this.creator = creator;
        this.createtime = createtime;
        this.warnReason = warnReason;
        this.warnResove = warnResove;
    }
    
    /**
     * Creates a new instance of Knowledge
     * @param id 编号
     */
    public Knowledge(String serialno,String gather_id) {
    	this.serialno = serialno;
        this.gather_id = gather_id;
    }
    
    /**
     * Creates a new instance of Knowledge
     * @param subject 主题
     * @param content 内容
     * @param creator 创建人
     */
    public Knowledge(String subject,String content,String creator,String warnReason,String warnResove) {
        this.subject = subject;
        this.content = content;
        this.creator = creator;
        this.warnReason = warnReason;
        this.warnResove = warnResove;

    }
    
    /**
     * 添加
     * @return 添加成功返回true，添加失败返回false
     */
    public boolean add(){
        //id = DataSetBean.getMaxId("tab_knowledge","id");
        createtime = new Date().getTime()/1000;
        String sql = "insert into tab_knowledge(serialno,subserialno,gather_id,subject,content,creator,createtime,warnreason,warnresove,ack_createtime,devicetype,sourceip,creatortype,sourcename) values('" + serialno+"',"+subserialno+",'" + gather_id + "','" + subject + "','" + content + "','" + creator + "', " + createtime + " ,'" + warnReason + "','" + warnResove + "', " + ack_CreateTime + " , " + deviceType + " ,'" + sourceIp + "'," + creatorType + " ,'" + sourceName + "')";     
        PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
        return DataSetBean.executeUpdate(sql)>0;
    }
    
    /**
     * 修改
     * @return 修改成功返回true，修改失败返回false
     */
    public boolean modify(){
        String sql = "update tab_knowledge set subject='"
        	+subject+"',content='"+content+"',warnreason='"
        	+warnReason+"',warnresove='"+warnResove+"' where serialno='"+serialno+"' and subserialno="+subserialno+" and gather_id='"+gather_id+"'" ;
        PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
        return DataSetBean.executeUpdate(sql)>0;
    }
    
    /**
     * 删除
     * @return 删除成功返回true，删除失败返回false
     */
    public boolean delete(){
        String sql = "delete from tab_knowledge where serialno='"+serialno+"' and subserialno="+subserialno+" and gather_id='"+gather_id+"'" ;
        PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
        return DataSetBean.executeUpdate(sql)>0;
    }
    
    public String getCreatorType()
	{
		String creatorTypeStr = null;
		switch (creatorType)
		{
		case 1:
			creatorTypeStr = "主机告警";
			break;
		case 2:
			creatorTypeStr = "PMEE告警";
			break;
		case 3:
			creatorTypeStr = "Syslog告警";
			break;
		case 4:
			creatorTypeStr = "Trap告警";
			break;
		case 5:
			creatorTypeStr = "规则引擎";
			break;
		case 6:
			creatorTypeStr = "Topo告警";
			break;
		case 7:
			creatorTypeStr = "业务告警";
			break;
		case 8:
			creatorTypeStr = "Ping检测设备通断";
			break;
		case 9:
			creatorTypeStr = "华为设备端口检查";
			break;
		case 10:
			creatorTypeStr = "Visualman";
			break;
		case 20:
			creatorTypeStr = "亚信告警";
			break;
		case 21:
			creatorTypeStr = "短信告警";
			break;
		default:
			creatorTypeStr = "未知告警";
		}
		return creatorTypeStr;
	}
}
