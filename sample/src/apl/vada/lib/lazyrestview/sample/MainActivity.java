package apl.vada.lib.lazyrestview.sample;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import apl.vada.lib.lazyrestview.LazyRestView;
import apl.vada.lib.lazyrestview.LazyRestView.OnErrorListener;
import apl.vada.lib.lazyrestview.LazyRestView.OnResultListener;

public class MainActivity extends ActionBarActivity {

	public LazyRestView restView;
	public static Context context;
	public static LayoutInflater inflater;
	public ArrayList<Book> array = new ArrayList<Book>();
	public ArrayAdapter<Book> adapter;
	public String url = "http://it-ebooks-api.info/v1/search/php/page/";
	public int page = 1;
	public Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		restView = (LazyRestView) findViewById(R.id.restView);
		button = (Button) findViewById(R.id.btnNextActivity);
		adapter = new BookListAdapter(this, array);
		restView.setAdapter(adapter);
		restView.setOnResultListener(new OnResultListener() {

			@Override
			public void onResult(String response) {
				try {
					Log.d("LazyRestViewSample", "response");
					ArrayList<Book> result = BookParser.getBookList(response);
					for (Book book : result) {
						Log.d("LazyRestViewSample", book.title);
						adapter.add(book);
					}
					Log.d("LazyRestViewSample", adapter.getCount() + "");
					restView.setNext(url + page++);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		restView.setOnErrorListener(new OnErrorListener() {

			@Override
			public void onError(Exception exception) {
				exception.printStackTrace();
			}
		});
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, A.class));
			}
		});
		restView.init(LazyRestView.METHOD_GET, url + page++);
		restView.setCacheMode(LazyRestView.CACHE_LOCAL_ON);
		restView.onStart();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		restView.onStop();
		super.onPause();
	}

	@Override
	protected void onStop() {
		restView.onStop();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		restView.onDestroy();
		super.onDestroy();
	}
}
