package com.gui;


/*
 * http://mobssiie.tistory.com/entry/%EC%9E%90%EB%B0%94-%EC%9E%91%EC%9D%80-%EA%B7%B8%EB%A6%BC%ED%8C%90AWT
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;


class Grid_cell {
	
	public int x, y, w, h;
	boolean selected;
	
	public Grid_cell(int lx, int ly, int w, int h) {
		this.x = lx;
		this.y = ly;
		this.w = w;
		this.h = h;
		
		selected=false;
	}
	
	public boolean get_selected () { return selected;}
	public void change_selected () { selected = true;}
}


class New_painter extends JPanel implements MouseMotionListener

{

	Grid_cell current_cell;
	
	private int x1;
	private int y1;
	private Grid_cell[][] cells;
	private int[] vec;
	//
	public New_painter(){
		setBackground(new Color(255, 255, 255));

		this.start();
		this.init();
		vec = new int[100];		

	}
	
	
	public void init(){

		int dw = getWidth()/10;
		int dh = getHeight()/10;

		cells = new Grid_cell[10][10];
		for(int i = 0; i<cells.length; i++)
			for(int j=0; j<cells[i].length; j++)
				cells[i][j] = new Grid_cell(j*dw, i*dh , dw, dh );
		
		repaint();
		
	}		
	
	public int[] extract_vec()
	{
		for(int i = 0; i<cells.length; i++)
			for(int j=0; j<cells[i].length; j++)
				vec[i*10 + j] = cells[i][j].selected ? 1:0;
		
		return vec.clone();
	}
	
	//
	public void start(){
		this.addMouseMotionListener(this);
					
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintComponent(g);
	}
	@Override
	protected void paintComponent(Graphics g) {
	
		super.paintComponents(g);
		
		for(int i = 0; i<cells.length; i++)
			for(int j=0; j<cells[i].length; j++)
			{
				Grid_cell buf = cells[i][j];
				if(!buf.selected)
				{
					g.setColor(Color.WHITE);
					g.fillRect(buf.x, buf.y, buf.w, buf.h);
				}
				
				else
				{

					g.setColor(Color.BLACK);
					g.fillRect(buf.x, buf.y, buf.w, buf.h);
				}
				
			}

		
		
	}

	
	
	public void mouseEntered(MouseEvent e){
		
	}
	
	public void mouseExited(MouseEvent e){
		
	}
	
	public void mouseMoved(MouseEvent e){
		
	}
	
	/*
	 * 마우스를 대르그할 때에는 움직이는 지점까지의 그림이 그때 그때 표현되어야 하기 때문에
	 * 해당 그림을 그려준다.
	 */
	public void mouseDragged(MouseEvent e){
		x1 = e.getX();
		y1 = e.getY();
		
		Grid_cell buf = trace_cell(x1, y1);
		if(current_cell == null)
		{
			current_cell = buf;
			current_cell.change_selected();
		}
		else if(current_cell !=buf)
		{
			current_cell = buf;
			current_cell.change_selected();
			
		}
		this.repaint();
	}

	private Grid_cell trace_cell (int x, int y)
	{

		for(int i = 0; i<cells.length; i++)
			for(int j=0; j<cells[i].length; j++)
			{
				Grid_cell buf = cells[i][j];
				if(buf.x <= x && x < buf.x+buf.w )
					if(buf.y <= y && y < buf.y+buf.h )
						return buf;
			}
		return null;
		
	}
	
}

