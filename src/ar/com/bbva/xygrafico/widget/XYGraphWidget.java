package ar.com.bbva.xygrafico.widget;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Pair;
import ar.com.bbva.xygrafico.core.XYSerie;

public class XYGraphWidget {

	
	private static final float DEFAULT_GRID_MARGIN_TOP_PIX = 5;
	private static final float DEFAULT_GRID_MARGIN_RIGHT_PIX = 5;
	private static final float DEFAULT_GRID_MARGIN_LEFT_PIX = 5;
	private static final float DEFAULT_GRID_MARGIN_BOTTOM_PIX = 20;
	
	private static final int DEFAULT_GRID_ROWS = 5;
	private static final int DEFAULT_GRID_COLS = 6;

	
	/** Label formated to show in y axis */
	private TextLabelWidget labelRangeWidget;
	
	/** Label formated to show in x axis */
	private TextLabelWidget labelDomainWidget;
	
	/** Meansurement of grid canvas (or view port) */
	private RectF gridRect;
	
	/** Tooltip witch show information of selected point */
	private TooltipWidget tooltipRect;
	
	/** Meansurement of graphic */
	private RectF graphRect;
	
	/**Paint background of grid canvas */
	private Paint gridBackgroundPaint;

	/**Paint of range lines */
	private Paint rangeLineTickPaint;
	
	/**Paint of domain lines */
	private Paint domainLineTickPaint;

	/**Paint of serie xy lines */
	private Paint xyLinePaint;
	
	/**Paint to fill the serie xy lines */
	private Paint xyLineFillPaint;
	
	/**When set to true, the line has filling */
	private boolean xyLineFillEnabled;
	
	/**Paint to fill the point serie xy lines */
	private Paint xyPointPaint;
	private Paint xyPointPaintSelected;
	
	/**Paint border to point serie xy lines */
	private Paint xyPointBorderPaint;
	private Paint xyPointBorderPaintSelected;
	
	private float xyPointRadio;
	
	
	/**Path of serie xy lines */
	private Path xyLinePath;

	
	private float startX;
	
	
	/** Value of min value of y axis that will be showing in the graph */
	private Double minValueY;

	/** Value of max value of y axis that will be showing in the graph */
	private Double maxValueY;

	/** List of pair ranges and domain values */
	private XYSerie<String,Number> xySerie;

	/** Values of y axis that wil be showing in the graph */
	private List<Number> rangeValueTicks;

	/** Count of rows thats will be showing in the viewport, including the origin */
	private int countRangeLines = DEFAULT_GRID_ROWS;
	
	/** Count of columns thats will be showing in the viewport */
	private int countDomainLines = DEFAULT_GRID_COLS;
	
	/** Count of maximum line thats will be showing in the graph */
	private int maxCountDomainLines = DEFAULT_GRID_COLS;
	
	/** Domain Step in pixel */
	private float domainStepPix;
	
	/** Range Step in pixel */
	private float rangeStepPix;
	
	/** It has the list of point x y to draw in the view */
	private List<PointF> pointsPix = new LinkedList<PointF>();
	
	private int indexPointSelected = -1;
	
	
	public XYGraphWidget() {
		tooltipRect = new TooltipWidget("$ 12.142,35", "18/05/14", null);
		init();
	}
	
	/***
	 * Not support more than one serie yet.
	 * @param xySerie
	 */
	public void addSerie(XYSerie<String, Number> xySerie) {
		this.xySerie = xySerie;
		
		Number minValueYN = (xySerie.iterator().hasNext())?xySerie.iterator().next().second:0;
		for (Pair<String, Number> xyLinePoint : xySerie) {
			if (xyLinePoint.second.doubleValue() < minValueYN.doubleValue()) {
				minValueYN = xyLinePoint.second; 
			}
		}

		Number maxValueYN = (xySerie.iterator().hasNext())?xySerie.iterator().next().second:0;
		for (Pair<String, Number> xyLinePoint : xySerie) {
			if (xyLinePoint.second.doubleValue() > maxValueYN.doubleValue()) {
				maxValueYN = xyLinePoint.second; 
			}
		}
		
		initGrid(minValueYN, maxValueYN);
	}

