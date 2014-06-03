package ar.com.bbva.xygrafico.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
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
    	
        int maxHeight = this.getMeasuredHeight();
        int maxWidth = this.getMeasuredWidth();

        contour.setBounds(new Rect(0, 0, maxWidth,maxHeight));
        maxWidth += mTmpContainerRect.right + mTmpContainerRect.left;
        maxHeight += mTmpContainerRect.top + mTmpContainerRect.bottom;
        maxHeight += arrowUp.getMinimumHeight();

	    setMeasuredDimension(maxWidth, maxHeight);
	}
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        final int count = getChildCount();

        arrowUp.setBounds(left + this.mTmpContainerRect.left, 
        		top, 
        		left + arrowUp.getIntrinsicWidth() + this.mTmpContainerRect.left, 
        		top + arrowUp.getIntrinsicHeight());
        
        top += arrowUp.getIntrinsicHeight() - 1;
        
        // These are the far left and right edges in which we are performing layout.
        int leftPos = left + this.mTmpContainerRect.left;
        int rightPos = right - left - this.mTmpContainerRect.right;

        // These are the top and bottom edges in which we are performing layout.
        int parentTop = getPaddingTop() + top + this.mTmpContainerRect.top;
        int parentBottom = bottom - top - this.mTmpContainerRect.bottom;
        
        int useHeight = 0;
        
        contour.setBounds(left, top, right, bottom);
        
        for (int i = 0; i < count; i++) {
        	
            final View child = getChildAt(i);
            
            if (child.getVisibility() != GONE) {

                //int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();

                // Place the child.
                child.layout(leftPos, parentTop + useHeight,
                		rightPos, parentTop + useHeight + height);
                
                useHeight += height;
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
/*    @Override
    protected void onDraw(Canvas canvas) {
    	// TODO Auto-generated method stub

    	super.onDraw(canvas);
    }
    
    @Override
	public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setColor(0xFFFF0000);
        canvas.drawLine(0, 0, getWidth(), getHeight(), paint);
    }*/
}
