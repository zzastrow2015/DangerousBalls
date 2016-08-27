package com.example.testgame;

import android.graphics.RectF;
import android.util.DisplayMetrics;

public class RedBall {
	
	private float left, top, right, bottom;
	private float velX, velY;
	private RectF ballBounds;
	float height, width;
	
	public RedBall(float left, float top, float right, float bottom){
		
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		this.ballBounds = new RectF();
		
	}
	
	public float getVelX(){
		return this.velX;
	}
	
	public void setVelX(float x){
		this.velX = x;
	}
	
	public float getVelY(){
		return this.velY;
	}
	
	public void setVelY(float y){
		this.velY = y;
	}
	
	public float getLeft(){
		return left;
	}
	
	public float getTop(){
		return top;
	}
	
	public float getRight(){
		return right;
	}
	
	public float getBottom(){
		return bottom;
	}
	
	public RectF updateBall(float maxW, float maxH){
		
		this.left += this.getVelX();
		this.right += this.getVelX();
		this.top += this.getVelY();
		this.bottom += this.getVelY();

		width = maxW;
		height = maxH;

		if((right >= width) || (left <= 0)){
			this.setVelX(getVelX() * -1);
		}
		
		if((bottom >= height) || (top <= 0)){
			this.setVelY(getVelY() * -1);
		}
		
		
		this.ballBounds.set(left, top, right, bottom);
		
		return this.ballBounds;
	}
	

}
