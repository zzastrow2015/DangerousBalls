package com.example.testgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class StartMenu extends View{
	
	private Paint paint;
	private Rect textBounds1;

	public StartMenu(Context context) {
		super(context);
		
		paint = new Paint();
		textBounds1 = new Rect();
	}
	
	
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		String option1 = "Press anywhere to play!";
		
		paint.getTextBounds(option1, 0, option1.length(), textBounds1);
		
		
		int width = (canvas.getWidth() / 2) - (Math.abs(textBounds1.width()) / 2);
		int height = (int) ((canvas.getHeight() / 2) - (((paint.descent() + paint.ascent()) / 2)) - (Math.abs(textBounds1.height()) / 2));
		
		paint.setColor(Color.BLUE);
		paint.setTextSize(100);
		canvas.drawText(option1, width, height, paint);
		
		
		invalidate();
		
	}
	
	public boolean onTouchEvent(MotionEvent event){
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			Context context = getContext();
			Intent i = new Intent(context, ViewAnimation.class);
			context.startActivity(i);
			
		}
		
		return false;
	}


}
