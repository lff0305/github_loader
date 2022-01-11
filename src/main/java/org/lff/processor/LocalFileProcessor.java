package org.lff.processor;

import org.json.JSONObject;
import org.lff.Config;
import org.lff.Logger;
import org.lff.Utility;

import java.lang.invoke.MethodHandles;

public class LocalFileProcessor implements FileProcessor {

    private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private Config config;

    public LocalFileProcessor(Config config) {
        this.config = config;
    }

    @Override
    public void getFromRemote() {
        this.process(config);
    }

    private void process(Config config) {
        String body = Utility.getFileMeta(config);
        if (Utility.isJsonArray(body)) {

        }
        if (Utility.isJsonObject(body)) {
            JSONObject o = new JSONObject(body);
            String download_url = o.getString("download_url");
            byte[] content = Utility.download(download_url, config.getToken());
            if (content == null) {
                logger.error("Failed to download from " + download_url);
                System.exit(1);
            }
            onFile(content);
        }
    }

    @Override
    public void onFile(byte[] content) {
        Utility.writeFile(config.getOutputDir() + "/" + config.getFile(), content);
    }
}
