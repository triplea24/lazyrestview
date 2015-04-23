package apl.vada.lib.lazyrestview.sample;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Book {
	protected String id;
	protected String title;
	protected String subtitle;
	protected String desc;
	protected String author;
	protected String isbn;
	protected int page;
	protected int year;
	protected String publisher;
	protected String image;
	protected String download;

	public Book(@NonNull String id, @NonNull String title,
			@Nullable String subtitle, @NonNull String desc,
			@NonNull String author, @NonNull String isbn, @NonNull int page,
			@NonNull int year, @NonNull String publisher,
			@NonNull String image, @NonNull String download) {
		this.id = id;
		this.title = title;
		this.subtitle = subtitle;
		this.desc = desc;
		this.author = author;
		this.isbn = isbn;
		this.page = page;
		this.year = year;
		this.publisher = publisher;
		this.image = image;
		this.download = download;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

}
