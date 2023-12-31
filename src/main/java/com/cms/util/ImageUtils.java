/*
 * 
 * 
 * 
 */
package com.cms.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FilenameUtils;

/**
 * Utils - 图片处理(支持JDK)
 * 
 * 
 * 
 */
public final class ImageUtils {

	/** 背景颜色 */
	private static final Color BACKGROUND_COLOR = Color.white;

	/** 目标图片品质(取值范围: 0 - 100) */
	private static final int DEST_QUALITY = 88;

	/**
	 * 不可实例化
	 */
	private ImageUtils() {
	}

	/**
	 * 等比例图片缩放
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param destWidth
	 *            目标宽度
	 * @param destHeight
	 *            目标高度
	 */
	public static void zoom(File srcFile, File destFile, int destWidth, int destHeight) {

		Graphics2D graphics2D = null;
		ImageOutputStream imageOutputStream = null;
		ImageWriter imageWriter = null;
		try {
			BufferedImage srcBufferedImage = ImageIO.read(srcFile);
			int srcWidth = srcBufferedImage.getWidth();
			int srcHeight = srcBufferedImage.getHeight();
			int width = destWidth;
			int height = destHeight;
			if (srcHeight >= srcWidth) {
				width = (int) Math.round(((destHeight * 1.0 / srcHeight) * srcWidth));
			} else {
				height = (int) Math.round(((destWidth * 1.0 / srcWidth) * srcHeight));
			}
			BufferedImage destBufferedImage = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
			graphics2D = destBufferedImage.createGraphics();
			graphics2D.setBackground(BACKGROUND_COLOR);
			graphics2D.clearRect(0, 0, destWidth, destHeight);
			graphics2D.drawImage(srcBufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), (destWidth / 2) - (width / 2), (destHeight / 2) - (height / 2), null);

			imageOutputStream = ImageIO.createImageOutputStream(destFile);
			imageWriter = ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(destFile.getName())).next();
			imageWriter.setOutput(imageOutputStream);
			ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
			imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			imageWriteParam.setCompressionQuality((float) (DEST_QUALITY / 100.0));
			imageWriter.write(null, new IIOImage(destBufferedImage, null, null), imageWriteParam);
			imageOutputStream.flush();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (graphics2D != null) {
				graphics2D.dispose();
			}
			if (imageWriter != null) {
				imageWriter.dispose();
			}
			try {
				if (imageOutputStream != null) {
					imageOutputStream.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 添加水印
	 * 
	 * @param srcFile
	 *            源文件
	 * @param destFile
	 *            目标文件
	 * @param watermarkFile
	 *            水印文件
	 * @param watermarkPosition
	 *            水印位置
	 * @param alpha
	 *            水印透明度
	 */
	public static void addWatermark(File srcFile, File destFile, File watermarkFile, String watermarkPosition) {

		if (watermarkFile == null || !watermarkFile.exists() || !watermarkFile.isFile() || watermarkPosition == null || "no".equals(watermarkPosition)) {
			return;
		}
		Graphics2D graphics2D = null;
		FileOutputStream fileOutputStream = null;
		try {
			BufferedImage srcBufferedImage = ImageIO.read(srcFile);
			int srcWidth = srcBufferedImage.getWidth();
			int srcHeight = srcBufferedImage.getHeight();
			BufferedImage destBufferedImage = new BufferedImage(srcWidth, srcHeight, BufferedImage.TYPE_INT_RGB);
			graphics2D = destBufferedImage.createGraphics();
			graphics2D.setBackground(BACKGROUND_COLOR);
			graphics2D.clearRect(0, 0, srcWidth, srcHeight);
			graphics2D.drawImage(srcBufferedImage, 0, 0, null);
			graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 50 / 100F));

			BufferedImage watermarkBufferedImage = ImageIO.read(watermarkFile);
			int watermarkImageWidth = watermarkBufferedImage.getWidth();
			int watermarkImageHeight = watermarkBufferedImage.getHeight();
			
			int x;
			int y;
			if("topLeft".equals(watermarkPosition)){
				x = 0;
				y = 0;
			}else if("topRight".equals(watermarkPosition)){
				x = srcWidth - watermarkImageWidth;
				y = 0;
			}else if("center".equals(watermarkPosition)){
				x = (srcWidth - watermarkImageWidth) / 2;
				y = (srcHeight - watermarkImageHeight) / 2;
			}else if("bottomLeft".equals(watermarkPosition)){
				x = 0;
				y = srcHeight - watermarkImageHeight;
			}else if("bottomRight".equals(watermarkPosition)){
				x = srcWidth - watermarkImageWidth;
				y = srcHeight - watermarkImageHeight;
			}else{
				x = srcWidth - watermarkImageWidth;
				y = srcHeight - watermarkImageHeight;
			}
			graphics2D.drawImage(watermarkBufferedImage, x, y, watermarkImageWidth, watermarkImageHeight, null);
			
			// 输出图片
			fileOutputStream = new FileOutputStream(destFile);
			ImageIO.write(destBufferedImage, "jpg", fileOutputStream);
			fileOutputStream.flush();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (graphics2D != null) {
				graphics2D.dispose();
			}
			try {
				if (fileOutputStream != null) {
				    fileOutputStream.close();
				}
			} catch (IOException e) {
			}
		}
	}

}