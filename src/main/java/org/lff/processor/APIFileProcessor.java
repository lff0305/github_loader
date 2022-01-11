package org.lff.processor;

import org.json.JSONObject;
import org.lff.Config;
import org.lff.Logger;
import org.lff.Utility;
import org.lff.exception.GithubException;

import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;

public class APIFileProcessor implements FileProcessor {
    private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private final StringBuffer sb;

    private Config config;

    public APIFileProcessor(Config config, StringBuffer sb) {
        this.config = config;
        this.sb = sb;
    }

    @Override
    public void getFromRemote() {
        this.process(config);
    }

    private void process(Config config) {
        String body = Utility.getFileMeta(config);
        if (Utility.isJsonArray(body)) {
            throw new GithubException("Invalid type (array found). A dir is set instead of a file ?");
        }
        if (Utility.isJsonObject(body)) {
            JSONObject o = new JSONObject(body);
            String download_url = o.getString("download_url");
            byte[] content = Utility.download(download_url, config.getToken());
            if (content == null) {
                throw new GithubException("Failed to download from " + download_url);
            }
            onFile(content);
        } else {
            throw new GithubException("Response is not a valid json");
        }
    }

    @Override
    public void onFile(byte[] content) {
        sb.append(new String(content, StandardCharsets.UTF_8));
    }
}
