package me.mrfcker.commands;

import java.awt.Color;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.mrfcker.interactiveJava.Command;
import me.mrfcker.interactiveJava.console.Console;
import me.mrfcker.utils.StringUtils;

public class Directory implements Command {

	@Override
	public Object execute(Console console, String[] args) {
		String path;
		if (args.length > 1)
			path = console.getVariable("PWD") + StringUtils.concat(args, 1);
		else
			path = console.getVariable("PWD").toString();

		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			StringBuilder sb = new StringBuilder();
			File[] files = file.listFiles();

			for (int i = 0; i < files.length; ++i) {
				File f = files[i];
				String name = f.getName();
				String date = new SimpleDateFormat().format(new Date(f.lastModified()));
				boolean dir = f.isDirectory();
				String size = formatSize(f.length());
				console.printColored(date + "\t" + size + "\t" + name, dir ? Color.ORANGE : Color.GRAY);
				if (dir)
					sb.append("<DIR>");
				sb.append("\t").append(date).append("\t").append(size).append("\t").append(name);
				if (i < files.length - 1)
					sb.append("\n");
			}
			
			if (files.length == 0) {
				sb.append(path).append(" is empty.");
				console.printColored(sb.toString(), Color.GRAY);
			}
			
			return sb.toString();
		}
		else
			console.printError(path + " is not an existing directory!");
		return null;
	}

	private String formatSize(long bytes) {
		long KB = bytes/1000;
		if (KB > 0) {
			long MB = KB/1000;
			if (MB > 0) {
				long GB = MB/1000;
				if (GB > 0)
					return GB + "," + MB%1000 + " GB";
				return  MB + "," + KB%1000 + " MB";
			}
			return KB + "," + bytes%1000 + " KB";
		}
		return bytes + " B";
	}

	@Override
	public String help() {
		return "dir - Shows files of the current directory (PWD).\n"
				+ "dir {DIR} - Shows files of {DIR}.";
	}

}
