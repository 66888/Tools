package flyingpig.barcode;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import javax.swing.JLabel;

/**
 * 打印箱标贴工具类
 * 
 * 包含获取字体宽度，条形码图片的拉伸、填充，条形码字符的拉伸、填充，物流箱标贴的生成和打印.
 * 
 * @author flyingpig
 *
 */
public class BoxTagImgPrinter { 
	
	public static void main(String[] args) {
		String path = "E:\\images\\test\\";
		Object obj = null;
		System.out.println(printBoxTagImg(path,obj,1));
	}
	
	/**
	 * 打印箱标贴图片
	 * @param path	箱标贴图片路径，和条形码图片路径一致.
	 * @param boxTagObj	箱标贴对象，包含条形码图片路径、名称，商品，仓库等信息.
	 * @param count	打印数量
	 */
	public static boolean printBoxTagImg(String path,Object boxTagObj,int count){
		boolean success = false;
		System.out.println("开始打印箱标贴...");
		//1.生成箱标贴图片
		String boxTagImgName = generatorBoxTagImg(boxTagObj);
		//2.打印箱标贴图片
		success = drawImage(path+boxTagImgName,count);
		//3.删除箱标贴图片
		if(success){
			deleteBoxTagImg(path,boxTagImgName);
		}
		return success;
	}
	
