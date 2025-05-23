/*
* LEGAL NOTICE
* This computer software was prepared by Battelle Memorial Institute,
* hereinafter the Contractor, under Contract No. DE-AC05-76RL0 1830
* with the Department of Energy (DOE). NEITHER THE GOVERNMENT NOR THE
* CONTRACTOR MAKES ANY WARRANTY, EXPRESS OR IMPLIED, OR ASSUMES ANY
* LIABILITY FOR THE USE OF THIS SOFTWARE. This notice including this
* sentence must appear on any copies of this computer software.
* 
* Copyright 2012 Battelle Memorial Institute.  All Rights Reserved.
* Distributed as open-source under the terms of the Educational Community 
* License version 2.0 (ECL 2.0). http://www.opensource.org/licenses/ecl2.php
* 
* EXPORT CONTROL
* User agrees that the Software will not be shipped, transferred or
* exported into any country or used in any manner prohibited by the
* United States Export Administration Act or any other applicable
* export laws, restrictions or regulations (collectively the "Export Laws").
* Export of the Software may require some form of license or other
* authority from the U.S. Government, and failure to obtain such
* export control license may result in criminal liability under
* U.S. laws. In addition, if the Software is identified as export controlled
* items under the Export Laws, User represents and warrants that User
* is not a citizen, or otherwise located within, an embargoed nation
* (including without limitation Iran, Syria, Sudan, Cuba, and North Korea)
*     and that User is not otherwise prohibited
* under the Export Laws from receiving the Software.
* 
*/
package glimpseUtil;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.JFrame;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
//TODO:  Deprecated XML code
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

//import ModelInterface.ModelGUI2.csvconv.DOMTreeBuilder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * A stand alone driver to run the CSV to XML conversion tool. This class serves
 * as both a static utility to to run the conversion as well as a main command
 * line program. The commandline expects at least three arguments the first of
 * which is one or more CSV files, next the header file and finally where to
 * write the resulting XML file.
 *
 * @author Pralit Patel...Modified by Dan Loughlin of EPA
 */
public class CSVToXMLMain {
	public static void main(String[] args) {
		if (args.length < 3) {
		}
		try {
			if (args.length == 1) {
				// Assuming we are running a batch file
				System.out.println("Running batch file: " + args[0]);
				runFromBatch(new File(args[0]));
			} else if (args.length >= 3) {
				// Assuming we are getting the conversion file names directly from the argument
				// list
				File xmlOutputFile = new File(args[args.length - 1]);
				File headerFile = new File(args[args.length - 2]);
				File[] csvFiles = new File[args.length - 2];
				for (int i = 0; i < args.length - 2; ++i) {
					csvFiles[i] = new File(args[i]);
				}

				Document doc = null;

				try {
					doc = runCSVConversion(csvFiles, headerFile, null);
				} catch (Exception ex) {
					System.out.println("error " + ex);
					System.out.println("Document model was not created successfully.");
				}
				if (doc != null)
					writeFile(xmlOutputFile, doc);
				    replaceTextInFile(xmlOutputFile.getPath(),"DELETE","");
				
			} else {
				System.err.println("Usage: CSVToXMLMain <CSV file> [<CSV file> ..] <header file> <output XML file>");
				System.err.println("   or: CSVToXMLMain <batch file>");
				System.exit(1);
			}

		} catch (Exception e) {
			System.out.println("!!!!!!!!!!!! Exception in GLIMPSE CSVtoXML utility !!!!!!!!!!!!!!!!!");
			e.printStackTrace();
			// System.exit(1);
		}
	}
	
