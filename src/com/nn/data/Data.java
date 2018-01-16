package com.nn.data;

import java.io.Serializable;
import java.util.Arrays;

public class Data implements Serializable{

	private static final long serialVersionUID = 4928379799827752188L;
	public double[] values;
	Datainfo metadata;
	public Data(Datainfo metadata, String[] strings) {
		
		this.metadata = metadata;
		values = new double[metadata.input_num+metadata.output_num];
		for(int i=0; i<metadata.input_num; i++)
			values[i] = Double.parseDouble(strings[i]);
		
		for(int i=metadata.input_num;i<metadata.output_num+metadata.input_num; i++)
			values[i] = Double.parseDouble(strings[i]);
				
	}
	
	public Data(Datainfo metadata, int[] only_int_inputs) {
		
		this.metadata = metadata;
		values = new double[metadata.input_num+metadata.output_num];
		for(int i=0; i<metadata.input_num; i++)
			values[i] = only_int_inputs[i];

				
	}

	public double[] get_result() {
		return Arrays.copyOfRange(values, metadata.input_num, metadata.output_num+metadata.input_num);
	}

	public double[] get_input() {
		return Arrays.copyOfRange(values, 0, metadata.input_num);
	}
	
	public String toString ()
	{
		String buf = "";
		
		for (double val : values) 
			buf += val+"\t";

		return buf;

	}
	
}
