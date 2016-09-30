package com.dev88.find.in.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.dev88.find.in.files.constraint.Constraint;

public class Main {
	private static List<File> rootFolders;
	private static List<Constraint> constraints;
	private static List<String> words;
	
	public static void main(String[] args) throws IOException {
		rootFolders = ConfigReader.getRootFolders();
		constraints = ConfigReader.getConstraints();
		words = ConfigReader.getWords();
		
		for (File root : rootFolders) {
			findRecursively(root);
		}
	}

	private static void findRecursively(final File folder) {
		
        for (File file : folder.listFiles()){
            
            if (file.isDirectory()) {
                findRecursively(file);
                
            } else if (file.isFile() && anyConstraintIsValid(file)) {
            	checkContent(file);
            }
        }
    }

	private static boolean anyConstraintIsValid(final File file) {
		if (constraints.isEmpty()) {
			return true;
		}
		
		for (Constraint constraint : constraints) {
			if (constraint.test(file)) {
				return true;
			}
		}
		return false;
	}

	private static void checkContent(File file) {
    	//System.out.println("Checking: " + file.getAbsolutePath());
    	
		StringBuilder message = new StringBuilder();
		
		BufferedReader reader;
		String line;
		int lineNumber;
		try {
			reader = new BufferedReader(new FileReader(file));
			lineNumber = 0;
			
			while (reader.ready()) {
				lineNumber++;
				line = reader.readLine();
				if (line == null) {
					break;
				}
				if (matchesAnyWord(line)) {
					message.append("  " + lineNumber + ": " + line.trim() + "\n");
				}
			}
			
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
		if (message.length() > 0) {
			System.out.println(file.getAbsolutePath());
			System.out.println(message.toString());
		}
	}

	private static boolean matchesAnyWord(String line) {
		if (words.isEmpty()) {
			return true;
		}
		
		for (String word : words) {
			if (line.contains(word)) {
				return true;
			}
		}
		return false;
	}

}
