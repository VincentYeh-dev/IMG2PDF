package org.vincentyeh.IMG2PDF.parameter;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * Size is the variable that define size of pages of PDFFile.
 * 
 * @author VincetYeh
 */
public enum PageSize {
	A0(PDRectangle.A0), A1(PDRectangle.A1), A2(PDRectangle.A2), A3(PDRectangle.A3), A4(PDRectangle.A4),
	A5(PDRectangle.A5), A6(PDRectangle.A6), LEGAL(PDRectangle.LEGAL), LETTER(PDRectangle.LETTER), DEPEND_ON_IMG(null);

	/**
	 * This is the constant used to create the PDF.
	 */
	private final PDRectangle pdrectangle;

	PageSize(PDRectangle pdrectangle) {
		this.pdrectangle = pdrectangle;
	}

	public PDRectangle getPdrectangle() {
		return pdrectangle;
	}

}