package com.cvezga.surveillance.camera;

public class Cell {
	
	private int x;
	private int y;

	private int[] pixels;

	private boolean active;
	private int w;
	private int h;
	
	
	public Cell(int x, int y, int cellWidth, int cellHeight) {
		this.x=x;
		this.y=y;
		this.w=cellWidth;
		this.h=cellHeight;
	}

	
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}



	public void setPixels(int[] pixelValues) {
		
		if(this.pixels!=null){
		  checkIfActive(pixelValues);
		}
		
		this.pixels = pixelValues;
		
	}



	private void checkIfActive(int[] pixelValues) {
		
		for(int i=0; i<pixelValues.length; i++){
			int r1 = getRed(this.pixels[i]);
			int g1 = getGreen(this.pixels[i]);
			int b1 = getBlue(this.pixels[i]);
			
			int r2 = getRed(pixelValues[i]);
			int g2 = getGreen(pixelValues[i]);
			int b2 = getBlue(pixelValues[i]);
			
			int difr = r2-r1;
			int difg = g2-g1;
			int difb = b2-b1;
			
			this.active=false;
			if(difr>50) this.active = true;
			if(difg>50) this.active = true;
			if(difb>50) this.active = true;
			
			
		}
		
	}



	private int getRed(int pixelValue) {
		return (pixelValue >> 16) & 0xFF ;
	}

	private int getBlue(int pixelValue) {
		return  (pixelValue >> 8) & 0xFF ;
	}
	
	private int getGreen(int pixelValue) {
		return  pixelValue & 0xFF ;
	}
	
	public boolean isActive() {
		return active;
	}




}
