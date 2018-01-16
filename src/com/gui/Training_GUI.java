package com.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;

import com.global.Configuration;
import com.global.Mathematical;
import com.nn.core.Neural.MultiLayerPerceptron;
import com.nn.core.learn.S_B_FF_Training;
import com.nn.data.Data;
import com.nn.data.Datainfo;
import com.nn.data.Dataset;
import com.nn.data.StochasticDataSet;

public class Training_GUI extends JFrame {
	
	JTextArea textarea;
	New_painter Paint_GUI;
	S_B_FF_Training sbff;
	Dataset ds;
	Datainfo metadata;
	MultiLayerPerceptron mlp;
	
	JFileChooser fc;
	File file;
	
	
	SpinnerNumberModel snm1;
	SpinnerNumberModel snm2;
	
	private final JPanel panel = new JPanel();
	private JTextField input_num;
	private JTextField output_num;
	private JPanel Data_File_GUI;
	private JTextField learning_rate;
	private JTextField decay_rate;
	private JTextField err_threshold;
	private JTextField guess_field;
	private JScrollPane scrollPane;
	private JSpinner num_hid_unit;

	class runner implements Runnable
	{

		
		Training_GUI frame;
		
		public runner(Training_GUI frame) {
			this.frame = frame;
		}
		
		@Override
		public void run() {
			
			while(true)
			{
				if(	frame!= null)
				{
					frame.textarea.repaint();
				}
		
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		
	}
	
	public void start ()
	{
		setBounds(new Rectangle(100, 100, 500, 500));
		setMinimumSize(new Dimension(500, 500));
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});	
		
		Configuration.textarea = this.textarea;
		Configuration.scrollPane = this.scrollPane;
		
		Thread t = new Thread(new runner(this));
		t.start();

	}
	
	
	
	public Training_GUI() {

		super("MLP 기반 숫자 인식기 - 고려대학교 최우성");
		//my Code
		fc = new JFileChooser();
		
		setResizable(false);
		setPreferredSize(new Dimension(400, 400));
		setMaximumSize(new Dimension(400, 350));
		setBackground(Color.WHITE);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnnetwork = new JMenu("about Network");
		mnFile.add(mnnetwork);
		
		JMenuItem mntmUploadnetwork = new JMenuItem("Upload_Network");
		mntmUploadnetwork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int returnVal = fc.showOpenDialog(Training_GUI.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {

					File myfile = fc.getSelectedFile();
					try {
						FileInputStream fis = new FileInputStream(myfile);
						ObjectInputStream ois = new ObjectInputStream(fis);
						mlp = (MultiLayerPerceptron)ois.readObject();
						num_hid_unit.setValue(mlp.layers[1].getNodes().length);
						metadata = mlp.getMetadata();
						
						ois.close();
						fis.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} 
				else 
				{
					textarea.append("File open error");
				}
				
				

			}
			
		});
		mnnetwork.add(mntmUploadnetwork);
		
		JMenuItem mntmSavenetwork = new JMenuItem("Save_Network");
		mntmSavenetwork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				

