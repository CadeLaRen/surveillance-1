package com.cvezga.surveillance.camera;

public class Cell {
	
	private static final float DIFF_VALUE = 0.15f * 255;
	
	
	private int x;
	private int y;
	
	private int w;
	private int h;
	
	
	private int[] pixels;
	
	private boolean active;
	
	private int count=0;
	
	private int activateCount;
	
	
	public Cell(int x, int y, int cellWidth, int cellHeight) {
		this.x=x;
		this.y=y;
		this.w=cellWidth;
		this.h=cellHeight;
		this.activateCount=(int) (w*h*0.01F);
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
		this.count=0;
		
		int scanFactor = (int) (pixelValues.length * 0.25f);
		
		int scanIncrement = pixelValues.length / scanFactor;
		
		for(int i=0; i<pixelValues.length; i=i+scanIncrement){
			int r1 = getRed(this.pixels[i]);
			int g1 = getGreen(this.pixels[i]);
			int b1 = getBlue(this.pixels[i]);
			
			int r2 = getRed(pixelValues[i]);
			int g2 = getGreen(pixelValues[i]);
			int b2 = getBlue(pixelValues[i]);
			
			int difr = Math.abs(r2-r1);
			int difg = Math.abs(g2-g1);
			int difb = Math.abs(b2-b1);
			
			this.active=false;
			if(difr>DIFF_VALUE ||
			   difg>DIFF_VALUE ||
			   difb>DIFF_VALUE) {
				count++;
			}
			
			
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
		return this.count>this.activateCount;
	}




}
