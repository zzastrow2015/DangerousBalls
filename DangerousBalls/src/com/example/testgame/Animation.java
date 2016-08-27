package com.example.testgame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.ActionMode.Callback;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("NewApi")
public class Animation extends View{
	
	private Paint paint;
	public float maxW, maxH;
	private RectF ballBounds1;
	private float left, top, right, bottom;
	private float velX, velY;
	private float accX, accY;
	private float distX, distY;
	private float prevX, prevY;
	private float startLeft , startTop;
	private float tempL, tempT, tempH;
	private float diffX, diffY;
	private float angle;
	private ArrayList<RedBall> redBall = new ArrayList<RedBall>();
	private int redBallLeft;
	private int redBallTop;
    boolean held = false;
    Random rn = new Random();
    int ballCount = 1;
    boolean collision = false;
    long beginTime = System.currentTimeMillis();

    
	public Animation(Context context) {
		super(context);
		
		final int width = getContext().getResources().getDisplayMetrics().widthPixels;
		final int height = getContext().getResources().getDisplayMetrics().heightPixels;
		
		paint = new Paint();
		ballBounds1 = new RectF();
		
		do{
			redBallLeft = rn.nextInt(width - 100) + 50;
			redBallTop = rn.nextInt(height - 100) + 50;
		}while(!(Math.abs(redBallLeft - left) > 100) && !(Math.abs(redBallTop - top) > 100));
		
		redBall.add(new RedBall(redBallLeft, redBallTop, redBallLeft + 50, redBallTop + 50));
	
		if(rn.nextBoolean()){
			redBall.get(0).setVelX(rn.nextFloat() * 6);
		}else{
			redBall.get(0).setVelX(rn.nextFloat() * -6);
		}
		
		if(rn.nextBoolean()){
			redBall.get(0).setVelY(rn.nextFloat() * 6);
		}else{
			redBall.get(0).setVelY(rn.nextFloat() * -6);
		}
		
		
			new Thread(new Runnable(){

				@Override
				public void run() {
					
					while((ballCount <= 20) && !collision){
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						do{
							redBallLeft = rn.nextInt(width - 110) + 60;
							redBallTop = rn.nextInt(height - 110) - 60;
						}while(!(Math.abs(redBallLeft - left) > 200) && !(Math.abs(redBallTop - top) > 200));
						
						redBall.add(new RedBall(redBallLeft, redBallTop, redBallLeft + 50, redBallTop + 50));
						
						if(rn.nextBoolean()){
							redBall.get(ballCount).setVelX(rn.nextFloat() * 6);
						}else{
							redBall.get(ballCount).setVelX(rn.nextFloat() * -6);
						}
						
						if(rn.nextBoolean()){
							redBall.get(ballCount).setVelY(rn.nextFloat() * 6);
						}else{
							redBall.get(ballCount).setVelY(rn.nextFloat() * -6);
						}
						
						ballCount++;
					}
					
					

					
				}
				
			}).start();

		
		
		left = 450;
		top = 250;
		right = 490;
		bottom = 290;
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		
		maxW = w;
		maxH = h;
		
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(!collision){
			update();
			
			paint.setColor(Color.RED);
			
			
			for(int x = 0; x < ballCount; x++){
				canvas.drawOval(redBall.get(x).updateBall(maxW, maxH), paint);
			}
		}
		

		
		ballBounds1.set(left, top, right, bottom);
		
		paint.setColor(Color.BLUE);

		canvas.drawOval(ballBounds1, paint);
		
		invalidate();
		
	}

