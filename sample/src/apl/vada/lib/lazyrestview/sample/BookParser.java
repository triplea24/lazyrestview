package apl.vada.lib.lazyrestview.sample;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookParser {

	public static ArrayList<Book> getBookList(String string) throws Exception {
		ArrayList<Book> list = new ArrayList<Book>();
		JSONObject json = new JSONObject(string);
		JSONArray array = json.getJSONArray("Books");
		for (int i = 0; i < array.length(); i++) {
			list.add(getBook(array.getJSONObject(i).toString()));
		}
		return list;
	}

	public static int getPages(String string) {
		try {
			JSONObject json = new JSONObject(string);
			return (int) json.getInt("Page");
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static Book getBook(String string) {
		try {
			JSONObject json = new JSONObject(string);
			String ID = json.getString("ID");
			String Title = json.getString("Title");
			String subtitle = "";
			if (json.has("SubTitle")) {
				subtitle = json.getString("SubTitle");
			}
			String Description = json.getString("Description");
			String Author = "";
			if (json.has("Author")) {
				Author = json.getString("Author");
			}
			String ISBN = json.getString("isbn");
			int Page = 0;
			if (json.has("Page")) {
				Page = json.getInt("Page");
			}
			int Year = 0;
			if (json.has("Year")) {
				Year = json.getInt("Year");
			}
			String Publisher = "";
			if (json.has("Publisher")) {
				Publisher = json.getString("Publisher");
			}
			String Image = json.getString("Image");
			String Download = "";
			if (json.has("Download")) {
				Download = json.getString("Download");
			}
			Book book = new Book(ID, Title, subtitle, Description, Author,
					ISBN, Page, Year, Publisher, Image, Download);
			return book;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static int getInt(String key, String string) {
		try {
			JSONObject json = new JSONObject(string);
			return (int) json.getInt(key);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getString(String key, String string) {
		try {
			JSONObject json = new JSONObject(string);
			return (String) json.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
