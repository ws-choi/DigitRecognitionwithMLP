package com.gui;

 import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;
import java.io.File;
 
import javax.imageio.ImageIO;

import org.w3c.dom.css.RGBColor;
 
 public class PixelCheck {
 
 	public static void main(String[] args)throws Exception{
 		File inputFile =new File("img/5.jpg");
 		BufferedImage bufferedImage = ImageIO.read(inputFile);
 		int w = bufferedImage.getWidth();
 		int h = bufferedImage.getHeight();
 		//Get Pixels
 		int[] rgbs =new int[w*h];
 		bufferedImage.getRGB(0,0, w, h, rgbs,0, w);//Get all pixels
 		
 		for(int i=0;i<w*h;i++)
 		 {
            int argb = rgbs[i];
            int r = (argb >> 16) & 0xff;
            int g = (argb >>  8) & 0xff;
            int b = (argb      ) & 0xff;
            rgbs[i] = (r+g+b)/3 > 120 ? 0: 1;
            
// 			System.out.println("rgbs["+i+"]= "+ (long)(rgbs[i]));
            System.out.print((rgbs[i] + "\t"));
 		 }
 		
/* 		 //when i printed this, I was expecting pixel values 
 		//but I got negative values... :| 
 		//Set Pixels
 		 int rgb =0xFF00FF00;// green  
 		 for(int j=0;j<20;j++)
 			 for(int k=0;k<20;k++)
 				 bufferedImage.setRGB(j,k, rgb);
 		//Instead of setting the pixels to green, 
 		//it is instead set to Gray... :confused:
*/ 	}
 }