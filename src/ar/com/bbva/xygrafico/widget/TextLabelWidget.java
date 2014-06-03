package ar.com.bbva.xygrafico.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Typeface;


public class TextLabelWidget {

	private Paint labelPaintBackground = new Paint();
	private Paint labelPaint = new Paint();
	private Paint borderPaint = new Paint();
	private String text;
	
	private float paddingTop;
	private float paddingRight;
	private float paddingLeft;
	private float paddingBottom;
	
	private float width;
	private float height;
	
	private Rect rectBackground;
	private RectF rectFBackground;
	
	
	public TextLabelWidget() {
		init();
	}
	
	public TextLabelWidget(String text) {
		init();
		this.text = text;
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
		paddingTop = 4;
		paddingRight = 4;
		paddingLeft = 4;
		paddingBottom = 4;
	}
	


	/**
	 * 
	 * @param canvas
	 * @param x
	 * @param y
	 */
	public void draw(Canvas canvas, float x, float y) {
		
		RectF rectF = includePaddingInBackground();
		
		applyAlign(x, y, rectF);
		
		canvas.drawRect(rectF, labelPaintBackground);
		canvas.drawText(text, x, y, labelPaint);
		canvas.drawRect(rectF, borderPaint);
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

	private RectF includePaddingInBackground() {
		RectF rectF = null;
		labelPaint.getTextBounds(text, 0, text.length(), rectBackground);
		rectFBackground.set(rectBackground);
		

		rectF = new RectF(0 - paddingLeft ,  
						rectFBackground.top - paddingTop, 
						this.width + paddingRight, 
						rectFBackground.bottom + paddingBottom);
		
		return rectF;
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
			
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public void setTextSize(float textSize) {
		this.labelPaint.setTextSize(textSize);
	}
	
	
}
