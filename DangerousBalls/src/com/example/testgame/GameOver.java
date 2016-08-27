package com.example.testgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

public class GameOver extends View{
	
	private Paint paint;
	private Rect textBounds;
	private Rect textBounds2;
	int done = 0;
	long time;
	
	public GameOver(Context context, long value) {
		super(context);
		paint = new Paint();
		textBounds = new Rect();
		textBounds2 = new Rect();
		time = value / 1000;
		
	}
	
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		String message = "Game Over :(";
		String message2 = String.valueOf(time) + " seconds";

		paint.getTextBounds(message, 0, message.length(), textBounds);
		paint.getTextBounds(message2, 0, message2.length(), textBounds2);
		
		int width = (canvas.getWidth() / 2) - (Math.abs(textBounds.width()) / 2);
		int width2 = (canvas.getWidth() / 2) - (Math.abs(textBounds2.width()) / 2);
		int height = (int) ((canvas.getHeight() / 2) - (((paint.descent() + paint.ascent()) / 2)) - (Math.abs(textBounds.height()) / 2));
		
		paint.setColor(Color.BLUE);
		paint.setTextSize(100);
		canvas.drawText(message, width, height, paint);
		canvas.drawText(message2, width2, height + 100, paint);
		

		newScreen();
		
		
		
		invalidate();
		
	}
	
	private void newScreen(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				
					try{
						Thread.sleep(4000);
					}catch(Exception e){						
				}
					
					done++;
			}
			
		}).start();
		
		if(done == 1){
			Context context = getContext();
			Intent i = new Intent(context, ViewStartMenu.class);
			context.startActivity(i);
			done++;
		}
		
	}
	
	

}
