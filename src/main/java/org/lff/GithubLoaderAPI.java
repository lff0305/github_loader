package org.lff;

import org.lff.processor.APIFileProcessor;
import org.lff.processor.Processor;

public class GithubLoaderAPI {
    public static String loadFile(String url, String token, String branch, String file) {
        Config config = Config.build(url, token, branch, file);
        StringBuffer sb = new StringBuffer();
        Processor processor = new APIFileProcessor(config, sb);
        processor.getFromRemote();
        return sb.toString();
    }

    public static String loadFile(String url, String token, String file) {
        return loadFile(url, token, Utility.MASTER, file);
    }
}
