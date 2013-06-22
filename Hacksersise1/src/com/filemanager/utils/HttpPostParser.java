package com.filemanager.utils;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.fileupload.ParameterParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.util.EntityUtils;

/**
 * This is the HTTP post request parser
 * @author Anubhav
 *
 */
public class HttpPostParser {

    private static final String POST_METHOD = "POST";

    public static boolean isPostMethod(HttpRequest request) {
        String method = request.getRequestLine().getMethod();
        return POST_METHOD.equalsIgnoreCase(method);
    }

    /**
     * This method parses the HTTP post request
     * @param request
     * @return
     * @throws IOException
     */
    public Map<String, String> parse(HttpRequest request) throws IOException {
        ParameterParser parser = new ParameterParser();
        parser.setLowerCaseNames(true);
        return parser.parse(getContent(request), '&');
    }

    public String getContent(HttpRequest request) throws IOException {
        HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
        return EntityUtils.toString(entity);
    }

}
