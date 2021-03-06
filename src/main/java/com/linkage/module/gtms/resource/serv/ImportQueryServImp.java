package com.linkage.module.gtms.resource.serv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.resource.dao.ImportQueryDAOImp;

public class ImportQueryServImp {

	private static Logger logger = LoggerFactory
			.getLogger(ImportQueryServImp.class);

	private ImportQueryDAOImp dao = null;

	@SuppressWarnings("rawtypes")
	List<Map> list = null;

	@SuppressWarnings("rawtypes")
	public Map readUploadFile(int curPage_splitPage,int num_splitPage,File file, int rowNum, UserRes curUser,
			String fileType, String fileName,String field,String flag) {
		Map tmp = new HashMap(); 
		String fieldname = field;
		if ("xls".equals(fileType)) {
			if("readUploadFile".equals(flag)){
				tmp = analyzeExcel(curPage_splitPage,num_splitPage,file, rowNum,fileName);
				fieldname = StringUtil.getStringValue(tmp, "fieldname");
			}
		} else {
			if("readUploadFile".equals(flag)){
				tmp = analyzeTxt(curPage_splitPage,num_splitPage,file,fileName);
				fieldname = StringUtil.getStringValue(tmp, "fieldname");
			}
		}
		if("error".equals(fieldname)){
			return tmp;
		}
		list = dao.getInfoList(curPage_splitPage, num_splitPage, fileName, fieldname);
		int maxPage = 1;
		if("getExcel".equals(flag) || "goPage".equals(flag)){
			
		}else{
			maxPage = dao.getCountTotals(num_splitPage, fileName, fieldname);
			tmp.put("maxPage", maxPage);
		}
		
		tmp.put("list", list);
		
		return tmp;
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map> readUploadFile(File file, int rowNum, UserRes curUser,
			String fileType, String fileName) {
		if ("xls".equals(fileType)) {
			return analyzeExcel(file, rowNum);
		} else {
			return analyzeTxt(file);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List<Map> readUploadFile_bak(File file, int rowNum, UserRes curUser,
			String fileType, String fileName) {
		if ("xls".equals(fileType)) {
			return analyzeExcel(file, rowNum);
		} else {
			return analyzeTxt(file);
		}
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getErrorData() {
		return list;
	}

	@SuppressWarnings({ "rawtypes" })
	public List<Map> analyzeExcel(File file, int rowNum) {
		Workbook wwb = null;
		Sheet ws = null;
		List<Map> list = new ArrayList<Map>();
		list = new ArrayList<Map>();
		try {
			wwb = Workbook.getWorkbook(file);
			int sheetNumber = 1; // ??????????????????
			for (int m = 0; m < sheetNumber; m++) {
				String loid = "";
				String deviceSerialnumber = "";
				String interAccount = "";
				String itvAccount = "";
				String vocieAccount = "";
				ws = wwb.getSheet(m);
				int rowCount = ws.getRows(); // ??????
				int columeCount = ws.getColumns(); // ??????
				if (rowCount > rowNum + 1) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("default", "????????????2000???");
					map.put("error",
							"?????????????????????????????????2000????????????????????????????????????????????????2000???????????????????????????");
					list.add(map);
					return list;
				}

				if (rowCount > 1 && columeCount > 0) {					
					String headVal = ws.getCell(0, 0).getContents().trim();
					if (headVal.equals("??????LOID")) {
						for (int i = 1; i < rowCount; i++) {
							loid = ws.getCell(0, i).getContents().trim();
							getData(list, loid, deviceSerialnumber,
									interAccount, itvAccount, vocieAccount);
						}
					}
					if (headVal.equals("???????????????")) {
						for (int i = 1; i < rowCount; i++) {
							deviceSerialnumber = ws.getCell(0, i).getContents()
									.trim();
							getData(list, loid, deviceSerialnumber,
									interAccount, itvAccount, vocieAccount);
						}
					}
					if (headVal.equals("????????????")) {
						for (int i = 1; i < rowCount; i++) {
							interAccount = ws.getCell(0, i).getContents()
									.trim();
							getData(list, loid, deviceSerialnumber,
									interAccount, itvAccount, vocieAccount);
						}
					}
					if (headVal.equals("ITV??????")) {
						for (int i = 1; i < rowCount; i++) {
							itvAccount = ws.getCell(0, i).getContents().trim();
							getData(list, loid, deviceSerialnumber,
									interAccount, itvAccount, vocieAccount);
						}
					}
					if (headVal.equals("????????????")) {
						for (int i = 1; i < rowCount; i++) {
							vocieAccount = ws.getCell(0, i).getContents()
									.trim();
							getData(list, loid, deviceSerialnumber,
									interAccount, itvAccount, vocieAccount);
						}
					}
				}
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Map analyzeExcel(int curPage_splitPage,int num_splitPage,File file, int rowNum,String fileName) {
		Workbook wwb = null;
		Sheet ws = null;
		List<Map> list = new ArrayList<Map>();
		list = new ArrayList<Map>();
		List<String> accountsList = new ArrayList<String>();
		Map tmp = new HashMap();
		String fieldname = "username";
		Map<String, String> map = new HashMap<String, String>();
		try {
			wwb = Workbook.getWorkbook(file);
			int sheetNumber = 1; // ??????????????????
			for (int m = 0; m < sheetNumber; m++) {
				String loid = "";
				String deviceSerialnumber = "";
				String interAccount = "";
				String itvAccount = "";
				String vocieAccount = "";
				ws = wwb.getSheet(m);
				int rowCount = ws.getRows(); // ??????
				int columeCount = ws.getColumns(); // ??????
				if (rowCount > rowNum + 1) {
					map.put("default", "????????????2000???");
					map.put("error",
							"?????????????????????????????????2000????????????????????????????????????????????????2000???????????????????????????");
					list.add(map);
					tmp.put("list", list);
					tmp.put("maxPage", 0);
					tmp.put("fieldname", "error");
					return tmp;
				}
				if (rowCount > 1 && columeCount > 0) {
					
					String headVal = ws.getCell(0, 0).getContents().trim();
					if (headVal.equals("??????LOID")) {
						fieldname = "username";
						for (int i = 1; i < rowCount; i++) {
							loid = ws.getCell(0, i).getContents().trim();
							accountsList.add(loid);
						}
					}
					if (headVal.equals("???????????????")) {
						fieldname = "devicesn";
						for (int i = 1; i < rowCount; i++) {
							deviceSerialnumber = ws.getCell(0, i).getContents()
									.trim();
							accountsList.add(deviceSerialnumber);
						}
					}
					if (headVal.equals("????????????")) {
						fieldname = "netaccount";
						for (int i = 1; i < rowCount; i++) {
							interAccount = ws.getCell(0, i).getContents()
									.trim();
							accountsList.add(interAccount);
						}
					}
					if (headVal.equals("ITV??????")) {
						fieldname = "itvaccount";
						for (int i = 1; i < rowCount; i++) {
							itvAccount = ws.getCell(0, i).getContents().trim();
							accountsList.add(itvAccount);
						}
					}
					if (headVal.equals("????????????")) {
						fieldname = "voiceaccount";
						for (int i = 1; i < rowCount; i++) {
							vocieAccount = ws.getCell(0, i).getContents()
									.trim();
							accountsList.add(vocieAccount);
						}
					}
				}
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dao.insertTmp(fileName, accountsList, fieldname);
		tmp.put("fieldname", fieldname);
		return tmp;
	}

	@SuppressWarnings("rawtypes")
	private void getData(List<Map> list, String loid,
			String deviceSerialnumber, String interAccount, String itvAccount,
			String vocieAccount) {
		Map map = null;
		if (!StringUtil.IsEmpty(loid)
				|| !StringUtil.IsEmpty(deviceSerialnumber)
				|| !StringUtil.IsEmpty(interAccount)
				|| !StringUtil.IsEmpty(itvAccount)
				|| !StringUtil.IsEmpty(vocieAccount)) {
			map = dao.getInfo(loid, deviceSerialnumber, interAccount,
					itvAccount, vocieAccount);
		}
		if (map != null) {
			list.add(map);
		} else {
			list.add(dao.getErrorData());
		}
	}

	/**
	 * ??????TXT??????
	 * 
	 * @param file
	 * @param rowNum
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> analyzeTxt(File file) {
		// ??????????????????SQL
		List<Map> list = new ArrayList<Map>();
		list = new ArrayList<Map>();
		// ??????check???????????????
		String encoding = "GBK"; // ????????????(??????????????????????????? )
		if (file.isFile() && file.exists()) {
			InputStreamReader read=null;
			BufferedReader bufferedReader=null;
			try {
				read = new InputStreamReader(new FileInputStream(file),
						encoding);
				bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				String loid = "";
				String deviceSerialnumber = "";
				String interAccount = "";
				String itvAccount = "";
				String vocieAccount = "";
				if ((lineTXT = bufferedReader.readLine()) != null) {
					if ("??????LOID".equals(lineTXT)) {
						while ((loid = bufferedReader.readLine()) != null) {
							if (!StringUtil.IsEmpty(loid)) {
								getTxtData(list, loid, deviceSerialnumber,
										interAccount, itvAccount, vocieAccount);
							}
						}
					}
					if ("???????????????".equals(lineTXT)) {
						while ((deviceSerialnumber = bufferedReader.readLine()) != null) {
							if (!StringUtil.IsEmpty(deviceSerialnumber)) {
								getTxtData(list, loid, deviceSerialnumber,
										interAccount, itvAccount, vocieAccount);
							}
						}
					}
					if ("????????????".equals(lineTXT)) {
						while ((interAccount = bufferedReader.readLine()) != null) {
							if (!StringUtil.IsEmpty(interAccount)) {
								getTxtData(list, loid, deviceSerialnumber,
										interAccount, itvAccount, vocieAccount);
							}
						}
					}
					if ("ITV??????".equals(lineTXT)) {
						while ((itvAccount = bufferedReader.readLine()) != null) {
							if (!StringUtil.IsEmpty(itvAccount)) {
								getTxtData(list, loid, deviceSerialnumber,
										interAccount, itvAccount, vocieAccount);
							}
						}
					}
					if ("????????????".equals(lineTXT)) {
						while ((vocieAccount = bufferedReader.readLine()) != null) {
							if (!StringUtil.IsEmpty(vocieAccount)) {
								getTxtData(list, loid, deviceSerialnumber,
										interAccount, itvAccount, vocieAccount);
							}
						}
					}
				}
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if (null != read) {
						read.close();
						read = null;
					}
				} catch (Exception e) {
				}
				
				try {
					if (null != bufferedReader) {
						bufferedReader.close();
						bufferedReader = null;
					}
				} catch (Exception e) {
				}
			}
		}
		return list;
	}

	/**
	 * ??????TXT??????
	 * 
	 * @param file
	 * @param rowNum
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map analyzeTxt(int curPage_splitPage,int num_splitPage,File file,String fileName) {
		// ??????????????????SQL
		List<Map> list = new ArrayList<Map>();
		list = new ArrayList<Map>();
		List<String> accountsList = new ArrayList<String>();
		Map tmp = new HashMap();
		String fieldname = "username";
		// ??????check???????????????
		String encoding = "GBK"; // ????????????(??????????????????????????? )
		if (file.isFile() && file.exists()) {
			InputStreamReader read=null;
			BufferedReader bufferedReader=null;
			try {
				read = new InputStreamReader(new FileInputStream(file),
						encoding);
				bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				String loid = "";
				String deviceSerialnumber = "";
				String interAccount = "";
				String itvAccount = "";
				String vocieAccount = "";
				if ((lineTXT = bufferedReader.readLine()) != null) {
					if ("??????LOID".equals(lineTXT)) {
						fieldname = "username";
						while ((loid = bufferedReader.readLine()) != null) {
							if (!StringUtil.IsEmpty(loid)) {
								accountsList.add(loid);
							}
						}
					}
					if ("???????????????".equals(lineTXT)) {
						fieldname = "devicesn";
						while ((deviceSerialnumber = bufferedReader.readLine()) != null) {
							if (!StringUtil.IsEmpty(deviceSerialnumber)) {
								accountsList.add(deviceSerialnumber);
							}
						}
					}
					if ("????????????".equals(lineTXT)) {
						fieldname = "netaccount";
						while ((interAccount = bufferedReader.readLine()) != null) {
							if (!StringUtil.IsEmpty(interAccount)) {
								accountsList.add(interAccount);
							}
						}
					}
					if ("ITV??????".equals(lineTXT)) {
						fieldname = "itvaccount";
						while ((itvAccount = bufferedReader.readLine()) != null) {
							if (!StringUtil.IsEmpty(itvAccount)) {
								accountsList.add(itvAccount);
							}
						}
					}
					if ("????????????".equals(lineTXT)) {
						fieldname = "voiceaccount";
						while ((vocieAccount = bufferedReader.readLine()) != null) {
							if (!StringUtil.IsEmpty(vocieAccount)) {
								accountsList.add(vocieAccount);
							}
						}
					}
				}
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if (null != read) {
						read.close();
						read = null;
					}
				} catch (Exception e) {
				}
				
				try {
					if (null != bufferedReader) {
						bufferedReader.close();
						bufferedReader = null;
					}
				} catch (Exception e) {
				}
			}
		}
		if(accountsList.size() > 2000){
			Map<String, String> map = new HashMap<String, String>();
			map.put("default", "????????????2000???");
			map.put("error",
					"?????????????????????????????????2000????????????????????????????????????????????????2000???????????????????????????");
			list.add(map);
			tmp.put("list", list);
			tmp.put("maxPage", 0);
			tmp.put("fieldname", "error");
			return tmp;
		}
		dao.insertTmp(fileName, accountsList, fieldname);
		tmp.put("fieldname", fieldname);
		
		return tmp;
	}
	
	@SuppressWarnings("rawtypes")
	private void getTxtData(List<Map> list, String loid,
			String deviceSerialnumber, String interAccount, String itvAccount,
			String vocieAccount) {
		Map map = dao.getInfo(loid, deviceSerialnumber, interAccount,
				itvAccount, vocieAccount);
		if (map != null) {
			list.add(map);
		} else {
			list.add(dao.getErrorData());
		}
	}

	public int getMaxPage(int num_splitPage,File file,String fieldname){
		logger.warn("ImportQueryServImp-->getMaxPage()");
		int maxPage = 1;
		maxPage = dao.getCountTotals(num_splitPage, file.getName(), fieldname);
		return maxPage;
	}
	
	public ImportQueryDAOImp getDao() {
		return dao;
	}

	public void setDao(ImportQueryDAOImp dao) {
		this.dao = dao;
	}
}