package com.nn.core.Neural.layer;

import java.io.Serializable;

import com.nn.core.Neural.node.Neuron;
import com.nn.core.Neural.node.Sigmoid_neuron;

public class HiddenLayer extends Layer implements Serializable{

	private static final long serialVersionUID = 2027078113413045098L;

	public HiddenLayer(int num_in, int num_out) {
		super(num_in, num_out);
		
		nodes = new Neuron[num_out];
		for (int i = 0; i < nodes.length; i++)
			nodes[i]= new Sigmoid_neuron(num_in);
	
	}

}
