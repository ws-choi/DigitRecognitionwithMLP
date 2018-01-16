package com.nn.data;

import java.io.Serializable;

public class Datainfo implements Serializable{

	private static final long serialVersionUID = -4392425385781166120L;
	int input_num;
	int output_num;
	
	public Datainfo() {
	}
	
	public int get_Input_Num() {return input_num;}
	public int get_Output_Num() {return output_num;}

}
