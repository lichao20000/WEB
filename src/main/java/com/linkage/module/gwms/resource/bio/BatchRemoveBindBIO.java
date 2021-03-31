package com.linkage.module.gwms.resource.bio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ResourceBind.ResultInfo;
import ResourceBind.UnBindInfo;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.uss.DateUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.resource.dao.BatchRemoveBindDAO;
import com.linkage.module.gwms.share.act.FileUploadAction;
import com.linkage.module.gwms.util.CreateObjectFactory;
import com.linkage.module.gwms.util.ResourceBindInterface;

public class BatchRemoveBindBIO {

	private static Logger logger = LoggerFactory
			.getLogger(BatchRemoveBindBIO.class);

	private BatchRemoveBindDAO dao;

	/**
	 * 批量解绑操作
	 * 
	 * @param fileName
	 * @param taskId
	 * @param dealstaff
	 * @param userline
	 * @return
	 * @throws IOException
	 */
	public String removeBind(String fileName, String taskId, String userId,
			String upload_date, String dealstaff, int userline)
			throws IOException {
		List<String> loidList = null;
		String up_date = DateUtil.transTime(Long.parseLong(upload_date),
				"yyyy-MM-dd HH:mm:ss");
		try {
			loidList = getImportDataByTXT(fileName);
		} catch (FileNotFoundException e) {
			logger.warn("{}文件没找到！", fileName);
			return null;
		} catch (IOException e) {
			logger.warn("{}文件解析出错！", fileName);
			return null;
		} catch (Exception e) {
			logger.warn("{}文件解析出错！", fileName);
			return null;
		}

		dao.insertTask(taskId, userId, upload_date, fileName);
		PrepareSQL psql = null;
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int i = 0; i < loidList.size(); i++) {
			psql = new PrepareSQL();
			String msg = "";
			String user_id = "";
			String loid = "";
			String deviceid = "";
			Map<String, String> map = dao.isExist(loidList.get(i));
			if (map != null) {
				user_id = String.valueOf(map.get("user_id"));
				loid = String.valueOf(map.get("username"));
				deviceid = String.valueOf(map.get("device_id"));
				if (dao.isBind(loid)) {
					ResourceBindInterface corba = CreateObjectFactory.createResourceBind(
							LipossGlobals.getGw_Type(String.valueOf(map
									.get("device_id"))));

					UnBindInfo[] arr = new UnBindInfo[1];

					arr[0] = new UnBindInfo();
					arr[0].accOid = user_id;
					arr[0].accName = dealstaff;
					arr[0].userId = user_id;
					arr[0].deviceId = deviceid;
					arr[0].userline = userline;
					ResultInfo rs = corba.release(arr);

					if (rs == null) {
						msg = "解绑失败，系统内部错误";
					} else {
						// String status = rs.status;
						// 成功
						if (rs.resultId[0].equals("1")) {
							msg = "解绑"
									+ Global.G_ResourceBind_statusCode
											.get(Integer
													.parseInt(rs.resultId[0]));
						}
						// 失败
						else {
							// 获取相关错误码
							msg = "解绑失败，"
									+ Global.G_ResourceBind_resultCode
											.get(Integer
													.parseInt(rs.resultId[0]));
						}
					}

				} else {
					msg = "解绑失败，未存在绑定关系";
				}
			} else {
				deviceid = "空";
				msg = "没有匹配到loid";
			}
			if (deviceid.equals("null")) {
				deviceid = "空";
			}
			psql.append("insert into tab_unbind_task_loid (task_id,loid,device_id,result_info) values('"
					+ taskId
					+ "','"
					+ loidList.get(i)
					+ "','"
					+ deviceid
					+ "','" + msg + "')");
			sqlList.add(psql.getSQL());
		}
		dao.insertTaskLoid(sqlList);

		return "批量解绑执行完毕！";
	}

	public List<Map> getResultList(String task_id, int curPage_splitPage,
			int num_splitPage) {
		return dao.getResultList(task_id, curPage_splitPage, num_splitPage);
	}

	public int getResultCount(String task_id, int curPage_splitPage,
			int num_splitPage) {
		return dao.getResultCount(task_id, curPage_splitPage, num_splitPage);
	}

	public List<Map> getResultExcel(String task_id) {
		return dao.getResultExcel(task_id);
	}

	/**
	 * 把txt文件中的数据放到List<String>中
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public List<String> getImportDataByTXT(String fileName) throws IOException {
		List<String> list = new ArrayList();
		File file = new File(getFilePath() + fileName);
		BufferedReader in = new BufferedReader(new FileReader(getFilePath()
				+ fileName));
		String line = in.readLine();
		// 从第二行开始读取数据，并对每行数据去掉首尾空格
		while ((line = in.readLine()) != null) {
			if (!"".equals(line.trim())) {
				list.add(line.trim());
			}
		}
		in.close();
		in = null;
		file.delete();
		return list;
	}

	/**
	 * 判断txt文件的行数，最多100行
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public String fileLineTest(String fileName) {

		File f = new File(getFilePath() + fileName);
		int count = 0;

		InputStream input=null;
		BufferedReader b=null;
		try {
			input = new FileInputStream(f);

			b = new BufferedReader(new InputStreamReader(input));

			String value = b.readLine();

			while (value != null) {
				count++;
				value = b.readLine();
			}
		} catch (FileNotFoundException e) {
			logger.warn("{}文件没找到！", fileName);
		} catch (IOException e) {
			logger.warn("{}文件解析出错！", fileName);
		}finally{
			try {
				if(b!=null){
					b.close();
					b=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(input!=null){
					input.close();
					input=null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (count > 101 || count == 1) {
			f.delete();
			if (count == 1) {
				return "至少有一行有效数据！";
			}
			return "数据必须控制在100行之内，请重新上传！";
		}

		return "";

	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		// 获取系统的绝对路径
		String lipossHome = "";
		String a = FileUploadAction.class.getResource("/").getPath();
		try {
			lipossHome = java.net.URLDecoder.decode(
					a.substring(0, a.lastIndexOf("WEB-INF") - 1), "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage());
			lipossHome = null;
		}
		logger.debug("{}待解析的文件路径", lipossHome);
		return lipossHome + "/temp/";
	}

	public BatchRemoveBindDAO getDao() {
		return dao;
	}

	public void setDao(BatchRemoveBindDAO dao) {
		this.dao = dao;
	}

}
