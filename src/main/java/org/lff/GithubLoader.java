package org.lff;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;

import org.apache.commons.cli.*;

public class GithubLoader {


    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {
        Options options = new Options();
        Option opt = new Option(null, "url", true, "url of github api");
        opt.setRequired(true);
        options.addOption(opt);

        Option token = new Option(null, "token", true, "token of github api");
        token.setRequired(false);
        options.addOption(token);

        Option dir = new Option("d", "dir", true, "dir to load");
        dir.setRequired(false);
        options.addOption(dir);

        Option file = new Option("f", "file", true, "single file to load");
        file.setRequired(false);
        options.addOption(file);

        Option output = new Option(null, "output", true, "output directory");
        output.setRequired(true);
        options.addOption(output);

        Option create = new Option(null, "create", false, "create output directory if not exists");
        create.setRequired(false);
        options.addOption(create);

        CommandLine commandLine = null;
        DefaultParser parser = new DefaultParser();
        try {
            commandLine = parser.parse(options, args);
            Config config = Config.build(commandLine);
            process(config);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void process(Config config) {
        if (config.isFileMode()) {
            processFile(config);
        }
    }

    private static void processFile(Config config) {
        String url = config.getEndpoint() + "/raw" + config.getFile();
        logger.info("Load file " + url);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "Java 11 HttpClient Bot");
        if (config.isAuth()) {
            builder.header("Authorization", Utility.basicAuth("user", config.getToken()));
        }
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            logger.info("Response is " + response.statusCode() + response.body());
            String body = response.body();
        } catch (IOException e) {
            logger.error("Failed to load config", e);
            if (response != null) {
                logger.error("Response is " + response.statusCode() + " / " + response.body());
            } else {
                logger.error("Response is null");
            }
        } catch (InterruptedException e) {
            return;
        } finally {
        }
        return;
    }



    public String loadDir(String repo, String owner, String path) {
        String url = String.format("", repo, owner, path);
        return null;
    }

}
