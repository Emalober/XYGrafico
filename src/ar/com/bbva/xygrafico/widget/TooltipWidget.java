package ar.com.bbva.xygrafico.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;

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
	
	private float width;
	private float height;
	
	private Rect rectBackground;
	private RectF rectFBackground;
	
	
	public TooltipWidget() {
		init();
	}
	
	public TooltipWidget(String text1, String text2) {
		init();
		this.primaryText = text1;
		this.secondaryText = text2;
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
		
		rectBackground = new Rect();
		rectFBackground = new RectF(rectBackground);
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
		
		float pixY =  y - this.getHeight() - offsetRadius;
		float pixX =  x;
		
		if(pixX > canvas.getWidth()/2)
			pixX -= this.getWidth();
		if(pixY <= 0)
			pixY += this.getHeight() + 2*offsetRadius;
		
		float label1pixY =  pixY + this.paddingTop;
		float label1pixX =  pixX + this.paddingLeft;
			
		RectF rectF = new RectF(pixX, pixY , pixX + this.getWidth(), pixY + this.getHeight());

		canvas.drawRoundRect(rectF, 5, 5, labelPaintBackground);
		canvas.drawText(this.primaryText, label1pixX, label1pixY, labelPaint);
		canvas.drawText(this.primaryText, label1pixX, label1pixY, labelPaint);
		canvas.drawRoundRect(rectF, 5, 5, borderPaint);
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
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
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
	
}
