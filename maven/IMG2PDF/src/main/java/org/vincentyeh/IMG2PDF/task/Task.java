package org.vincentyeh.IMG2PDF.task;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.jdom2.Element;
import org.vincentyeh.IMG2PDF.file.ImgFile;
import org.vincentyeh.IMG2PDF.pdf.page.Align;
import org.vincentyeh.IMG2PDF.pdf.page.PageDirection;
import org.vincentyeh.IMG2PDF.pdf.page.Size;

/**
 * <b>Task is the pre-work of the conversion</b>. All attributes of PDF file
 * will be define in this step. So the program that convert the images to PDF
 * doesn't need to do pre-work of conversion.
 * 
 * The function of Task:
 * <ol>
 * <li>define how to sort files</li>
 * <li>define password of PDF</li>
 * <li>compute the name of destination file</li>
 * <li>convert itself to XML element</li>
 * </ol>
 * 
 * @see Element
 * 
 * @author VincentYeh
 */
public abstract class Task {
	protected final Align align;
	protected final Size size;
	protected final String destination;
//	protected final String owner_pwd;
//	protected final String user_pwd;
	protected final ArrayList<ImgFile> imgs = new ArrayList<ImgFile>();
	protected final PageDirection defaultDirection;
	protected final boolean autoRotate;

	/**
	 * Create the task by arguments.
	 * 
	 * @param files       Source files.Only can handle files.
	 * @param destination The destination of PDF output
	 * @param own         owner password
	 * @param user        user password
	 * @param sortby      files will be sorted by Name or Date
	 * @param order       order by increase or decrease value
	 * @param align       Where should Images of PDF be located on PDF.
	 * @param size        Which size of pages of PDF.
	 * @throws FileNotFoundException When file is not exists.
	 */
	public Task(String destination, Align align, Size size, PageDirection defaultDirection, boolean autoRotate)
			throws FileNotFoundException {
		if (destination == null)
			throw new NullPointerException("destination is null.");
		this.align = align;
		this.size = size;
		this.destination = destination;
		this.defaultDirection = defaultDirection;
		this.autoRotate = autoRotate;
	}

	public Align getAlign() {
		return align;
	}

	public String getDestination() {
		return destination;
	}

	public Size getSize() {
		return size;
	}

	public PageDirection getDefaultDirection() {
		return defaultDirection;
	}
	public boolean getAutoRotate() {
		return autoRotate;
	}
}
