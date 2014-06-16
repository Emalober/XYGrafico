package ar.com.bbva.xygrafico;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import ar.com.bbva.xygrafico.activity.R;
import ar.com.bbva.xygrafico.core.XYSerie;
import ar.com.bbva.xygrafico.widget.XYGraphWidget;





/**
 * TODO: add select point event
 * TODO: add tooltip
 * TODO: add frame
 * TODO: add more effect
 * TODO: user scroller
 * @author XA03550
 *
 */

public class XYPlot extends View implements OnTouchListener {

	public static final int TEXT_ALIGN_CENTER = 1;
	public static final int TEXT_ALIGN_LEFT   = 2;
	public static final int TEXT_ALIGN_RIGHT  = 3;
	
	private static final String DEBUG_TAG = "Gestures";
	
	
	private GestureDetectorCompat mDetector; 
	
	/** Meansurement of main canvas */
	private RectF canvasRect;
	
	/** Graph component*/
	private XYGraphWidget xyGraphWidget;
	
	/**Paint background of main canvas */
	private Paint backgroundPaint;
	
	/** Touch handler  */
	private TouchHandler touchHandler;
	
	private Drawable tooltip;

	/** List of pair ranges and domain values */
	private XYSerie<String,Number> xySerie;

	public XYPlot(Context context, AttributeSet attrs)	{
		super(context, attrs);
		init();
		initAttributes(context, attrs);
	}
	
	public XYPlot(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		initAttributes(context, attrs);
	}


	
	private void initAttributes(Context context, AttributeSet attrs) {

		TypedArray a = context.getTheme().obtainStyledAttributes(
		        attrs,
		        R.styleable.XYPlot,
		        0, 0);

		   try {
			   xyGraphWidget.getLabelRangeWidget().setColor(a.getColor(R.styleable.XYPlot_labelRangeColor, Color.BLACK));
			   xyGraphWidget.getLabelRangeWidget().setHeight(a.getDimension(R.styleable.XYPlot_labelRangeHeight, 50));
			   xyGraphWidget.getLabelRangeWidget().setWidth(a.getDimension(R.styleable.XYPlot_labelRangeWidth, 50));
			   xyGraphWidget.getLabelRangeWidget().setBackgroundColor(a.getColor(R.styleable.XYPlot_labelRangeBackgroundColor, Color.TRANSPARENT));
			   xyGraphWidget.getLabelRangeWidget().setBorderColor(a.getColor(R.styleable.XYPlot_labelRangeBorderColor, Color.TRANSPARENT));
			   xyGraphWidget.getLabelRangeWidget().setTextSize(a.getDimension(R.styleable.XYPlot_labelRangeSize, 9));
			   xyGraphWidget.getLabelRangeWidget().setTextAlign(convertToAlign(a.getInt(R.styleable.XYPlot_labelRangeTextAlign, TEXT_ALIGN_CENTER)));
			   xyGraphWidget.setCountRangeLines(a.getInteger(R.styleable.XYPlot_countRangeLines, 6));
			   
			   xyGraphWidget.getLabelDomainWidget().setColor(a.getColor(R.styleable.XYPlot_labelDomainColor, Color.BLACK));
			   xyGraphWidget.getLabelDomainWidget().setHeight(a.getDimension(R.styleable.XYPlot_labelDomainHeight, 50));
			   xyGraphWidget.getLabelDomainWidget().setWidth(a.getDimension(R.styleable.XYPlot_labelDomainWidth, 50));
			   xyGraphWidget.getLabelDomainWidget().setBackgroundColor(a.getColor(R.styleable.XYPlot_labelDomainBackgroundColor, Color.TRANSPARENT));
			   xyGraphWidget.getLabelDomainWidget().setBorderColor(a.getColor(R.styleable.XYPlot_labelDomainBorderColor, Color.TRANSPARENT));
			   xyGraphWidget.getLabelDomainWidget().setTextSize(a.getDimension(R.styleable.XYPlot_labelDomainSize, 9));
			   xyGraphWidget.getLabelDomainWidget().setTextAlign(convertToAlign(a.getInt(R.styleable.XYPlot_labelDomainTextAlign, TEXT_ALIGN_CENTER)));
			   xyGraphWidget.setCountDomainLines(a.getInteger(R.styleable.XYPlot_countDomainLines, 5));
			   
			   xyGraphWidget.getGridBackgroundPaint().setColor(a.getColor(R.styleable.XYPlot_gridBackgroundColor, Color.BLACK));
			   backgroundPaint.setColor(a.getColor(R.styleable.XYPlot_backgroundColor, Color.BLACK));
			   xyGraphWidget.getDomainLineTickPaint().setColor(a.getColor(R.styleable.XYPlot_lineDomainColor, Color.WHITE));
			   xyGraphWidget.getRangeLineTickPaint().setColor(a.getColor(R.styleable.XYPlot_lineRangeColor, Color.WHITE));
			   
			   xyGraphWidget.getXyPointBorderPaint().setColor(a.getColor(R.styleable.XYPlot_pointBorderColor, Color.TRANSPARENT));
			   xyGraphWidget.getXyPointBorderPaintSelected().setColor(a.getColor(R.styleable.XYPlot_pointBorderColorSelected, Color.TRANSPARENT));
			   xyGraphWidget.getXyPointBorderPaint().setStrokeWidth(a.getFloat(R.styleable.XYPlot_pointBorderWidth, 1));
			   xyGraphWidget.getXyPointBorderPaintSelected().setStrokeWidth(a.getFloat(R.styleable.XYPlot_pointBorderWidth, 1));
			   
			   xyGraphWidget.setXyPointRadio(a.getFloat(R.styleable.XYPlot_pointRadio, 1));
			   xyGraphWidget.getXyPointPaint().setColor(a.getColor(R.styleable.XYPlot_pointColor, Color.BLACK));
			   xyGraphWidget.getXyPointPaintSelected().setColor(a.getColor(R.styleable.XYPlot_pointColorSelected, Color.BLACK));
			   xyGraphWidget.getXyPointPaint().setStrokeWidth(a.getFloat(R.styleable.XYPlot_pointWidth, 1));
			   xyGraphWidget.getXyPointPaintSelected().setStrokeWidth(a.getFloat(R.styleable.XYPlot_pointWidth, 1));
				  
			   xyGraphWidget.getXyLinePaint().setColor(a.getColor(R.styleable.XYPlot_lineColor, Color.BLACK));
			   xyGraphWidget.getXyLinePaint().setStrokeWidth(a.getFloat(R.styleable.XYPlot_lineWidth, 1));
			   xyGraphWidget.setXyLineFillEnabled(a.getBoolean(R.styleable.XYPlot_lineFillEnabled, false));
			   
			   xyGraphWidget.getTooltip().setDrawable(a.getDrawable(R.styleable.XYPlot_tooltipBackground));
			   xyGraphWidget.getTooltip().setStylePrimaryText(a.getColor(R.styleable.XYPlot_tooltipLabelPrimaryColor, Color.BLACK),
					   							a.getDimension(R.styleable.XYPlot_tooltipLabelPrimarySize,14));
			   xyGraphWidget.getTooltip().setStyleSecondText(a.getColor(R.styleable.XYPlot_tooltipLabelSecondColor, Color.BLACK),
					   							a.getDimension(R.styleable.XYPlot_tooltipLabelSecondSize,10));
		   } finally {
		       a.recycle();
		   }
		
	}