	private void init() {
		
		labelRangeWidget = new TextLabelWidget();
		labelDomainWidget = new TextLabelWidget();


		gridBackgroundPaint = new Paint();
		gridBackgroundPaint.setColor(Color.WHITE);


		rangeLineTickPaint = new Paint();
		rangeLineTickPaint.setAntiAlias(true);
		rangeLineTickPaint.setStrokeWidth(2);
		
		domainLineTickPaint = new Paint();
		domainLineTickPaint.setStrokeWidth(2);
		domainLineTickPaint.setAntiAlias(true);
		

		xyPointPaint = new Paint();
		xyPointPaint.setAntiAlias(true);
		xyPointPaint.setStyle(Style.FILL);
		
		xyPointBorderPaint = new Paint();
		xyPointBorderPaint.setAntiAlias(true);
		xyPointBorderPaint.setStyle(Style.STROKE);
		
		xyPointPaintSelected = new Paint();
		xyPointPaintSelected.setAntiAlias(true);
		xyPointPaintSelected.setStyle(Style.FILL);
		
		xyPointBorderPaintSelected = new Paint();
		xyPointBorderPaintSelected.setAntiAlias(true);
		xyPointBorderPaintSelected.setStyle(Style.STROKE);
		        
		xyLinePaint = new Paint();
		xyLinePaint.setAntiAlias(true);
		xyLinePaint.setStyle(Paint.Style.STROKE);
		
		xyLineFillPaint = new Paint();
		//xyLineFillPaint.setAlpha(210);
		xyLineFillPaint.setShader(new LinearGradient(0, 0, 0, 300, Color.argb(210, 0, 158, 229) , Color.argb(10, 0, 158, 229), Shader.TileMode.CLAMP));

		xyLinePath = new Path();
		pointsPix = new ArrayList<PointF>();

	}

	public void initGrid(Number minValueYN, Number maxValueYN) {

		minValueY = minValueYN.doubleValue();
		maxValueY = maxValueYN.doubleValue();
		int countDivision = countRangeLines - 1;
		int divY = (int) ((maxValueY - minValueY) / countDivision);

		int mult = 10;  
		while(divY/mult > 10 && mult < Integer.MAX_VALUE/10 ){
			mult=mult*10;
		}

		if (divY % mult != 0) {
			divY = ((divY/mult)+1)*mult;
		}

		int minCota = (int)(minValueY / mult) * mult;
		int maxCota = (countDivision) * divY + minCota;

		minValueY = Double.valueOf(minCota).doubleValue();
		maxValueY = Double.valueOf(maxCota).doubleValue();


		rangeValueTicks = new LinkedList<Number>();
		for (int i = 0; i<countRangeLines; i++) {
			Number rangeValueTick = maxValueY - (i * divY);
			rangeValueTicks.add(rangeValueTick);
		}
		
		
	}
	
	

	/***
	 * Calculate domain step in pixels
	 * @return rangeStepPix
	 */
	private float calculateDomainStepGrid() {
		float weight = gridRect.right - gridRect.left;
		float countDomainCols = this.countDomainLines + 1;
		float domainStepPix = weight / countDomainCols;
		return domainStepPix;
	}
	
	/***
	 * Calculate range step in pixels
	 * @return rangeStepPix
	 */
	private float calculateRangeStepGrid() {
		float heightGrid = gridRect.bottom - gridRect.top;
		float countRangeRows = this.countRangeLines + 1;
		float rangeStepPix = heightGrid / countRangeRows; 
		return rangeStepPix;
	}	
	
	private void buildGraphicRect() {
		float lastXPix = (maxCountDomainLines + 1) * domainStepPix;
		graphRect = new RectF(gridRect.left, gridRect.top, lastXPix, gridRect.bottom);
		
	}

	/***
	 * Called when a change to the class' dimensions is made
	 * @param cavasRect
	 */
	public void layout(RectF canvasRect) {
		gridRect = new RectF(canvasRect.left + DEFAULT_GRID_MARGIN_LEFT_PIX, 
				canvasRect.top + DEFAULT_GRID_MARGIN_TOP_PIX, 
				canvasRect.right - DEFAULT_GRID_MARGIN_RIGHT_PIX, 
				canvasRect.bottom - DEFAULT_GRID_MARGIN_BOTTOM_PIX);

		
		domainStepPix = calculateDomainStepGrid();
		rangeStepPix = calculateRangeStepGrid(); 
		
		buildGraphicRect();
	}
	
