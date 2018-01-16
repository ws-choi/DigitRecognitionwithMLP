package com.nn.core.Neural;

import java.io.Serializable;
import java.util.Iterator;

import com.global.Configuration;
import com.nn.core.Neural.layer.HiddenLayer;
import com.nn.core.Neural.layer.InputLayer;
import com.nn.core.Neural.layer.Layer;
import com.nn.core.Neural.layer.OutputLayer;
import com.nn.core.Neural.node.Node;
import com.nn.data.Data;
import com.nn.data.Datainfo;
import com.nn.data.Dataset;

//2014 10 07
public class MultiLayerPerceptron extends FeedForwardNet{

	public MultiLayerPerceptron(Datainfo metadata) {
		
		this.metadata = metadata;
		init();
	}
	
	void init ()
	{

		layers = new Layer[3];
		layers[0] = inputLayer = new InputLayer(0, metadata.get_Input_Num());
		layers[1] = new HiddenLayer (metadata.get_Input_Num(), Configuration.num_hid_units);
		layers[2] = outputLayer = new OutputLayer(Configuration.num_hid_units, metadata.get_Output_Num());
		connect_all_layer();
	}

	@Override
	public void modify_all(Data data, double learning_rate) {

		//Back Propagation
		backward_computation(data);	
		back_propagation(learning_rate);		
	}



	@Override
	public void add_delta_weight(Data data, double learning_rate) {

		backward_computation(data);	
		bckprp_without_mdfing(learning_rate);
		
	}

	@Override
	public
	void recontruct() {
		init();
	}

}
