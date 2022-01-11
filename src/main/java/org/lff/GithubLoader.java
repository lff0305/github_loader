package org.lff;

import java.lang.invoke.MethodHandles;

import org.apache.commons.cli.*;
import org.lff.processor.DirProcessor;
import org.lff.processor.LocalFileProcessor;
import org.lff.processor.Processor;

public class GithubLoader {

    private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());


    public static void main(String[] args) {

        Options options = initCommandlineOptions();

        CommandLine commandLine = null;
        DefaultParser parser = new DefaultParser();
        try {
            commandLine = parser.parse(options, args);
            Config config = Config.build(commandLine);
            createOutputDir(config);
            Processor processor = config.isFileMode() ? new LocalFileProcessor(config) : new DirProcessor(config);
            processor.getFromRemote();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static Options initCommandlineOptions() {
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

        Option branch = new Option("b", "branch", true, "branch");
        branch.setRequired(false);
        options.addOption(branch);

        Option file = new Option("f", "file", true, "single file to load");
        file.setRequired(false);
        options.addOption(file);

        Option output = new Option(null, "output", true, "output directory");
        output.setRequired(true);
        options.addOption(output);

        Option create = new Option("c", "create", false, "create output directory if not exists");
        create.setRequired(false);
        options.addOption(create);
        return options;
    }

    private static void createOutputDir(Config config) {
        if (config.isCreateOutputDir()) {
            if (!Utility.mkdir(config.getOutputDir())) {
                logger.error("Failed to create output dir");
                System.exit(1);
            }
        } else {
            logger.info("No need to create output dir " + config.getOutputDir());
        }
    }
}
