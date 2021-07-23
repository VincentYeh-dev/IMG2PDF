package org.vincentyeh.IMG2PDF.framework.pdf.objects;

import org.vincentyeh.IMG2PDF.framework.pdf.factory.Position;
import org.vincentyeh.IMG2PDF.framework.pdf.factory.Size;

public interface PDFObject<OBJECT>{
    OBJECT get();
    Size getSize();
    Position getPosition();
}
