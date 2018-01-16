package com.global;

import java.awt.TextArea;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.ws.handler.MessageContext.Scope;

//2014 09 20
public class Configuration {

	//debug
	public static final long waiting_time =00;
	
	//mathematical
	public static final double false_value = 0;
	public static final double sign_threshold = 0.5;

	//parameter for training
	public static double learning_rate = 0.1;
	
	//training option
	public static final boolean limited_epoch = true;
	public static int max_epoch_num = 1024;

	public static double err_threshlod = 0.01;
	
	public static int num_hid_units = 100;

	public static double min_learning_rate = 0.001;

	public static double learn_dekay = 1;
	
	
	//output
	public static JTextArea textarea;
	public static JScrollPane scrollPane;
	
	public static void setText (String str)
	{
		if(textarea!=null)
		{
			textarea.append(str);
			textarea.repaint();
		}
		
		if(scrollPane != null)
		{
			JScrollBar bar = scrollPane.getVerticalScrollBar();
			bar.setValue(bar.getMaximum());
			
		}	
	}
	
}
