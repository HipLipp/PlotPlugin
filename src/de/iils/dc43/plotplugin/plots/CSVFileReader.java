package de.iils.dc43.plotplugin.plots;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.iils.dc43.transformationengine.popup.actions.TransformationRunner;

public class CSVFileReader {

	// ------- V A R I A B L E S ------- //
	private org.apache.log4j.Logger logger = TransformationRunner.getLogger(CSVFileReader.class);

	private String csvFile;
	private String columnSplitSymbol;

	// ------------- E N D ------------- //

	/**
	 * Constructor
	 * 
	 * @param filepath
	 * @param columnSplitSymbol
	 * @throws FileNotFoundException
	 */
	public CSVFileReader() {
	}

	/**
	 * Read the CVS-File.
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public void readFile(String filepath, String columnSplitSymbol, boolean overWriteDataInDB)
			throws FileNotFoundException {

		if (columnSplitSymbol.equals(" ")) {
			this.columnSplitSymbol = "\\s+";
		} else {
			this.columnSplitSymbol = columnSplitSymbol;
		}

		this.csvFile = filepath;
		if (!new File(csvFile).exists()) {
			throw new FileNotFoundException();
		}

		// Database
		PlotDatabase plotDatabase = PlotDatabase.getDatabase();

		// Dataset Container
		ArrayList<ArrayList<Double>> dataCollector = null;

		// IO Stuff
		BufferedReader br = null;
		String line = null;

		int[] numberInfo = getLineWithFirstNumbersAndNumbercount();
		int numberStartLine = numberInfo[0];
		int numbersPerLine = numberInfo[1];

		int lineCounter = 0;

		// Add datasets
		dataCollector = new ArrayList<ArrayList<Double>>(numbersPerLine);
		for (int ii = 1; ii <= numbersPerLine; ii++) {
			ArrayList<Double> dataset = new ArrayList<Double>();
			dataCollector.add(dataset);
		}

		// ReadVarNames
		List<String> datasetNames = getVariableNames(numberStartLine - 1, numbersPerLine);

		try {

			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {
				lineCounter++;

				if (lineCounter >= numberStartLine) {
					// Split Line
					String[] splittedLine = line.split(this.columnSplitSymbol);

					// Try Formatting to Number
					int doubleCounter = 0;
					for (String string : splittedLine) {
						try {
							Double newValue = Double.parseDouble(string);
							ArrayList<Double> dataset = dataCollector.get(doubleCounter);
							dataset.add(newValue);
							doubleCounter++;

						} catch (Exception e) {
							// Do Nothing
						}

						// Safety, when all numbers are read quit loop
						if (doubleCounter + 1 > numbersPerLine)
							break;

					} // foreach string
				} // if numberStartLine
			} // while br.readLine

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// Add data to database
		int datasetCounter = 0;
		for (ArrayList<Double> dataset : dataCollector) {
			plotDatabase.addDatasetToDatabase(datasetNames.get(datasetCounter), dataset,
					overWriteDataInDB);
			datasetCounter++;
		}

	}

	/**
	 * Get the line number at which the numbers start and the number of numbers
	 * per line. Returns -1 as first argument if the algorithm can not say where
	 * the numbers start.
	 * 
	 * @return int[] {lineWithFirstNumbers,nNumbersInLines}
	 */
	private int[] getLineWithFirstNumbersAndNumbercount() {

		BufferedReader br = null;

		int lineWithFirstNumbers = -1;
		int lineNumber = 1;

		int nNumbersInLastLine = 0;
		int nNumbersInMyLine = 0;
		int nNumbersInLinesWereEqual = 0;

		try {

			String line = null;
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// Split Line
				nNumbersInMyLine = 0;
				String[] splittedLine = line.split(columnSplitSymbol);

				for (String string : splittedLine) {
					try {
						Double.parseDouble(string);
						nNumbersInMyLine++;
					} catch (NumberFormatException e) {

					}
				}

				// Compares number count of last line with this one
				if (nNumbersInMyLine == nNumbersInLastLine && nNumbersInMyLine != 0) {
					nNumbersInLinesWereEqual++;
				}

				// If the numberCount was 10 Times equal, we reached the number
				// part
				if (nNumbersInLinesWereEqual >= 5) {
					lineWithFirstNumbers = lineNumber - 5;
					break;
				}

				nNumbersInLastLine = nNumbersInMyLine;
				lineNumber++;

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return new int[] { lineWithFirstNumbers, nNumbersInLastLine };

	}

	/**
	 * Read the variable names.
	 * 
	 * @param numberOfVariables
	 * @return
	 */
	private List<String> getVariableNames(int lineWithNames, int numberOfVariables) {

		List<String> names = new ArrayList<String>();
		BufferedReader br = null;
		Pattern pattern = Pattern.compile("[\\w\\-]+");

		try {

			String line = null;
			int linecounter = 1;
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null && linecounter <= lineWithNames) {

				if (linecounter == lineWithNames) {
					Matcher match = pattern.matcher(line);

					while (match.find()) {
						names.add(line.substring(match.start(), match.end()));
					}

					break;
				}
				linecounter++;

			} // while br.readLine

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// If too less names add UnnamedVar
		if (names.size() < numberOfVariables) {
			int ii = 0;
			while (names.size() < numberOfVariables) {
				names.add("UnnamedVar" + ii);
				ii++;
			}
			// If too many names remove last
		} else if (names.size() > numberOfVariables) {
			int ii = names.size() - 1;
			while (names.size() != numberOfVariables && names.size() > 0) {
				names.remove(ii);
				ii--;
			}
		}

		return names;
	}
}
