package org.lff;

public class Logger {

    private String name;

    public Logger(Class<?> clz) {
        this.name = clz.getName();
    }

    public static Logger getLogger(Class<?> clz) {
        return new Logger(clz);
    }

    public void info(String msg) {
        System.out.println("INFO: " + msg);
    }

    public synchronized void error(String msg, Exception e) {
        System.out.println("ERROR: " + msg);
        e.printStackTrace(System.out);
    }

    public void error(String msg) {
        System.out.println("ERROR: " + msg);
    }
}
