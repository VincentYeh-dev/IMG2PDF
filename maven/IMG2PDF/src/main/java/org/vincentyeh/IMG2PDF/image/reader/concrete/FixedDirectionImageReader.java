package org.vincentyeh.IMG2PDF.image.reader.concrete;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.vincentyeh.IMG2PDF.image.reader.framework.ImageReader;
import org.vincentyeh.IMG2PDF.util.file.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class FixedDirectionImageReader extends ImageReader {
    @Override
    public BufferedImage read(File file) throws IOException, ImageProcessingException, MetadataException {
        FileUtils.checkExists(file);
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        if (metadata.containsDirectoryOfType(ExifIFD0Directory.class))
            return handleExifIFD0(file, metadata);

        return ImageIO.read(file);
    }

    private BufferedImage handleExifIFD0(File file, Metadata metadata) throws MetadataException, IOException {
        ExifIFD0Directory exifIFD0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        int orientation = exifIFD0.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        switch (orientation) {
            case 1: // [Exif IFD0] Orientation - Top, left side (Horizontal / normal)
                return ImageIO.read(file);
            case 6: // [Exif IFD0] Orientation - Right side, top (Rotate 90 CW)
                return rotateImage(ImageIO.read(file), 90);
            case 3: // [Exif IFD0] Orientation - Bottom, right side (Rotate 180)
                return rotateImage(ImageIO.read(file), 180);
            case 8: // [Exif IFD0] Orientation - Left side, bottom (Rotate 270 CW)
                return rotateImage(ImageIO.read(file), 270);
            default:
                throw new RuntimeException("image==null");
        }
    }
}
