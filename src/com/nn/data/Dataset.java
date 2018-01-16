package com.nn.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

abstract public class Dataset extends ArrayList<Data> implements Serializable{

	private static final long serialVersionUID = 0001L;
	
	File src;
	protected Datainfo metadata;
	BufferedReader br;
	
	public Dataset(String filename) {
		try {
			src = new File(filename);
			br = new BufferedReader(new FileReader(src));
			parse();
		} catch (Exception e) {
			System.out.println("File not found");
		}	
	}
	
	public Dataset (File file) {

		src= file;
		Collections.shuffle(this);
		try {
			br = new BufferedReader(new FileReader(src));
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		parse();
	}
	
	protected void parse() {
		
		String buf;
		metadata = new Datainfo();

		try {
			while( !(buf=br.readLine()).equalsIgnoreCase("@"));
			metadata.input_num = Integer.parseInt(br.readLine());

			while( !(buf=br.readLine()).equalsIgnoreCase("@"));
			metadata.output_num = Integer.parseInt(br.readLine());

			while( !(buf=br.readLine()).equalsIgnoreCase("@"));

			while( (buf=br.readLine()) != null)
			{
				String[] strings = buf.split("\t");
				add(new Data(metadata, strings));
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public Datainfo getMetadata() {
		return metadata;
	}

	public void setMetadata(Datainfo metadata) {
		this.metadata = metadata;
	}

	abstract public Data next();
	

}
