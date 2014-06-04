package ar.com.bbva.xygrafico.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
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

    public Tooltip(Context context, AttributeSet attrs) {
        super(context, attrs);

        
        initAttributes(context, attrs);

        contour.getPadding(mTmpContainerRect);
        
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
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	
        int maxHeight = 0;	//this.getMeasuredHeight();
        int maxWidth = 0;	//this.getMeasuredWidth();

    	for(int i = 0; i < this.getChildCount(); i++) {
    		View child = this.getChildAt(i);
    		
    		int widthChild = child.getMeasuredWidth();
    		int heightChild = child.getMeasuredHeight();
    		
    		maxWidth = Math.max(maxWidth, widthChild);
    		
    		maxHeight += heightChild;
    	}

        maxWidth += mTmpContainerRect.right + mTmpContainerRect.left;
        maxHeight += mTmpContainerRect.top + mTmpContainerRect.bottom;

        maxHeight += arrowUp.getIntrinsicHeight();

	    setMeasuredDimension(maxWidth, maxHeight);
	}
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        final int count = getChildCount();
        
        int height = bottom - top;
        int width = right - left;
        
        arrowUp.setBounds(this.mTmpContainerRect.left, 
        		0, 
        		arrowUp.getIntrinsicWidth() + this.mTmpContainerRect.left, 
        		arrowUp.getIntrinsicHeight());
    
        contour.setBounds(0, arrowUp.getIntrinsicHeight() - arrowUp.getIntrinsicHeight()/3, 
        		width, 
        		height);
        
        // These are the far left and right edges in which we are performing layout.
        int leftPos = left + this.mTmpContainerRect.left;
        int rightPos = right - left - this.mTmpContainerRect.right;

        // These are the top and bottom edges in which we are performing layout.
        int parentTop = getPaddingTop() + arrowUp.getIntrinsicHeight() + this.mTmpContainerRect.top;
        int parentBottom = bottom - this.mTmpContainerRect.bottom;
        
        int useHeight = 0;
        
        for (int i = 0; i < count; i++) {
        	
            final View child = getChildAt(i);
            
            if (child.getVisibility() != GONE) {

                //int width = child.getMeasuredWidth();
                int heightC = child.getMeasuredHeight();

                // Place the child.
                child.layout(leftPos, parentTop + useHeight,
                		rightPos, parentTop + useHeight + heightC);
                
                useHeight += heightC;
            }
        }
    }
    
    @Override
    protected void dispatchDraw(Canvas canvas)
    {
    	contour.draw(canvas);
    	super.dispatchDraw(canvas);
    	arrowUp.draw(canvas);
    }
}