	 /**
	 * Draws lines and points for each element in the series.
	 * @param canvas 
	 */
	public void draw(Canvas canvas) {
			
		try {
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.clipRect(gridRect, android.graphics.Region.Op.INTERSECT);
			
			drawMesh(canvas);
			drawXYLine(canvas);
			drawTooltip(canvas);		
			drawValueLabels(canvas);
			drawDomainLabels(canvas);
			
		} finally {
			canvas.restore();
		}
		
	}


	/***
	 * Draw the label range values in canvas
	 * @param canvas
	 */
	private void drawDomainLabels(Canvas canvas) {
		float originX = this.startX;
		int lineDomianNumber = 1;
		for (Pair<String, Number> pointSerie : xySerie) {

			float x = originX + (lineDomianNumber * domainStepPix);
			float y = gridRect.bottom - (rangeStepPix/4);
			labelDomainWidget.setText(pointSerie.first);			
			labelDomainWidget.draw(canvas, x, y);
			lineDomianNumber++;
			
		}
		
	}
	
	
	/***
	 * Draw the label range values in canvas
	 * @param canvas
	 */
	private void drawValueLabels(Canvas canvas) {
		
		int lineRangeNumber = 1;
		float originY = labelRangeWidget.getHeight() / 4;
		float origenX = gridRect.left + labelRangeWidget.getWidth();
		
		for (Number rangeValue : rangeValueTicks) {
			labelRangeWidget.setText(rangeValue.intValue()+"");
			labelRangeWidget.draw(canvas, origenX, originY + (lineRangeNumber*rangeStepPix));
			lineRangeNumber++;
		}

	}
	
	/***
	 * Draw the serie values in canvas
	 * @param canvas
	 */
	private void drawXYLine(Canvas canvas) {
		float Xpix  = this.startX + this.gridRect.left;
		
		xyLinePath.reset();
		
		PointF firstPoint = null;
		PointF lastPoint = null;
		
		int domianLineCounter=1;
		int indexPointToUpdate = 0;
		
		/**  calculate points to draw  */
		for (Pair<String, Number> xyLinePoint : xySerie) {
			Number value = xyLinePoint.second;
			
			float pixY = convertValueToYPix(value.floatValue());
			float pixX = Xpix + (domianLineCounter*domainStepPix);
	
			if (pointsPix.size() < xySerie.getSize()) {
				pointsPix.add(new PointF(pixX, pixY));
			} else {
				pointsPix.get(indexPointToUpdate).x = pixX;
				pointsPix.get(indexPointToUpdate).y = pixY;
			}
			indexPointToUpdate++;
			domianLineCounter++;
		}
		
		firstPoint = (pointsPix.size() > 0)?pointsPix.get(0):null;
		lastPoint = pointsPix.get(pointsPix.size() - 1);
		
		/**  build path  */
		for (PointF pointF : pointsPix) {
			if (firstPoint.equals(pointF)) {
				xyLinePath.moveTo(pointF.x, pointF.y);
			} else {
				xyLinePath.lineTo(pointF.x, pointF.y);
			}
		}

		/** Draw below xy line */
		Path outlinePath = new Path(xyLinePath);
		if (isXyLineFillEnabled()) {
			xyLinePath.lineTo(lastPoint.x, gridRect.bottom);
			xyLinePath.lineTo(firstPoint.x, gridRect.bottom);
			xyLinePath.close();
			xyLineFillPaint.setShader(new LinearGradient(0, 0, 0, gridRect.bottom, 
					Color.argb(210, 0, 158, 229) , Color.argb(0, 0, 158, 229), 
					Shader.TileMode.CLAMP));

			canvas.drawPath(xyLinePath, xyLineFillPaint);
		}
		/** Draw xy line */
		canvas.drawPath(outlinePath, xyLinePaint);
		
		PointF pointSelected = (indexPointSelected >= 0 ? pointsPix.get(indexPointSelected) : null);
		/** Draw xy Point */ 
		for (PointF pointF : pointsPix) {
			if(pointF == pointSelected) {
				canvas.drawCircle(pointF.x, pointF.y, xyPointRadio, xyPointBorderPaintSelected);
				canvas.drawCircle(pointF.x, pointF.y, xyPointRadio, xyPointPaintSelected);				
			} else {
				canvas.drawCircle(pointF.x, pointF.y, xyPointRadio, xyPointPaint);
				canvas.drawCircle(pointF.x, pointF.y, xyPointRadio, xyPointBorderPaint);
			}
		}
		
		xyLinePath.rewind();


	}


