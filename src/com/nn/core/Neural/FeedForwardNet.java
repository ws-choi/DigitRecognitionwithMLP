package com.nn.core.Neural;

import java.io.Serializable;

import javax.xml.crypto.NodeSetData;

import com.global.Configuration;
import com.nn.core.Neural.layer.InputLayer;
import com.nn.core.Neural.layer.Layer;
import com.nn.core.Neural.layer.OutputLayer;
import com.nn.core.Neural.node.Neuron;
import com.nn.core.Neural.node.Node;
import com.nn.data.Data;
import com.nn.data.Datainfo;
import com.nn.data.Dataset;

//2014 09 20
abstract public class FeedForwardNet implements Serializable {

	Datainfo metadata;
	private static final long serialVersionUID = 5033249996112226472L;
	public Layer[] layers;
	protected OutputLayer outputLayer;
	protected InputLayer inputLayer;

	abstract public void recontruct () ;
	/*connecting layers*/
	protected void connect_all_layer() {
		for(int i=1; i<layers.length; i++)
			connect(layers[i-1], layers[i]);
	}
	
	public void connect (Layer from, Layer to){

		from.setToLayer(to);
		to.setFromLayer(from);	
	}
	/*connecting layers*/
	
	//Forwarding input	
	public void forward_computation(Data data) {
		
		inputLayer.setData(data);
		
		for(int i=0; i<layers.length; i++){
			layers[i].receive();
			layers[i].transfer();
		}		
	}
	
	//Forwarding input	

	abstract public void modify_all(Data data, double learning_rate);
	abstract public void add_delta_weight(Data data, double learning_rate);

	/*bpp algorithm*/
	public void backward_computation(Data data) {
		
		outputLayer.gen_and_set_delta_err(data);
		for(int i=layers.length-2; i>-1; i--)
			layers[i].gen_and_set_delta_err();
		
	}
	protected void back_propagation(double learning_rate) {
		for(int i=layers.length-1; i>0; i--)
			layers[i].modify_backpropagate(learning_rate);
	}
	
	protected void bckprp_without_mdfing(double learning_rate) {
		for(int i=layers.length-1; i>0; i--)
			layers[i].backpropagate(learning_rate);
	}


	/*bpp algorithm*/

	/*for training*/
	public double get_total_err(Data data) {
		double err_sum = 0;
		
	/*
		Data = {input_vec, output_vec}, 
		(ex) (0, 1, 1) in xor gate
		return err_sum which is the sum of error, according to the given data
	
		Each node Nx in the output layer has the forwarded value Nx.v
		err_sum = sum of Nx.v for each Nx in the output layer
	*/			

		Node[] nodes = outputLayer.getNodes();
		double[] result = data.get_result(); //extracts output_vec from data
		
		forward_computation(data);

		for(int i=0; i<nodes.length; i++)
			err_sum += nodes[i].get_err_square(result[i]);
	
		return err_sum;
	}

	/*for validating the model*/
	public boolean test_dataset(Dataset dataset, double threshold) {

		double total_err=0;
		Data[] testset = dataset.toArray(new Data[0]);
		
		for (Data data : testset) 	
			total_err+= get_total_err(data);

		System.out.println("total error is " + total_err);
		Configuration.setText("total error is " + total_err+'\n');
		if(total_err>threshold) return false;
		else
		{
			System.out.println("Test dataset successfully classified (threshold: " + threshold +')');
			Configuration.setText("Test dataset successfully classified (threshold: " + threshold +")\n");
			return true;
		}
	}
	/*for validating the model*/
	

	public void batch_modifying() {
		
		for(int i=1; i<layers.length; i++)
			layers[i].batch_modify();
	}

	public void init_dw_vector() {
		
		for(int i=1; i<layers.length; i++)
			for(int j=0; j<layers[i].getNodes().length; j++)
				((Neuron)layers[i].getNodes()[j]).init_dw_vector();
	}
	
	public double[] get_output_vec ()
	{
		return outputLayer.getVector();
	}

	public Datainfo getMetadata() {
		return metadata;
	}

}


