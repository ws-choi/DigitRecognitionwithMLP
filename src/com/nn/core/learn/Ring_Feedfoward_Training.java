package com.nn.core.learn;

import com.global.Configuration;
import com.nn.core.Neural.MultiLayerPerceptron;
import com.nn.core.Neural.FeedForwardNet;
import com.nn.core.Neural.node.Neuron;
import com.nn.data.Data;
import com.nn.data.Dataset;
import com.nn.data.RingDataset;
import com.nn.data.StochasticDataSet;

//2014 09 20
public class Ring_Feedfoward_Training extends TrainingFrame implements Training{

	public Ring_Feedfoward_Training(Dataset dataset, FeedForwardNet network) {
		super(dataset);
		this.network = network;
	}

	@Override
	public Data getNextData() {
		return dataset.next();
	}
	
	@Override
	public void training ()
	{
		do
		{
			Data data = getNextData();
			epoch_num++;
			network.forward_computation(data) ;
			network.modify_all(data, Configuration.learning_rate);
			
		}while(!terminate_condition(Configuration.err_threshlod));
		

		System.out.println("epoch numbuer: " +epoch_num/dataset.size());	
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

	public static void main(String[] args) {
		
		//RingDataset rd = new RingDataset("test.txt");
		
		Dataset rd = new StochasticDataSet("test.txt");
		//NeuralNetwork network = new Perceptron(rd.getMetadata());
		FeedForwardNet network2 = new MultiLayerPerceptron(rd.getMetadata());
		
		((Neuron)(network2.layers[1].getNodes()[0] )).weight[0] = 0.5;
		((Neuron)(network2.layers[1].getNodes()[0] )).weight[1] = 0.4;
		((Neuron)(network2.layers[1].getNodes()[0] )).weight[2] = -0.8;
		
		((Neuron)(network2.layers[1].getNodes()[1] )).weight[0] = 0.9;
		((Neuron)(network2.layers[1].getNodes()[1] )).weight[1] = 1.0;
		((Neuron)(network2.layers[1].getNodes()[1] )).weight[2] = 0.1;
		
		((Neuron)(network2.layers[2].getNodes()[0] )).weight[0] = -1.2;
		((Neuron)(network2.layers[2].getNodes()[0] )).weight[1] = 1.1;
		((Neuron)(network2.layers[2].getNodes()[0] )).weight[2] = -0.3;
		
		
		Ring_Feedfoward_Training ot = new Ring_Feedfoward_Training(rd, network2);
		
		ot.training();
		
		
		
	}

}
