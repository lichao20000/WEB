/**
 * TopoGraphics.java
 * 
 * @Date 创建时间:2007-2-5
 * 
 * @Author shenkejian
 * 
 * Copyright 2007 联创科技.山东项目 版权所有
 * 
 */
package com.linkage.litms.webtopo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.webtopo.warn.MyFile;

/**
 * @author shenkejian
 * 
 * @Version 1.00
 * 
 * @Since Liposs 2.1
 */
public class TopoGraphics {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(TopoGraphics.class);
    private Cursor cursor = null;

    private Map fields = null;

    private BufferedImage image = null;

    private List objList = null;

    private List linkList = null;

    private Map map = null;

    private int maxCoordX = 0;

    private int maxCoordY = 0;

    private Map imgMap = null;

    private String maxCoordX_Img = null;

    private String maxCoordY_Img = null;

    private int maxStrLn = 0;

    // 返回信息
    private String strMsg = null;

    // 文件名前缀
    private String strFile = null;

    // 采集区域
    private String strGather_id = null;

    // icon路径
    private String iconPath = "";

    // webtopoxml文件路径
    private String webtopoxmlPath = "";

    // 生成images图片路径
    private String imagesPath = "";

    // 生成的images图片名称
    private String imagesfile = "";

    // Web程序部署路径
    private String WEB_FILE_PATH = null;

    private String pid = null;

    // 单一模式实例
    // private static TopoGraphics instance = new TopoGraphics();

    public TopoGraphics() {
	map = new HashMap();
	objList = new ArrayList();
	linkList = new ArrayList();
	imgMap = new HashMap();
	// 获取web路径
	WEB_FILE_PATH = LipossGlobals.getLipossHome();
	// 初始化拓扑icon图片
	getImageSize();
	// InitIcon();
	// 初始化拓扑图数据
	// initImageData(getxmlPath());
    }

    /**
     * 程序入口
     * 
     * @return 单一实例
     */
    // public static TopoGraphics getInstance() {
    // return instance;
    // }
    /*
     * public String getMsg(HttpServletRequest request) { String str = "";
     * TopoGraphics topographics = new TopoGraphics(); str =
     * topographics.createImage(); return str; }
     */
    /**
     * 获取登录用户对应域的拓扑xml文件路径
     * 
     * @return Path
     */
    public String getxmlPath() {
	webtopoxmlPath = WEB_FILE_PATH + File.separator + "webtopo"
		+ File.separator + "webtopodata" + File.separator + strFile;
	logger.debug(strFile);
	return webtopoxmlPath;
    }

    /**
     * 生成images图片路径及文件名称
     * 
     * @return Path
     */
    public String saveImagesPath() {
	// imagesfile = strGather_id + "_webtopo.jpg";
	imagesfile = pid + "_webtopo.jpg";
	imagesPath = WEB_FILE_PATH + File.separator + "webtopo"
		+ File.separator + "webtopodata" + File.separator + "images"
		+ File.separator + imagesfile;

	return imagesPath;
    }

    /**
     * icon图片路径
     * 
     * @return Path
     */
    public String getImagesPath() {
	iconPath = WEB_FILE_PATH + File.separator + "webtopo" + File.separator
		+ "images" + File.separator;

	return iconPath;
    }

