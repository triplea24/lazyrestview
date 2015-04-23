package apl.vada.lib.lazyrestview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;

public class LazyRestView extends ListView implements OnScrollListener {

	protected static final String TAG = "LazyRestView";
	public static final int METHOD_GET = Request.Method.GET;
	public static final int METHOD_POST = Request.Method.POST;

	private OnScrollListener mOnScrollListener;

	private LayoutInflater mInflater;

	private LinearLayout mFooterView;
	private ProgressBar mProgressBarLoadMore;

	private boolean mIsLoadingMore = false;
	private int mCurrentScrollState;
	private boolean initialize = false;
	private boolean next = false;
	private boolean hasOnScrollListener = false;

	private LazyRestHttp lazyRestHttp;

	private int method;
	private String url;

	public static final int CACHE_LOCAL_ON = 12345;
	public static final int CACHE_LOCAL_OFF = 15315;
	protected int cacheMode;

	/*
	 * Constructor
	 */
	public LazyRestView(Context context) {
		super(context);
		create(context);
	}

	public LazyRestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		create(context);
	}

	public LazyRestView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		create(context);
	}

	private void create(Context context) {
		log("init");
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFooterView = (LinearLayout) mInflater.inflate(R.layout.view_footer,
				this, false);
		mProgressBarLoadMore = (ProgressBar) mFooterView
				.findViewById(R.id.prgLoadMore);
		addFooterView(mFooterView);
		lazyRestHttp = new LazyRestHttp(context, this);
		cacheMode = CACHE_LOCAL_ON;
	}

	/*
	 * Activity LifeCycle
	 */
	public void onDestroy() {
		lazyRestHttp.onDestroy();
	}

	public void onStop() {
		if (initialize)
			lazyRestHttp.onStop();
	}

	public void onStart() {
		if (!initialize)
			throw new IllegalStateException(
					"You must first initialize!!! (call init method)");
		else {
			lazyRestHttp.request(method, url);
		}
	}

	/*
	 * Rest View LifeCycle
	 */

	public void init(int method, String url) {
		if (method == METHOD_GET || method == METHOD_POST) {
			this.method = method;
			this.url = url;
			initialize = true;
		} else
			throw new IllegalStateException("Unknown Http Method");
	}

	/*
	 * Load Methods
	 */

	public void onLoadMore() {
		log("onLoadMore()");
		mIsLoadingMore = true;
		lazyRestHttp.request(method, url);
	}

	/*
	 * Scroll Methods
	 */
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}
		if (next) {
			if (visibleItemCount == totalItemCount) {
				mProgressBarLoadMore.setVisibility(View.GONE);
				return;
			}
			boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
			if (!mIsLoadingMore && loadMore
					&& mCurrentScrollState != SCROLL_STATE_IDLE) {
				mProgressBarLoadMore.setVisibility(View.VISIBLE);
				mIsLoadingMore = true;
				onLoadMore();
			}
		}
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			view.invalidateViews();
		}

		mCurrentScrollState = scrollState;

		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}

	}

	protected void notifyDataReceived() {
		if (!hasOnScrollListener && getAdapter() != null
				&& getAdapter().getCount() > 0) {
			super.setOnScrollListener(this);
			hasOnScrollListener = true;
		} else {
			if (mProgressBarLoadMore.getVisibility() == View.VISIBLE)
				mProgressBarLoadMore.setVisibility(View.GONE);
		}
	}

	public void cancel() {
		lazyRestHttp.cancelPendingRequests(LazyRestHttp.HttpTag);
		if (mProgressBarLoadMore.getVisibility() == View.VISIBLE)
			mProgressBarLoadMore.setVisibility(View.GONE);
	}

	/*
	 * Setters
	 */
	@Override
	public void setAdapter(ListAdapter adapter) {
		log("setAdapter");
		super.setAdapter(adapter);
	}

	@Override
	public void setOnScrollListener(AbsListView.OnScrollListener l) {
		mOnScrollListener = l;
	}

	public void setOnResultListener(OnResultListener mOnResultListener) {
		if (lazyRestHttp != null)
			lazyRestHttp.setOnResultListener(mOnResultListener);
	}

	public void setOnErrorListener(OnErrorListener mOnErrorListener) {
		if (lazyRestHttp != null)
			lazyRestHttp.setOnErrorListener(mOnErrorListener);
	}

	protected void setIsLoadingMore(boolean isLoadingMore) {
		mIsLoadingMore = isLoadingMore;
	}

	public void setCacheMode(int cacheMode) {
		this.cacheMode = cacheMode;
	}

	protected void setNext(boolean next) {
		log("setNext : " + next);
		this.next = next;
	}

	public void setNext(String url) {
		if (!url.equals(this.url)) {
			this.url = url;
			setNext(true);
		}
	}

	/*
	 * Interfaces
	 */

	public interface OnResultListener {
		public void onResult(String response);
	}

	public interface OnErrorListener {
		public void onError(Exception exception);
	}

	/*
	 * Utils
	 */
	protected static void log(String msg) {
		if (msg != null)
			Log.d(TAG, msg);
	}
}