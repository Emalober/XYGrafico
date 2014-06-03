package ar.com.bbva.xygrafico;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

public class TouchHandler {

	public static final String TAG="TouchHandler"; 
	
	private XYPlot mGraph;


	/** Point of the first finger*/
	private PointF firstFinger;
	
	/** Point of the first finger*/
	private PointF upFinger;
	
	/** Movement in axis X */
	private float lastXScrolling;


	public TouchHandler(XYPlot graph) {
		this.mGraph = graph;
	}

	/**
	 * Handles the touch event.
	 * 
	 * @param event the touch event
	 */
	public boolean handleTouch(MotionEvent event) {

		switch(event.getAction() & MotionEvent.ACTION_MASK)		{
		
		case MotionEvent.ACTION_DOWN: //start gesture
			firstFinger = new PointF(event.getX(), event.getY());

			break;

		case MotionEvent.ACTION_MOVE:

			final PointF oldFirstFinger = firstFinger;
			firstFinger = new PointF(event.getX(), event.getY());
			lastXScrolling = oldFirstFinger.x - firstFinger.x;
			mGraph.scroll(lastXScrolling);

			mGraph.redraw();
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			upFinger = new PointF(event.getX(), event.getY());
			//Log.d(TAG, "Solte el dedo en : ".concat(upFinger.toString()));
//			if (mGraph.isHittingLinePoint(upFinger)) {
//				mGraph.showTooltip(upFinger);
//			}
		}
		return true;
	}
}