    /**
     * 生成图片
     * 
     * 
     */
    public ArrayList createImage() {
	ArrayList list = new ArrayList();
	list.clear();
	ImageIcon icon = null;
	// 定义缺损图片
	ImageIcon Default_Img = new ImageIcon(getImagesPath() + "router.png");
	// 判断xml文件中icon图标是否在imgMap存在
	if ((imgMap.get(maxCoordX_Img)) != null) {
	    icon = (ImageIcon) imgMap.get(maxCoordX_Img);
	} else {
	    icon = Default_Img;
	}
	if (maxStrLn < icon.getIconWidth())
	    maxStrLn = icon.getIconWidth();
	int w = maxCoordX + maxStrLn / 2 + 50;
	// 判断xml文件中icon图标是否在imgMap存在
	if ((imgMap.get(maxCoordY_Img)) != null) {
	    icon = (ImageIcon) imgMap.get(maxCoordY_Img);
	} else {
	    icon = Default_Img;
	}
	int h = maxCoordY + icon.getIconHeight() + 60;
	image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

	Graphics g = image.getGraphics();
	g.setColor(new Color(0xDCDCDC));
	g.fillRect(0, 0, w, h);

	// 背景图片
	ImageIcon bgIcon = new ImageIcon(getImagesPath() + "top_background.gif");
	int dx1 = 0;
	int dy1 = 0;
	while (dy1 < h) {
	    if (dx1 > w && dy1 < h) {
		dy1 += bgIcon.getImage().getHeight(null);
		// reset
		dx1 = 0;
	    }
	    // logger.debug(dx1+" : "+dy1);
	    g.drawImage(bgIcon.getImage(), dx1, dy1, null);

	    dx1 += bgIcon.getImage().getWidth(null);
	    // dy1 += bgIcon.getImage().getHeight(null);
	}

	// Image img = Toolkit.getDefaultToolkit().getImage("lain_5.jpg");

	// 画网元对象
	IconObj obj = null;
	LinkObj lnkObj = null;
	int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
	g.setColor(Color.GREEN);

	for (int i = 0; i < linkList.size(); i++) {
	    lnkObj = ((LinkObj) linkList.get(i));
	    obj = (IconObj) lnkObj.getFrom();
	    // 判断是否为空，防止画网元的对象为隐藏对象
	    if (obj != null) {
		if (imgMap.get((String) obj.getIcon()) != null) {
		    icon = (ImageIcon) imgMap.get((String) obj.getIcon());
		} else {
		    icon = Default_Img;
		}
		x1 = obj.getX() + (int) icon.getIconWidth() / 2;
		y1 = obj.getY() + (int) icon.getIconHeight() / 2;
	    }
	    obj = (IconObj) lnkObj.getTo();
	    if (obj != null) {
		if (imgMap.get((String) obj.getIcon()) != null) {
		    icon = (ImageIcon) imgMap.get((String) obj.getIcon());
		} else {
		    icon = Default_Img;
		}
		x2 = obj.getX() + (int) icon.getIconWidth() / 2;
		y2 = obj.getY() + (int) icon.getIconHeight() / 2;
	    }
	    g.drawLine(x1, y1, x2, y2);

	}
	g.setColor(Color.BLACK);
	Font font = new Font("SansSerif", Font.PLAIN, 12);
	g.setFont(font);
	int _str_w, x, y;
	// logger.debug("devList = " + objList.size());
	for (int i = 0; i < objList.size(); i++) {
	    obj = (IconObj) objList.get(i);
	    if (imgMap.get((String) obj.getIcon()) != null) {
		icon = (ImageIcon) imgMap.get((String) obj.getIcon());
	    } else {
		icon = Default_Img;
	    }
	    g.drawImage(icon.getImage(), obj.getX(), obj.getY(), null);

	    _str_w = getStrLn(obj.getTitle());
	    x = obj.getX() + (int) (icon.getIconWidth() - _str_w) / 2;
	    y = obj.getY() + icon.getIconHeight() + 10;
	    g.drawString(obj.getTitle(), x, y);
	}
	try {
	    ImageIO.write(image, "png", new File(saveImagesPath()));
	    strMsg = "生成图片成功,可选择保存功能将图片下载到本目录，谢谢！";
	} catch (IOException ex) {
	    strMsg = "无法生成图片，请重新操作......";
	    ex.printStackTrace();
	}
	list.add(strMsg);
	list.add(strGather_id);
	list.add(saveImagesPath());
	list.add(imagesfile);
	// 清除内存数据
	map.clear();
	map = null;
	imgMap.clear();
	imgMap = null;
	icon = null;
	image = null;

	return list;
    }

