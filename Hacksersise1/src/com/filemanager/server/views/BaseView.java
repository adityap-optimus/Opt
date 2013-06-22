package com.filemanager.server.views;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;

/**
 * This is a generic class for a view
 * @author Anubhav
 *
 * @param <T>
 * @param <A>
 */
public abstract class BaseView<T, A> {

	/**
	 * THis is a render function for a view
	 * @param request
	 * @param content
	 * @return
	 * @throws IOException
	 */
    public HttpEntity render(HttpRequest request, T content) throws IOException {
        return this.render(request, content, null);
    }

    /**
     * This function renders the view
     * @param request
     * @param content
     * @param args
     * @return
     * @throws IOException
     */
    public abstract HttpEntity render(HttpRequest request, T content, A args) throws IOException;

}
