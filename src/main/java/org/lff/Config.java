package org.lff;

import org.apache.commons.cli.CommandLine;

import java.lang.invoke.MethodHandles;

public class Config {

    private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private String endpoint;
    private String token;
    private String dir;
    private String file;
    private String outputDir;
    private boolean createOutputDir;
    private String branch;


    public static Config build(CommandLine commandLine) {
        String url = commandLine.getOptionValue("url");
        logger.info("url = " + url);
        Config config = new Config();
        config.endpoint = url;
        String token = commandLine.getOptionValue("token");
        logger.info("token = " + token);
        config.token = token;

        String branch = commandLine.getOptionValue("branch", "master");
        logger.info("branch = " + branch);
        config.branch = branch;

        String file = commandLine.getOptionValue("file");
        logger.info("file = " + file);
        config.file = file;
        String dir = commandLine.getOptionValue("dir");
        logger.info("dir = " + dir);
        config.dir = dir;
        String output = commandLine.getOptionValue("output");
        logger.info("output = " + output);
        config.outputDir = output;
        return config;
    }

    public boolean isFileMode() {
        return file != null && !file.isEmpty();
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public String getBranch() {
        return this.branch;
    }
    public String getToken() {
        return this.token;
    }

    public String getTenant() {
        String path = null;
        if (isFileMode()) {
            path = this.file;
        } else {
            path = this.dir;
        }
        path = Utility.removeLeadingTailing(path);
        String[] items = path.split("/");
        return items[0] + "/" + items[1];
    }

    public String getPath() {
        String path = null;
        if (isFileMode()) {
            path = this.file;
        } else {
            path = this.dir;
        }
        path = Utility.removeLeadingTailing(path);
        String[] items = path.split("/");
        int end = isFileMode()?items.length - 1 : items.length;
        StringBuffer result = new StringBuffer();
        for (int i=2; i<end; i++) {
            result.append(items[i]);
            result.append("/");
        }
        return Utility.removeTailing(result.toString());
    }

    public String getFile() {
        String path = null;
        if (isFileMode()) {
            path = this.file;
        } else {
            return "";
        }
        path = Utility.removeLeadingTailing(path);
        String[] items = path.split("/");
        return items[items.length - 1];
    }

    public boolean isAuth() {
        return this.token != null && !this.token.isEmpty();
    }

    public Object getOutput() {
        return this.outputDir;
    }
}
