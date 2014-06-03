package ar.com.bbva.xygrafico.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import ar.com.bbva.xygrafico.activity.R;


public class Tooltip extends LinearLayout {

    /** The amount of space used by children in the left gutter. */
    private int mLeftWidth;

    /** The amount of space used by children in the right gutter. */
    private int mRightWidth;

    /** These are used for computing child frames based on their gravity. */
    private final Rect mTmpContainerRect = new Rect();
    private final Rect mTmpChildRect = new Rect();
    
    private Drawable contour;
    
    private Drawable arrowUp;
    
    private Drawable arrowDown;
    
    public Tooltip(Context context) {
        super(context);
    }

    public Tooltip(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
    }
    
    private void initAttributes(Context context, AttributeSet attrs) {

		TypedArray a = context.getTheme().obtainStyledAttributes(
		        attrs,
		        R.styleable.Tooltip,
		        0, 0);

		   try {
			   
			   contour = a.getDrawable(R.styleable.Tooltip_contour);
			   arrowUp = a.getDrawable(R.styleable.Tooltip_arrow_up);
			   arrowDown = a.getDrawable(R.styleable.Tooltip_arrow_down);
			   
		   } finally {
		       a.recycle();
		   }
		
	}
  
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
    	int x = 0;
    	
        if(arrowUp != null)
        	x = arrowUp.getMinimumHeight();
    	
        int width = super.getMeasuredWidth();
        int height = super.getMeasuredHeight();
        
        Rect bounds = new Rect(0, x, width, x + height);
        contour.setBounds(bounds);
        
        if(arrowUp != null)
        	height += arrowUp.getMinimumHeight();
     
        setMeasuredDimension(width, height);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    	// TODO Auto-generated method stub
    	contour.draw(canvas);
    	arrowUp.draw(canvas);
    	super.onDraw(canvas);
    }
}
