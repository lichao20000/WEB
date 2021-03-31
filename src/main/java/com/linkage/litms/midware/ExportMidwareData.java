package com.linkage.litms.midware;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

/**
 * 中间件管理导出excel数据功能
 * @author 陈仲民（５２４３）
 * @since 2008-01-24
 */
public class ExportMidwareData
{
	
	/**
	 * 导出域数据
	 * @param os 输出流
	 */
	public static void getAreaExcelFile(OutputStream os){
		
		//定义文件标题
		String[] title = new String[2];
		title[0] = "area";
		title[1] = "Oui_sn";
		
		//获取数据
		Cursor batchAreaInfo = getBatchAreaInfo(1);
		String[][] dataList = new String[batchAreaInfo.getRecordSize()][2];
		
		int i = 0;
		Map fields = batchAreaInfo.getNext();
		while (fields != null){
			
			dataList[i][0] = (String)fields.get("area_id");
			dataList[i][1] = (String)fields.get("device_serialnumber");
			
			i++;
			fields = batchAreaInfo.getNext();
		}
		
		//生成excel文档
		exportToExcel(title, dataList, os);
	}
	
	/**
	 * 导出设备数据
	 * @param os 输出流
	 */
	public static void getDeviceExcelFile(OutputStream os){
		
		//定义文件标题
		String[] title = new String[7];
		title[0] = "oui";
		title[1] = "sn";
		title[2] = "type";
		title[3] = "area";
		title[4] = "gp";
		title[5] = "note";
		title[6] = "status";
		
		//获取数据
		Cursor batchDeviceInfo = getBatchDeviceInfo(1);
		int rowCount = batchDeviceInfo.getRecordSize();
		String[][] dataList = new String[rowCount][7];
		
		int i = 0;
		Map fields = batchDeviceInfo.getNext();
		while (fields != null){
			dataList[i][0] = fields.get("oui") == null ? "" : (String)fields.get("oui");
			dataList[i][1] = fields.get("device_serialnumber") == null ? "" : (String)fields.get("device_serialnumber");
			dataList[i][2] = "e8b";
			dataList[i][3] = fields.get("area_id") == null ? "0" : (String)fields.get("area_id");
			dataList[i][4] = "0";
			dataList[i][5] = fields.get("phonenumber") == null ? "" : (String)fields.get("phonenumber");
			dataList[i][6] = fields.get("device_status") == null ? "" : (String)fields.get("device_status");
			
			i++;
			fields = batchDeviceInfo.getNext();
		}
		
		//生成excel文档
		exportToExcel(title, dataList, os);
	}
	
	/**
	 * 根据数据生成excel文件
	 * @param title  标题
	 * @param dataList  数据集，二维数组形式
	 * @param os  输出流
	 */
	private static void exportToExcel(String[] title,String[][] dataList,OutputStream os){
		
		// 然后将结果集转化为Excel输出
		// 初始化工作
		WritableWorkbook wwb = null;
		Label labelC = null;
		int k = -1;

		try {
			// 创建工作表
			wwb = Workbook.createWorkbook(os);
			WritableSheet ws = null;
			
			// 逐行添加数据
			for (int i=0;i<dataList.length;i++){
				
				//每10000条记录分一页
				if (i/10000 > k){
					k = i/10000;
					ws = wwb.createSheet("Sheet" + k, k);
					
					//在每页的第一行输入标题
					for (int l=0;l<title.length;l++){
						labelC = new Label(l, 0, title[l]);
						ws.addCell(labelC);
						
					}
				}
				
				//输出数据
				for (int j=0;j<dataList[i].length;j++){
					labelC = new Label(j, i-10000*k+1, dataList[i][j]);
					ws.addCell(labelC);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (wwb != null) {
				try{
					wwb.write();
					wwb.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 设备信息数据的初始同步
	 * @param gw_type
	 * @return
	 */
	private static Cursor getBatchDeviceInfo(int gw_type) {
		// gw_type 1:家庭网关设备 2:企业网关设备
		
		ArrayList list = new ArrayList();
		list.clear();
		String sql = "select d.device_id, d.device_type, d.oui, d.device_serialnumber, d.device_status, h.phonenumber, a.area_id "
			+ " from tab_gw_device d, tab_hgwcustomer h, tab_gw_res_area a " 
			+ " where gw_type=" + gw_type 
			+ " and h.device_serialnumber=d.device_serialnumber " 
			+ " and a.res_id=d.device_id ";

		PrepareSQL psql = new PrepareSQL(sql);
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		return cursor;
	}

	/**
	 * 域信息数据的初始同步
	 * @param gw_type
	 * @return
	 */
	private static Cursor getBatchAreaInfo(int res_type) {
		// res_type 1：TR069 GW
		
		ArrayList list = new ArrayList();
		list.clear();
		String sql = "select a.area_id, d.device_serialnumber " 
			+ " from tab_gw_res_area a, tab_gw_device d "
			+ " where a.res_type=" + res_type + " and a.res_id=d.device_id";
		PrepareSQL psql = new PrepareSQL(sql);
		Cursor cursor = DataSetBean.getCursor(psql.getSQL());
		return cursor;
	}
}
