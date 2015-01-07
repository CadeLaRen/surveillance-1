package com.cvezga.surveillance.camera;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Camera {
	
	private List<Cell> cellList;
	
	private int cellWith;
	private int cellHeight;
	
	private boolean activated;
	
	public Camera(int with, int height, int cellWidth, int cellHeight){
		
		this.cellWith = cellWidth;
		this.cellHeight = cellHeight;
		
		this.cellList = new ArrayList<Cell>();
		int nx = with / cellWidth;
		int ny = height / cellHeight;
		for(int x=0; x<nx; x++){
			for(int y=0; y<ny; y++){
				Cell cell = new Cell(x*cellWidth,y*cellHeight,cellWidth,cellHeight);
				this.cellList.add(cell);
			}
		}
		
	}
	
	
	
	public void updateImage(BufferedImage image){
		
		updateCells(image);
		
		
		
	}



	private void updateCells(BufferedImage image) {
		this.activated=false;
		
		for(Cell cell : this.cellList){
			int[] pixelValues = getPixelValues(image, cell);
			
			cell.setPixels(pixelValues);
			if(cell.isActive()){
				System.out.println("Cell "+cell.getX()+"-"+cell.getY()+" activated");
				this.activated=true;
			}
		}
		
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
