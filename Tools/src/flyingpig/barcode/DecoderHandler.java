package flyingpig.barcode;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.Code128Reader;

/**
 * 根据指定标准，将条形码图片解码成字符串
 * 
 * @author flyingpig
 *
 */
public class DecoderHandler {

	public static void main(String[] args) {
		String imgPath = "src\\Code128-6923450657713.png";
		DecoderHandler handler = new DecoderHandler();
		String decodeContent = handler.decode(imgPath);
		System.out.println("解码内容如下：");
		System.out.println(decodeContent);
		System.out.println("完成解码。");
	}

	public String decode(String imgPath) {
		BufferedImage image = null;
		Result result = null;
		try {
			image = ImageIO.read(new File(imgPath));
			if (image == null) {
				System.out.println("图片不存在。");
			}
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Code128Reader reader = new Code128Reader();//使用code128标准解码
			result = reader.decode(bitmap);
			System.out.println("result: "+result);
			String text = result.getText();
			System.out.println("text: "+text);
			
			return text;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
