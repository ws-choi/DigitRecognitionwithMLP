package com.global;

public class Mathematical {

	static public double[] add_vec(double[] vec_a, double[] vec_b) {
		
		
		double[] out = vec_a.clone();
		for(int i=0; i<out.length; i++)
			out[i] = out[i] + vec_b[i];
		return out;
	}


	static public double[] mul_constant_vec(double scalar, double[] vec_a) {
		double[] out = vec_a.clone();
		for(int i=0; i<out.length; i++)
			out[i] = scalar*out[i];
		return out;
	}

	static public double sign(double value) {
		if(value< Configuration.sign_threshold) return Configuration.false_value;
		else return 1;
	}


	public static double sigmoid(double value) {
		 return (1/( 1 + Math.pow(Math.E,(-1*value))));
	}
	
	public static int get_max_index (double[] data)
	{
		double buf = Double.MIN_VALUE;
		int index = -1;
		for(int i=0;i<data.length; i++)
			if ( buf < data[i]) {
				buf = data[i];
				index = i;
			}
		
		return index;
	}

}
