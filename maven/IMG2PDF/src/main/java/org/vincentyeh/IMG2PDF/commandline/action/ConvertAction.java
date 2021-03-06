package org.vincentyeh.IMG2PDF.commandline.action;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.vincentyeh.IMG2PDF.commandline.CheckHelpParser;
import org.vincentyeh.IMG2PDF.commandline.Configuration;
import org.vincentyeh.IMG2PDF.commandline.action.exception.HelperException;
import org.vincentyeh.IMG2PDF.pdf.converter.ConversionListener;
import org.vincentyeh.IMG2PDF.pdf.converter.PDFConverter;
import org.vincentyeh.IMG2PDF.pdf.document.ImagesPDFDocument;
import org.vincentyeh.IMG2PDF.task.Task;
import org.vincentyeh.IMG2PDF.task.TaskList;

public class ConvertAction extends AbstractAction {

	protected final File[] tasklist_sources;
	protected final boolean open_when_complete;

	private static final Option opt_help;

	static {
		opt_help = createOption("h", "help", "help_convert");
	}

	public ConvertAction(String[] args) throws MissingOptionException, ParseException {
		super(getLocaleOptions());

		CommandLine cmd = (new CheckHelpParser(opt_help)).parse(options, args);

		if (cmd.hasOption("-h"))
			throw new HelperException(options);

		String[] str_sources = cmd.getOptionValues("tasklist_source");

		open_when_complete = cmd.hasOption("o");

		tasklist_sources = new File[str_sources.length];
		for (int i = 0; i < tasklist_sources.length; i++) {
			System.out.println(Configuration.getResString("source_tasklist_verifying"));
			tasklist_sources[i] = new File(str_sources[i]);

			System.out.println("\t[" + Configuration.getResString("common_verifying") + "] "
					+ tasklist_sources[i].getAbsolutePath());
			System.out.print("\t");
			if (!tasklist_sources[i].exists()) {
				System.err.printf(Configuration.getResString("err_filenotfound") + "\n",
						tasklist_sources[i].getAbsolutePath());
//				System.err.println("File not found:" + tasklist_sources[i].getAbsolutePath());
				continue;
			} else if (tasklist_sources[i].isDirectory()) {
				System.err.printf(Configuration.getResString("err_path_is_file") + "\n",
						tasklist_sources[i].getAbsolutePath());
				continue;
			} else {
				System.out.println("[" + Configuration.getResString("common_verified") + "] "
						+ tasklist_sources[i].getAbsolutePath());
			}

		}

	}

	@Override
	public void start() throws Exception {
		System.out.println(Configuration.getResString("import_tasklists"));
		for (File src : tasklist_sources) {
			System.out.print(
					"\t[" + Configuration.getResString("common_importing") + "] " + src.getAbsolutePath() + "\n");
			TaskList tasks = new TaskList(src);

			System.out
					.print("\t[" + Configuration.getResString("common_imported") + "] " + src.getAbsolutePath() + "\n");

			ExecutorService executor = Executors.newSingleThreadExecutor();
			for (Task task : tasks) {
				ImagesPDFDocument result = null;

				try {
					PDFConverter pdf = new PDFConverter(task);
					pdf.setListener(listener);
					Future<ImagesPDFDocument> future = executor.submit(pdf);
					try {
						result = future.get();
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					result.save();

					if (open_when_complete) {
						Desktop desktop = Desktop.getDesktop();

						File dst = new File(task.getPDFDestination());

						if (dst.exists())
							try {
								desktop.open(dst);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}

				} catch (IOException e) {
					e.printStackTrace();
				} catch (RuntimeException e) {
					e.printStackTrace();
				} finally {
					try {
						if (result != null)
							result.close();
					} catch (IOException ignore) {
					}
				}

			}
			executor.shutdown();
		}

	}

	private static Options getLocaleOptions() {
		Options options = new Options();
		Option opt_tasklist_source = createArgOption("lsrc", "tasklist_source", "help_convert_tasklist_source");
		opt_tasklist_source.setRequired(true);

		Option opt_open_when_complete = createOption("o", "open_when_complete", "help_create_pdf_align");

		options.addOption(opt_help);
		options.addOption(opt_tasklist_source);
		options.addOption(opt_open_when_complete);

		Option opt_mode = new Option("m", "mode", true, "mode");
		options.addOption(opt_mode);

		return options;
	}

	private ConversionListener listener = new ConversionListener() {
		private int size_of_imgs;
		private double perImg;
		private double progress = 0;

		@Override
		public void onConversionPreparing(Task task) {
			size_of_imgs = task.getImgs().size();
			perImg = (10. / size_of_imgs);
			System.out.printf("###%s###\n", Configuration.getResString("pdf_conversion_task"));
			System.out.printf("%s:%s\n", Configuration.getResString("arg_pdf_dst"), task.getPDFDestination());
			System.out.printf("%sname:%s\n", Configuration.getResString("common_name"),
					new File(task.getPDFDestination()).getName());
			System.out.printf("%s->", Configuration.getResString("common_progress"));
			System.out.print("0%[");

		}

		@Override
		public void onConverting(int index) {
			progress += perImg;
			while (progress >= 1) {
				System.out.print("=");
				progress -= 1;
			}
		}

		@Override
		public void onConversionComplete() {
			System.out.print("]%100\n");

//			try {
//				Runtime.getRuntime().exec("explorer.exe /select," + );
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

		@Override
		public void onConversionFail(int index, Exception e) {
			System.out.print("CONVERSION FAIL]\n");
			System.err.println(e.getMessage());
//			e.printStackTrace();

		}

		@Override
		public void onImageReadFail(int index, IOException e) {
			System.out.print("IMAGE READ FAIL]\n");
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}

	};
}
