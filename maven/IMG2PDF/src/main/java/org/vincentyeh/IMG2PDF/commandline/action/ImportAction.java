package org.vincentyeh.IMG2PDF.commandline.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import org.vincentyeh.IMG2PDF.file.FileFilterHelper;
import org.vincentyeh.IMG2PDF.file.ImgFile;
import org.vincentyeh.IMG2PDF.file.text.UTF8InputStream;
import org.vincentyeh.IMG2PDF.task.Task;
import org.vincentyeh.IMG2PDF.task.TaskList;
import org.vincentyeh.IMG2PDF.util.NameFormatter;

import net.sourceforge.argparse4j.annotation.Arg;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

public class ImportAction extends CreateAction {

	@Arg(dest = "source")
	protected ArrayList<String> sources;

	public ImportAction() {

	}

	@Override
	public void start() {
		try {
			createFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setupByNamespace(Namespace ns) {
		super.setupByNamespace(ns);
		this.sources = (ArrayList<String>) ns.get("source");
	}

	public void createFile() throws IOException {
//		ConfigureTaskList tasks = new ConfigureTaskList();
		TaskList tasks=new TaskList();
		
		for (String source : sources) {
			tasks.addAll(importTasksFromTXT(new File(source)));
		}
		tasks.toXMLFile(new File(list_destination));
	}

	public static void setupParser(Subparsers subparsers) {
		Subparser import_parser = subparsers.addParser("import").help("Type \"create -h\" to get more help.");
		import_parser.setDefault("action", new ImportAction());
		import_parser.addArgument("-s", "--source").nargs("*");
	}
}