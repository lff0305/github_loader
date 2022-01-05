package org.lff.processor;

import org.json.JSONObject;
import org.lff.Config;
import org.lff.Logger;
import org.lff.Utility;

import java.lang.invoke.MethodHandles;

public class FileProcessor implements Processor {

    private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private Config config;

    public FileProcessor(Config config) {
        this.config = config;
    }

    @Override
    public void process() {
        this.process(config);
    }

    private void process(Config config) {
        String body = Utility.getFileMeta(config);
        if (Utility.isJsonArray(body)) {

        }
        if (Utility.isJsonObject(body)) {
            JSONObject o = new JSONObject(body);
            String download_url = o.getString("download_url");
            String content = Utility.download(download_url, config.getToken());
            if (content == null) {
                logger.error("Failed to download from " + download_url);
                System.exit(1);
            }
            Utility.writeFile(config.getOutputDir() + "/" + config.getFile(), content);
        }
    }
}
