package com.nn.core.Neural;import com.nn.core.Neural.layer.InputLayer;
import com.nn.core.Neural.layer.Layer;
import com.nn.core.Neural.layer.OutputLayer;
import com.nn.data.Data;
import com.nn.data.Datainfo;
import com.nn.data.Dataset;

//2014 09 20
public class SingleLayerPerceptron extends FeedForwardNet{

	public SingleLayerPerceptron(Datainfo metadata) {
		this.metadata =metadata;
		init();
	}

	void init ()
	{
		layers = new Layer[2];
		layers[0] = inputLayer = new InputLayer(0, metadata.get_Input_Num());
		layers[1] = outputLayer = new OutputLayer(metadata.get_Input_Num(), metadata.get_Output_Num());
		connect_all_layer();
	}
	@Override
	public void modify_all(Data data,double learnig_rate) {
		outputLayer.modify_Step_Neurons(data, learnig_rate);
	}



	@Override
	public void add_delta_weight(Data data, double learning_rate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public
	void recontruct() {
		init();
	}

}
