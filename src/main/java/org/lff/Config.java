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


    public static Config build(CommandLine commandLine) {
        String url = commandLine.getOptionValue("url");
        logger.info("url = " + url);
        Config config = new Config();
        config.endpoint = url;
        String token = commandLine.getOptionValue("token");
        logger.info("token = " + token);
        config.token = token;
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

    public String getFile() {
        return this.file;
    }

    public String getToken() {
        return this.token;
    }

    public boolean isAuth() {
        return this.token != null && !this.token.isEmpty();
    }
}