    private int getStrLn(String s) {
	int ln = 0;
	for (int i = 0; i < s.length(); i++) {
	    if (s.charAt(i) > 256)
		ln += 2;
	    else
		ln++;
	}

	return ln * 6;
    }

    /**
     * Icon对应图片,目前没有用该方法,有错误
     * 
     */
    public void InitIcon() {
	String strSQL = "select distinct icon_name from tp_devicetype_icon_map";
	String icon_name = null;
	PrepareSQL psql = new PrepareSQL(strSQL);
	psql.getSQL();
	cursor = DataSetBean.getCursor(strSQL);
	fields = cursor.getNext();
	while (fields != null) {
	    icon_name = (String) fields.get("icon_name");
	    imgMap.put(icon_name, getImagesPath() + icon_name);
	    fields = cursor.getNext();
	}
	fields = null;
    }

    /**
     * Icon对应图片 可以从tp_devicetype_icon读取所有Icon，然后构造Image
     * @throws MalformedURLException 
     */
    private void getImageSize() {
	File file = new File(getImagesPath());
	File[] files = file.listFiles();
	for (int i = 0; i < files.length; i++) {
	    ImageIcon imgObject = new ImageIcon(files[i].getAbsolutePath());
	    imgMap.put(files[i].getName(),imgObject);
	}
	/*
	ImageIcon a1 = new ImageIcon(getImagesPath() + "sun.png");
	imgMap.put("sun.png", a1);
	ImageIcon a2 = new ImageIcon(getImagesPath() + "router.png");
	imgMap.put("router.png", a2);
	ImageIcon a3 = new ImageIcon(getImagesPath() + "router2.png");
	imgMap.put("router2.png", a3);
	ImageIcon a4 = new ImageIcon(getImagesPath() + "network_cloud.png");
	imgMap.put("network_cloud.png", a4);
	ImageIcon a5 = new ImageIcon(getImagesPath() + "network_cloud2.png");
	imgMap.put("network_cloud2.png", a5);
	ImageIcon a6 = new ImageIcon(getImagesPath() + "network_cloud4.png");
	imgMap.put("network_cloud4.png", a6);
	ImageIcon a7 = new ImageIcon(getImagesPath() + "FC_switch1.png");
	imgMap.put("FC_switch1.png", a7);
	ImageIcon a8 = new ImageIcon(getImagesPath() + "FC_switch2.png");
	imgMap.put("FC_switch2.png", a8);
	ImageIcon a9 = new ImageIcon(getImagesPath() + "firewall.png");
	imgMap.put("firewall.png", a9);
	ImageIcon a10 = new ImageIcon(getImagesPath() + "firewall2.png");
	imgMap.put("firewall2.png", a10);
	ImageIcon a11 = new ImageIcon(getImagesPath() + "modem.png");
	imgMap.put("modem.png", a11);
	ImageIcon a12 = new ImageIcon(getImagesPath() + "topban_1.gif");
	imgMap.put("topban_1.gif", a12);
	ImageIcon a13 = new ImageIcon(getImagesPath() + "bras.png");
	imgMap.put("bras.png", a13);
	ImageIcon a14 = new ImageIcon(getImagesPath() + "laptop.png");
	imgMap.put("laptop.png", a14);
	ImageIcon a15 = new ImageIcon(getImagesPath() + "server.png");
	imgMap.put("server.png", a15);
	ImageIcon a16 = new ImageIcon(getImagesPath() + "7609.gif");
	imgMap.put("7609.gif", a16);
	ImageIcon a17 = new ImageIcon(getImagesPath() + "Alpine3804.gif");
	imgMap.put("Alpine3804.gif", a17);
	ImageIcon a18 = new ImageIcon(getImagesPath() + "BD6808.gif");
	imgMap.put("BD6808.gif", a18);
	ImageIcon a19 = new ImageIcon(getImagesPath() + "GSR12816.gif");
	imgMap.put("GSR12816.gif", a19);
	ImageIcon a20 = new ImageIcon(getImagesPath() + "S8512.png");
	imgMap.put("S8512.png", a20);
	ImageIcon a21 = new ImageIcon(getImagesPath() + "SE800.gif");
	imgMap.put("SE800.gif", a21);
	ImageIcon a22 = new ImageIcon(getImagesPath() + "Shasta.gif");
	imgMap.put("Shasta.gif", a22);
	ImageIcon a23 = new ImageIcon(getImagesPath() + "juniper160.png");
	imgMap.put("juniper160.png", a23);
	ImageIcon a24 = new ImageIcon(getImagesPath() + "virtual.png");
	imgMap.put("virtual.png", a24);
	ImageIcon a25 = new ImageIcon(getImagesPath() + "NE5000E.png");
	imgMap.put("NE5000E.png", a25);
	ImageIcon a26 = new ImageIcon(getImagesPath() + "user4.png");
	imgMap.put("user4.png", a26);
	ImageIcon a27 = new ImageIcon(getImagesPath() + "usergroup1.png");
	imgMap.put("usergroup1.png", a27);
	ImageIcon a28 = new ImageIcon(getImagesPath() + "user1.png");
	imgMap.put("user1.png", a28);
	ImageIcon a29 = new ImageIcon(getImagesPath() + "customer1.png");
	imgMap.put("customer1.png", a29);
	ImageIcon a30 = new ImageIcon(getImagesPath() + "accesspoint1.png");
	imgMap.put("accesspoint1.png", a30);
	ImageIcon a31 = new ImageIcon(getImagesPath() + "egw.png");
	imgMap.put("egw.png", a31);
	ImageIcon a32 = new ImageIcon(getImagesPath() + "ap_icon.png");
	imgMap.put("ap_icon.png", a32);*/
    }

