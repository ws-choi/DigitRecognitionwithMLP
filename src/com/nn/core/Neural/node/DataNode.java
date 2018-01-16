package com.nn.core.Neural.node;

import java.io.Serializable;

public class DataNode extends Node implements Serializable {
	
	private static final long serialVersionUID = -9076028420930980375L;

	@Override
	public void gen_and_set_val(double[] input) {
		System.out.println("not allowed");
		return;
	}

}
