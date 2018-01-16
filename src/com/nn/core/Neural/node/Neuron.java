package com.nn.core.Neural.node;

import java.io.Serializable;

import com.global.Mathematical;

public abstract class Neuron extends Node implements Serializable {

	private static final long serialVersionUID = 7220537072529067296L;

	int input_num;

	public double delta_err;	
	
	public double before_activated;
	public double step_val_after_sig;
	
	public double[] weight; //weight[length-1] is the weight for bias whose value is always 1
	public double[] delta_weight_sum; //weight[length-1] is the weight for bias whose value is always 1

	public Neuron(int input_num) {

		this.input_num = input_num; //num is the number of inputs
		weight = new double[input_num+1];
		delta_weight_sum = new double[input_num+1];
		
		for( int i = 0; i<weight.length; i++)
			weight[i] = Math.random()* 0.6 - 0.3;
		
		
	}

	public void gen_and_set_val(double[] input)
	{
		if(input.length != input_num)
		{
			System.out.println("Mismatch");
			return;
		}
		
		before_activated = weight[weight.length-1];
		
		for( int i = 0; i<input_num; i++)
			before_activated += weight[i] * input[i];
		
		//before_actovated: = (input[], [1]) * weight_vector
		
		value = activate(before_activated);
		step_val_after_sig = Mathematical.sign(Mathematical.sigmoid(before_activated));
		
	}
		
	@Override
	public double get_err_square(double correct){
	return Math.pow(correct - Mathematical.sigmoid(before_activated) , 2);
	}
	
	abstract protected double activate(double value);

	@Override
	public String toString() {
		String buf = "";
		
		for (double val : weight) 
			buf += val+"\t";

		return buf;
}

	public void batch_modify() {
		for(int i=0;i<weight.length;i++)
			weight[i] += delta_weight_sum[i];
	}
	
	public void init_dw_vector() {}
	{
		delta_weight_sum = new double[input_num+1];
	}
	
	public double get_before_activated()
	{
		return before_activated;
	}
}
