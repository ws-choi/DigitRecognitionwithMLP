package com.nn.core.Neural.node;

import java.io.Serializable;

import com.global.Mathematical;

public class Step_neuron extends Neuron implements Serializable{

	private static final long serialVersionUID = -1193864053428456856L;

	public Step_neuron(int input_num) {
		super(input_num);
	}

	@Override
	protected double activate(double value) {
		return Mathematical.sign(value);
	}


}
