package com.cvezga.surveillance.camera;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Camera {
	
	private List<Cell> cellList;
	
	private int cellWith;
	private int cellHeight;
	
	private boolean activated;
	private BufferedImage bimage;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS a");
	
	public Camera(int width, int height, int cellWidth, int cellHeight){
		
		this.cellWith = cellWidth;
		this.cellHeight = cellHeight;
		
		this.bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		this.cellList = new ArrayList<Cell>();
		int nx = width / cellWidth;
		int ny = height / cellHeight;
		for(int x=0; x<nx; x++){
			for(int y=0; y<ny; y++){
				Cell cell = new Cell(x*cellWidth,y*cellHeight,cellWidth,cellHeight);
				this.cellList.add(cell);
			}
		}
		
	}
	
	
	
	public BufferedImage updateImage(BufferedImage image){
		
		updateCells(image);
		
		
		return this.bimage;
	}



	private void updateCells(BufferedImage image) {
		this.activated=false;
		
		Graphics g = this.bimage.getGraphics();
		
		g.drawImage(image, 0, 0, null);
		
		g.setColor(Color.WHITE);
		g.drawString(sdf.format(new Date()), 5, 20); 
		for(Cell cell : this.cellList){
			int[] pixelValues = getPixelValues(image, cell);
			
			cell.setPixels(pixelValues);
			if(cell.isActive()){
				System.out.println("Cell "+cell.getX()+"-"+cell.getY()+" activated");
				this.activated=true;
				g.setColor(Color.GREEN);
				g.drawRect(cell.getX(), cell.getY(), this.cellWith,  this.cellHeight);
			}
		}
		
		g.dispose();
		
	}
	
	private int[] getPixelValues(BufferedImage image, Cell cell){
		int[] values = new int[this.cellWith*this.cellHeight];
		
		
		image.getRGB(cell.getX(),cell.getY(),this.cellWith, this.cellHeight, values, 0, this.cellWith);
		
		return values;
	}



	public boolean isActivated() {
		return this.activated;
	}

}