    public static boolean replaceTextInFile(String filePath,String oldText,String newText) {
    	boolean b=true;
        try {
            // Read the file content into a string
            String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);

            // Replace the desired text
            content = content.replace(oldText, newText);

            // Write the modified string back to the file
            Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8));

            System.out.println("Text replaced successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            b=false;
        }
        return b;
    }

	/**
	 * Takes a CSV file, and Headers file, then processes the files by building a
	 * new tree with the DOMTreeBuilder class. The resulting XML document is
	 * returned
	 * 
	 * @param csvFiles    the CSV files
	 * @param headerFile  the Headers file
	 * @param parentFrame A GUI frame which may be used to display error messages to
	 *                    if it is not null.
	 * @return The generated XML dom document
	 */
	public static Document runCSVConversion(File[] csvFiles, File headerFile, JFrame parentFrame) {
		StringTokenizer st;
		String intValueStr = null;
		String strToReplace;
		int counter;
		int dollarindex = 0;
		String inputLine = "";
		boolean verbose = true;

		ArrayList<String> dataArr;
		HashMap<String, String> nickNameMap = new HashMap<String, String>(); // shortname -> long string to
		// append to end
		HashMap<String, String> tableIDMap = new HashMap<String, String>(); // tableID -> long string of headers
		DOMTreeBuilder tree = new DOMTreeBuilder();

		try {

			FileInputStream hashfis = new FileInputStream(headerFile);
			DataInputStream hashfin = new DataInputStream(hashfis);
			BufferedReader hashInput = new BufferedReader(new InputStreamReader(hashfin));
			hashInput.readLine(); // ignores first line of file
			inputLine = trimString(hashInput.readLine());
			while (inputLine != null && inputLine.length() > 0 && inputLine.charAt(0) == '$') { // read in
				// header
				// nick
				// names
				st = new StringTokenizer(inputLine, ",", false);
				intValueStr = st.nextToken(); // $nickname
				inputLine = inputLine.substring(intValueStr.length() + 1).trim();
				nickNameMap.put(intValueStr, inputLine);
				if ((inputLine = hashInput.readLine()) != null) {
					inputLine=trimString(inputLine);
				}
			}
			while (inputLine != null) {
				if ((!inputLine.equals("")) && (!inputLine.startsWith("#"))) {
					st = new StringTokenizer(inputLine, ",", false);
					intValueStr = st.nextToken(); // numID
					inputLine = inputLine.substring(intValueStr.length() + 1); // everything
					// but
					// numID
					try {

						inputLine = inputLine.replaceAll("[,][\\s]*[,]", ""); // gets

						// Dan added this to get rid of multiple commas from exporting CSV from Excel
						inputLine = inputLine.replaceAll(",,", "");
						// rid
						// of
						// end
						// commas
						while (inputLine.endsWith(",")) { // gets ride of last //new... testing out while instead of if
							// comma if there is one
							inputLine = inputLine.substring(0, inputLine.length() - 1);
						} // extra commas are now all gone

						dollarindex = 0;
						while ((dollarindex = inputLine.indexOf('$')) != -1) {
							counter = dollarindex;
							while (counter < inputLine.length() && inputLine.charAt(counter) != ',') {
								counter++;
							}
							strToReplace = inputLine.substring(dollarindex, counter);
							if (nickNameMap.containsKey(strToReplace)) {
								// strToReplace = strToReplace.substring(1);
								// strToReplace = "^[.]*"+strToReplace+"[.]*$";
								inputLine = inputLine.replaceAll("\\" + strToReplace, nickNameMap.get(strToReplace));
							} else {
								System.out.println("***Couldn't find replacement for " + strToReplace + "!***");
								if (parentFrame != null) {
									JOptionPane.showMessageDialog(parentFrame,
											"Couldn't find replacement for " + strToReplace, "Warning",
											JOptionPane.WARNING_MESSAGE);
								}
							}
						}
						tableIDMap.put(intValueStr, inputLine);
					} catch (NumberFormatException e) {
						System.out.println("*** Hashtable file formatted incorrectly ***" + e);
						if (parentFrame != null) {
							JOptionPane.showMessageDialog(parentFrame, "Hashtable file formatted incorrectly\n" + e,
									"Exception", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				if ((inputLine = hashInput.readLine()) != null) {
					inputLine=trimString(inputLine);
				}
			}

			// Dan added this so he could iterate over all the states. Couldn't refer to
			// utils version since this one is static
			String[] states = { "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "HI", "IA", "ID",
					"IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE",
					"NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VA",
					"VT", "WA", "WI", "WV", "WY", "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA",
					"HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT",
					"NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN",
					"TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY" };
			// tableIDMap should now be all set up ...

			for (int j = 0; j < csvFiles.length; ++j) {
				FileInputStream fis = new FileInputStream(csvFiles[j]);
				DataInputStream fin = new DataInputStream(fis);
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(fin));

				inputLine = trimString(stdInput.readLine()); // read one line of input

				
				// Dan added to address issue of CSV-written files having lines ending in commas
				String header = "";
				while (inputLine != null) {

					while ((inputLine.startsWith("#") || (inputLine == "") || (inputLine == " "))) {
						inputLine = trimString(stdInput.readLine());
						if (inputLine == null)
							break;
					}
					if (inputLine.contains("INPUT_TABLE")) {
						inputLine = trimString(stdInput.readLine());
						if (!inputLine.contains("Variable ID")) {
							System.out.println(
									"Variable ID indicator does not follow INPUT_TABLE. Please check formatting of CSV file.");
							return tree.getDoc();
						}

						// reading in header text
						header = intValueStr = trimString(stdInput.readLine());

						if (tableIDMap.containsKey(header)) {
							tree.setHeader(tableIDMap.get(header));
							stdInput.readLine(); // ignores blank line after header
							String columns = stdInput.readLine(); // reads column names
							System.out.println("Using header: " + header + "... table data:" + columns);

							// begins reading data
							inputLine = trimString(stdInput.readLine());

							// data
							while (inputLine != null && !inputLine.equals("") && inputLine.charAt(0) != ',') {
								st = new StringTokenizer(inputLine, ",", false);
								int NUM_COLS = st.countTokens();
								dataArr = new ArrayList<String>(NUM_COLS);
								for (int i = 0; i < NUM_COLS; i++) {
									String str = st.nextToken().trim();
									dataArr.add(i, str);
								} // one line of data stores in arraylist

								// Dan: Hack that replicates entry for all states if 1st item (region) is split
								// by :s
								String str = dataArr.get(0).trim();
								if ((str.toLowerCase().equals("all states")) || (str.toLowerCase().equals("allstates"))
										|| (str.toLowerCase().equals("all-states"))) {
									for (int s = 0; s < states.length; s++) {
										dataArr.set(0, states[s]);
										tree.addToTree(dataArr);
									}

								} else if (str.indexOf(":") > -1) {

									String[] regions = str.split(":");

									for (int k = 0; k < regions.length; k++) {
										dataArr.set(0, regions[k]);
										tree.addToTree(dataArr);
									}

								} else {

									tree.addToTree(dataArr);

								}

								dataArr.clear();
								if ((inputLine = stdInput.readLine()) != null) {
									inputLine = trimString(inputLine);
								}
							}
						} else {
							System.out.println("***** Warning: could not find header (" + header
									+ ") in header file. Skipping table: " + intValueStr + "! *****");
						}
					}
					if ((inputLine = stdInput.readLine()) != null) {
						inputLine = trimString(inputLine);
					}
					
				}
				fin.close();
				hashfin.close();

			}
			return tree.getDoc();

		} catch (Exception e) {
			if (parentFrame != null) {
		
				JOptionPane.showMessageDialog(parentFrame,
						"Difficulty processing " + intValueStr, "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
			System.out.println("========= Error in CSV2XML processing ==========");
			System.out.println("Using header " + intValueStr);
			System.out.println("inputLine: " + inputLine);
			System.out.println("Exception thrown while trying to read csv and header files");
			e.printStackTrace();
			String error="Error processing "+intValueStr;
			GLIMPSEUtils.getInstance().warningMessage(error);
			System.out.println("================================================");
			return null;
		}
	}
	
	//removes spaces and trailing commas from end of string
	public static String trimString(String s) {

		if (s!=null) {
		while (s.endsWith(",")) { // gets ride of last //new... testing out while instead of if
			// comma if there is one
			s = s.substring(0, s.length() - 1);
		} // extra commas are now all gone
		s=s.trim();
		}
		return s;
	}

	public String getState(int i) {
		String state_str = "";

		return state_str;
	}

	/**
	 * Writes the DOM document to the specified file. Note this is used in many
	 * places and should really be in a utility however in this case we need that
	 * utility to have no extra library dependencies.
	 * 
	 * @param file  where the XML tree will be written to
	 * @param thDoc the tree that should be written
	 * @return whether the file was actually written or not
	 */
	public static boolean writeFile(File file, Document theDoc) {
		// specify output formating properties
		OutputFormat format = new OutputFormat(theDoc);
		format.setEncoding("UTF-8");
		format.setLineSeparator("\r\n");
		format.setIndenting(true);
		format.setIndent(3);
		format.setLineWidth(0);
		format.setPreserveSpace(false);
		format.setOmitDocumentType(true);

		// create the searlizer and have it print the document

		try {
			FileWriter fw = new FileWriter(file);
			XMLSerializer serializer = new XMLSerializer(fw, format);
			serializer.asDOMSerializer();
			serializer.serialize(theDoc);
			fw.close();
		} catch (java.io.IOException e) {
			System.err.println("Error outputing tree: " + e);
			return false;
		}
		return true;
	}

	/**
	 * Run CSV to XML conversion(s) by reading header, csv, and output filenames
	 * from a ModelInterface style batch command. This code is adapated from
	 * ModelInterface.ModelGUI2.InputViewer.runBatch which could not be called
	 * directly to avoid dependencies. Note many conversions could be specified in a
	 * single batch file. Non CSV conversion commands will produce a warning and
	 * skipped.
	 * 
	 * @param batchFile The batch file to parse and run.
	 * @throws Exception Any error from parsing the batch file or during a
	 *                   conversion.
	 */
	private static void runFromBatch(File batchFile) throws Exception {
		// Convert the file path to a URI before parsing to
		// ensure the document has a valid heirarchical URI.
		final URI docURI = batchFile.toURI();

		// Attempt to parse the document.
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document loadedDocument = parser.parse(docURI.getPath());

		// Parse the batch file looking for CSV file commands and parsing out the
		// header, csv,
		// and output files.
		NodeList rootChildren = loadedDocument.getDocumentElement().getChildNodes();
		for (int rootIndex = 0; rootIndex < rootChildren.getLength(); ++rootIndex) {
			Node currClassNode = rootChildren.item(rootIndex);
			// only interested in InputViewer which is the class typically used to do the
			// converion
			if (currClassNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			if (currClassNode.getNodeName().equals("class")
					&& ((Element) currClassNode).getAttribute("name").equals("ModelInterface.ModelGUI2.InputViewer")) {
				NodeList commands = currClassNode.getChildNodes();
				for (int commandIndex = 0; commandIndex < commands.getLength(); ++commandIndex) {
					Node command = commands.item(commandIndex);
					// Only interesed in the action CSV file
					if (command.getNodeType() != Node.ELEMENT_NODE) {
						continue;
					}
					String actionCommand = ((Element) command).getAttribute("name");
					if (actionCommand == null) {
						continue;
					}

					if (actionCommand.equals("CSV file")) {
						File headerFile = null;
						File outFile = null;
						ArrayList<File> csvFiles = new ArrayList<File>();
						// read file names for header file, csv files, and the output file
						NodeList fileNameChildren = command.getChildNodes();
						for (int j = 0; j < fileNameChildren.getLength(); ++j) {
							Node fileNode = fileNameChildren.item(j);
							if (fileNode.getNodeType() != Node.ELEMENT_NODE) {
								continue;
							}
							File tempFile = new File(fileNode.getTextContent());
							// find header, csv, and output files
							if (fileNode.getNodeName().equals("headerFile")) {
								headerFile = tempFile;
							} else if (fileNode.getNodeName().equals("outFile")) {
								outFile = tempFile;
							} else if (fileNode.getNodeName().equals("csvFile")) {
								csvFiles.add(tempFile);
							} else {
								System.out.println(
										"Unknown tag while parsing CSV file command: " + fileNode.getNodeName());
							}
						}
						// run the conversion and save the results
						File[] csvFilesArr = new File[csvFiles.size()];
						csvFilesArr = csvFiles.toArray(csvFilesArr);
						Document doc = runCSVConversion(csvFilesArr, headerFile, null);
						writeFile(outFile, doc);
					} else {
						System.out.println(
								"Invalid command: " + actionCommand + ", only CSV file can be run in this mode");
					}
				}
			} else {
				System.out.println("Invalid class, only ModelInterface.ModelGUI2.InputViewer can be run in this mode");
			}
		}
	}
}
