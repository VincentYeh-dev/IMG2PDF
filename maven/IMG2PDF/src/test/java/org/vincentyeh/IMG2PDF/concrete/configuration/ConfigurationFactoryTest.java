package org.vincentyeh.IMG2PDF.concrete.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.vincentyeh.IMG2PDF.framework.configuration.Configuration;
import org.vincentyeh.IMG2PDF.concrete.util.file.exception.FileNotExistsException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigurationFactoryTest {
    @TempDir
    static Path tempDir;

    @ParameterizedTest
    @NullSource
    public void testNullSource(File file) {
        assertThrows(IllegalArgumentException.class,
                () -> ConfigurationFactory.createFromPropertiesFile(file));

        assertThrows(IllegalArgumentException.class,
                () -> ConfigurationFactory.createValidOptionFromPropertiesFile(file));
    }

    @Test
    public void testNotExists() {
        assertThrows(FileNotExistsException.class,
                () -> ConfigurationFactory.createValidOptionFromPropertiesFile(new File("AAA.txt"))
        );

        assertThrows(FileNotExistsException.class,
                () -> ConfigurationFactory.createFromPropertiesFile(new File("AAA.txt"))
        );
    }

    @Test
    public void testInvalidOption() throws IOException {
        File file = createInvalidOptionProperties();
        Configuration configuration = ConfigurationFactory.createFromPropertiesFile(file);
        assertThrows(UnsupportedCharsetException.class,
                configuration::getDirectoryListCharset);

//        Configuration configuration1=ConfigurationFactory.createValidOptionFromPropertiesFile(file);

//        assertEquals(new DefaultConfiguration().getLocale().toLanguageTag(),configuration1.getLocale().toLanguageTag());
    }

    private File createInvalidOptionProperties() throws IOException {
        File file = Files.createTempFile(tempDir, null, ".properties").toFile();
        Properties properties = new Properties();
        properties.setProperty("dirlist-read-charset", "CCCCC");
        properties.setProperty("language", "AAA");
        FileOutputStream fos = new FileOutputStream(file);
        properties.store(fos, null);
        fos.close();
        return file;
    }


}
