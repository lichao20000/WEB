package action.resource;

import java.io.File;

import com.opensymphony.xwork2.ActionSupport;

import dao.resource.ImportDeviceDAO;

public class ImportDeviceAction extends ActionSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4462434269480153671L;
	
	private ImportDeviceDAO importDev;
	
	//导入文件
	private File file;

	public void setFile(File file)
	{
		this.file = file;
	}

	public void setImportDev(ImportDeviceDAO importDev)
	{
		this.importDev = importDev;
	}

	public String execute() throws Exception
	{
		
		importDev.importFile(file);
		
		return SUCCESS;
	}
	
	
	
}
