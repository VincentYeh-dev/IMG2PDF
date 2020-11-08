package org.vincentyeh.IMG2PDF.sample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.vincentyeh.IMG2PDF.commandline.MainProgram;

public class WalkAnimation {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		File project_root = new File("").getAbsoluteFile().getParentFile().getParentFile();
		File sample_root = new File(project_root, "sample\\walk-animation");
		File taskslist_destination = new File(sample_root, "taskslist\\test.xml");
		File image_sources_dir = new File(sample_root, "image-sources").getAbsoluteFile();

		File sources_list = new File(sample_root, "dirlist.txt").getAbsoluteFile();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sources_list), "UTF-8"));
		writer.write(image_sources_dir.getAbsolutePath() + "\n\n");
		writer.close();

		String create_command = "create " 
				+ "-pz A4 " 
				+ "-ps NUMERTIC " 
				+ "-pa CENTER|CENTER " 
				+ "-pdi Vertical "
				+ "-par yes " 
				+ "-po INCREASE " 
				+ "-pupwd 1234AAA " 
				+ "-popwd 1234AAA " 
				+ "-pp 11 " 
				+ "-pdst "
				+ sample_root.getAbsolutePath() 
				+ "\\output\\$PARENT{0}.pdf " 
				+ "-pdi Vertical " + "-ldst "
				+ taskslist_destination.getAbsolutePath() 
				+ " " 
				+ "import -s " 
				+ sources_list.getAbsolutePath();

		String convert_command = "convert "+ taskslist_destination.getAbsolutePath();
		
		MainProgram.main(create_command.split("\\s"));
		MainProgram.main(convert_command.split("\\s"));
		
//		new TaskListCreator(str);
//		new TaskProcessor(taskslist_destination.getAbsolutePath());

	}

}
