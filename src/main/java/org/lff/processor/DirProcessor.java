package org.lff.processor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lff.Config;
import org.lff.Logger;
import org.lff.Utility;

import java.io.File;
import java.lang.invoke.MethodHandles;

public class DirProcessor implements Processor {

    private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private final Config config;

    public DirProcessor(Config config) {
        this.config = config;
    }

    @Override
    public void getFromRemote() {
        processDir(config, 0, "");
    }

    private void processDir(Config config, int depth, String baseDir) {
        if (depth > config.getMaxDepth()) {
            return;
        }
        String body = Utility.getFileMeta(config);
        if (Utility.isJsonArray(body)) {
            JSONArray list = new JSONArray(body);
            for (int i=0; i<list.length(); i++) {
                JSONObject item = list.getJSONObject(i);
                String name = item.getString("name");
                String type = item.getString("type");

                String fileName = Utility.removeTailing(config.getOutputDir()) + File.separator +
                        Utility.removeTailing((baseDir) +
                                File.separator + name);

                if (type.equals("file")) {
                    String download_url = item.getString("download_url");
                    byte[] content = Utility.download(download_url, config.getToken());
                    if (content == null) {
                        logger.error("Failed to download from " + download_url);
                        System.exit(1);
                    }
                    Utility.writeFile(fileName, content);
                }
                if (type.equals("dir")){
                    Utility.mkdir(fileName);
                    Config newConfig = config.subDir(name);
                    processDir(newConfig, depth + 1, baseDir + "/" + name);
                }
            }
        }
    }
}
