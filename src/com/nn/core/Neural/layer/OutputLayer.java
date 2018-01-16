package com.nn.core.Neural.layer;

import java.io.Serializable;

import com.global.Configuration;
import com.global.Mathematical;
import com.nn.core.Neural.node.Neuron;
import com.nn.core.Neural.node.Node;
import com.nn.core.Neural.node.Step_neuron;
import com.nn.data.Data;

public class OutputLayer extends Layer implements Serializable {

	private static final long serialVersionUID = -1108205610456387387L;

	public OutputLayer(int num_in, int num_out) {
		super(num_in, num_out);
		
		nodes = new Neuron[num_out];
		for (int i = 0; i < nodes.length; i++)
			nodes[i]= new Step_neuron(num_in);
	}

		
	public boolean test (Data data)
	{
		double[] correct_result = data.get_result();
		if(correct_result.length != nodes.length) return false;
	
		for(int i=0; i<nodes.length; i++)
			if(nodes[i].getValue() != correct_result[i])
				return false;


		return true;
	}


	public void modify_Step_Neurons(Data data, double learnig_rate) {
	
		System.out.println("\t\t\t\t data:"+data);
		double[] correct_result = data.get_result();
		if(correct_result.length != nodes.length) return;
	
		for(int i=0; i<nodes.length; i++)
			if(nodes[i].getValue() != correct_result[i])
			{
				Neuron node = (Neuron)nodes[i];
				System.out.println("\t\t\t\t weights (before):" + node);
				System.out.println("\t\t\t\t node_val:" + node.getValue());


				double[] old_ = node.weight;
				double[] delta_ = Mathematical.mul_constant_vec
						(learnig_rate*correct_result[i], data.get_input());
				
				double[] delta_with_bias = new double[old_.length];
				System.arraycopy(delta_, 0, delta_with_bias, 0, delta_.length);
				delta_with_bias[delta_with_bias.length-1]= learnig_rate*correct_result[i];
				
				double[] new_ = Mathematical.add_vec(old_, delta_with_bias);
				
				node.weight = new_;
				
				System.out.println("\t\t\t\t weights (after):" + node);
				try {
					Thread.sleep(Configuration.waiting_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		
	}
	
	public void gen_and_set_delta_err (Data data)
	{
		int num = nodes.length;
		for(int i = 0; i<num; i++){
			double yk = Mathematical.sigmoid(((Neuron)nodes[i]).before_activated);
//			double yk = ((Neuron)nodes[i]).before_activated;
			((Neuron)nodes[i]).delta_err = (data.get_result()[i] - yk) * yk * (1-yk);			
		}
			
	}
	
	@Override
	public void modify_backpropagate(double learning_rate) {
		Node[] fromNodes = fromLayer.getNodes();
		
		for(int i=0; i<nodes.length; i++)
		{
			Neuron node = (Neuron)nodes[i];
			for(int j=0; j<fromNodes.length; j++)
			{
				node.weight[j] += learning_rate * node.delta_err * fromNodes[j].getValue(); 
			}
			

			node.weight[fromNodes.length] 
					+= learning_rate * node.delta_err * 1; 
		}
		
		
	}
	
}
