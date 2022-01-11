package org.lff.processor;

public interface FileProcessor extends Processor {
    public void onFile(byte[] content);
}
