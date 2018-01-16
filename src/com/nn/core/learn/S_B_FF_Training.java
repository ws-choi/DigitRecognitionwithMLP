package com.nn.core.learn;

import java.awt.TextArea;
import java.util.Iterator;

import com.global.Configuration;
import com.nn.core.Neural.FeedForwardNet;
import com.nn.core.Neural.MultiLayerPerceptron;
import com.nn.core.Neural.node.Neuron;
import com.nn.data.Data;
import com.nn.data.Dataset;
import com.nn.data.StochasticDataSet;


//Stochastic Batch mode for training FeedForward

public class S_B_FF_Training extends TrainingFrame implements Training, Runnable{

	Iterator<Data> iter;
	
	public S_B_FF_Training(Dataset dataset, FeedForwardNet network) {
		super(dataset);
		this.network = network;
	}

	@Override
	public Data getNextData() {
		return iter.next();
	}

	@Override
	public void training() {

		do
		{
			epoch_num++;
			
			iter = ((StochasticDataSet)dataset).getShuffledIter();
			
			network.init_dw_vector();
			while(iter.hasNext())
			{
				Data data = getNextData();
				network.forward_computation(data) ;
				network.modify_all(data, Configuration.learning_rate);
			}
			
			decay_rate();
			
			
		}while(!terminate_condition(Configuration.err_threshlod));
		
		System.out.println("epoch numbuer: " +epoch_num);	
		Configuration.setText("epoch number: " +epoch_num +'\n');
	}

	@Override
	public boolean terminate_condition(double threshold) {
		
		if(Configuration.limited_epoch)
			if(epoch_num > Configuration.max_epoch_num){
				System.out.println("Max epoch num");
				return true;
			}
				
		return network.test_dataset(dataset, threshold);
	}
	
	public boolean validate (double threshold) {
				
		return network.test_dataset(dataset, threshold);
	}


	public static void main(String[] args) {
		
		//RingDataset rd = new RingDataset("test.txt");
		
		Dataset rd = new StochasticDataSet("test.txt");
		//NeuralNetwork network = new Perceptron(rd.getMetadata());
		FeedForwardNet network2 = new MultiLayerPerceptron(rd.getMetadata());
		/*
		((Neuron)(network2.layers[1].getNodes()[0] )).weight[0] = 0.5;
		((Neuron)(network2.layers[1].getNodes()[0] )).weight[1] = 0.4;
		((Neuron)(network2.layers[1].getNodes()[0] )).weight[2] = -0.8;
		
		((Neuron)(network2.layers[1].getNodes()[1] )).weight[0] = 0.9;
		((Neuron)(network2.layers[1].getNodes()[1] )).weight[1] = 1.0;
		((Neuron)(network2.layers[1].getNodes()[1] )).weight[2] = 0.1;
		
		((Neuron)(network2.layers[2].getNodes()[0] )).weight[0] = -1.2;
		((Neuron)(network2.layers[2].getNodes()[0] )).weight[1] = 1.1;
		((Neuron)(network2.layers[2].getNodes()[0] )).weight[2] = -0.3;
		
		*/
		//Ring_Feedfoward_Training ot = new Ring_Feedfoward_Training(rd, network2);
		S_B_FF_Training sbff = new S_B_FF_Training(rd, network2);
		
		sbff.training();	
		
	}

	@Override
	public void run() {
		this.training();
	}


	
}
