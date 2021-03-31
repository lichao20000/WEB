
package com.linkage.module.itms.report.obj;


/**
 * @author guankai (Ailk No.300401)
 * @version 1.0
 * @since 2020年4月22日
 * @category com.linkage.module.itms.report.obj
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class FileExportObj
{

	/**
	 * 任务ID
	 */
	private String id;
	/**
	 * 操作人
	 */
	private String fileExportUser;
	/**
	 * 操作时间
	 */
	private String fileExportTime;
	/**
	 * 文件说明
	 */
	private String fileExportDesc;
	/**
	 * 状态 ：1已生成 ，0 未生成
	 */
	private String status;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 取数sql
	 */
	private String fileExportSql;
	
	/**
	 * 参数
	 */
	private String fileExportField;
	
	/**
	 * 参数名
	 */
	private String fileExportFieldName;
	/**
	 * 地市
	 */
	private String cityId;
	/**
	 * 文件生成时间
	 */
	private String fileFinishTime;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getFileExportUser()
	{
		return fileExportUser;
	}

	public void setFileExportUser(String fileExportUser)
	{
		this.fileExportUser = fileExportUser;
	}

	public String getFileExportTime()
	{
		return fileExportTime;
	}

	public void setFileExportTime(String fileExportTime)
	{
		this.fileExportTime = fileExportTime;
	}

	public String getFileExportDesc()
	{
		return fileExportDesc;
	}

	public void setFileExportDesc(String fileExportDesc)
	{
		this.fileExportDesc = fileExportDesc;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	
	public String getFileExportSql()
	{
		return fileExportSql;
	}

	
	public void setFileExportSql(String fileExportSql)
	{
		this.fileExportSql = fileExportSql;
	}
	
	

	
	public String getFileExportField()
	{
		return fileExportField;
	}

	
	public void setFileExportField(String fileExportField)
	{
		this.fileExportField = fileExportField;
	}

	public String getCityId()
	{
		return cityId;
	}

	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	public String getFileFinishTime()
	{
		return fileFinishTime;
	}

	public void setFileFinishTime(String fileFinishTime)
	{
		this.fileFinishTime = fileFinishTime;
	}

	
	public String getFileExportFieldName()
	{
		return fileExportFieldName;
	}

	
	public void setFileExportFieldName(String fileExportFieldName)
	{
		this.fileExportFieldName = fileExportFieldName;
	}

	@Override
	public String toString()
	{
		return "FileExportObj [id=" + id + ", fileExportUser=" + fileExportUser + ", fileExportTime=" + fileExportTime
				+ ", fileExportDesc=" + fileExportDesc + ", status=" + status + ", fileName=" + fileName + ", fileExportSql="
				+ fileExportSql + ", fileExportField=" + fileExportField + ", fileExportFieldName=" + fileExportFieldName
				+ ", cityId=" + cityId + ", fileFinishTime=" + fileFinishTime + "]";
	}

	

}
