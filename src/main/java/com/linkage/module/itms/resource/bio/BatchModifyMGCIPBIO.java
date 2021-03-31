
package com.linkage.module.itms.resource.bio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.DateTimeUtil;
import com.linkage.commons.util.StringUtil;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.util.SocketUtil;

/**
 * @author yinlei3 (Ailk No.73167)
 * @version 1.0
 * @since 2016年3月21日
 * @category com.linkage.module.itms.resource.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class BatchModifyMGCIPBIO
{

	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(BatchModifyMGCIPBIO.class);

	/***
	 * 下载模板文件
	 * 
	 * @param filepath
	 * @param response
	 */
	public void download(String filepath, HttpServletResponse response)
	{
		logger.debug("download({},{})", new Object[] { filepath, response });
		InputStream fis=null;
		OutputStream os=null;
		try
		{
			// path是指欲下载的文件的路径
			File file = new File(filepath);
			// 取得文件名
			String filename = file.getName();
			// 以流的形式下载文件。
			fis = new BufferedInputStream(new FileInputStream(filepath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			fis=null;
			
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(filename, "UTF-8"));
			response.addHeader("Content-Length", "" + file.length());
			os = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			os.write(buffer);
			os.flush();
			os.close();
			os=null;
		}
		catch (IOException e)
		{
			logger.error("download file:[{}], error:", filepath, e);
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 保存文件
	 * 
	 * @param source
	 * @param newFileDir
	 * @param newFileName
	 * @return
	 */
	public String saveFile(File source, String newFileDir, String newFileName)
	{
		logger.debug("saveFile({})", new Object[] { source.getAbsolutePath(), newFileDir,
				newFileName });
		String result = "1";
		File target = new File(newFileDir, newFileName);
		try
		{
			// 文件复制
			FileUtils.copyFile(source, target);
		}
		catch (Exception e)
		{
			logger.error("copy file error:", e);
			result = "0";
		}
		// 1成功,0失败
		return result;
	}

	/**
	 * 解析excel入库操作
	 * 
	 * @param downLoadTargetFile
	 *            文件名
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String parseExcel(String downLoadTargetFile) throws FileNotFoundException,
			IOException
	{
		logger.warn("开始解析 " + downLoadTargetFile);
		// 逻辑ID
		String loid = "";
		// 设备类型
		String devType = "";
		// 业务电话号码
		String voipTelepone = "";
		// 属地
		String cityId = "";
		// 终端标识
		String regId = "";
		// 终端标识的类型
		String regIdType = "";
		// 主用MGCIP地址
		String MGCaddr = "";
		// 主用MGC器端口
		String MGCPort = "";
		// 备用MGCIP地址
		String standMGCaddr = "";
		// 备用MGC端口
		String standMGCPort = "";
		// 终端物理标示
		String linePort = "";
		// 协议类型
		String agreementType = "";
		File target = new File(downLoadTargetFile);
		// 为了兼容2003与2007
		Workbook workbook = null;
		try
		{
			workbook = new XSSFWorkbook(new FileInputStream(target));
		}
		catch (Exception e)
		{
			workbook = new HSSFWorkbook(new FileInputStream(target));
		}
		if (null != target && target.exists())
		{
			// 创建工作表
			// 获得sheet
			Sheet sheet = workbook.getSheetAt(0);
			// 获得sheet的总行数
			int rowCount = sheet.getLastRowNum();
			logger.debug("获得sheet的总行数:" + rowCount);
			if (rowCount > 1000)
			{
				target.delete();
				target = null;
				return "Excel行数超过1000,影响效率,请修改后重试!";
			}
			Cell cellFirst = null;
			Cell cellSecond = null;
			Cell cellThird = null;
			Cell cellFourth = null;
			Cell cellFiveth = null;
			Cell cellSixth = null;
			Cell cellSeventh = null;
			Cell cellEghth = null;
			Cell cellNighth = null;
			Cell cellTen = null;
			Cell cellEleven = null;
			Cell cellTwelve = null;
			// 循环解析每一行
			for (int i = 0; i <= rowCount; i++)
			{
				// 获得行对象
				Row row = sheet.getRow(i);
				cellFirst = row.getCell(0);
				cellSecond = row.getCell(1);
				cellThird = row.getCell(2);
				cellFourth = row.getCell(3);
				cellFiveth = row.getCell(4);
				cellSixth = row.getCell(5);
				cellSeventh = row.getCell(6);
				cellEghth = row.getCell(7);
				cellNighth = row.getCell(8);
				cellTen = row.getCell(9);
				cellEleven = row.getCell(10);
				cellTwelve = row.getCell(11);
				if (i == 0)
				{
					if (cellFirst == null || cellSecond == null || cellThird == null
							|| cellFourth == null || cellFiveth == null
							|| cellSixth == null || cellSeventh == null
							|| cellEghth == null || cellNighth == null || cellTen == null
							|| cellEleven == null || cellTwelve == null)
					{
						target.delete();
						target = null;
						logger.warn("Excel格式错误,字段不对", new Object[] { i + 1 });
						return "Excel格式错误,字段不对,请重试!";
					}
					// 判断excel里sheet抬头是否正确
					if (!"loid".equals(StringUtil.getStringValue(cellFirst
							.getRichStringCellValue()))
							|| !"设备类型（默认填1）".equals(StringUtil.getStringValue(cellSecond
									.getRichStringCellValue()))
							|| !"业务电话号码".equals(StringUtil.getStringValue(cellThird
									.getRichStringCellValue()))
							|| !"属地（属地代码，例如：哈尔滨填：451）".equals(StringUtil
									.getStringValue(cellFourth.getRichStringCellValue()))
							|| !"终端标识(loid.voip)".equals(StringUtil
									.getStringValue(cellFiveth.getRichStringCellValue()))
							|| !"终端标识的类型（默认填1）".equals(StringUtil
									.getStringValue(cellSixth.getRichStringCellValue()))
							|| !"主用MGCIP地址".equals(StringUtil.getStringValue(cellSeventh
									.getRichStringCellValue()))
							|| !"主用MGC器端口".equals(StringUtil.getStringValue(cellEghth
									.getRichStringCellValue()))
							|| !"备用MGCIP地址（如果没有的话可以不填）".equals(StringUtil
									.getStringValue(cellNighth.getRichStringCellValue()))
							|| !"备用MGC端口".equals(StringUtil.getStringValue(cellTen
									.getRichStringCellValue()))
							|| !"终端物理标示（语音1口为A0，2口为A1）".equals(StringUtil
									.getStringValue(cellEleven.getRichStringCellValue()))
							|| !"协议类型（默认填1）".equals(StringUtil.getStringValue(cellTwelve
									.getRichStringCellValue())))
					{
						target.delete();
						target = null;
						logger.warn("Excel格式错误,字段不对");
						return "Excel格式错误,字段不对,请重试!";
					}
					continue;
				}
				// 逻辑ID
				loid = "";
				// 设备类型
				devType = "";
				// 业务电话号码
				voipTelepone = "";
				// 属地
				cityId = "";
				// 终端标识
				regId = "";
				// 终端标识的类型
				regIdType = "";
				// 主用MGCIP地址
				MGCaddr = "";
				// 主用MGC器端口
				MGCPort = "";
				// 备用MGCIP地址
				standMGCaddr = "";
				// 备用MGC端口
				standMGCPort = "";
				// 终端物理标示
				linePort = "";
				// 协议类型
				agreementType = "";
				if (null != cellFirst)
				{
					cellFirst.setCellType(HSSFCell.CELL_TYPE_STRING);
					loid = StringUtil.getStringValue(cellFirst.getRichStringCellValue())
							.trim();
				}
				if (null != cellSecond)
				{
					cellSecond.setCellType(HSSFCell.CELL_TYPE_STRING);
					devType = StringUtil.getStringValue(
							cellSecond.getRichStringCellValue()).trim();
				}
				if (null != cellThird)
				{
					cellThird.setCellType(HSSFCell.CELL_TYPE_STRING);
					voipTelepone = StringUtil.getStringValue(
							cellThird.getRichStringCellValue()).trim();
				}
				if (null != cellFourth)
				{
					cellFourth.setCellType(HSSFCell.CELL_TYPE_STRING);
					cityId = StringUtil.getStringValue(
							cellFourth.getRichStringCellValue()).trim();
				}
				if (null != cellFiveth)
				{
					cellFiveth.setCellType(HSSFCell.CELL_TYPE_STRING);
					regId = StringUtil
							.getStringValue(cellFiveth.getRichStringCellValue()).trim();
				}
				if (null != cellSixth)
				{
					cellSixth.setCellType(HSSFCell.CELL_TYPE_STRING);
					regIdType = StringUtil.getStringValue(
							cellSixth.getRichStringCellValue()).trim();
				}
				if (null != cellSeventh)
				{
					cellSeventh.setCellType(HSSFCell.CELL_TYPE_STRING);
					MGCaddr = StringUtil.getStringValue(
							cellSeventh.getRichStringCellValue()).trim();
				}
				if (null != cellEghth)
				{
					cellEghth.setCellType(HSSFCell.CELL_TYPE_STRING);
					MGCPort = StringUtil.getStringValue(
							cellEghth.getRichStringCellValue()).trim();
				}
				if (null != cellNighth)
				{
					cellNighth.setCellType(HSSFCell.CELL_TYPE_STRING);
					standMGCaddr = StringUtil.getStringValue(
							cellNighth.getRichStringCellValue()).trim();
				}
				if (null != cellTen)
				{
					cellTen.setCellType(HSSFCell.CELL_TYPE_STRING);
					standMGCPort = StringUtil.getStringValue(
							cellTen.getRichStringCellValue()).trim();
				}
				if (null != cellEleven)
				{
					cellEleven.setCellType(HSSFCell.CELL_TYPE_STRING);
					linePort = StringUtil.getStringValue(
							cellEleven.getRichStringCellValue()).trim();
				}
				if (null != cellTwelve)
				{
					cellTwelve.setCellType(HSSFCell.CELL_TYPE_STRING);
					agreementType = StringUtil.getStringValue(
							cellTwelve.getRichStringCellValue()).trim();
				}
				DateTimeUtil dt = new DateTimeUtil();
				StringBuffer bssSheet = new StringBuffer("");
				bssSheet.append("15").append("|||");
				bssSheet.append("1").append("|||");
				bssSheet.append(dt.getYYYYMMDDHHMMSS()).append("|||");
				bssSheet.append(devType).append("|||");
				bssSheet.append(loid).append("|||");
				bssSheet.append(voipTelepone).append("|||");
				bssSheet.append(cityId).append("|||");
				bssSheet.append(regId).append("|||");
				bssSheet.append(regIdType).append("|||");
				bssSheet.append(MGCaddr).append("|||");
				bssSheet.append(MGCPort).append("|||");
				bssSheet.append(standMGCaddr).append("|||");
				bssSheet.append(standMGCPort).append("|||");
				bssSheet.append(linePort).append("|||");
				bssSheet.append(agreementType);
				bssSheet.append("LINKAGE");
				logger.warn("[{}] send E8-C H248 VOIP工单:  {}", loid, bssSheet.toString());
				String retResult = sendSheet(bssSheet.toString(), "1");
				logger.warn("[{}] E8-C H248 VOIP回单结果:  {}", loid, retResult);
			}
			target.delete();
			target = null;
			logger.warn("解析调用工单完成");
			return "解析调用后台成功";
		}
		else
		{
			logger.error("文件不存在");
			return "文件不存在,请重试!";
		}
	}

	/**
	 * 向工单接口发送模拟的BSS工单。正常返回工单接口的回单结果，如果过程中出现问题返回null
	 * 
	 * @return String 回单信息
	 */
	public String sendSheet(String bssSheet, String gw_type)
	{
		logger.debug("sendSheet({})", bssSheet);
		if (StringUtil.IsEmpty(bssSheet))
		{
			logger.warn("sendSheet is null");
			return null;
		}
		if ("1".equals(gw_type))
		{
			return SocketUtil.sendStrMesg(Global.G_ITMS_Sheet_Server,
					Global.G_ITMS_Sheet_Port, bssSheet + "\n");
		}
		else
		{
			return SocketUtil.sendStrMesg(Global.G_BBMS_Sheet_Server,
					Global.G_BBMS_Sheet_Port, bssSheet + "\n");
		}
	}
}