				int returnVal = fc.showOpenDialog(Training_GUI.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {

					File myfile = fc.getSelectedFile();
					FileOutputStream fos;
					try {
						fos = new FileOutputStream(myfile);
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						
						oos.writeObject(Training_GUI.this.mlp);
						oos.flush();
						fos.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
				} 
				else 
				{
					textarea.append("File open error");
				}
				
			}
		});
		mnnetwork.add(mntmSavenetwork);
		
		JMenuItem mntmTest = new JMenuItem("Test");
		mntmTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				int returnVal = fc.showOpenDialog(Training_GUI.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {

					file = fc.getSelectedFile();
					ds = new StochasticDataSet(file);
					metadata = ds.getMetadata();
					
					int cnt=0;
					int correct = 0;

					int[][] output = new int[2][metadata.get_Output_Num()];
					
					while(!ds.isEmpty())
					{
						Data data =ds.remove(ds.size()-1);
						
						mlp.forward_computation(data);
						
						int answer = Mathematical.get_max_index(data.get_result());
						output[0][answer] ++;
						int guess = Mathematical.get_max_index(mlp.get_output_vec());
						
						System.out.println(answer + "vs" + guess);
						if(answer == guess) {
							correct++;
							output[1][guess] ++;
						}
						
						cnt++;

					}
					
					Configuration.setText("Size of test data set: " + cnt +"\naccuracy ratio: " + (double)(correct)/cnt + "\n");

					for(int i=0 ; i<metadata.get_Output_Num(); i++)
						Configuration.setText(i+"'s accuracy is: "+ ((double)(output[1][i])/output[0][i])*100+"%\n");

				

				} 
				else 
				{
					textarea.append("File open error");
				}
				
				
				
			}
		});
		mnFile.add(mntmTest);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel training_Panel = new JPanel();
		training_Panel.setBackground(new Color(255, 255, 224));
		getContentPane().add(training_Panel, BorderLayout.NORTH);
		GridBagLayout gbl_training_Panel = new GridBagLayout();
		gbl_training_Panel.rowWeights = new double[]{0.0, 1.0};
		gbl_training_Panel.columnWidths = new int[] {100, 100, 100, 100};

		gbl_training_Panel.rowHeights = new int[] {10, 40};
		gbl_training_Panel.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0};

		training_Panel.setLayout(gbl_training_Panel);
		
		JButton btnOpen = new JButton("Get Training Data File");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int returnVal = fc.showOpenDialog(Training_GUI.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {

					file = fc.getSelectedFile();
					ds = new StochasticDataSet(file);
					metadata = ds.getMetadata();
					mlp = new MultiLayerPerceptron(ds.getMetadata());
					sbff = new S_B_FF_Training(ds, mlp);

					input_num.setText(""+ds.getMetadata().get_Input_Num());
					output_num.setText(""+ds.getMetadata().get_Output_Num());
					
					
					
				} 
				else 
				{
					textarea.append("File open error");
				}
			}
		});
		GridBagConstraints gbc_btnOpen = new GridBagConstraints();
		gbc_btnOpen.gridwidth = 4;
		gbc_btnOpen.weighty = 1.0;
		gbc_btnOpen.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnOpen.insets = new Insets(0, 0, 5, 0);
		gbc_btnOpen.gridx = 0;
		gbc_btnOpen.gridy = 0;
		training_Panel.add(btnOpen, gbc_btnOpen);
		
		Data_File_GUI = new JPanel();
		Data_File_GUI.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_Data_File_GUI = new GridBagConstraints();
		gbc_Data_File_GUI.weightx = 1.0;
		gbc_Data_File_GUI.gridwidth = 2;
		gbc_Data_File_GUI.insets = new Insets(0, 0, 5, 5);
		gbc_Data_File_GUI.fill = GridBagConstraints.BOTH;
		gbc_Data_File_GUI.gridx = 0;
		gbc_Data_File_GUI.gridy = 1;
		training_Panel.add(Data_File_GUI, gbc_Data_File_GUI);
		GridBagLayout gbl_Data_File_GUI = new GridBagLayout();
		gbl_Data_File_GUI.columnWidths = new int[] {100, 100};
		gbl_Data_File_GUI.rowHeights = new int[] {0, 0, 0};
		gbl_Data_File_GUI.columnWeights = new double[]{0.0, 1.0};
		gbl_Data_File_GUI.rowWeights = new double[]{0.0, 0.0, 0.0};
		Data_File_GUI.setLayout(gbl_Data_File_GUI);
		
		JLabel lblAboutData = new JLabel("Data File Pasing Info.\r\n");
		lblAboutData.setFont(new Font("����", Font.BOLD, 14));
		GridBagConstraints gbc_lblAboutData = new GridBagConstraints();
		gbc_lblAboutData.gridwidth = 2;
		gbc_lblAboutData.insets = new Insets(0, 0, 5, 0);
		gbc_lblAboutData.gridx = 0;
		gbc_lblAboutData.gridy = 0;
		Data_File_GUI.add(lblAboutData, gbc_lblAboutData);
		
		JLabel lblOfInput = new JLabel("# of Input");
		GridBagConstraints gbc_lblOfInput = new GridBagConstraints();
		gbc_lblOfInput.weightx = 1.0;
		gbc_lblOfInput.anchor = GridBagConstraints.EAST;
		gbc_lblOfInput.insets = new Insets(0, 0, 5, 5);
		gbc_lblOfInput.gridx = 0;
		gbc_lblOfInput.gridy = 1;
		Data_File_GUI.add(lblOfInput, gbc_lblOfInput);
		
		input_num = new JTextField();
		input_num.setBackground(new Color(255, 255, 255));
		input_num.setMinimumSize(new Dimension(3, 21));
		input_num.setText("none");
		input_num.setEditable(false);
		GridBagConstraints gbc_input_num = new GridBagConstraints();
		gbc_input_num.weightx = 1.0;
		gbc_input_num.fill = GridBagConstraints.HORIZONTAL;
		gbc_input_num.insets = new Insets(0, 0, 5, 0);
		gbc_input_num.gridx = 1;
		gbc_input_num.gridy = 1;
		Data_File_GUI.add(input_num, gbc_input_num);
		input_num.setColumns(4);
		
		JLabel lblOfOutput = new JLabel("# of Output");
		GridBagConstraints gbc_lblOfOutput = new GridBagConstraints();
		gbc_lblOfOutput.weightx = 1.0;
		gbc_lblOfOutput.insets = new Insets(0, 0, 0, 5);
		gbc_lblOfOutput.anchor = GridBagConstraints.EAST;
		gbc_lblOfOutput.gridx = 0;
		gbc_lblOfOutput.gridy = 2;
		Data_File_GUI.add(lblOfOutput, gbc_lblOfOutput);
		
		output_num = new JTextField();
		output_num.setEditable(false);
		output_num.setBackground(new Color(255, 255, 255));
		output_num.setText("none");
		GridBagConstraints gbc_output_num = new GridBagConstraints();
		gbc_output_num.weightx = 1.0;
		gbc_output_num.fill = GridBagConstraints.HORIZONTAL;
		gbc_output_num.gridx = 1;
		gbc_output_num.gridy = 2;
		Data_File_GUI.add(output_num, gbc_output_num);
		output_num.setColumns(4);
		
		JPanel Network_Layout_GUI = new JPanel();
		Network_Layout_GUI.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_Network_Layout_GUI = new GridBagConstraints();
		gbc_Network_Layout_GUI.weightx = 1.0;
		gbc_Network_Layout_GUI.gridwidth = 2;
		gbc_Network_Layout_GUI.insets = new Insets(0, 0, 5, 5);
		gbc_Network_Layout_GUI.fill = GridBagConstraints.BOTH;
		gbc_Network_Layout_GUI.gridx = 2;
		gbc_Network_Layout_GUI.gridy = 1;
		training_Panel.add(Network_Layout_GUI, gbc_Network_Layout_GUI);
		GridBagLayout gbl_Network_Layout_GUI = new GridBagLayout();
		gbl_Network_Layout_GUI.columnWidths = new int[] {50, 50};
		gbl_Network_Layout_GUI.rowHeights = new int[] {0, 0, 0};
		gbl_Network_Layout_GUI.columnWeights = new double[]{0.0, 0.0};
		gbl_Network_Layout_GUI.rowWeights = new double[]{0.0, 0.0, 0.0};
		Network_Layout_GUI.setLayout(gbl_Network_Layout_GUI);
		
		JLabel lblNetworkLayout = new JLabel("Network Layout");
		lblNetworkLayout.setFont(new Font("����", Font.BOLD, 14));
		GridBagConstraints gbc_lblNetworkLayout = new GridBagConstraints();
		gbc_lblNetworkLayout.insets = new Insets(0, 0, 5, 0);
		gbc_lblNetworkLayout.gridwidth = 2;
		gbc_lblNetworkLayout.gridx = 0;
		gbc_lblNetworkLayout.gridy = 0;
		Network_Layout_GUI.add(lblNetworkLayout, gbc_lblNetworkLayout);
		
		JLabel lblOfLayer = new JLabel("# of Layer");
		GridBagConstraints gbc_lblOfLayer = new GridBagConstraints();
		gbc_lblOfLayer.weightx = 1.0;
		gbc_lblOfLayer.insets = new Insets(0, 0, 5, 5);
		gbc_lblOfLayer.gridx = 0;
		gbc_lblOfLayer.gridy = 1;
		Network_Layout_GUI.add(lblOfLayer, gbc_lblOfLayer);
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(3), null, null, new Integer(0)));
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.weightx = 1.0;
		gbc_spinner.insets = new Insets(0, 0, 5, 0);
		gbc_spinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 1;
		Network_Layout_GUI.add(spinner, gbc_spinner);
		
		JLabel lblOfH = new JLabel("# of H. units");
		GridBagConstraints gbc_lblOfH = new GridBagConstraints();
		gbc_lblOfH.weightx = 1.0;
		gbc_lblOfH.insets = new Insets(0, 0, 0, 5);
		gbc_lblOfH.gridx = 0;
		gbc_lblOfH.gridy = 2;
		Network_Layout_GUI.add(lblOfH, gbc_lblOfH);
		
		num_hid_unit = new JSpinner();

		snm2 = new SpinnerNumberModel(30, 1, 2000, 5);
		num_hid_unit.setModel(snm2);
		GridBagConstraints gbc_spinner_1 = new GridBagConstraints();
		gbc_spinner_1.weightx = 1.0;
		gbc_spinner_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_1.gridx = 1;
		gbc_spinner_1.gridy = 2;
		Network_Layout_GUI.add(num_hid_unit, gbc_spinner_1);
		
		JPanel Main_GUI = new JPanel();
		getContentPane().add(Main_GUI, BorderLayout.CENTER);
		GridBagLayout gbl_Main_GUI = new GridBagLayout();
		gbl_Main_GUI.columnWidths = new int[] {100, 100, 100, 100};
		gbl_Main_GUI.rowHeights = new int[] {0};
		gbl_Main_GUI.columnWeights = new double[]{1.0};
		gbl_Main_GUI.rowWeights = new double[]{1.0};
		Main_GUI.setLayout(gbl_Main_GUI);
		
		JPanel Training_GUI = new JPanel();
		GridBagConstraints gbc_Training_GUI = new GridBagConstraints();
		gbc_Training_GUI.weightx = 1.0;
		gbc_Training_GUI.gridwidth = 2;
		gbc_Training_GUI.fill = GridBagConstraints.BOTH;
		gbc_Training_GUI.insets = new Insets(0, 0, 0, 5);
		gbc_Training_GUI.gridx = 0;
		gbc_Training_GUI.gridy = 0;
		Main_GUI.add(Training_GUI, gbc_Training_GUI);
		Training_GUI.setBackground(SystemColor.inactiveCaptionBorder);
		Training_GUI.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagLayout gbl_Training_GUI = new GridBagLayout();
		gbl_Training_GUI.columnWidths = new int[] {0, 0};
		gbl_Training_GUI.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
		gbl_Training_GUI.columnWeights = new double[]{0.0, 1.0};
		gbl_Training_GUI.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		Training_GUI.setLayout(gbl_Training_GUI);
		
		JLabel lblTrainingOptions = new JLabel("Training Options");
		lblTrainingOptions.setFont(new Font("����", Font.BOLD, 14));
		GridBagConstraints gbc_lblTrainingOptions = new GridBagConstraints();
		gbc_lblTrainingOptions.insets = new Insets(0, 0, 5, 0);
		gbc_lblTrainingOptions.gridwidth = 2;
		gbc_lblTrainingOptions.gridx = 0;
		gbc_lblTrainingOptions.gridy = 0;
		Training_GUI.add(lblTrainingOptions, gbc_lblTrainingOptions);
		
		JLabel lblNewLabel = new JLabel("Learning Rate");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.weightx = 1.0;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		Training_GUI.add(lblNewLabel, gbc_lblNewLabel);
		
		learning_rate = new JTextField();
		learning_rate.setPreferredSize(new Dimension(4, 21));
		learning_rate.setText("0.1");
		GridBagConstraints gbc_learning_rate = new GridBagConstraints();
		gbc_learning_rate.weightx = 1.0;
		gbc_learning_rate.insets = new Insets(0, 0, 5, 0);
		gbc_learning_rate.fill = GridBagConstraints.HORIZONTAL;
		gbc_learning_rate.gridx = 1;
		gbc_learning_rate.gridy = 1;
		Training_GUI.add(learning_rate, gbc_learning_rate);
		learning_rate.setColumns(3);
		
		JLabel lblDecayRate = new JLabel("Decay Rate");
		GridBagConstraints gbc_lblDecayRate = new GridBagConstraints();
		gbc_lblDecayRate.weightx = 1.0;
		gbc_lblDecayRate.insets = new Insets(0, 0, 5, 5);
		gbc_lblDecayRate.gridx = 0;
		gbc_lblDecayRate.gridy = 2;
		Training_GUI.add(lblDecayRate, gbc_lblDecayRate);
		
		decay_rate = new JTextField();
		decay_rate.setText("1");
		GridBagConstraints gbc_decay_rate = new GridBagConstraints();
		gbc_decay_rate.weightx = 1.0;
		gbc_decay_rate.insets = new Insets(0, 0, 5, 0);
		gbc_decay_rate.fill = GridBagConstraints.HORIZONTAL;
		gbc_decay_rate.gridx = 1;
		gbc_decay_rate.gridy = 2;
		Training_GUI.add(decay_rate, gbc_decay_rate);
		decay_rate.setColumns(3);
		
		JLabel lblStoppingCriteria = new JLabel("Stopping criteria");
		lblStoppingCriteria.setFont(new Font("����", Font.BOLD, 14));
		GridBagConstraints gbc_lblStoppingCriteria = new GridBagConstraints();
		gbc_lblStoppingCriteria.insets = new Insets(0, 0, 5, 0);
		gbc_lblStoppingCriteria.gridwidth = 2;
		gbc_lblStoppingCriteria.gridx = 0;
		gbc_lblStoppingCriteria.gridy = 3;
		Training_GUI.add(lblStoppingCriteria, gbc_lblStoppingCriteria);
		
		JLabel lblMaximumIter = new JLabel("Maximum Iter");
		GridBagConstraints gbc_lblMaximumIter = new GridBagConstraints();
		gbc_lblMaximumIter.weightx = 1.0;
		gbc_lblMaximumIter.insets = new Insets(0, 0, 5, 5);
		gbc_lblMaximumIter.gridx = 0;
		gbc_lblMaximumIter.gridy = 4;
		Training_GUI.add(lblMaximumIter, gbc_lblMaximumIter);
		
		JSpinner spinner_2 = new JSpinner();
		snm1 = new SpinnerNumberModel(new Integer(1024), new Integer(256), null, new Integer(256));
		spinner_2.setModel(snm1);
		GridBagConstraints gbc_spinner_2 = new GridBagConstraints();
		gbc_spinner_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner_2.weightx = 1.0;
		gbc_spinner_2.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_2.gridx = 1;
		gbc_spinner_2.gridy = 4;
		Training_GUI.add(spinner_2, gbc_spinner_2);
		
		JLabel lblErrSumthreshold = new JLabel("Err Threshold");
		GridBagConstraints gbc_lblErrSumthreshold = new GridBagConstraints();
		gbc_lblErrSumthreshold.weightx = 1.0;
		gbc_lblErrSumthreshold.insets = new Insets(0, 0, 5, 5);
		gbc_lblErrSumthreshold.gridx = 0;
		gbc_lblErrSumthreshold.gridy = 5;
		Training_GUI.add(lblErrSumthreshold, gbc_lblErrSumthreshold);
		
		err_threshold = new JTextField();
		err_threshold.setText("0.01");
		GridBagConstraints gbc_err_threshold = new GridBagConstraints();
		gbc_err_threshold.fill = GridBagConstraints.HORIZONTAL;
		gbc_err_threshold.weightx = 1.0;
		gbc_err_threshold.insets = new Insets(0, 0, 5, 0);
		gbc_err_threshold.gridx = 1;
		gbc_err_threshold.gridy = 5;
		Training_GUI.add(err_threshold, gbc_err_threshold);
		err_threshold.setColumns(3);
		
		JButton btnStartTraining = new JButton("Start Training!");
		btnStartTraining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Configuration.learning_rate = Double.parseDouble(	learning_rate.getText());
				Configuration.err_threshlod = Double.parseDouble( err_threshold.getText());

				Configuration.learn_dekay = Double.parseDouble( decay_rate.getText());
				Configuration.num_hid_units = (Integer) snm2.getValue();
				Configuration.max_epoch_num = (Integer) snm1.getValue();
				System.out.println(Configuration.num_hid_units);
				sbff.init();
				Thread t = new Thread(sbff);
				t.start();
				
				System.out.println(Configuration.learning_rate);
				if(sbff.validate(Configuration.err_threshlod)) textarea.append("success\n");
				else textarea.append("Failed. plz try again\n");
			}
		});
		GridBagConstraints gbc_btnStartTraining = new GridBagConstraints();
		gbc_btnStartTraining.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStartTraining.gridwidth = 2;
		gbc_btnStartTraining.gridx = 0;
		gbc_btnStartTraining.gridy = 6;
		Training_GUI.add(btnStartTraining, gbc_btnStartTraining);
		
		JPanel Test_Pannel = new JPanel();
		Test_Pannel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, SystemColor.controlLtHighlight, null));
		Test_Pannel.setBackground(new Color(245, 255, 250));
		GridBagConstraints gbc_Test_Pannel = new GridBagConstraints();
		gbc_Test_Pannel.weightx = 1.0;
		gbc_Test_Pannel.gridwidth = 2;
		gbc_Test_Pannel.insets = new Insets(0, 0, 0, 5);
		gbc_Test_Pannel.fill = GridBagConstraints.BOTH;
		gbc_Test_Pannel.gridx = 2;
		gbc_Test_Pannel.gridy = 0;
		Main_GUI.add(Test_Pannel, gbc_Test_Pannel);
		GridBagLayout gbl_Test_Pannel = new GridBagLayout();
		gbl_Test_Pannel.columnWidths = new int[] {0, 0, 0, 0};
		gbl_Test_Pannel.rowHeights = new int[] {20, 70, 20};
		gbl_Test_Pannel.columnWeights = new double[]{1,1,1,1};
		gbl_Test_Pannel.rowWeights = new double[]{0.0, 0.0, 0.0};
		Test_Pannel.setLayout(gbl_Test_Pannel);
		
		JLabel lblTest = new JLabel("Test!");
		lblTest.setHorizontalAlignment(SwingConstants.CENTER);
		lblTest.setFont(new Font("����", Font.BOLD, 14));
		GridBagConstraints gbc_lblTest = new GridBagConstraints();
		gbc_lblTest.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTest.gridwidth = 4;
		gbc_lblTest.anchor = GridBagConstraints.NORTH;
		gbc_lblTest.insets = new Insets(0, 0, 5, 0);
		gbc_lblTest.gridx = 0;
		gbc_lblTest.gridy = 0;
		Test_Pannel.add(lblTest, gbc_lblTest);
		
		JPanel Test_Paint_GUI = new JPanel();
		GridBagConstraints gbc_Test_Paint_GUI = new GridBagConstraints();
		gbc_Test_Paint_GUI.gridwidth = 4;
		gbc_Test_Paint_GUI.fill = GridBagConstraints.BOTH;
		gbc_Test_Paint_GUI.insets = new Insets(0, 0, 5, 0);
		gbc_Test_Paint_GUI.gridx = 0;
		gbc_Test_Paint_GUI.gridy = 1;
		Test_Pannel.add(Test_Paint_GUI, gbc_Test_Paint_GUI);
		GridBagLayout gbl_Test_Paint_GUI = new GridBagLayout();
		gbl_Test_Paint_GUI.columnWidths = new int[] {0, 0, 100, 0, 0};
		gbl_Test_Paint_GUI.rowHeights = new int[] {20, 100, 20};
		gbl_Test_Paint_GUI.columnWeights = new double[]{1.0, 1.0, 0.0, 1.0, 1.0};
		gbl_Test_Paint_GUI.rowWeights = new double[]{0.0, 0.0, 0.0};
		Test_Paint_GUI.setLayout(gbl_Test_Paint_GUI);
		
		JButton btnInitialize = new JButton("Initialize!");

		GridBagConstraints gbc_btnInitialize = new GridBagConstraints();
		gbc_btnInitialize.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnInitialize.insets = new Insets(0, 0, 5, 5);
		gbc_btnInitialize.gridx = 2;
		gbc_btnInitialize.gridy = 0;
		Test_Paint_GUI.add(btnInitialize, gbc_btnInitialize);
		
		Paint_GUI = new New_painter();
		btnInitialize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Training_GUI.this.Paint_GUI.init();
			}
		});
		Paint_GUI.setPreferredSize(new Dimension(100, 100));
		Paint_GUI.setSize(new Dimension(100, 100));
		Paint_GUI.setBackground(new Color(255, 255, 255));
		FlowLayout flowLayout = (FlowLayout) Paint_GUI.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		GridBagConstraints gbc_Paint_GUI = new GridBagConstraints();
		gbc_Paint_GUI.insets = new Insets(0, 0, 5, 5);
		gbc_Paint_GUI.fill = GridBagConstraints.BOTH;
		gbc_Paint_GUI.gridx = 2;
		gbc_Paint_GUI.gridy = 1;
		Test_Paint_GUI.add(Paint_GUI, gbc_Paint_GUI);
		
		JButton btnExtractVec = new JButton("Extract Vec");
		btnExtractVec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int[] vec = Paint_GUI.extract_vec();
				String buf ="";
				
				for(int i=0; i< vec.length; i++ )
					buf += vec[i] + "\t";
				
				buf += "\n";
				textarea.append(buf);
			}
		});
		GridBagConstraints gbc_btnExtractVec = new GridBagConstraints();
		gbc_btnExtractVec.insets = new Insets(0, 0, 0, 5);
		gbc_btnExtractVec.gridx = 2;
		gbc_btnExtractVec.gridy = 2;
		Test_Paint_GUI.add(btnExtractVec, gbc_btnExtractVec);
		
		JPanel Test_Bot = new JPanel();
		GridBagConstraints gbc_Test_Bot = new GridBagConstraints();
		gbc_Test_Bot.weightx = 1.0;
		gbc_Test_Bot.gridwidth = 4;
		gbc_Test_Bot.anchor = GridBagConstraints.NORTH;
		gbc_Test_Bot.fill = GridBagConstraints.HORIZONTAL;
		gbc_Test_Bot.gridx = 0;
		gbc_Test_Bot.gridy = 2;
		Test_Pannel.add(Test_Bot, gbc_Test_Bot);
		
		JButton btnTestThis = new JButton("Test this!");
		btnTestThis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				Data data = new Data( metadata, Paint_GUI.extract_vec());
				mlp.forward_computation(data);
				
				
				double[] result = mlp.get_output_vec();
				DecimalFormat df = new DecimalFormat(".####");
				
				for(int i=0; i< result.length; i++)
					textarea.append("["+ i+"]: "+ df.format(mlp.get_output_vec()[i]) + '\t');
				
				textarea.append("\n");
				
				int guess = Mathematical.get_max_index(result);
				guess_field.setText("My Guess is " + guess);
				
				
			}
		});
		Test_Bot.add(btnTestThis);
		
		guess_field = new JTextField();
		guess_field.setBackground(Color.WHITE);
		guess_field.setEditable(false);
		Test_Bot.add(guess_field);
		guess_field.setColumns(8);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane((Component) null);
		panel_1.add(scrollPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(2, 80));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		textarea = new JTextArea();
		textarea.setMinimumSize(new Dimension(500, 100));
		scrollPane.setViewportView(textarea);
	
		
		
		
		//usercode
		
		init();
		start();	
	}
	
	private void init()
	{
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Training_GUI();
	}

}
