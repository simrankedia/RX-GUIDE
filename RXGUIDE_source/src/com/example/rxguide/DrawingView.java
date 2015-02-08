package com.example.rxguide;

import android.content.Context;
import android.view.View;
import android.util.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;


public class DrawingView extends View {
	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial 
	private int paintColor = 0xFF000000;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;
	
	private boolean erase=false;
	private float brushSize, lastBrushSize;

	public DrawingView(Context context,AttributeSet attrs) {
		super(context,attrs);
		setupDrawing();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	//view given size
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
	//draw view
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	//detect user touch
		float touchX = event.getX();
		float touchY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		    drawPath.moveTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_MOVE:
		    drawPath.lineTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_UP:
		    drawCanvas.drawPath(drawPath, drawPaint);
		    drawPath.reset();
		    break;
		default:
		    return false;
		}
		invalidate();
		return true;
	}
	
	public void setErase(boolean isErase){
		//set erase true or false
		erase=isErase;
		if(erase){
			
			drawPaint.setColor(0xffFFFFFF);
			drawPaint.setAntiAlias(true);
			drawPaint.setStrokeWidth(brushSize);
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setStrokeJoin(Paint.Join.ROUND);
			drawPaint.setStrokeCap(Paint.Cap.ROUND);
			canvasPaint = new Paint(Paint.DITHER_FLAG);
			
			
		}
			
		else {
			drawPaint.setColor(0xFF000000);
			drawPaint.setAntiAlias(true);
			drawPaint.setStrokeWidth(brushSize);
			drawPaint.setStyle(Paint.Style.STROKE);
			drawPaint.setStrokeJoin(Paint.Join.ROUND);
			drawPaint.setStrokeCap(Paint.Cap.ROUND);
			canvasPaint = new Paint(Paint.DITHER_FLAG);
		}
		}
	
	private void setupDrawing(){
		//get drawing area setup for interaction
		brushSize = getResources().getInteger(R.integer.small_size);
		lastBrushSize = brushSize;
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(brushSize);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}
	
	public void setBrushSize(float newSize){
		//update size
		float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
			    newSize, getResources().getDisplayMetrics());
			brushSize=pixelAmount;
			drawPaint.setStrokeWidth(brushSize);
		}
	public void setLastBrushSize(float lastSize){
	    lastBrushSize=lastSize;
	}
	public float getLastBrushSize(){
	    return lastBrushSize;
	}
	//public void setColor(String newColor){
		//set 
		//invalidate();
		//paintColor = Color.parseColor(newColor);
		//drawPaint.setColor(paintColor);
	//	}


}