	/**
	 * 删除箱标贴图片
	 * @param boxTagImgPath	箱标贴图片名称
	 * @param boxTagImgName	箱标贴图片路径
	 * @return
	 */
	private static boolean deleteBoxTagImg(String boxTagImgPath,String boxTagImgName){
		boolean success = false;
		System.out.println("3.删除[箱标贴图片], 名称: "+boxTagImgName);
		File tempImg = new File(boxTagImgPath+boxTagImgName);
		
		if(tempImg.isFile()){
			success = tempImg.delete();
		}
		
		System.out.println("删除[箱标贴图片],是否成功: " + success);
		return success;
	}
    private  static boolean drawImage(String fileName, int count){ 
    	System.out.println("2.开始打印箱标贴图片...");
    	System.out.println("箱标贴名称: " + fileName + " ,打印数量: " + count);
    	boolean success = false;
    	
    	FileInputStream fin = null;
        try { 
            DocFlavor dof = null; 
             
            if(fileName.endsWith(".gif")){ 
                dof = DocFlavor.INPUT_STREAM.GIF; 
            }else if(fileName.endsWith(".jpg")){ 
                dof = DocFlavor.INPUT_STREAM.JPEG; 
            }else if(fileName.endsWith(".png")){ 
                dof = DocFlavor.INPUT_STREAM.PNG; 
            } 
             
            PrintService ps = PrintServiceLookup.lookupDefaultPrintService(); 
             
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();  
            pras.add(MediaSizeName.A);
            pras.add(OrientationRequested.PORTRAIT); 
            
            pras.add(new Copies(count)); 
            pras.add(PrintQuality.HIGH); 
            DocAttributeSet das = new HashDocAttributeSet(); 
            MediaPrintableArea mpa = new MediaPrintableArea(100, 100, 100, 100, MediaPrintableArea.MM);
            
            das.add(mpa);  
            fin = new FileInputStream(fileName);     
         
            Doc doc = new SimpleDoc(fin ,dof, das); 
             
            DocPrintJob job = ps.createPrintJob(); 
             
            job.print(doc, pras);
            System.out.println("打印箱标贴图片完毕.");
            return success = true;
        } catch (Exception e) { 
            System.out.println("打印箱标贴图片出现异常.");
            e.printStackTrace(); 
            return success;
        } finally {
			if(fin != null){
				 try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
    }  
    /**
	 * 生成箱标贴图片
	 * @param obj  箱标贴对象
	 * @return 箱标贴图片名称
	 */
	private static String generatorBoxTagImg(Object obj) {
		System.out.println("1.开始生成箱标贴图片...");
		final int x = 390;// 箱标贴宽度
		final int y = 390;// 箱标贴高度

		String boxTagImgName = "";
		
		
		// 传入箱标贴对象，获取箱标贴图片所需相关参数
		
		//此处参数需要定义Object之后确定
		
		String barcodeImgPath = "E:\\images\\test\\"; // 条形码图片路径
		String barcodeImgName = "FHL00091604150025.png"; // 条形码图片名称
		String orderNumber = "1000314212";// 订单号
		String shopName = "华润苏果上海徐家汇店"; // 商铺名称
		String area = "上海"; // 区域
		String huozhu = "华润苏果"; // 货主
		String project = "默认"; // 项目
		String fachucang = "徐家汇仓"; // 发出仓
		String fahuoriqi = "2016-4-15"; // 发货日期
		String page = "3-1"; // 页码
		
		System.out.println("箱标贴图片参数 ： barcodeImgPath:"+barcodeImgPath+" ,barcodeImgName:"+barcodeImgName+" ,orderNumber:"+orderNumber+" ,shopName:"+shopName+" ,area:"
				+area+" ,huozhu:"+huozhu+" ,project:"+project+" ,fachucang:"+fachucang+" ,fahuoriqi:"+fahuoriqi+" ,page:"+page);
		
		BufferedImage buffImg = new BufferedImage(x, y, BufferedImage.TYPE_INT_BGR);//设置背景图
		
		// 得到条形码图片
		Image srcImg  = null;
		FileOutputStream fos  = null;
		try {
			srcImg = ImageIO.read(new File(barcodeImgPath + barcodeImgName));

			Graphics2D graphics = buffImg.createGraphics();

			graphics.setColor(Color.WHITE);// 设置笔刷白色
			graphics.fillRect(0, 0, x, y);// 填充整个屏幕

			graphics.setColor(Color.BLACK); // 设置笔刷为黑色
			graphics.drawImage(srcImg, 112, 20, null);// 填充条形码

			graphics.setFont(new Font("宋体", Font.BOLD, 15));
			graphics.drawString("订单号: " + orderNumber, 16, 110);// 填充订单号

			// 商铺名称超过6个字符，换行,超过28个字符再换行，每行最多22个字符
			if (shopName != null && shopName.length() > 6) {
				if (shopName.length() <= 28) {
					String line1 = shopName.substring(0, 6);
					String line2 = shopName.substring(6);
					graphics.drawString("商铺名称: " + line1, 198, 110);// 填充第一行商铺名称
					// 换行从头打印第二行
					graphics.drawString(line2, 20, 128);// 填充第二行商铺名称
				} else {
					String line1 = shopName.substring(0, 6);
					String line2 = shopName.substring(6, 28);
					String line3 = shopName.substring(28);
					graphics.drawString("商铺名称: " + line1, 198, 110);// 填充第一行商铺名称
					// 换行从头打印第二行
					graphics.drawString(line2, 20, 128);// 填充第二行商铺名称
					// 换行从头打印第三行
					graphics.drawString(line3, 20, 146);// 填充第三行商铺名称
				}
			} else {
				graphics.drawString("商铺名称: " + shopName, 200, 110);// 填充商铺名称
			}

			graphics.setFont(new Font("宋体", Font.BOLD, 60));
			graphics.drawString("区域 " + area, 55, 235);// 填充区域

			graphics.setFont(new Font("宋体", Font.BOLD, 15));
			graphics.drawString("货主: " + huozhu, 16, 320);// 填充货主
			graphics.drawString("项目: " + project, 210, 320);// 填充项目
			graphics.drawString("发出仓: " + fachucang, 16, 340);// 填充发出仓
			graphics.drawString("发货日期: " + fahuoriqi, 210, 340);// 发货日期
			graphics.drawString(page, 180, 370);// 填充页码

			//为箱标贴添加边框
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setColor(Color.BLACK);
			BasicStroke wideStroke = new BasicStroke(1.5f);
			graphics.setStroke(wideStroke);
			graphics.drawRect(5, 5, 380, 380);// 边框X坐标，边框Y坐标，边框宽度，边框高度

			graphics.dispose();
			
			boxTagImgName = "BoxTag__"+barcodeImgName;//BoxTag__条形码.png: BoxTag__FHL00091604150025.png  
			fos = new FileOutputStream(barcodeImgPath + boxTagImgName);//E:\\images\\BoxTag__FHL00091604150025.png  
			ImageIO.write(buffImg, "png", fos);
			
			System.out.println("生成箱标贴图片完毕, 名称: " + boxTagImgName);
			
			return boxTagImgName;
		} catch (IOException e) {
			System.out.println("生成箱标贴图片失败");
			e.printStackTrace();
			return boxTagImgName;
		}finally {
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 生成附带条形码字符串的完整条形码图片  -->作废, ZxingEncoderHandler.generateBarcodeImg(...)实现了该功能
	 * 
	 * 将条形码字符串写入条形码图片
	 * 
	 * @param barcodeStr
	 *            条形码字符串
	 * @throws Exception
	 */
	private static void generateFullBarcodeImg(String barcodeStr) throws Exception {
		final int x = 166;// 拉伸后的条形码宽度
		final int y = 62;
		final double charWidth = 6;// 平均字符宽度,A-Z:6;0-9:7
		// barcodeStr = pullBarcodeStr(barcodeStr);//拉伸条形码字符串
		System.out.println(barcodeStr.length());
		int barcodeLength = barcodeStr.length();

		int barcodeStrLength = (int) (charWidth * barcodeLength);

		BufferedImage buffImg = new BufferedImage(x, y, BufferedImage.TYPE_INT_BGR);
		// 得到图片
		Image srcImg = ImageIO.read(new File("E:\\images\\Code128-test-pull.png"));
		Graphics2D graphics = buffImg.createGraphics();

		graphics.setColor(Color.WHITE);// 设置笔刷白色
		graphics.fillRect(0, 0, x, y);// 填充整个屏幕

		graphics.setColor(Color.BLACK); // 设置笔刷为黑色
		// 166*50
		graphics.drawImage(srcImg, 0, 0, null);// 填充条形码
		graphics.setFont(new Font("宋体", Font.PLAIN, 12));
		graphics.drawString(barcodeStr, (int) x / 2 - (int) barcodeStrLength / 2, 60);// 填充条形码下面的字符串

		graphics.dispose();
		FileOutputStream os = new FileOutputStream("E:\\images\\Code128-test-pull-with-barcode.png");
		ImageIO.write(buffImg, "png", os);
	}

	/**
	 * 拉伸条形码图片,高度不变
	 * 
	 * @param imgPath
	 *            需拉伸的条形码图片路径
	 * @param imgName
	 *            需拉伸的条形码图片名称
	 */
	private static String pullPicture(String imgPath, String imgName) {
		int width = 166;
		int height = 50;

		String newImgName = "";// 拉伸后的条形码图片名称

		File file = new File(imgPath + imgName);// E:\\images\\FHL00091604150025.png
		FileOutputStream fos = null;
		try {
			BufferedImage originalImage = ImageIO.read(file);

			BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
			Graphics graphics = newImage.getGraphics();
			graphics.drawImage(originalImage, 0, 0, width, height, null);

			graphics.dispose();
			originalImage = newImage;

			newImgName = "pulled-" + imgName;

			fos = new FileOutputStream(imgPath + newImgName);// E:\\images\\pulled-FHL00091604150025.png
			ImageIO.write(originalImage, "png", fos);

			System.out.println("拉伸条形码图片完毕, 名称: " + newImgName);
			return newImgName;
		} catch (IOException e) {
			System.out.println("拉伸条形码图片异常");
			e.printStackTrace();
			return newImgName;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 拉伸条形码字符串
	private static String pullBarcodeStr(String barcodeStr) {
		String separator = " ";// 中间间隔字符,字符宽度6或7
		StringBuilder sb = new StringBuilder();

		sb.append(separator);
		if (barcodeStr != null && barcodeStr.length() > 0) {
			for (int i = 0; i < barcodeStr.length(); i++) {
				sb.append(separator);
				sb.append(barcodeStr.charAt(i));
			}
		}

		sb.append(separator);
		sb.append(separator);
		barcodeStr = sb.toString();
		// System.out.println("拉伸后的字符串: " + barcodeStr);

		return barcodeStr;
	}

	// 获取字体宽度，用于拉伸条形码字符串长度
	private static void getStringWidth(String str) {
		JLabel label = new JLabel();
		FontMetrics metrics;
		int textH = 0;
		int textW = 0;
		label.setText(str);
		metrics = label.getFontMetrics(label.getFont());
		textH = metrics.getHeight();// 字符串的高,只和字体有关
		textW = metrics.stringWidth(label.getText());// 字符串的宽
		int[] num = new int[2];
		num[0] = textH;
		num[1] = textW;
		System.out.println(textH);
		System.out.println(textW);
	}
}