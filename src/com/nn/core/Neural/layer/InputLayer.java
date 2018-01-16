package com.nn.core.Neural.layer;

import java.io.Serializable;

import com.nn.core.Neural.node.DataNode;
import com.nn.data.Data;


public class InputLayer extends Layer implements Serializable {

	private static final long serialVersionUID = 803542139331564360L;
	Data data;

	public InputLayer(int num_in, int num_out) {
		super(num_in, num_out);

		nodes = new DataNode[num_out];
		for (int i = 0; i < nodes.length; i++) 
			nodes[i] = new DataNode();
	}


	public void fetch (Data data)
	{
		if(nodes.length != data.get_input().length) return;
		
		for(int i=0; i<nodes.length; i++)
			nodes[i].setValue(data.values[i]);
		
	}

	@Override
	public void receive() {
		try {
			fetch(data);
		} catch (Exception e) {
			System.out.println("InputLayer: error occured in receive.");
			e.printStackTrace();
		}
	}

	public void setData(Data data) {
		this.data=data;
	}

	public void gen_and_set_delta_err(){}

/*	public double total_err() {
		
		double err_sum = 0;
		for (Node node: nodes) err_sum += node.geterr();		
		return err_sum;
	}
*/	
	
}
