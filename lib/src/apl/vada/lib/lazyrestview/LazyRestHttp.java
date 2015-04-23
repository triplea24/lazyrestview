package apl.vada.lib.lazyrestview;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.text.TextUtils;
import apl.vada.lib.lazyrestview.LazyRestView.OnErrorListener;
import apl.vada.lib.lazyrestview.LazyRestView.OnResultListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LazyRestHttp {

	public static final String HttpTag = LazyRestHttp.class.getSimpleName();
	private WeakReference<LazyRestView> view;
	private RequestQueue requestQueue;
	private Context context;
	private static LazyRestHttp mInstance;
	private OnResultListener mOnResultListener;
	private OnErrorListener mOnErrorListener;
	private boolean hasView;
	private LazyRestDatabase database;

	protected LazyRestHttp(Context context, LazyRestView restView) {
		this.context = context;
		this.view = new WeakReference<LazyRestView>(restView);
		hasView = view != null && view.get() != null;
		database = new LazyRestDatabase(context);
		mInstance = this;
	}

	public static synchronized LazyRestHttp getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(context);
		}
		return requestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? HttpTag : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(HttpTag);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (requestQueue != null) {
			requestQueue.cancelAll(tag);
		}
	}

	public void onStop() {
		if (requestQueue != null) {
			requestQueue.cancelAll(HttpTag);
		}
	}

	public void onDestroy() {
		if (database != null)
			database.close();
	}

	public void request(final int method, final String url) {
		if (hasView && view.get().cacheMode == LazyRestView.CACHE_LOCAL_ON) {
			String response = database.get(url);
			if (response != "") {
				LazyRestView.log("get from cache : " + url);
				if (hasView) {
					view.get().setNext(false);
				}
				mOnResultListener.onResult(response);
				if (hasView) {
					view.get().notifyDataReceived();
					view.get().setIsLoadingMore(false);
				}
				return;
			}
		}
		StringRequest request = new StringRequest(method, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (hasView) {
							view.get().setNext(false);
						}
						if (hasView
								&& view.get().cacheMode == LazyRestView.CACHE_LOCAL_ON
								&& database.get(url) == "")
							database.save(url, response);
						mOnResultListener.onResult(response);
						if (hasView) {
							view.get().notifyDataReceived();
							view.get().setIsLoadingMore(false);
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						LazyRestView.log(error.getMessage());
						mOnErrorListener.onError(error);
						if (hasView) {
							view.get().setIsLoadingMore(false);
							view.get().setNext(false);
							view.get().notifyDataReceived();
						}
					}
				});
		request.setTag(HttpTag);
		getRequestQueue().add(request);
	}

	public void setOnResultListener(OnResultListener mOnResultListener) {
		this.mOnResultListener = mOnResultListener;
	}

	public void setOnErrorListener(OnErrorListener mOnErrorListener) {
		this.mOnErrorListener = mOnErrorListener;
	}
}