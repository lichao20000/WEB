package com.linkage.module.gtms.stb.resource.serv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FTP������
 * 
 * @author chenjie
 * @version 1.0
 * @since 1.0
 * @date 2011-11-10
 */

public class MyftpClient {
	
	//	logger
	private static Logger log = LoggerFactory.getLogger(MyftpClient.class);
	
	private String server = null ;

	private String username = null;

	private String password = null;

	private FTPClient ftp = null;

	private boolean binaryTransfer = true;

	/**
	 * @param server
	 *            ftp��������ַ
	 * @param username
	 *            ftp��������½�û�
	 * @param password
	 *            ftp�û�����
	 */
	public MyftpClient(String server, String username, String password) {

		this.server = server;
		this.username = username;
		this.password = password;
		ftp = new FTPClient();
	}

	/**
	 * Ĭ�Ϲ���
	 */
	public MyftpClient() {
	}

	/**
	 * ����
	 * @return
	 */
	public boolean connect() {
		log.debug("FtpClient connect to server: ip[{}], username:[{}], password:[{}]", new Object[]{server, username, password});
		try {
			int reply;
			ftp.connect(server);

			// ���Ӻ��ⷵ������У�������Ƿ�ɹ�
			reply = ftp.getReplyCode();

			if (FTPReply.isPositiveCompletion(reply)) {
				if (ftp.login(username, password)) {
					// passive mode off
					ftp.enterLocalActiveMode();
					log.warn("connect to server, success!");
					return true;
				}
			} else {
				ftp.disconnect();
				log.error("FTP server refused connection.");
			}
		} catch (IOException e) {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
				}
			}
			log.error("Could not connect to server.", e);
		}
		return false;
	}

	/**
	 * �ϴ�һ�������ļ���Զ��ָ���ļ�
	 * 
	 * @param remoteAbsoluteFile
	 *            Զ���ļ���(��������·��)
	 * @param localAbsoluteFile
	 *            �����ļ���(��������·��)
	 * @return �ɹ�ʱ������true��ʧ�ܷ���false
	 */
	public boolean put(String remoteAbsoluteFile, String localAbsoluteFile,
			boolean delFile) {
		
		log.debug("FtpClient put [{}] to [{}] , delFile[{}]", new Object[]{localAbsoluteFile, remoteAbsoluteFile, delFile} );
		
		InputStream input = null;
		try {
			// //�����ļ���������
			if (binaryTransfer) {
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			} else {
				ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
			}
			// ���?��
			input = new FileInputStream(localAbsoluteFile);
			OutputStream os = ftp.storeFileStream(remoteAbsoluteFile);
			uploadFile(input, os);
			log.debug("put " + localAbsoluteFile);
			input.close();
			if (delFile) {
				(new File(localAbsoluteFile)).delete();
			}
			log.debug("delete " + localAbsoluteFile);
			return true;
		} catch (FileNotFoundException e) {
			log.error("local file not found.", e);
		} catch (IOException e1) {
			log.error("Could put file to server.", e1);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (Exception e2) {
			}
		}

		return false;
	}

	/**
	 * ����һ��Զ���ļ������ص�ָ���ļ�
	 * 
	 * @param remoteAbsoluteFile
	 *            Զ���ļ���(��������·��)
	 * @param localAbsoluteFile
	 *            �����ļ���(��������·��)
	 * @return �ɹ�ʱ������true��ʧ�ܷ���false
	 */
	public boolean get(String remoteAbsoluteFile, String localAbsoluteFile,
			boolean delFile) {
		log.debug("FtpClient get [{}] to [{}] , delFile[{}]", new Object[]{remoteAbsoluteFile, localAbsoluteFile, delFile} );
		
		OutputStream output = null;
		boolean result;
		try {
			// �����ļ���������
			if (binaryTransfer) {
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			} else {
				ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
			}
			// ���?��
			output = new FileOutputStream(localAbsoluteFile);
			result = ftp.retrieveFile(remoteAbsoluteFile, output);
			output.close();
			if (delFile) { // ɾ��Զ���ļ�
				ftp.deleteFile(remoteAbsoluteFile);
			}
			return result;
		} catch (FileNotFoundException e) {
			log.error("local file not found.", e);
		} catch (IOException e1) {
			log.error("Could get file from server.", e1);
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e2) {
			}
		}
		return false;
	}

	/**
	 * �г�Զ��Ŀ¼�����е��ļ�
	 * 
	 * @param remotePath
	 *            Զ��Ŀ¼��
	 * @return Զ��Ŀ¼�������ļ�����б?Ŀ¼�����ڻ���Ŀ¼��û���ļ�ʱ����0���ȵ�����
	 */
	public String[] listNames(String remotePath) {
		log.debug("FtpClient listNames, remotePath[{}]", remotePath);
		String[] fileNames = null;
		try {
			FTPFile[] remotefiles = ftp.listFiles(remotePath);
			fileNames = new String[remotefiles.length];
			for (int i = 0; i < remotefiles.length; i++) {
				fileNames[i] = remotefiles[i].getName();
			}

		} catch (IOException e) {
			log.error("Could not list file from server.", e);
		}
		return fileNames;
	}

	/**
	 * �Ͽ�ftp����
	 */
	public void disconnect() {
		try {
			ftp.logout();
			/* delete by chenjie 2012-7-5
			if (ftp.isConnected()) {
				ftp.disconnect();
			}
			*/
		} catch (IOException e) {
			log.error("Logout from server error.", e);
		}
		finally{
			// disconnectд��finally�У�logout()Ҳ�п����׳��쳣  modify by chenjie 2012-7-5
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException e) {
					log.error("Could not disconnect from server.", e);
				}
			}
		}
	}
	
	/**
	 * �ϴ��ļ�
	 * @param srcFileStream
	 * @param desFileStream
	 * @return
	 * @throws IOException
	 */
    private boolean uploadFile(InputStream srcFileStream,
			OutputStream desFileStream) throws IOException
	{
		try
		{
			int poi = 0;
			byte[] buffered = new byte[1024];
			while ((poi = srcFileStream.read(buffered)) != -1)
			{
				desFileStream.write(buffered, 0, poi);
			}
		}
		catch (Exception e)
		{
			log.error("�ļ��ϴ������쳣,�쳣��Ϣ����", e);
			return false;
		}
		finally
		{
			if (srcFileStream != null)
			{
				srcFileStream.close();
			}
			if (desFileStream != null)
			{
				desFileStream.flush();
				desFileStream.close();
			}
		}
		return true;
	}
    
    /**
     * ɾ���ļ�
     * @param file
     * @return 
     */
    public boolean deleteFile(String file)
    {
    	log.debug("deleteFile({})", file);
    	boolean result = true;
    	try {
    		result = ftp.deleteFile(file);
		} catch (IOException e) {
			log.error("delete remote file[{}] error!", file);
			result = false;
		}
		return result;
    }

	/**
	 * @return Returns the binaryTransfer.
	 */
	public boolean isBinaryTransfer() {
		return binaryTransfer;
	}

	/**
	 * @param binaryTransfer
	 *            The binaryTransfer to set.
	 */
	public void setBinaryTransfer(boolean binaryTransfer) {
		this.binaryTransfer = binaryTransfer;
	}

	public static void main(String[] args) {
		
		MyftpClient ftp = new MyftpClient("202.102.39.141", "linkage", "JSNet@)!!Test");
		// connect
		ftp.connect();
		
		// test list file
		/*
		String[] temp = ftp.listNames("/export/home/linkage/chenjie6");
		System.out.println("connect sucess");
		System.out.println(temp.length);
		for(String s : temp)
		{
			System.err.println(s);
		}
		*/
		boolean result;
		
		// test get
		//result = ftp.get("/export/home/linkage/chenjie6/test.txt", "D:\\test.txt", false);
		
		// test put
		// result = ftp.put("/export/home/linkage/chenjie6/test.txt", "D:\\test.txt" , true);
		
		// delete
		result = ftp.deleteFile("/export/home/linkage/chenjie6/ddd.xml");
		
		// �Ͽ�����
		ftp.disconnect();
		
		System.err.println(result);
	}
}