package org.vincentyeh.IMG2PDF.task;

import java.io.File;
import java.io.FileNotFoundException;

import org.jdom2.Element;
import org.junit.Test;
import org.vincentyeh.IMG2PDF.file.ImgFile;
import org.vincentyeh.IMG2PDF.file.PDFFile;

public class TaskTest {
	@Test(expected = NullPointerException.class)
	public void testCreateTaskWithNulls1() throws Exception {
		String str = "test_file\\raw\\TEST\\";
		File[] f=(new File(str)).listFiles();
		
		new Task(null, null, null, null,ImgFile.SORTBY_NAME,ImgFile.ORDER_INCREASE,PDFFile.ALIGN_CENTER,PDFFile.Size.A4);
	}
	@Test(expected = NullPointerException.class)
	public void testCreateTaskWithNulls2() throws Exception {
		String str = "test_file\\raw\\TEST\\";
		File[] f=(new File(str)).listFiles();
		
		new Task(f, null, null, null, ImgFile.SORTBY_NAME,ImgFile.ORDER_INCREASE,PDFFile.ALIGN_CENTER,PDFFile.Size.A4);
	}
	
	@Test
	public void testCreateTaskWithNulls3() throws Exception {
		String str = "test_file\\raw\\TEST\\";
		File[] f=(new File(str)).listFiles();
		new Task(f, "test.pdf", null, null,ImgFile.SORTBY_NAME,ImgFile.ORDER_INCREASE,PDFFile.ALIGN_CENTER,PDFFile.Size.A4);
	}

	@Test
	public void testCreateTaskWith() throws Exception {
		String str = "test_file\\raw\\TEST\\";
		File[] f=(new File(str)).listFiles();
		new Task(f, "test.pdf", null, null,ImgFile.SORTBY_NAME,ImgFile.ORDER_INCREASE,PDFFile.ALIGN_CENTER,PDFFile.Size.A4);
	}
	
	@Test
	public void testCreateTaskWithCorrectArgument() throws Exception {
		String str = "test_file\\raw\\TEST\\";
		File[] f=(new File(str)).listFiles();
		for(int i=0x01;i<=0x04;i++) {
			for(int j=0x01;j<=0x04;j++) {
				new Task(f, "test.pdf", null, null,
						ImgFile.SORTBY_NAME,ImgFile.ORDER_INCREASE,
						i<<4|j<<0,PDFFile.Size.A4);
			}
		}
	}
	
	@Test
	public void testCreateTaskWithIllegalArgument() throws FileNotFoundException{
		String str = "test_file\\raw\\TEST\\";
		File[] f=(new File(str)).listFiles();
		for(int i=0x05;i<=0x0F;i++) {
			for(int j=0x05;j<=0x0F;j++) {
				try {
//					new Task(f, "test.pdf", null, null,
//							ImgFile.SORTBY_NAME,ImgFile.ORDER_INCREASE,
//							i<<4|j<<0,PDFFile.Size.A4);
//					assertTrue(false);
				} catch (IllegalArgumentException e) {
					
				}
			}
		}
			
	}
	
	@Test(expected = NullPointerException.class)
	public void testCreateTaskByElement() {
		Element e=new Element("TASK");
		PDFFile.Size size=PDFFile.Size.getSizeFromString("A4");
//		e.setAttribute("destination", "A");
//		e.setAttribute("size", size.getStrSize());
//		e.setAttribute("align", 15 + "");
//		e.setAttribute("owner", "");
//		e.setAttribute("user", "");
		Task task=new Task(e);
	}
	
}
