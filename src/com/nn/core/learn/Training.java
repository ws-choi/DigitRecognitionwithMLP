package com.nn.core.learn;

import com.nn.data.Data;

//2014 09 20
public interface Training  {

	Data getNextData();
	public void training ();
	boolean terminate_condition (double threshold);
		
}
