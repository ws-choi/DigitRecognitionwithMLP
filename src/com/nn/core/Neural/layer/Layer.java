package com.nn.core.Neural.layer;

import java.io.Serializable;

import com.global.Configuration;
import com.global.Mathematical;
import com.nn.core.Neural.node.Neuron;
import com.nn.core.Neural.node.Node;

public abstract class Layer implements Serializable{
	
	private static final long serialVersionUID = -6336482883421066946L;
	
	protected int num_in, num_out;
	protected Node[] nodes;
	protected Layer toLayer;
	protected Layer fromLayer;
	protected double[] input_buffer;
	
	public Layer(int num_in, int num_out) {
		this.num_in = num_in;
		this.num_out = num_out;
	}
	
	public void receive() {
		if(fromLayer == null) return;
		for(int i = 0; i<nodes.length; i++)
			nodes[i].gen_and_set_val(input_buffer);	
	}

	public void transfer() {
		if(toLayer == null) return;
	
		int num = nodes.length;
		toLayer.input_buffer = new double[num];

		for(int i = 0; i<num; i++)
			toLayer.input_buffer[i] = nodes[i].getValue();		
	}
	

	public void gen_and_set_delta_err() {
		
		int num = nodes.length;
		for(int i = 0; i<num; i++){
			double oj = nodes[i].getValue();
			
			double buf = 0;
			Neuron[] tonodes = (Neuron[])toLayer.getNodes();
			
			for(int j=0 ; j < tonodes.length; j++)
			{
				buf += tonodes[j].weight[i] * tonodes[j].delta_err;
				((Neuron)nodes[i]).delta_err = oj * (1-oj) * buf;
			}
			
			
		}
		
	}

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
			
			try {
				Thread.sleep(Configuration.waiting_time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.err.println(this.getClass());
			//System.out.println(node);
		}
		
	}

	
	
	public Node[] getNodes () {return this.nodes;}
	public void setToLayer(Layer to) {		this.toLayer = to;}
	public void setFromLayer(Layer from) {	this.fromLayer = from;}
	
	
	public double[] getVector () {
		double[] output = new double[num_out];
		for(int i=0; i<num_out; i++)
			output[i] = Mathematical.sigmoid(((Neuron)nodes[i]).before_activated);
		
		return output;
	}
	@Override
	public String toString() {
		String buf = "";
		
		for (Node node : nodes) 
			buf += node.getValue()+"\t";

		buf += "\n";
		return buf;	}

	public void backpropagate(double learning_rate) {
		
		Node[] fromNodes = fromLayer.getNodes();
	
		
		for(int i=0; i<nodes.length; i++)
		{
			Neuron node = (Neuron)nodes[i];			
			for(int j=0; j<fromNodes.length; j++)
			{
				node.delta_weight_sum[j] += learning_rate * node.delta_err * fromNodes[j].getValue(); 
			}

			node.delta_weight_sum[fromNodes.length] 
					+= learning_rate * node.delta_err * 1; 
			
			try {
				Thread.sleep(Configuration.waiting_time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.err.println(this.getClass());
			//System.out.println(node);
		}
	}

	public void batch_modify() {
		
		for(int i = 0; i<nodes.length; i++)
		{
			((Neuron)nodes[i]).batch_modify();
		}
	}

	
	
	
}
