package org.vincentyeh.IMG2PDF.concrete.util.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class GlobbingFileFilterTest {
    @Test
    public void testNullArgumentInput(){
        assertThrows(IllegalArgumentException.class,
                ()->new GlobbingFileFilter(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "|","*","\"","?","<",">","\0",":",
            "Hello|World",
            "\"Hello World\""
    })
    public void testIllegalFileName(String input){
        GlobbingFileFilter filter=new GlobbingFileFilter("*");
        assertFalse(filter.accept(new File(input)));
    }
    @ParameterizedTest
    @ValueSource(strings = {"Hello.txt","World.jpg","abc.png","doc.docx","data.db"})
    public void testNormal(String input){
        GlobbingFileFilter filter=new GlobbingFileFilter("*."+input.split("\\.")[1]);
        assertTrue(filter.accept(new File(input)));
    }


}
