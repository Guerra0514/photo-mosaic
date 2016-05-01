package com.refactula.photomosaic.dataset;

import com.refactula.photomosaic.image.ArrayImage;
import com.refactula.photomosaic.image.ColorChannel;
import com.refactula.photomosaic.image.Image;
import com.refactula.photomosaic.utils.IOUtils;

import java.io.*;

public class EightyMillionTinyImages extends StreamImageDataset {

    private static final String DATASET_URL = "http://horatio.cs.nyu.edu/mit/tiny/data/tiny_images.bin";

    public static final int IMAGE_WIDTH = 32;
    public static final int IMAGE_HEIGHT = 32;

    public static final int SIZE = 79302017;

    private static final int IMAGE_SIZE_BYTES = ColorChannel.values().length * IMAGE_WIDTH * IMAGE_HEIGHT;

    private final ArrayImage buffer = new ArrayImage(IMAGE_WIDTH, IMAGE_HEIGHT);

    private DataInputStream input = null;

    @Override
    protected void changePosition(int index) throws IOException {
        close();
        input = new DataInputStream(new BufferedInputStream(
                IOUtils.connectHttp(DATASET_URL, (long) index * IMAGE_SIZE_BYTES)
        ));
    }

    @Override
    protected Image next() throws IOException {
        try {
            buffer.readFrom(input);
            return buffer;
        } catch (EOFException e) {
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        if (input != null) {
            input.close();
            input = null;
        }
    }

}