	private Align convertToAlign(int align) {
		if (TEXT_ALIGN_CENTER == align) {
			return Align.CENTER;
		} else if (TEXT_ALIGN_LEFT == align) {
			return Align.LEFT;
		} else if (TEXT_ALIGN_RIGHT == align) {
			return Align.RIGHT;
		}
		return Align.CENTER;
	}

	/***
	 * Not support more than one serie yet.
	 * @param xySerie
	 */
	public void addSerie(XYSerie<String, Number> xySerie) {
		this.xySerie = xySerie;
		xyGraphWidget.addSerie(xySerie);
	}

	private void init() {
		
		xyGraphWidget = new XYGraphWidget();
		backgroundPaint = new Paint();
		touchHandler = new TouchHandler(this);
		
		mDetector = new GestureDetectorCompat(this.getContext(), new XYGestureListener(this));
		
		
		this.setOnTouchListener(this);
	}

	protected synchronized void onSizeChanged (int w, int h, int oldw, int oldh) {

		canvasRect = new RectF(0, 0, w, h);
		xyGraphWidget.layout(canvasRect);

		super.onSizeChanged(w, h, oldw, oldh);

	}


	@Override 
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

		canvas.drawRect(canvasRect, backgroundPaint);

		xyGraphWidget.draw(canvas);

	}

	@Override
	public boolean onTouch(View v, MotionEvent motionEvent)  {
		this.mDetector.onTouchEvent(motionEvent);
		return touchHandler.handleTouch(motionEvent);
		
	}
	
	/** 
	 * Use the scrolling information
	 * @param lastXScrolling Distances of scrolling in x axis
	 */
	public void scroll(float lastXScrolling) {
		xyGraphWidget.scroll(lastXScrolling);
	}

	/**
	 * redraw all view
	 */
	public void redraw() {
		this.invalidate();
	}
	
	public XYGraphWidget getXyGraphWidget() {
		return xyGraphWidget;
	}
	public void setXyGraphWidget(XYGraphWidget xyGraphWidget) {
		this.xyGraphWidget = xyGraphWidget;
	}
	
	 
	 
	 private class XYGestureListener extends GestureDetector.SimpleOnGestureListener {
		 private static final String DEBUG_TAG = "Gestures"; 

		 XYPlot mXyPlot;
		 
		 public XYGestureListener(XYPlot aXyPlot) {
			 super();
			 mXyPlot = aXyPlot;
		 }
		 		 
		 
		 @Override
		 public boolean onDown(MotionEvent event) { 
			 Log.d(DEBUG_TAG,"onDown: " + event.toString()); 
			 return true;
		 }

		 @Override
	    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
	            												float distanceY) {
	        Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
	        return true;
	    }
		 
		 @Override
	    public boolean onSingleTapConfirmed(MotionEvent event) {
	        Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
	        
			if (mXyPlot.isHittingLinePoint(event.getX(), event.getY())) {
				mXyPlot.showPointTooltipOn(event.getX(), event.getY());
			}
	        
	        return true;
	    }
	 }



	public boolean isHittingLinePoint(float x, float y) {

		return this.getXyGraphWidget().hasAPointIn(x, y);
	}

	public void showPointTooltipOn(float x, float y) {
		this.getXyGraphWidget().updateTooltipOn(x, y);
		this.invalidate();
	}
	 
	 

}