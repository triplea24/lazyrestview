package apl.vada.lib.lazyrestview.sample;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BookListAdapter extends ArrayAdapter<Book> implements Filterable {
	private ArrayList<Book> original;
	private ArrayList<Book> filtered;

	public BookListAdapter(Context context, ArrayList<Book> array) {
		super(context, R.layout.adapter_book_list, array);
		this.original = array;
		this.filtered = array;
	}

	protected static class ViewHolder {
		protected ViewGroup layout;
		protected CheckBox downloadBook;
		protected ImageView imageBook;
		protected TextView titleBook;
		protected TextView descriptionBook;
		protected ProgressBar progressBar;

		public ViewHolder(View view) {
			layout = (ViewGroup) view.findViewById(R.id.lnrBookListAdapter);
			downloadBook = (CheckBox) view
					.findViewById(R.id.chkDownloadBookAdapter);
			imageBook = (ImageView) view.findViewById(R.id.imgBookImageAdapter);
			titleBook = (TextView) view.findViewById(R.id.txtBookTitleAdapter);
			descriptionBook = (TextView) view
					.findViewById(R.id.txtBookDescAdapter);
			progressBar = (ProgressBar) view
					.findViewById(R.id.prgBookImageCompleteAdapter);
		}

		public void fill(final ArrayAdapter<Book> adapter, final Book item,
				final int position) {
			progressBar.setVisibility(View.VISIBLE);
			titleBook.setText(item.getTitle());
			descriptionBook.setText(item.getDesc());
		}
	}

	@Override
	public Book getItem(int position) {
		return filtered.get(position);
	}

	@Override
	public int getCount() {
		return filtered.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Book item = getItem(position);
		if (convertView == null) {
			convertView = MainActivity.inflater.inflate(
					R.layout.adapter_book_list, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.fill(this, item, position);
		return convertView;
	}

	@Override
	public Filter getFilter() {
		return new Filter() {

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				filtered = (ArrayList<Book>) results.values;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				String query = constraint.toString().toLowerCase();
				FilterResults filterResults = new FilterResults();
				ArrayList<Book> filteredArrayNames = new ArrayList<Book>();
				for (int i = 0; i < original.size(); i++) {
					Book item = original.get(i);
					if (item.getTitle().toLowerCase().contains(query)
							|| item.getDesc().toLowerCase().contains(query)) {
						filteredArrayNames.add(item);
					}
				}
				filterResults.count = filteredArrayNames.size();
				filterResults.values = filteredArrayNames;
				return filterResults;
			}
		};
	}
}
