package com.filemanager.server.views;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;

/**
 * This class is definition of custom view , how will they be shown
 * 
 * @author Anubhav
 * 
 */
public class ViewFactory {

	static class Holder {
		static ViewFactory instance = new ViewFactory();
	}

	public static ViewFactory getSingleton() {
		return Holder.instance;
	}

	/** Type of {@link StringView } */
	public static final int TYPE_STRING = 0x0001;
	/** Type of {@link FileView } */
	public static final int TYPE_FILE = 0x0002;
	/** Type of {@link TempView } */
	public static final int TYPE_TEMPLATE = 0x0003;

	private ViewFactory() {
	}

	/**
	 * This is the http entity renderer
	 * 
	 * @param request
	 * @param view
	 * @param content
	 * @param args
	 * @return
	 * @throws IOException
	 */
	public <T, A> HttpEntity render(HttpRequest request, BaseView<T, A> view,
			T content, A args) throws IOException {
		return view.render(request, content, args);
	}

	/**
	 * This function checks for the type of view to be rendered
	 * @param request
	 * @param type
	 * @param content
	 * @param args
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public <T, A> HttpEntity render(HttpRequest request, int type, T content,
			A args) throws IOException {
		BaseView<T, A> view;
		switch (type) {
		case TYPE_STRING:
			view = (BaseView<T, A>) new StringView();
			break;
		case TYPE_FILE:
			view = (BaseView<T, A>) new FileView();
			break;
		case TYPE_TEMPLATE:
			view = (BaseView<T, A>) new TempView();
			break;
		default:
			throw new IOException("Unsupport view type.");
		}
		return render(request, view, content, args);
	}

	/**
	 * @see #render(HttpRequest, int, Object, Object)
	 */
	public <T> HttpEntity render(HttpRequest request, int type, T content)
			throws IOException {
		return this.render(request, type, content, null);
	}

	/**
	 * @see StringView#render(HttpRequest, String, Object[])
	 */
	public HttpEntity renderString(HttpRequest request, String content,
			Object[] args) throws IOException {
		return render(request, new StringView(), content, args);
	}

	/**
	 * @see #renderString(HttpRequest, String, Object[])
	 */
	public HttpEntity renderString(HttpRequest request, String content)
			throws IOException {
		return this.renderString(request, content, null);
	}

	/**
	 * @see FileView#render(HttpRequest, File, String)
	 */
	public HttpEntity renderFile(HttpRequest request, File file,
			String contentType) throws IOException {
		return render(request, new FileView(), file, contentType);
	}

	/**
	 * @see #renderFile(HttpRequest, File, String)
	 */
	public HttpEntity renderFile(HttpRequest request, File file)
			throws IOException {
		return this.renderFile(request, file, null);
	}

	/**
	 * @see TempView#render(HttpRequest, String, Map)
	 */
	public HttpEntity renderTemp(HttpRequest request, String tempFile,
			Map<String, Object> data) throws IOException {
		return render(request, new TempView(), tempFile, data);
	}

	/**
	 * @see #renderString(HttpRequest, String, Object[])
	 */
	public HttpEntity renderTemp(HttpRequest request, String tempFile)
			throws IOException {
		return this.renderTemp(request, tempFile, null);
	}

}
