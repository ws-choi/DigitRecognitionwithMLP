package com.nn.core.Neural.node;

import java.io.Serializable;

import com.global.Mathematical;

public class Sigmoid_neuron extends Neuron implements Serializable{

	private static final long serialVersionUID = 2170229301955884067L;

	public Sigmoid_neuron(int input_num) {
		super(input_num);
	}

	@Override
	protected double activate(double value) {
		return Mathematical.sigmoid(value);
	}


}
