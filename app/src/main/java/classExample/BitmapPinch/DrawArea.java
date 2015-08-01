package classExample.BitmapPinch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawArea extends SurfaceView implements SurfaceHolder.Callback, OnTouchListener {

	SurfaceHolder mDrawSurface = null;
	//Context mResources = null;

	int mWidth, mHeight; // Width and Height of the drawing area

	Bitmap mDroid;		// the droid we want to draw
	Rect mSrc, mDst;    // areas to draw
	final int kDroidWidth = 200;
	final int kDroidHeight = 200;

	Paint mPaint; 	// the paint for drawing

	// for pinch scaling
	final int kInvalidID = -1;
	private ScaleGestureDetector mScaleDetector;
	private int mPointerId = kInvalidID;
	private float mScaleFactor = 1.f;

	// user changeable variables
	float mTouchX = 0f, mTouchY = 0f;

	// Region: Constructors and object initialization
	public DrawArea(Context context) {
		super(context);

		initSurface(context);
	}

	public DrawArea(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initSurface(context);
	}

	private void initSurface(Context context)
	{
		mDrawSurface = getHolder();

		// enable the calling of SurfaceHolder.Callback functions!
		mDrawSurface.addCallback(this);

		//create paint to draw with on canvas
		mPaint = new Paint();

		// initialize scale gesture detector
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}
	// EndRegion

	// Region  these are from SurfaceHodler.CallBack
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i("MyDebug", "Surface:Created");
		setWillNotDraw(false);
		setBackgroundColor(Color.GRAY);

		// enable onTouch
		setOnTouchListener(this);

		// load the droid image
		Bitmap droid = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		mDroid = Bitmap.createScaledBitmap(droid, kDroidWidth, kDroidHeight, false);
		mSrc = new Rect(0, 0, kDroidWidth, kDroidHeight);
		mDst = new Rect(mSrc);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
		Log.i("MyDebug", "Surface:" + width + "x" + height);
		mWidth = width;
		mHeight = height;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}
	//EndRegion

	// Region: function from OnTouchListener
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// try to detect scale gesture
		mScaleDetector.onTouchEvent(event);

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// say we are only only to look at actions, and nothing else

			case MotionEvent.ACTION_DOWN:
				// let's wait to move before decide if we should move
				if (event.getPointerCount() == 1)
					mPointerId = event.getPointerId(0);
				break;
			case MotionEvent.ACTION_MOVE:
				if ((!mScaleDetector.isInProgress()) &&
						(kInvalidID != mPointerId) &&
						(event.getPointerCount() == 1))
				{
					int index = event.findPointerIndex(mPointerId);
					mTouchX = event.getX(index);
					mTouchY = event.getY(index);
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				mPointerId = kInvalidID;
				break;

			case MotionEvent.ACTION_POINTER_UP:
				// what this means is the "other pointer(finger)" has come up
				// but it is really possible for this to turn out to be our primary finger!
				mPointerId = kInvalidID;
				break;

		}

		invalidate();  // forces redraw!
		return true; // event handled
	}
	// EndRegion


	@Override
	protected void onDraw(Canvas c) {
		if (this.isInEditMode())
			return;

		// where we want to draw:
		//      center is located at (mTouchX, mTouchY)
		//      size is mScaleFactor * mDroid.getWidth() (or getHeight())
		int newW = (int) (mScaleFactor * mDroid.getWidth());
		int newH = (int) (mScaleFactor * mDroid.getHeight());
		int x = (int) (mTouchX - (newW/2));
		int y = (int) (mTouchY - (newH/2));
		mDst.set(x, y, x+newW, y+newH);

		c.drawBitmap(mDroid, mSrc, mDst, null);

	}

	// Region ScaleListener
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {

			mTouchX = detector.getFocusX();
			mTouchY = detector.getFocusY();

			mScaleFactor *= detector.getScaleFactor();

			// Don't let the object get too small or too large.
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

			invalidate();
			return true;
		}
	}
	// EndRegion
}
