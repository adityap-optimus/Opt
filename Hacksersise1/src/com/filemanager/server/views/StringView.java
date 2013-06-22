package com.filemanager.server.views;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.entity.StringEntity;
import com.filemanager.utils.Constants.Config;

/**
 * This is a custom view to show a string
 * @author Anubhav
 *
 */
public class StringView extends BaseView<String, Object[]> {

    @Override
    public HttpEntity render(HttpRequest request, String content, Object[] args) throws IOException {
        if (args != null) {
            content = String.format(content, args);
        }
        return new StringEntity(content, Config.ENCODING);
    }

}
