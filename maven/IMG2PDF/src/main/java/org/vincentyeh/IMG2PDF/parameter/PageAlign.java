package org.vincentyeh.IMG2PDF.parameter;


/**
 * The class which define Alignment of page of PDF
 * 
 * @author vincent
 */
public class PageAlign {
//	水平對齊
	private final HorizontalAlign hori_align;
//	垂直對齊
	private final VerticalAlign verti_align;

	/**
	 * Create Align by enums.
	 *
	 * @param verti_align Vertical Align
	 * @param hori_align Horizontal Align
	 */
	public PageAlign(final VerticalAlign verti_align, final HorizontalAlign hori_align) {
		this.hori_align = hori_align;
		this.verti_align = verti_align;
	}

	public static PageAlign valueOf(String value){
		String[] verti_hori_align = value.split("-");
		return new PageAlign(VerticalAlign.valueOf(verti_hori_align[0]), HorizontalAlign.valueOf(verti_hori_align[1]));
	}
	public HorizontalAlign getHorizontal() {
		return hori_align;
	}

	public VerticalAlign getVertical() {
		return verti_align;
	}

	@Override
	public String toString() {
		return String.format("%s-%s", verti_align, hori_align);
	}

	public enum HorizontalAlign {
		RIGHT, LEFT, CENTER

	}

	public enum VerticalAlign {
		TOP, BOTTOM, CENTER
	}

}