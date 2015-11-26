package com.sky.comparator;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/*
 * @author Akash Sethiya
 * */
public class CSVComparator {
	private static CSVReader reader;
	private static Map<String, String[]> resultFileMap, expectedFileMap;
	private static CSVWriter resultFileWriter, summaryFileWriter;
	private static String[] header;
	
	public static void compare(String resultFile, String expectedFile,String summaryFile,String detailsFile,int keyIndex){
		System.out.println(new Date()+": ***********************Comparison Begins*****************");
		System.out.println(new Date()+": Result File : "+resultFile);
		System.out.println(new Date()+": Expected Result File : "+expectedFile);
		System.out.println(new Date()+": Index of Key Element : "+keyIndex);
		List<String[]> resultList = null,expectedList=null;
		resultFileMap = new HashMap<String, String[]>();
		expectedFileMap = new HashMap<String, String[]>();
		
		try{
			reader = new CSVReader(new FileReader(resultFile));
			resultList = reader.readAll();
			reader = new CSVReader(new FileReader(expectedFile));
			expectedList = reader.readAll();
			header = resultList.get(0);
			resultList.remove(0);
			expectedList.remove(0);
		}catch(Exception e){
			e.printStackTrace();
		}
		for (String[] actual : resultList) {
			String keyStr = actual[keyIndex];
			resultFileMap.put(keyStr, actual);
		}
		for (String[] expected : expectedList) {
			String keyStr = expected[keyIndex];
			expectedFileMap.put(keyStr, expected);
		}
		try{
			resultFileWriter = new CSVWriter(new FileWriter(detailsFile));
			System.out.println(new Date()+": Details File : "+detailsFile+" Created");
			String resultFileHeaderStr = "DataFile";
			for(int i=0;i<header.length;i++){
				resultFileHeaderStr=resultFileHeaderStr+","+header[i];
			}
			resultFileWriter.writeNext(resultFileHeaderStr.split(","));
			System.out.println(new Date()+": Header = ["+resultFileHeaderStr+"] Written to the Details File - '"+detailsFile+"'");
			summaryFileWriter = new CSVWriter(new FileWriter(summaryFile));
			System.out.println(new Date()+": Summary File : "+summaryFile+" Created");
			summaryFileWriter.writeNext((header[keyIndex]+",Status").split(","));
			System.out.println(new Date()+": Header = ["+header[keyIndex]+",Status"+"] Written to the Details File - '"+detailsFile+"'");
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(new Date()+": =================================================");
		System.out.println(new Date()+": Starts Writting into Details File - '"+detailsFile+"'");
		System.out.println(new Date()+": Starts Writting into Summary File - '"+summaryFile+"'");
		for (String key : resultFileMap.keySet()) {
			String[] tempActualArr = resultFileMap.get(key);
			String[] tempExpectedArr = expectedFileMap.get(key);
			createResultFile(tempActualArr, tempExpectedArr,resultFile,expectedFile);
			resultFileWriter.writeNext(new String[0]);
			int count = 0;
			if (tempActualArr != null && tempExpectedArr != null) {
				for (int i = 0; i < tempExpectedArr.length; i++) {
					if (tempExpectedArr[i].equals(tempActualArr[i])) {
						count++;
					}
				}
				if (count == tempExpectedArr.length) {
					createSummaryReport(key, true);
				} else {
					createSummaryReport(key, false);
				}
			}
		}
		System.out.println(new Date()+": Details File - '"+detailsFile+"' created Successfully");
		System.out.println(new Date()+": Summary File - '"+summaryFile+"' created Successfully");
		System.out.println(new Date()+": =================================================");
	}
	private static void createSummaryReport(String key, boolean status) {
		String insertString = key + ",";
		if (status) {
			insertString += "Success";
		} else {
			insertString += "Failed";
		}
		summaryFileWriter.writeNext(insertString.split(","));
	}

	private static void createResultFile(String[] resultList,String[] expectedList,String resultFile,String expectedFile){
		String[] tempResultList = new String[resultList.length+1];
		String[] tempExpectedList = new String[expectedList.length+1];
		tempResultList[0]=resultFile;
		tempExpectedList[0]=expectedFile;
		for(int i=1,j=0;i<tempResultList.length;i++,j++){
			tempResultList[i] = resultList[j];
		}
		for(int i=1,j=0;i<tempExpectedList.length;i++,j++){
			tempExpectedList[i] = expectedList[j];
		}
		try{
			resultFileWriter.writeNext(tempExpectedList);
			resultFileWriter.writeNext(tempResultList);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void closeAll(){
		try{
			resultFileWriter.close();
			summaryFileWriter.close();
			System.out.println(new Date()+": All instances Closed");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
