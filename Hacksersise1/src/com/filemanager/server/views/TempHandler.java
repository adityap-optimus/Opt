package com.filemanager.server.views;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.asfun.jangod.template.TemplateEngine;

import com.filemanager.utils.Constants.Config;

/**
 * This is the template handler
 * @author Anubhav
 *
 */
public class TempHandler {

    /* package */ static final TemplateEngine engine;

    static {
        engine = new TemplateEngine();
        engine.getConfiguration().setWorkspace(Config.SERV_TEMP_DIR);
        Map<String, Object> globalBindings = new HashMap<String, Object>();
        globalBindings.put("SERV_ROOT_DIR", Config.SERV_ROOT_DIR);
        engine.setEngineBindings(globalBindings);
    }

    /**
     * render the current view
     * @param tempFile
     * @param data
     * @return
     * @throws IOException
     */
    public static String render(String tempFile, Map<String, Object> data) throws IOException {
        return engine.process(tempFile, data);
    }

}
