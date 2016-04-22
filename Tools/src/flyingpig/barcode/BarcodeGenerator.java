package flyingpig.barcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/**
 * 条形码生成工具类
 * 	
 * 使用google的zxing包生成指定标准条形码
 * 
 * 包含无字符和有字符的条形码图片生成，条形码图片打印
 * 
 * @author flyingpig
 *
 */
public class BarcodeGenerator {
	
	/**
	 * 打印条形码图片
	 * @param fileName   条形码图片文件完整路径：path+imgName，形如: E:\\images\\test\\FHL00091604150025.png
	 * @param count		  打印数量
	 * @return	是否打印成功
	 */
	public static boolean printImage(String fileName, int count) {
		System.out.println("开始打印条形码...");
		
		boolean success = false;
		
		FileInputStream fin = null;
		try {
			DocFlavor dof = null;

			if (fileName.endsWith(".gif")) {
				dof = DocFlavor.INPUT_STREAM.GIF;
			} else if (fileName.endsWith(".jpg")) {
				dof = DocFlavor.INPUT_STREAM.JPEG;
			} else if (fileName.endsWith(".png")) {
				dof = DocFlavor.INPUT_STREAM.PNG;
			}

			PrintService ps = PrintServiceLookup.lookupDefaultPrintService();

			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			pras.add(OrientationRequested.PORTRAIT);

			pras.add(new Copies(count));
			pras.add(PrintQuality.HIGH);
			DocAttributeSet das = new HashDocAttributeSet();
			MediaPrintableArea mpa = new MediaPrintableArea(8, 8, 8, 8, MediaPrintableArea.INCH);

			das.add(mpa);
			fin = new FileInputStream(fileName);

			Doc doc = new SimpleDoc(fin, dof, das);

			DocPrintJob job = ps.createPrintJob();

			job.print(doc, pras);
			System.out.println("打印条形码完毕");
			return success=true;
		} catch (Exception e) {
			System.out.println("打印条形码失败");
			e.printStackTrace();
			return success;
		}finally {
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
	 * 生成CODE128类型无条形码字符串的临时条形码图片
	 * @param barcode  条形码字符串
	 * @param imgPath	图片保存路径
	 * @return	生成的无条形码字符串图片名称
	 */
	private static String generateTempBarcode(String imgPath,String barcode){
		String barcodeName = barcode + "__temp.png";
		System.out.println("1.生成[临时条形码图片], 名称： "  + barcodeName);
		
		int width = 100;//条形码图片宽度
		int height = 50;//条形码突破高度
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = new MultiFormatWriter().encode(barcode, BarcodeFormat.CODE_128, width, height);
			MatrixToImageWriter.writeToFile(bitMatrix, "png", new File(imgPath+barcodeName));
			System.out.println("生成[临时条形码图片]完毕.");
			return barcodeName;
		} catch (Exception e) {
			System.out.println("生成[临时条形码图片]发生异常.");
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 生成带条形码字符串的条形码图片
	 * @param barcode  条形码字符串
	 * @param tempImgName  临时图片名称
	 * @param imgPath	图片保存路径
	 * @return	生成的带条形码字符串图片名称 
	 */
	private static boolean generateBarcode(String imgPath,String tempImgName,String barcode){
		boolean success = false;
		System.out.println("2.生成[条形码图片]...");
		String imgName = barcode+".png";//生成的带条形码字符串图片名称
		final int x = 166;//条形码背景图片宽度
		final int y = 62;//条形码背景图片高度
		final double charWidth = 6;//平均字符宽度。计算字符宽度是为了使条形码字符串均匀分布在条形码图片下方
		if(barcode == null){
			return success;
		}
		int barcodeLength = barcode.length();
		int barcodeStrLength = (int)(charWidth*barcodeLength);
		
		if(tempImgName == null){
			return success;
		}
		
		BufferedImage buffImg = new BufferedImage(x, y, BufferedImage.TYPE_INT_BGR);//定义新图片
	
		Image srcImg = null;
		FileOutputStream fos = null;
		try {
			srcImg = ImageIO.read(new File(imgPath+tempImgName));//读取不带条形码字符串的临时图片
			
			Graphics2D graphics = buffImg.createGraphics();

			graphics.setColor(Color.WHITE);// 设置笔刷白色
			graphics.fillRect(0, 0, x, y);// 填充整个屏幕

			graphics.setColor(Color.BLACK); // 设置笔刷为黑色
			graphics.drawImage(srcImg, 0, 0, null);// 填充临时条形码图片
			graphics.setFont(new Font("宋体", Font.PLAIN, 12));
			graphics.drawString(barcode, (int)x/2-(int)barcodeStrLength/2, 60);// 填充条形码下面的字符串
			
			graphics.dispose();
			
			fos = new FileOutputStream(imgPath+imgName);//生成完整的条形码图片
			ImageIO.write(buffImg, "png", fos);
			
			System.out.println("生成[条形码图片]完毕, 名称："+imgName);
			return success = true;
		} catch (IOException e) {
			System.out.println("生成[条形码图片]发生异常.");
			e.printStackTrace();
			return success;
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
	 * 删除不带条形码字符串的临时图片
	 * @param imgName	临时图片名称
	 * @param imgPath	临时图片路径
	 * @return	是否删除成功
	 */
	private static boolean deleteTempImg(String tempImgPath,String tempImgName){
		boolean success = false;
		System.out.println("3.删除[临时条形码图片], 名称: "+tempImgName);
		File tempImg = new File(tempImgPath+tempImgName);
		
		if(tempImg.isFile()){
			success = tempImg.delete();
		}
		
		return success;
	}
	
	/**
	 * 生成条形码图片
	 * 
	 * @param imgPath  条形码图片保存
	 * @param barcode  条形码字符串
	 * @return 条形码图片名称
	 */
	public static String generateBarcodeImg(String imgPath,String barcode){
		String barcodeImgName = "";
		//生成临时图片
		String tempImgName = generateTempBarcode(imgPath,barcode);
		//生成完整条形码图片
		boolean success = generateBarcode(imgPath,tempImgName,barcode);
		//完整条形码图片生成成功，删除临时图片
		if(success){
			barcodeImgName = barcode + ".png";
			boolean deleteResult = deleteTempImg(imgPath,tempImgName);//删除不带条形码字符串的临时图片
			System.out.println("删除[临时条形码图片], 结果: " + deleteResult);
		}
		return barcodeImgName;
	}
	
	public static void main(String[] args) {
	
		String barcode = "FHL00091604150025";
		String imgPath = "E:\\images\\test\\";
		String barcodeImgName = generateBarcodeImg(imgPath,barcode);
	}
}