	/***
	 * Transform range value to y pixel position inside of grid
	 * @param floatValue
	 * @return
	 */
	private float convertValueToYPix(float floatValue) {
		float proporcionalValue =  (floatValue - minValueY.floatValue()) / (maxValueY.floatValue() - minValueY.floatValue()) ;
		float countRangeRowsDrawable = this.countRangeLines - 1;
		float heightRangeDrawble = countRangeRowsDrawable * rangeStepPix;
		float originYPix = gridRect.bottom - rangeStepPix;
		return originYPix - (heightRangeDrawble * proporcionalValue);
	}

	/**
	 * Draw mesh on canvas
	 * @param canvas
	 */
	private void drawMesh(Canvas canvas) {

		canvas.drawRect(gridRect, gridBackgroundPaint);
		drawRangeTicksLine(canvas);
		drawDomainTicksLine(canvas);

	}
	
	/**
	 * Draw the information of selected point
	 * @param canvas
	 */
	private void drawTooltip(Canvas canvas) {
		
		if(indexPointSelected < 0) return;
		
		this.tooltipRect.draw(canvas, pointsPix.get(indexPointSelected).x, 
									  pointsPix.get(indexPointSelected).y, 
									  (int)xyPointRadio);
	}

	
	/**
	 * Draw range lines in canvas.
	 * @param canvas
	 */
	private void drawRangeTicksLine(Canvas canvas) {

		float stopXPix = gridRect.right;
		float originXPix = gridRect.left;
		
		float rangeOriginF = gridRect.top;
		float yPix = rangeOriginF + rangeStepPix;
		
		int lineNumber = 1;
		for (; yPix <= gridRect.bottom; yPix = rangeOriginF + (lineNumber * rangeStepPix)) {
			if (yPix >= gridRect.top && yPix < gridRect.bottom && lineNumber <= this.countRangeLines) {
				canvas.drawLine(originXPix, yPix , stopXPix, yPix, rangeLineTickPaint);
			}
			lineNumber++;
		}
	}
	
	/**
	 * Draw domain lines in canvas.
	 * @param canvas 
	 */
	private void drawDomainTicksLine(Canvas canvas) {

		float startY = gridRect.top;
		float stopY = gridRect.bottom;

		float domainOriginF = gridRect.left + startX;
		
		float xPix = domainOriginF + domainStepPix;
		int lineNumber = 1;
		for (; xPix < gridRect.right; xPix = domainOriginF + (lineNumber * domainStepPix)) {
			if (xPix > gridRect.left && xPix < gridRect.right && lineNumber <= this.maxCountDomainLines) {
				canvas.drawLine(xPix, startY, xPix, stopY, domainLineTickPaint);
			}
			lineNumber++;
		}
	}
	
	/** 
	 * Use the scrolling information
	 * @param lastXScrolling Distances of scrolling in x axis
	 */
	public void scroll(float lastXScrolling) {
		float startX = 0;
		startX  = this.getStartX() - lastXScrolling;
		if (isBoundaryGraphicRespect(startX)) {
			startX  = this.getStartX() - lastXScrolling;
			this.setStartX(startX);
		}
		tooltipRect.hide();
	}
	
	
	private boolean isBoundaryGraphicRespect(float currentStartX) {
		// axis x
		boolean xBoundaryRespect = ((gridRect.left >= graphRect.left + currentStartX) && 
									(gridRect.right <= graphRect.right + currentStartX));
					
		return xBoundaryRespect;
	}
	
	public boolean hasAPointIn(float x, float y) {
		
		for (PointF pointPixel : pointsPix) {
			float dist = (float) Math.hypot((double)(x - pointPixel.x) , 
									(double)(y - pointPixel.y));
			if (dist < 2*xyPointRadio) {
				pointsPix.indexOf(pointPixel);
				return true;
			}
		}
		return false;
	}

