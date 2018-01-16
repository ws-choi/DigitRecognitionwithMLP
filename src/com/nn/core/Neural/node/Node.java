package com.nn.core.Neural.node;

import java.io.Serializable;


abstract public class Node implements Serializable{


	private static final long serialVersionUID = 8003415046536723472L;
	protected double value;
		
	abstract public void gen_and_set_val(double[] input);

	public double get_err_square (double correct) {return 0;};
	public double getValue() 			{return value;}
	public void setValue(double value) 	{this.value = value;}

}