    /**
     * 获取拓扑图对应xml数据
     * 
     * 
     * @param url
     */
    public void initImageData(String url) {
	try {
	    // 判断是否为vpn拓扑图
	    // boolean vpn_bool = false;
	    //			
	    // if(url.startsWith("vpn_")){
	    // vpn_bool = true;
	    // }

	    SAXReader reader = new SAXReader();
	    Document document = reader.read(new File(url));
	    Node node = null;
	    // 获取网元对象信息
	    List devList = document.selectNodes("//Nodes/Device");
	    int x, y, _str_w;
	    String id, title, icon, state;
	    // logger.debug("devList = "+devList.size());
	    for (int i = 0; i < devList.size(); i++) {
		node = (Node) devList.get(i);
		id = node.valueOf("@id");
		title = node.valueOf("@title");
		icon = node.valueOf("@icon");
		state = node.valueOf("@state");

		if (state == null || state.equals(""))
		    state = "1";

		// 判断网元对象是否为隐藏对象，0为隐藏，1为显示
		if (state.equals("1")) {
		    IconObj obj = new IconObj();
		    obj.setTitle(title);
		    obj.setIcon(icon);
		    x = Integer.parseInt(node.valueOf("@x"), 10);
		    y = Integer.parseInt(node.valueOf("@y"), 10);
		    _str_w = getStrLn(title);
		    if (maxCoordX < x) {
			maxCoordX = x;
			maxCoordX_Img = icon;
		    }
		    if (maxCoordY < y) {
			maxCoordY = y;
			maxCoordY_Img = icon;
		    }
		    if (maxStrLn < _str_w)
			maxStrLn = _str_w;
		    obj.setX(x);
		    obj.setY(y);
		    objList.add(obj);
		    map.put(id, obj);
		}
	    }
	    // 获取链路信息
	    List lnkList = document.selectNodes("//Nodes/Link");
	    // Integer[] coord = new Integer[2];
	    for (int i = 0; i < lnkList.size(); i++) {
		node = (Node) lnkList.get(i);
		LinkObj obj = new LinkObj();
		obj.setFrom((IconObj) map.get(node.valueOf("@from")));
		obj.setTo((IconObj) map.get(node.valueOf("@to")));
		linkList.add(obj);
	    }
	    document.clearContent();
	    document = null;

	    // logger.debug("linkList = "+linkList.size());
	} catch (DocumentException ex) {
	    ex.printStackTrace();
	}
    }

