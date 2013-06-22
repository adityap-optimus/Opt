package com.filemanager.utils;

import java.util.Map;

import org.apache.commons.fileupload.ParameterParser;
import org.apache.http.HttpRequest;

/**
 * This class is the HTTP get request parser
 * @author Anubhav
 *
 */
public class HttpGetParser {

    private static final String GET_METHOD = "GET";

    public static boolean isGetMethod(HttpRequest request) {
        String method = request.getRequestLine().getMethod();
        return GET_METHOD.equalsIgnoreCase(method);
    }

    /**
     * This method parses the HTTP request
     * @param request
     * @return
     */
    public Map<String, String> parse(HttpRequest request) {
        ParameterParser parser = new ParameterParser();
        parser.setLowerCaseNames(true);
        return parser.parse(getContent(request), '&');
    }

    public String getContent(HttpRequest request) {
        String uri = request.getRequestLine().getUri();
        int index = uri.indexOf('?');
        return index == -1 || index + 1 >= uri.length() ? null : uri.substring(index + 1);
    }

}
