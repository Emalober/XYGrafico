package ar.com.bbva.xygrafico.widget;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;

public class TooltipWidget {

	private Paint labelPaint1 = new Paint();
	private Paint labelPaint2 = new Paint();
	private String primaryText;
	private String secondaryText;

	private boolean hidden = true;
	
	private float paddingTop;
	private float paddingRight;
	private float paddingLeft;
	private float paddingBottom;
	
	private Drawable background;
	
	private float width;
	private float height;
	
	private Rect rectBackgroundPadding;
	private RectF rectFBackground;
	
	
	public TooltipWidget(Drawable background) {
		init();
		this.background = background;
	}
	
	@SuppressLint("WrongCall")
	public TooltipWidget(String text1, String text2, Drawable background) {
		init();
		this.background = background;
		this.primaryText = text1;
		this.secondaryText = text2;
		
		measure();
	}

	protected void measure() {
		
		Rect bounds1 = new Rect();
		labelPaint1.getTextBounds(primaryText, 0, primaryText.length(), bounds1);
		
		Rect bounds2 = new Rect();
		labelPaint2.getTextBounds(secondaryText, 0, secondaryText.length(), bounds2);
		
		width = Math.max(bounds1.right - bounds1.left, bounds2.right - bounds2.left);
		height = bounds1.bottom - bounds1.top + bounds2.bottom - bounds2.top;
		
	}
	
	public void init() {
		labelPaint1.setAntiAlias(true);		
		labelPaint1.setStyle(Paint.Style.FILL);
		labelPaint1.setTextAlign(Paint.Align.LEFT);
		labelPaint1.setStrokeWidth(5);
		labelPaint1.setTypeface(Typeface.DEFAULT_BOLD);

		labelPaint2.setAntiAlias(true);		
		labelPaint2.setStyle(Paint.Style.FILL);
		labelPaint2.setTextAlign(Paint.Align.LEFT);
		labelPaint2.setStrokeWidth(5);
		labelPaint2.setTypeface(Typeface.DEFAULT_BOLD);

		rectBackgroundPadding = new Rect();
		rectFBackground = new RectF(rectBackgroundPadding);
		paddingTop = 10;
		paddingRight = 10;
		paddingLeft = 10;
		paddingBottom = 10;
	}
	


	/**
	 * 
	 * @param canvas
	 * @param x
	 * @param y
	 */
	public void draw(Canvas canvas, float x, float y, int offsetRadius) {
		
		if(hidden)
			return;
		
		background.getPadding(rectBackgroundPadding);
		
		float pixY =  y - this.getHeight() - offsetRadius - rectBackgroundPadding.bottom;
		float pixX =  x;

		
		Rect bounds1 = new Rect();
		labelPaint1.getTextBounds(primaryText, 0, primaryText.length(), bounds1);
		
		Rect bounds2 = new Rect();
		labelPaint2.getTextBounds(secondaryText, 0, secondaryText.length(), bounds2);
		
		float label1pixY =  pixY;
		float label1pixX =  pixX - this.getWidth() /2;
		
		background.setBounds((int)(pixX - this.getWidth() /2) - rectBackgroundPadding.left, 
				(int)pixY - rectBackgroundPadding.top,
				(int)(pixX + this.getWidth()/2) + rectBackgroundPadding.right, 
				(int)(pixY + this.getHeight()) + rectBackgroundPadding.bottom);

		background.draw(canvas);

		canvas.drawText(this.primaryText, label1pixX, label1pixY - bounds1.top, labelPaint1);
		label1pixY += bounds1.bottom - bounds1.top;
		canvas.drawText(this.secondaryText, label1pixX, label1pixY - bounds2.top, labelPaint2);

	}


	private void applyAlign(float x, float y, RectF rectF) {
		float amountX = 0;
		if (labelPaint1.getTextAlign().equals(Align.CENTER)) {
			amountX = x - (width /2);
		} else if (labelPaint1.getTextAlign().equals(Align.LEFT)) {
			amountX = x;
		} else if (labelPaint1.getTextAlign().equals(Align.RIGHT)) {
			amountX = x - width;
		}
		rectF.offset(amountX, y);
	}
	

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		//this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		//this.height = height;
	}
	
	public void setColor(int color) {
		labelPaint1.setColor(color);
	}
	
	public void setTextAlign(Align align){
		labelPaint1.setTextAlign(align);
	}
				
	public void setTextSize(float textSize) {
		this.labelPaint1.setTextSize(textSize);
	}

	public void setDrawable(Drawable drawable) {
		background = drawable;
	}
	
	public void setStylePrimaryText(int color, float size) {
		labelPaint1.setTextSize(size);
		labelPaint1.setColor(color);
		measure();
	}
	
	public void setStyleSecondText(int color, float size) {
		labelPaint2.setTextSize(size);
		labelPaint2.setColor(color);
		measure();
	}
	
	public void show() {
		hidden = false;
	}
	
	public void hide() {
		hidden = false;
	}
	public void setTexts(String text1, String text2) {
		primaryText = text1;
		secondaryText = text2;
		
		measure();
	}
}