    /**
     * 生成图标对象
     * 
     * @author shenkejian
     * 
     * @Version 1.00
     * 
     * @Since Liposs 2.1
     */

    class IconObj {
	private int x;

	private int y;

	private String icon;

	private String title;

	public int getX() {
	    return x;
	}

	public int getY() {
	    return y;
	}

	public String getIcon() {
	    return icon;
	}

	public String getTitle() {
	    return title;
	}

	public void setX(int x) {
	    this.x = x;
	}

	public void setY(int y) {
	    this.y = y;
	}

	public void setIcon(String icon) {
	    this.icon = icon;
	}

	public void setTitle(String title) {
	    this.title = title;
	}
    }

    /**
     * 生成链路对象
     * 
     * @author shenkejian
     * 
     * @Version 1.00
     * 
     * @Since Liposs 2.1
     */
    class LinkObj {
	private IconObj from;

	private IconObj to;

	public IconObj getFrom() {
	    return from;
	}

	public IconObj getTo() {
	    return to;
	}

	public void setFrom(IconObj from) {
	    this.from = from;
	}

	public void setTo(IconObj to) {
	    this.to = to;
	}
    }

    /**
     * 保存WebTopo数据为xml文件
     * 
     */
    public void GenerateWebTopoXML(String pid, String xml) {
	String path = null;
	String str_pid = null;
	String str_file = "_webtopo.xml";
	// int num1 = 0, num2 = 0;
	// num1 = pid.indexOf("/");
	// num2 = pid.lastIndexOf("/");
	// str_pid = pid.substring(num1 + 1, num2);
	str_pid = pid.replaceAll("/", "_");
	str_file = str_pid + str_file;
	strGather_id = str_pid;
	strFile = str_file;
	path = WEB_FILE_PATH + File.separator + "webtopo" + File.separator
		+ "webtopodata" + File.separator;
	String outFile = path + str_file;

	// 创建文件
	MyFile.clearFile(outFile);
	BufferedWriter xmlWriter = MyFile.openAppendWriter(outFile);
	MyFile.WriteXml(xmlWriter, xml);
	MyFile.closeWriter(xmlWriter);
    }

    /**
     * 保存WebTopo数据为xml文件
     * 
     * @param pid
     * @param xml
     */
    public void generateWebTopoXML(String parentid, String xml) {
	String path = null;

	parentid = parentid.replaceAll("/", "_");

	pid = parentid;

	strFile = pid + "_webtopo.xml";
	path = WEB_FILE_PATH + File.separator + "webtopo" + File.separator
		+ "webtopodata" + File.separator;
	String outFile = path + strFile;
	logger.debug(outFile);
	// 创建文件
	MyFile.clearFile(outFile);
	BufferedWriter xmlWriter = MyFile.openAppendWriter(outFile);
	MyFile.WriteXml(xmlWriter, xml);
	MyFile.closeWriter(xmlWriter);
    }

    /**
     * 主函数，用于测试
     * 
     * @param args
     */
    public static void main(String[] args) {
	// ArrayList list = new ArrayList();
	// String str = "";
	// TopoGraphics topographics = new TopoGraphics();
	// list = topographics.createImage();
	// String str_gather_id=String.valueOf(list.get(1));
	// logger.debug(str_gather_id);

    }
}
