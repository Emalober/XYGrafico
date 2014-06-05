package ar.com.bbva.xygrafico.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;

public class TooltipWidget {

	private Paint labelPaintBackground = new Paint();
	private Paint labelPaint = new Paint();
	private Paint borderPaint = new Paint();
	private String primaryText;
	private String secondaryText;
	
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
	
	public TooltipWidget(String text1, String text2, Drawable background) {
		init();
		this.background = background;
		this.primaryText = text1;
		this.secondaryText = text2;
		
		Rect bounds1 = new Rect();
		labelPaint.getTextBounds(text1, 0, text1.length(), bounds1);
		
		Rect bounds2 = new Rect();
		labelPaint.getTextBounds(text2, 0, text2.length(), bounds2);
		
		width = Math.max(bounds1.right - bounds1.left, bounds2.right - bounds2.left);
		height = bounds1.bottom - bounds1.top + bounds2.bottom - bounds2.top;
	}
	
	public void init() {
		labelPaint.setAntiAlias(true);		
		labelPaint.setStyle(Paint.Style.FILL);
		labelPaint.setTextAlign(Paint.Align.LEFT);
		labelPaint.setStrokeWidth(5);
		labelPaint.setTypeface(Typeface.DEFAULT_BOLD);
		
		labelPaintBackground.setAntiAlias(true);
		labelPaintBackground.setColor(Color.TRANSPARENT);
		
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setColor(Color.TRANSPARENT);
		
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
		
		float pixY =  y - this.getHeight() - offsetRadius - rectBackgroundPadding.bottom;
		float pixX =  x;
		
//		if(pixX > canvas.getWidth()/2)
//			pixX -= this.getWidth();
//		if(pixY <= 0)
//			pixY += this.getHeight() + 2*offsetRadius;
		
		float label1pixY =  pixY;
		float label1pixX =  pixX - this.getWidth() /2;
			
//		RectF rectF = new RectF(pixX - this.getWidth() /2, pixY , pixX + this.getWidth() /2, pixY + this.getHeight());
		background.getPadding(rectBackgroundPadding);
		
		background.setBounds((int)(pixX - this.getWidth() /2) - rectBackgroundPadding.left, 
				(int)pixY - rectBackgroundPadding.top,
				(int)(pixX + this.getWidth()/2) + rectBackgroundPadding.right, 
				(int)(pixY + this.getHeight()) + rectBackgroundPadding.bottom);

		background.draw(canvas);
		//canvas.drawRoundRect(rectF, 5, 5, labelPaintBackground);
		canvas.drawText(this.primaryText, label1pixX, label1pixY, labelPaint);
		
		canvas.drawText(this.secondaryText, label1pixX, label1pixY + this.getHeight()/2, labelPaint);
		//canvas.drawRoundRect(rectF, 5, 5, borderPaint);
	}
	

	private void applyAlign(float x, float y, RectF rectF) {
		float amountX = 0;
		if (labelPaint.getTextAlign().equals(Align.CENTER)) {
			amountX = x - (width /2);
		} else if (labelPaint.getTextAlign().equals(Align.LEFT)) {
			amountX = x;
		} else if (labelPaint.getTextAlign().equals(Align.RIGHT)) {
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

	public void setBorderColor(int color) {
		borderPaint.setColor(color);
	}
	
	public void setColor(int color) {
		labelPaint.setColor(color);
	}
	
	public void setBackgroundColor(int color) {
		labelPaintBackground.setColor(color);
	}
	
	public void setTextAlign(Align align){
		labelPaint.setTextAlign(align);
	}
				
	public void setTextSize(float textSize) {
		this.labelPaint.setTextSize(textSize);
	}

	public void setDrawable(Drawable drawable) {
		background = drawable;
		
	}
	
}
