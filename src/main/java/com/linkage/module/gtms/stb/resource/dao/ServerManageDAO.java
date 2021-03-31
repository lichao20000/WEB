package com.linkage.module.gtms.stb.resource.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.StringUtil;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.JdbcTemplateExtend;

public class ServerManageDAO
{

	private static Logger logger = LoggerFactory.getLogger(ServerManageDAO.class);

	private JdbcTemplateExtend jt;


	/**
	 *  机顶盒运营画面服务器管理查询
	 *
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param serverName
	 * @param serverUrl
	 * @return
	 */
	public List<Map> queryServer(int curPage_splitPage, int num_splitPage, String serverName, String serverUrl) {

		logger.debug("ServerManageDAO==>queryServer({},{},{},{})",
				new Object[] { curPage_splitPage, num_splitPage, serverName,
						serverUrl });

		PrepareSQL psql = new PrepareSQL();
		psql.append("select server_id, server_name, server_url, access_user, access_passwd, file_type from stb_tab_picture_file_server ");
		psql.append(" where 1=1 ");

		if(null != serverName && false == serverName.isEmpty()){
			psql.append("   and server_name = '");
			psql.append(serverName);
			psql.append("'");
		}

		if (null != serverUrl && false == serverUrl.isEmpty()) {
			psql.append("   and server_url = '");
			psql.append(serverUrl);
			psql.append("'");
		}

		List<Map> list = jt.querySP(psql.getSQL(), (curPage_splitPage - 1) * num_splitPage,
				num_splitPage, new RowMapper(){
			public Object mapRow(ResultSet rs, int arg1) throws SQLException
			{
				Map<String, String> map = new HashMap<String, String>();

				map.put("serverId", StringUtil.getStringValue(rs.getString("server_id")));
				map.put("serverName", StringUtil.getStringValue(rs.getString("server_name")));
				map.put("serverUrl", StringUtil.getStringValue(rs.getString("server_url")));
				map.put("accessUser", StringUtil.getStringValue(rs.getString("access_user")));
				map.put("accessPasswd", StringUtil.getStringValue(rs.getString("access_passwd")));
				String fileType = StringUtil.getStringValue(rs.getString("file_type"));
				map.put("fileType", fileType);
				if ("1".equals(fileType)) {
					map.put("fileTypeValue", "上传");
				} else if ("2".equals(fileType)) {
					map.put("fileTypeValue", "下载");
				}else {
					map.put("fileTypeValue", "-");
				}

				return map;
			}
		});

		return list;
	}


	public int countServerNum(int curPage_splitPage, int num_splitPage, String serverName, String serverUrl){

		logger.debug("ServerManageDAO==>countServerNum({},{},{},{})",
				new Object[] { curPage_splitPage, num_splitPage, serverName,
						serverUrl });

		PrepareSQL psql = new PrepareSQL();
		psql.append("select count(*) from stb_tab_picture_file_server ");
		psql.append(" where 1=1 ");

		if(null != serverName && false == serverName.isEmpty()){
			psql.append("   and server_name = '");
			psql.append(serverName);
			psql.append("'");
		}

		if (null != serverUrl && false == serverUrl.isEmpty()) {
			psql.append("   and server_url = '");
			psql.append(serverUrl);
			psql.append("'");
		}

		int total = jt.queryForInt(psql.getSQL());
		int maxPage = 1;
		if (total % num_splitPage == 0)
		{
			maxPage = total / num_splitPage;
		}
		else
		{
			maxPage = total / num_splitPage + 1;
		}
		return maxPage;
	}


	/**
	 * 新增
	 *
	 * @param serverNameAdd
	 * @param accessUserAdd
	 * @param accessPasswdAdd
	 * @param serverUrlAdd
	 * @param fileTypeAdd
	 * @return
	 */
	public String addServer(String serverNameAdd, String accessUserAdd, String accessPasswdAdd, String serverUrlAdd, String fileTypeAdd) {

		logger.debug("ServerManageDAO==>addServer({},{},{},{},{})",
				new Object[] { serverNameAdd, accessUserAdd, accessPasswdAdd,
						serverUrlAdd, fileTypeAdd });

		PrepareSQL psql = new PrepareSQL();

		long maxServerId = DBOperation.getMaxId("server_id", "stb_tab_picture_file_server");
		++maxServerId;

		psql.append("insert into stb_tab_picture_file_server(server_id, server_name, server_url, access_user, access_passwd, file_type) values(");
		psql.append(StringUtil.getStringValue(maxServerId));
		psql.append(",'");
		psql.append(serverNameAdd);
		psql.append("','");
		psql.append(serverUrlAdd);
		psql.append("','");
		psql.append(accessUserAdd);
		psql.append("','");
		psql.append(accessPasswdAdd);
		psql.append("',");
		psql.append(fileTypeAdd);
		psql.append(")");

		int[] iCodes = DataSetBean.doBatch(psql.getSQL());

		if (iCodes != null && iCodes.length > 0) {
			return "新增成功！";
		} else {
			return "新增失败！";
		}
	}



	/**
	 * 编辑
	 *
	 * @param serverId
	 * @param serverNameAdd
	 * @param accessUserAdd
	 * @param accessPasswdAdd
	 * @param serverUrlAdd
	 * @param fileTypeAdd
	 * @return
	 */
	public String editServer(String serverId, String serverNameAdd, String accessUserAdd, String accessPasswdAdd, String serverUrlAdd, String fileTypeAdd) {

		logger.debug("ServerManageDAO==>editServer({},{},{},{},{},{})",
				new Object[] { serverId, serverNameAdd, accessUserAdd, accessPasswdAdd,
						serverUrlAdd, fileTypeAdd });

		PrepareSQL psql = new PrepareSQL();
		psql.append("update stb_tab_picture_file_server set server_name = '");
		psql.append(serverNameAdd);
		psql.append("',");
		psql.append("server_url = '");
		psql.append(serverUrlAdd);
		psql.append("',");
		psql.append("access_user = '");
		psql.append(accessUserAdd);
		psql.append("',");
		psql.append("access_passwd = '");
		psql.append(accessPasswdAdd);
		psql.append("',");
		psql.append("file_type = ");
		psql.append(fileTypeAdd);
		psql.append(" where server_id = ");
		psql.append(serverId);

		int[] iCodes = DataSetBean.doBatch(psql.getSQL());

		if (iCodes != null && iCodes.length > 0) {
			return "编辑成功！";
		} else {
			return "编辑失败！";
		}
	}


	/**
	 * 删除
	 *
	 * @param serverId
	 * @return
	 */
	public String deleServer(String serverId) {

		logger.debug("ServerManageDAO==>deleServer({})", new Object[]{serverId});

		PrepareSQL psql = new PrepareSQL();
		psql.append("delete from stb_tab_picture_file_server where server_id = ");
		psql.append(serverId);

		int[] iCodes = DataSetBean.doBatch(psql.getSQL());

		if (iCodes != null && iCodes.length > 0) {
			return "删除成功！";
		} else {
			return "删除失败！";
		}
	}

	public void setDao(DataSource dao) {
		jt = new JdbcTemplateExtend(dao);
	}
}
