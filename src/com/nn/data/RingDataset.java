package com.nn.data;


public class RingDataset extends Dataset {

	private static final long serialVersionUID = 1L;

	int index=-1;
	
	public RingDataset(String filename) {
		super(filename);
	}
	
	
	@Override
	public Data next() {
		index = ((index+1)%size() == 0 ) ? 0 : ++index;
		return get(index);
		
	}
}
