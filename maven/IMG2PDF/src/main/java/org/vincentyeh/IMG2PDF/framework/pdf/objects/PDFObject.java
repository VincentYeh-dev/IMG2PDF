package org.vincentyeh.IMG2PDF.framework.pdf.objects;

import org.vincentyeh.IMG2PDF.framework.pdf.calculation.Position;
import org.vincentyeh.IMG2PDF.framework.pdf.calculation.Size;

public interface PDFObject<OBJECT>{
    OBJECT get();
    Size getSize();
    Position getPosition();
}
