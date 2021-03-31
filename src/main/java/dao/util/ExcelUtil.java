package dao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtil {

	private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);
	
	/**
	 * 根据传入的数组生成excel文件流，每10000行分一页  （该方法暂时不使用，保留）
	 * @param title  表格显示的标题，默认作为每一页的第一行
	 * @param dataList  结果集数组
	 * @param os  输出流
	 */
	public static void exportToExcel(String[] title,String[][] dataList,OutputStream os){
		
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
	 * 输入excel文件，解析后返回ArrayList
	 * @param file 输入的excel文件
	 * @return ArrayList<Map>，其中的map以第一行的内容为键值
	 */
	public static ArrayList<Map> importToExcel(File file){
		//初始化返回值和字段名数组
		ArrayList<Map> arr = new ArrayList<Map>();
		String[] title;
		
		Workbook wwb = null;
		
		try{
			//读取excel文件
			wwb = Workbook.getWorkbook(file);
			
			//总sheet数
			int sheetNumber = wwb.getNumberOfSheets();
			log.debug("sheetNumber:" + sheetNumber);
			
			for (int m=0;m<sheetNumber;m++){
				Sheet ws = wwb.getSheet(m);
				
				//当前页总记录行数和列数
				int rowCount = ws.getRows();
				int columeCount = ws.getColumns();
				
				log.debug("rowCount:" + rowCount);
				log.debug("columeCount:" + columeCount);
				
				//第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0){
					
					//取第一列为字段名
					title = new String[columeCount];
					for (int k=0;k<columeCount;k++){
						title[k] = ws.getCell(k, 0).getContents();
					}
					
					//取当前页所有值放入list中
					for (int i=1;i<rowCount;i++){
						
						Map<String,String> dataMap = new HashMap<String,String>();
						for (int j=0;j<columeCount;j++){
							dataMap.put(title[j], ws.getCell(j, i).getContents());
						}
						
						arr.add(dataMap);
					}
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return arr;
	}
	
	/**
	 * 输入excel文件，解析后返回ArrayList
	 * @param file 输入的excel文件
	 * @return ArrayList<Map>，其中的map以第一行的内容为键值，若有重复则添加数字标记
	 */
	public static ArrayList<Map> importToExcel_col(File file){
		//初始化返回值和字段名数组
		ArrayList<Map> arr = new ArrayList<Map>();
		String[] title;
		String[] tmp;
		Workbook wwb = null;
		
		try{
			//读取excel文件
			wwb = Workbook.getWorkbook(file);
			
			//总sheet数
			int sheetNumber = wwb.getNumberOfSheets();
			
			for (int m=0;m<sheetNumber;m++){
				Sheet ws = wwb.getSheet(m);
				
				//当前页总记录行数和列数
				int rowCount = ws.getRows();
				int columeCount = ws.getColumns();
				
				//第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0){
					
					//取第一列为字段名，若有重复则增加数字做标记
					title = new String[columeCount];
					tmp = new String[columeCount];
					int count = 0;
					for (int k=0;k<columeCount;k++){
						tmp[k] = ws.getCell(k, 0).getContents();
						count = checkExit(tmp , tmp[k]);
						if (count <= 1){
							title[k] = ws.getCell(k, 0).getContents();
						}
						else{
							title[k] = ws.getCell(k, 0).getContents() + "_" + count;
						}
					}
					
					//取当前页所有值放入list中
					for (int i=1;i<rowCount;i++){
						
						Map<String,String> dataMap = new HashMap<String,String>();
						for (int j=0;j<columeCount;j++){
							dataMap.put(title[j], ws.getCell(j, i).getContents());
						}
						
						arr.add(dataMap);
					}
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return arr;
	}
	
	/**
	 * 根据传入的数组生成excel文件流，每10000行分一页 （使用poi方法） 
	 * @param title  表格显示的标题，默认作为每一页的第一行
	 * @param dataList  结果集数组
	 * @param os  输出流
	 */
	public static void exportToExcel_poi(String[] title,String[][] dataList,OutputStream os){
		
		//将结果集转化为Excel输出
		int k = -1;
		HSSFWorkbook wwb = null;

		try {
			// 创建工作表
			wwb = new HSSFWorkbook();
			HSSFSheet ws = null;
			HSSFRow row = null;
			HSSFCell cell = null;
			
			// 逐行添加数据
			for (int i=0;i<dataList.length;i++){
				
				//每10000条记录分一页
				if (i/10000 > k){
					k = i/10000;
					ws = wwb.createSheet("Sheet" + k);
					row = ws.createRow(0);
					
					//在每页的第一行输入标题
					for (int l=0;l<title.length;l++){
						cell = row.createCell((short)l);
						cell.setCellValue(new HSSFRichTextString(title[l]));
					}
				}
				
				//输出数据
				for (int j=0;j<dataList[i].length;j++){
					row = ws.createRow(i-10000*k+1);
					cell = row.createCell((short)j);
					cell.setCellValue(new HSSFRichTextString(dataList[i][j]));
				}
				
			}
			wwb.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				os.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 传入excel文件，解析后返回ArrayList
	 * @param file
	 * @param startIndex 从第几行开始解析
	 */
	public static ArrayList<ArrayList> importExcel_poi(File file,int startIndex){
		ArrayList<ArrayList> arr = new ArrayList<ArrayList>();
		
		//初始化
		FileInputStream readFile = null;
		HSSFWorkbook wb = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		
		try{
			//读取文件
			readFile = new FileInputStream(file);
			wb = new HSSFWorkbook(readFile);
			
			//文档的页数
			int numOfSheets = wb.getNumberOfSheets();
			
			log.debug("numOfSheets:"+numOfSheets);
			
			for (int k=0;k<numOfSheets;k++){
				//获取当前的
				HSSFSheet st = wb.getSheetAt(k);
				
				//当前页的行数
				int rows = st.getLastRowNum();
				
				//分行解析
				for (int i=startIndex;i<=rows;i++){
					//行为空则执行下一行
					if (st.getRow(i) == null){
						continue;
					}
					row = st.getRow(i);
					int cells = row.getLastCellNum();
					
					ArrayList<String> data = new ArrayList<String>();
					//分列
					for (int j=0;j<cells;j++){
						//列为空则输入空字符串
						if (row.getCell((short)j) == null){
							data.add("");
							continue;
						}
						
						cell = row.getCell((short)j);
						
						//对字段分类处理
						switch (cell.getCellType()){
							case HSSFCell.CELL_TYPE_NUMERIC:{
								Integer num = new Integer((int) cell.getNumericCellValue());
								data.add(String.valueOf(num));
								break;
							}
							case HSSFCell.CELL_TYPE_STRING:{
								data.add(cell.getRichStringCellValue().toString());
								break;
							}
							default:data.add("");
						}
					}
					
					arr.add(data);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				readFile.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return arr;
	}
	
	/**
	 * 传入excel文件，解析后返回ArrayList。文件的第一行表示字段的名称
	 * @param file
	 */
	public static ArrayList<Map> importExcel_poi(File file){
		ArrayList<Map> arr = new ArrayList<Map>();
		String[] title;
		
		//初始化
		FileInputStream readFile = null;
		HSSFWorkbook wb = null;
		HSSFRow row = null;
		HSSFSheet st = null;
		HSSFCell cell = null;
		try{
			//读取文件
			readFile = new FileInputStream(file);
			wb = new HSSFWorkbook(readFile);
			
			//文档的页数
			int numOfSheets = wb.getNumberOfSheets();
			
			log.debug("numOfSheets:"+numOfSheets);
			
			for (int k=0;k<numOfSheets;k++){
				//获取当前的
				st = wb.getSheetAt(k);
				
				//当前页的行数
				int rows = st.getLastRowNum();
				
				//如果行数大于0，则先取第一行为字段名
				if (rows > 0){
					
					row = st.getRow(0);
					int cells = row.getLastCellNum();
					title = new String[cells];
					
					
					for (int j=0;j<cells;j++){
						//列为空则输入空字符串
						if (row.getCell((short)j) == null){
							title[j] = "";
							continue;
						}
						
						cell = row.getCell((short)j);
						switch (cell.getCellType()){
							case HSSFCell.CELL_TYPE_NUMERIC:{
								Integer num = new Integer((int) cell.getNumericCellValue());
								title[j] = String.valueOf(num);
								break;
							}
							case HSSFCell.CELL_TYPE_STRING:{
								title[j] = cell.getRichStringCellValue().toString();
								break;
							}
							default:title[j] = "";
						}
					}
					
					//分行解析
					for (int i=1;i<=rows;i++){
						//行为空则执行下一行
						if (st.getRow(i) == null){
							continue;
						}
						
						//将每行数据放入map中
						row = st.getRow(i);
						arr.add(getCellMap(row,cells,title));
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				readFile.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return arr;
	}
	
	/**
	 * 根据传入的ｅｘｃｅｌ行数据得到数据Ｍａｐ
	 * @param row
	 * @param cells
	 * @param title
	 * @return
	 */
	private static Map getCellMap(HSSFRow row,int cells,String[] title){
		//初始化
		HSSFCell cell = null;
		Map<String,String> data = new HashMap<String,String>();
		//分列
		for (int j=0;j<cells;j++){
			//列为空则输入空字符串
			if (row.getCell((short)j) == null){
				data.put(title[j], "");
				continue;
			}
			
			cell = row.getCell((short)j);
			
			//对字段分类处理
			switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC:{
					Integer num = new Integer((int) cell.getNumericCellValue());
					data.put(title[j], String.valueOf(num));
					break;
				}
				case HSSFCell.CELL_TYPE_STRING:{
					data.put(title[j], cell.getRichStringCellValue().toString());
					break;
				}
				default:data.put(title[j], "");
			}
		}
		
		return data;
	}
	/**
	 * 返回数组中有几个重复纪录
	 * @param arr
	 * @param value
	 * @return
	 */
	private static int checkExit(String[] arr, String value){
		int j=0;
		for (int i=0;i<arr.length;i++){
			if (arr[i] != null && value.equals(arr[i])){
				j++;
			}
		}
		return j;
	}

}