	private void update(){
		
		
		if(Math.abs(distX) > Math.abs(distY)){		
			if(held){
				velX += accX;
				velY += accY * (distY / distX);
			}
			
			if(!held){
				prevX = velX;
				prevY = velY;
				
				velX -= accX;
				velY -= accY * (distY / distX);		
				
				if((prevX > 0 && velX < 0) || (prevX < 0 && velX > 0)){
					velX = 0;
					accX = 0;
				}
				
				if((prevY > 0 && velY < 0) || (prevY < 0 && velY > 0)){
					velY = 0;
					accY = 0;
				}
				
			}
		}else if(Math.abs(distY) > Math.abs(distX)){
			if(held){
				velX += accX * (distX / distY);
				velY += accY;
			}
			
			if(!held){
				prevX = velX;
				prevY = velY;
				
				velX -= accX * (distX / distY);
				velY -= accY;
				
				if((prevX > 0 && velX < 0) || (prevX < 0 && velX > 0)){
					velX = 0;
					accX = 0;
				}
				
				if((prevY > 0 && velY < 0) || (prevY < 0 && velY > 0)){
					velY = 0;
					accY = 0;
				}
			}
		}
		
			
			if(left > maxW){
				
				diffX = left - maxW;
				
				tempL = left - (startLeft - diffX);
				tempT = top - startTop;
				tempH = (float) Math.sqrt((tempL * tempL) + (tempT * tempT));				
				
				top -= tempT * (maxW / tempL);

				
				if(top < -40){
					angle = (float) Math.acos(tempT / tempH);
					left = startLeft - (float) ((startTop + 40) * Math.tan(angle));
					top = -40;
					startTop = top;
					startLeft = left;
				}else if(top > maxH){
					angle = (float) Math.acos(tempT / tempH);
					left = startLeft + (float) ((maxH - startTop) * Math.tan(angle));
					top = maxH;
					startTop = top;
					startLeft = left;
				}else if (velY < 0){
					angle = (float) Math.acos(tempL / tempH);
					top = startTop + (float) ((startLeft + 40) * Math.tan(angle));
					left = -40;
					startTop = top;
					startLeft = left;
				}else if(velY >= 0){
					angle = (float) Math.acos(tempL / tempH);
					top = startTop - (float) ((startLeft + 40) * Math.tan(angle));
					left = -40;
					startTop = top;
					startLeft = left;
				}
				
				
				
			}else if(left < -40){
				
				diffX = -40 - left;
				
				tempL = startLeft - (left - diffX);
				tempT = startTop - top;
				tempH = (float) Math.sqrt((tempL * tempL) + (tempT * tempT));
				
				top += tempT * (maxW / tempL);
				
				if(top < -40){
					angle = (float) Math.acos(tempT / tempH);
					left = startLeft - (float) ((startTop + 40) * Math.tan(angle));
					top = -40;
					startTop = top;
					startLeft = left;
				}else if(top > maxH){
					angle = (float) Math.acos(tempT / tempH);
					left = startLeft + (float) ((maxH - startTop) * Math.tan(angle));
					top = maxH;
					startTop = top;
					startLeft = left;
				}else if (velY < 0){
					angle = (float) Math.acos(tempL / tempH);
					top = startTop + (float) ((maxW - startLeft) * Math.tan(angle));
					left = maxW;
					startTop = top;
					startLeft = left;
				}else if(velY >= 0){
					angle = (float) Math.acos(tempL / tempH);
					top = startTop - (float) ((maxW - startLeft) * Math.tan(angle));
					left = maxW;
					startTop = top;
					startLeft = left;
				}
	
			}
			
			if(top > maxH){
				
				diffY = top - maxH;
				
				tempL = left - startLeft;
				tempT = top - (startTop - diffY);
				tempH = (float) Math.sqrt((tempL * tempL) + (tempT * tempT));				
				
				left -= tempL * (maxH / tempT);
				
				if(left < -40){
					angle = (float) Math.acos(tempL / tempH);
					top = startTop - (float) ((startLeft + 40) * Math.tan(angle));
					left = -40;
					startTop = top;
					startLeft = left;
				}else if(left > maxW){
					angle = (float) Math.acos(tempL / tempH);
					top = startTop + (float) ((maxW - startLeft) * Math.tan(angle));
					left = maxW;
					startTop = top;
					startLeft = left;
				}else if (velX < 0){
					angle = (float) Math.acos(tempT / tempH);
					left = startLeft + (float) ((startTop + 40) * Math.tan(angle));
					top = -40;
					startTop = top;
					startLeft = left;
				}else if(velX >= 0){
					angle = (float) Math.acos(tempT / tempH);
					left = startLeft - (float) ((startTop + 40) * Math.tan(angle));
					top = -40;
					startTop = top;
					startLeft = left;
				}

			}else if(top < -40){
				
				diffY = -40 - top;
				
				tempL = startLeft - left;
				tempT = startTop - (top - diffY);
				tempH = (float) Math.sqrt((tempL * tempL) + (tempT * tempT));				
				
				left += tempL * (maxH / tempT);
				
				if(left < -40){
					angle = (float) Math.acos(tempL / tempH);
					top = startTop - (float) ((startLeft + 40) * Math.tan(angle));
					left = -40;
					startTop = top;
					startLeft = left;
				}else if(left > maxW){
					angle = (float) Math.acos(tempL / tempH);
					top = startTop + (float) ((maxW - startLeft) * Math.tan(angle));
					left = maxW;
					startTop = top;
					startLeft = left;
				}else if (velX < 0){
					angle = (float) Math.acos(tempT / tempH);
					left = startLeft + (float) ((maxH - startTop) * Math.tan(angle));
					top = maxH;
					startTop = top;
					startLeft = left;
				}else if(velX >= 0){
					angle = (float) Math.acos(tempT / tempH);
					left = startLeft - (float) ((maxH - startTop) * Math.tan(angle));
					top = maxH;
					startTop = top;
					startLeft = left;
				}
				
			}

			left += velX;
			top += velY;
			
			right = left + 40;
			bottom = top + 40;	
			
			for(int x = 0; x < ballCount; x++){
				if(((left >= redBall.get(x).getLeft()) && (left <= redBall.get(x).getRight())) || ((right >= redBall.get(x).getLeft()) && (right <= redBall.get(x).getRight()))){
					if(((top >= redBall.get(x).getTop()) && (top <= redBall.get(x).getBottom())) || ((bottom >= redBall.get(x).getTop()) && (bottom <= redBall.get(x).getBottom()))){
						collision = true;
						long endTime = System.currentTimeMillis();
						long totalTime = endTime - beginTime;
						System.out.println(totalTime);
						
						Context context = getContext();
						Intent i = new Intent(context, ViewGameOver.class);

						i.putExtra("key", totalTime);
						
						context.startActivity(i);
						
					}
					
				}
			}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		
		if((event.getAction() == MotionEvent.ACTION_DOWN) && (velX == 0) && (velY == 0)){
			
			startLeft = left;
			startTop = top;

			distX = Math.abs(event.getX() - (left + 20));	
			distY = Math.abs(event.getY() - (top + 20));
			
			if(event.getX() >= (left + 20)){
				accX = (float) .05;
			}else{
				accX = (float) -.05;
			}
			
			if(event.getY() >= (top + 20)){
				accY = (float) .05;
			}else{
				accY = (float) -.05;
			}
			
			held = true;
			return true;
			
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			held = false;
			return true;
		}
		
		return false;
		
		
	}

}
