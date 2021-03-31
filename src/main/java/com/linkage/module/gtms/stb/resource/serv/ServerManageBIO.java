package com.linkage.module.gtms.stb.resource.serv;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.gtms.stb.resource.dao.ServerManageDAO;


public class ServerManageBIO
{

	private static Logger logger = LoggerFactory.getLogger(ServerManageBIO.class);
	
	private ServerManageDAO serverManageDAO ;
	
	
	/**
	 *  查询机顶盒运营画面服务器
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param serverName
	 * @param serverUrl
	 * @return
	 */
	public List<Map> queryServer(int curPage_splitPage, int num_splitPage, String serverName, String serverUrl)
	{
		return serverManageDAO.queryServer(curPage_splitPage, num_splitPage, serverName, serverUrl);
	}
	
	/**
	 * 统计机顶盒运营画面服务器 
	 * 
	 * 用于分页
	 * 
	 * @param curPage_splitPage
	 * @param num_splitPage
	 * @param serverName
	 * @param serverUrl
	 * @return
	 */
	public int countServerNum(int curPage_splitPage, int num_splitPage, String serverName, String serverUrl){
		return serverManageDAO.countServerNum(curPage_splitPage, num_splitPage, serverName, serverUrl);
	}
	
	/**
	 * 新增 机顶盒运营画面服务器
	 * 
	 * @param serverNameAdd
	 * @param accessUserAdd
	 * @param accessPasswdAdd
	 * @param serverUrlAdd
	 * @param fileTypeAdd
	 * @return
	 */
	public String addServer(String serverNameAdd, String accessUserAdd, String accessPasswdAdd, String serverUrlAdd, String fileTypeAdd) {
		
		logger.debug("ServerManageBIO==>addServer({},{},{},{},{})",
				new Object[] { serverNameAdd, accessUserAdd, accessPasswdAdd,
						serverUrlAdd, fileTypeAdd });
		
		return serverManageDAO.addServer(serverNameAdd, accessUserAdd, accessPasswdAdd, serverUrlAdd, fileTypeAdd);
	}
	
	/**
	 * 修改
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
		
		logger.debug("ServerManageBIO==>editServer({},{},{},{},{},{})",
				new Object[] { serverId, serverNameAdd, accessUserAdd, accessPasswdAdd,
						serverUrlAdd, fileTypeAdd });
		
		return serverManageDAO.editServer(serverId, serverNameAdd, accessUserAdd, accessPasswdAdd, serverUrlAdd, fileTypeAdd);
	}
	
	/**
	 * 删除
	 * 
	 * @param serverId
	 * @return
	 */
	public String deleServer(String serverId) {
		
		logger.debug("ServerManageBIO==>deleServer({})", new Object[] { serverId });
		
		return serverManageDAO.deleServer(serverId);
	}
	
	public ServerManageDAO getServerManageDAO() {
		return serverManageDAO;
	}

	public void setServerManageDAO(ServerManageDAO serverManageDAO) {
		this.serverManageDAO = serverManageDAO;
	}
}
