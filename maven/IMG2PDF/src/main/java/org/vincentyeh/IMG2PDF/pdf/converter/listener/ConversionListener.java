package org.vincentyeh.IMG2PDF.pdf.converter.listener;

import org.vincentyeh.IMG2PDF.task.framework.Task;

import java.io.File;


/**
 * This interface can listen the event when conversion started.
 * @author vincent
 */
public interface ConversionListener {

	void initializing(Task task);

	/**
	 * When the single image written to a page.
	 * @param index the index of image files
	 */
	void onConverting(int index, File file);
	
	/**
	 * When the conversion is done.
	 */
	void onConversionComplete();

	void onFinally();
}
