package com.nn.core.learn;
import com.global.Configuration;
import com.nn.core.Neural.FeedForwardNet;
import com.nn.data.Dataset;

//2014 09 20
abstract public class TrainingFrame {

	Dataset dataset;
	FeedForwardNet network;
	
	int epoch_num;
	
	public TrainingFrame(Dataset dataset) {
		this.dataset = dataset;
		epoch_num=0;
	}
	
	public void init ()
	{
		epoch_num = 0;
		network.recontruct();	
	}
	
	public void decay_rate ()
	{
		if(Configuration.learning_rate <= Configuration.min_learning_rate)
			return;
		
		Configuration.learning_rate *= Configuration.learn_dekay;
	}
	/*	
	public void learn(Dataset dataset)
	{
		boolean stop_con = false;
		do{
			stop_con = run_epoch(dataset);
		}while(!stop_con);
	}
	
	public boolean run_epoch (Dataset dataset)
	{
		return false;
	}
	*/
}
