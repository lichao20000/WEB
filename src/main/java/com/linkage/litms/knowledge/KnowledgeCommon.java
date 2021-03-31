/*
 * KnowledgeCommon.java
 * Created on 2006年2月14日, 下午3:49
 * Copyright 2006 联创科技.版权所有
 */

package com.linkage.litms.knowledge;

import java.util.ArrayList;
import java.util.Map;

import com.linkage.commons.db.DBUtil;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * 
 * @author yanhj
 */
public class KnowledgeCommon
{

	public static Knowledge getKnowledge(String serialno, String gather_id)
	{
		String sql = "select * from tab_knowledge where serialno='" + serialno
				+ "' and subserialno=0 and gather_id='" + gather_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select subject, content, creator, warnreason, warnresove, devicetype, sourceip, creatortype, " +
					"gather_id, sourcename, createtime, ack_createtime from tab_knowledge where serialno='" + serialno
					+ "' and subserialno=0 and gather_id='" + gather_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Map hashtable = DataSetBean.getRecord(sql);
		if (hashtable != null)
		{
			Knowledge knowledge = new Knowledge(serialno, gather_id);
			knowledge.subject = (String) hashtable.get("SUBJECT".toLowerCase());
			knowledge.content = (String) hashtable.get("CONTENT".toLowerCase());
			knowledge.creator = (String) hashtable.get("CREATOR".toLowerCase());
			knowledge.warnReason = (String) hashtable.get("WARNREASON"
					.toLowerCase());
			knowledge.warnResove = (String) hashtable.get("WARNRESOVE"
					.toLowerCase());
			knowledge.deviceType = Long.valueOf(
					(String) hashtable.get("devicetype")).longValue();

			knowledge.sourceIp = (String) hashtable.get("sourceip");
			knowledge.creatorType = Integer.valueOf(
					(String) hashtable.get("creatortype")).intValue();

			knowledge.gather_id = (String) hashtable.get("gather_id");
			knowledge.sourceName = (String) hashtable.get("sourcename");
			try
			{
				knowledge.createtime = Long.parseLong((String) hashtable
						.get("CREATETIME".toLowerCase()));
				knowledge.ack_CreateTime = Long.parseLong((String) hashtable
						.get("ack_createtime"));

			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return knowledge;
		} else
		{
			return null;
		}
	}

	public static ArrayList getKnowledgeList(String key,
			boolean searchSubjectOnly, String creator, long start, long end)
	{
		String sql = "select * from tab_knowledge";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select serialno, gather_id, subject, content, creator, createtime, warnreason, warnresove " +
					" from tab_knowledge";
		}
		String subSql = "";
		if (creator != null)
		{
			creator = creator.trim();
			if (!creator.equals(""))
				subSql = " creator='" + creator + "'";
		}
		if (start != 0)
			subSql += " and createtime>=" + start;
		if (end != 0)
			subSql += " and createtime<" + (end + 3600);
		// if(key!=null) && !key.trim().equals("")){
		if (key != null)
		{
			key = key.trim();
			if (!key.equals(""))
			{
				if (searchSubjectOnly)
					subSql += " and subject like '%" + key + "%'";
				else
					subSql += " and (subject like '%" + key
							+ "%' or content like '%" + key + "%')";
			}
		}
		if (!subSql.equals(""))
		{
			final String STR = " and";
			if (subSql.startsWith(STR))
				subSql = subSql.substring(STR.length());
			sql = sql + " where" + subSql;
		}

		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		ArrayList list = new ArrayList();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map hashtable = cursor.getNext();
		while (hashtable != null)
		{
			Knowledge knowledge = new Knowledge();
			knowledge.serialno = (String) hashtable.get("serialno"
					.toLowerCase());
			knowledge.gather_id = (String) hashtable.get("gather_id"
					.toLowerCase());
			knowledge.subject = (String) hashtable.get("SUBJECT".toLowerCase());
			knowledge.content = (String) hashtable.get("CONTENT".toLowerCase());
			knowledge.creator = (String) hashtable.get("CREATOR".toLowerCase());
			knowledge.createtime = Long.parseLong((String) hashtable
					.get("CREATETIME".toLowerCase()));
			knowledge.warnReason = (String) hashtable.get("WARNREASON"
					.toLowerCase());
			knowledge.warnResove = (String) hashtable.get("WARNRESOVE"
					.toLowerCase());
			list.add(knowledge);

			hashtable = cursor.getNext();
		}
		return list;
	}

	public static ArrayList getKnowledgeList(String creator, long start,
			long end)
	{
		return getKnowledgeList(null, true, creator, start, end);
	}

	public static ArrayList getKnowledgeList(String creator)
	{
		return getKnowledgeList(null, true, creator, 0, 0);
	}

	public static ArrayList getKnowledgeList()
	{
		return getKnowledgeList(null, true, null, 0, 0);
	}

	public static ArrayList getUserList()
	{
		String sql = "select acc_loginname from tab_accounts";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		ArrayList list = new ArrayList();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map hashtable = cursor.getNext();
		while (hashtable != null)
		{
			list.add((String) hashtable.get("ACC_LOGINNAME".toLowerCase()));
			hashtable = cursor.getNext();
		}
		return list;
	}
}
