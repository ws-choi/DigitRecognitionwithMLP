package com.nn.data;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;

public class StochasticDataSet extends Dataset{

	private static final long serialVersionUID = 2008422541474988910L;
	Iterator<Data> iter;
	
	public StochasticDataSet(String filename) {
		super(filename);
		Collections.shuffle(this);
		iter = this.iterator();
	}

	public StochasticDataSet(File file) {
		super(file);
		Collections.shuffle(this);
		iter = this.iterator();
	}
	
	@Override
	public Data next() {
		if(iter.hasNext()) return iter.next();
		else
		{
			Collections.shuffle(this);			
			iter = this.iterator();			
			return iter.next();
		}
	}
	
	public Iterator<Data> getShuffledIter ()
	{
		Collections.shuffle(this);			
		return this.iterator();			
	}
	
/*	public static void main(String[] args) {
	
		StochasticDataSet ss= new StochasticDataSet("test.txt");
		
		System.out.println("?");
		System.out.println(ss.next());
		System.out.println(ss.next());
		System.out.println(ss.next());
		System.out.println(ss.next());
		System.out.println(ss.next());
		System.out.println(ss.next());
		System.out.println(ss.next());
		System.out.println(ss.next());
		System.out.println(ss.next());
		System.out.println(ss.next());
		System.out.println(ss.next());

	

	}*/

}