	public void updateTooltipOn(float x, float y) {
		for (PointF pointPixel : pointsPix) {
			float dist = (float) Math.hypot((double)(x - pointPixel.x) , 
									(double)(y - pointPixel.y));
			if (dist < 2*xyPointRadio) {
				indexPointSelected = pointsPix.indexOf(pointPixel);
				
				Pair<String, Number> xyLinePoint = xySerie.get(indexPointSelected);
				tooltipRect.setTexts("$ "+xyLinePoint.second.toString(), 
						xyLinePoint.first);
				tooltipRect.show();
			}
		}
	}

	public int getCountRangeLines() {
		return countRangeLines;
	}
	
	public void setCountRangeLines(int countRangeLines) {
		this.countRangeLines = countRangeLines;
	}
	
	public int getCountDomainLines() {
		return countDomainLines;
	}
	
	public void setCountDomainLines(int countDomainLines) {
		this.countDomainLines = countDomainLines;
	}

	public TextLabelWidget getLabelRangeWidget() {
		return labelRangeWidget;
	}

	public void setLabelRangeWidget(TextLabelWidget labelRangeWidget) {
		this.labelRangeWidget = labelRangeWidget;
	}

	public TextLabelWidget getLabelDomainWidget() {
		return labelDomainWidget;
	}

	public void setLabelDomainWidget(TextLabelWidget labelDomainWidget) {
		this.labelDomainWidget = labelDomainWidget;
	}

	public Paint getGridBackgroundPaint() {
		return gridBackgroundPaint;
	}

	public void setGridBackgroundPaint(Paint gridBackgroundPaint) {
		this.gridBackgroundPaint = gridBackgroundPaint;
	}

	public Paint getRangeLineTickPaint() {
		return rangeLineTickPaint;
	}

	public void setRangeLineTickPaint(Paint rangeLineTickPaint) {
		this.rangeLineTickPaint = rangeLineTickPaint;
	}

	public Paint getDomainLineTickPaint() {
		return domainLineTickPaint;
	}

	public void setDomainLineTickPaint(Paint domainLineTickPaint) {
		this.domainLineTickPaint = domainLineTickPaint;
	}

	public Paint getXyPointPaint() {
		return xyPointPaint;
	}

	public Paint getXyPointPaintSelected() {
		return xyPointPaintSelected;
	}
	
	public void setXyPointPaint(Paint xyPointPaint) {
		this.xyPointPaint = xyPointPaint;
	}

	public Paint getXyPointBorderPaint() {
		return xyPointBorderPaint;
	}
	
	public Paint getXyPointBorderPaintSelected() {
		return xyPointBorderPaintSelected;
	}

	public void setXyPointBorderPaint(Paint xyPointBorderPaint) {
		this.xyPointBorderPaint = xyPointBorderPaint;
	}

	public float getXyPointRadio() {
		return xyPointRadio;
	}

	public void setXyPointRadio(float xyPointRadio) {
		this.xyPointRadio = xyPointRadio;
	}

	public Paint getXyLinePaint() {
		return xyLinePaint;
	}

	public void setXyLinePaint(Paint xyLinePaint) {
		this.xyLinePaint = xyLinePaint;
	}

	public Paint getXyLineFillPaint() {
		return xyLineFillPaint;
	}

	public void setXyLineFillPaint(Paint xyLineFillPaint) {
		this.xyLineFillPaint = xyLineFillPaint;
	}

	public Path getXyLinePath() {
		return xyLinePath;
	}

	public void setXyLinePath(Path xyLinePath) {
		this.xyLinePath = xyLinePath;
	}

	public float getStartX() {
		return startX;
	}

	public void setStartX(float startX) {
		this.startX = startX;
	}

	public List<Number> getRangeValueTicks() {
		return rangeValueTicks;
	}

	public void setRangeValueTicks(List<Number> rangeValueTicks) {
		this.rangeValueTicks = rangeValueTicks;
	}

	public int getMaxCountDomainLines() {
		return maxCountDomainLines;
	}

	public void setMaxCountDomainLines(int maxCountDomainLines) {
		this.maxCountDomainLines = maxCountDomainLines;
	}

	public boolean isXyLineFillEnabled() {
		return xyLineFillEnabled;
	}

	public void setXyLineFillEnabled(boolean xyLineFillEnabled) {
		this.xyLineFillEnabled = xyLineFillEnabled;
	}
	
	public TooltipWidget getTooltip() {
		return tooltipRect;
	}
}
