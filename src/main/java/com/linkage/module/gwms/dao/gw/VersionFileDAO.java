package com.linkage.module.gwms.dao.gw;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.SuperDAO;

/**
 * @author Jason(3412)
 * @date 2009-7-13
 */
public class VersionFileDAO extends SuperDAO 
{
	private static Logger logger = LoggerFactory.getLogger(VersionFileDAO.class);

	/**
	 * 获取文件服务器路径信息
	 * 
	 * @param 1:soft
	 *            2:config 3:log
	 * @author Jason(3412)
	 * @date 2009-7-13
	 * @return String
	 */
	public String getFileParam(int type) {
		logger.debug("getFileParam(int type)" + type);
		String fileUrl = null;
		String strSQL = "";
		PrepareSQL psql = new PrepareSQL();
		if(DBUtil.GetDB()==Global.DB_MYSQL){
			psql.append("select outter_url,server_dir,dir_id ");
		}else{
			psql.append("select * ");
		}
		psql.append("from tab_file_server where file_type=? ");
		psql.setInt(1, type);
		List rList = jt.queryForList(psql.getSQL());
		if (null != rList && false == rList.isEmpty()) {
			Map tMap = (Map) rList.get(0);
			fileUrl = tMap.get("outter_url") + "/" + tMap.get("server_dir")
					+ "?dir_id=" + tMap.get("dir_id");
		} else {
			logger.warn("没有对应的文件服务器路径记录存在");
		}
		return fileUrl;
	}

}
