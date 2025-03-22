package mx.engineer.utils.file;

import java.io.File;

import mx.engineer.utils.general.ValidatePathSistem;
import mx.engineer.utils.string.StringUtils;



public class PrintDirectoryTree {

	/**
	 * Pretty print the directory tree and its file names.
	 * 
	 * @param folder must be a folder.
	 * @return
	 */
	public static String printDirectoryTree(File folder) {
//		folder = new File(ValidatePathSistem.getUrlSistem(folder.getAbsolutePath()));
		if (!folder.isDirectory()) {
			System.out.println("ERROR :: folder is not a Directory");
			return "error - ";
		}

		int indent = 0;
		StringBuilder sb = new StringBuilder();
		printDirectoryTree(folder, indent, sb);
		return sb.toString();
	}

	private static void printDirectoryTree(File folder, int indent, StringBuilder sb) {
		if (!folder.isDirectory()) {
			System.out.println("ERROR :: folder is not a Directory");
		}
		sb.append(getIndentString(indent));
		sb.append("+--");
		sb.append(folder.getName());
		sb.append("/");
		sb.append("\n");
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				printDirectoryTree(file, indent + 1, sb);
			} else {
				printFile(file, indent + 1, sb);
			}
		}
	}

	private static void printFile(File file, int indent, StringBuilder sb) {
		sb.append(getIndentString(indent));
		sb.append("+--");
		sb.append(file.getName());
		sb.append("\n");
	}

	private static String getIndentString(int indent) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			sb.append("|  ");
		}
		return sb.toString();
	}
	
	public static void main(String argrs[]) {
		File test = new File("/home/share/");
		System.out.println(PrintDirectoryTree.printDirectoryTree(test));
	}

}
