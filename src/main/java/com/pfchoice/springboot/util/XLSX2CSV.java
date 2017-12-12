package com.pfchoice.springboot.util;

/* ====================================================================
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==================================================================== */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.poi.xssf.model.StylesTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * A rudimentary XLSX -> CSV processor modeled on the POI sample program
 * XLS2CSVmra from the package org.apache.poi.hssf.eventusermodel.examples. As
 * with the HSSF version, this tries to spot missing rows and cells, and output
 * empty entries for them.
 * <p/>
 * Data sheets are read using a SAX parser to keep the memory footprint
 * relatively small, so this should be able to read enormous workbooks. The
 * styles table and the shared-string table must be kept in memory. The standard
 * POI styles table class is used, but a custom (read-only) class is used for
 * the shared string table because the standard POI SharedStringsTable grows
 * very quickly with the number of unique strings.
 * <p/>
 * For a more advanced implementation of SAX event parsing of XLSX files, see
 * {@link XSSFEventBasedExcelExtractor} and {@link XSSFSheetXMLHandler}. Note
 * that for many cases, it may be possible to simply use those with a custom
 * {@link SheetContentsHandler} and no SAX code needed of your own!
 */
public class XLSX2CSV {

	private static final Logger LOG = LoggerFactory.getLogger(XLSX2CSV.class);

	/**
	 * Uses the XSSF Event SAX helpers to do most of the work of parsing the
	 * Sheet XML, and outputs the contents as a (basic) CSV.
	 */
	private class SheetToCSV implements SheetContentsHandler {
		private boolean firstCellOfRow = false;
		private int currentRow = -1;
		private int currentCol = -1;

		private void outputMissingRows(int number) {
			for (int i = 0; i < number; i++) {
				for (int j = 0; j < minColumns; j++) {
					output.append(',');
				}
				output.append('\n');
			}
		}

		@Override
		public void startRow(int rowNum) {
			// If there were gaps, output the missing rows
			outputMissingRows(rowNum - currentRow - 1);
			// Prepare for this row
			firstCellOfRow = true;
			currentRow = rowNum;
			currentCol = -1;
		}

		@SuppressWarnings("unused")
		public void endRow(int rowNum) {
			// Ensure the minimum number of columns
			for (int i = currentCol; i < minColumns; i++) {
				output.append(',');
			}
			output.append('\n');
		}

		@Override
		public void cell(String cellReference, String formattedValue) {
			if (firstCellOfRow) {
				firstCellOfRow = false;
			} else {
				output.append(',');
			}

			// gracefully handle missing CellRef here in a similar way as
			// XSSFCell does
			if (cellReference == null) {
				cellReference = new CellReference(currentRow, currentCol).formatAsString();
			}

			// Did we miss any cells?
			int thisCol = (new CellReference(cellReference)).getCol();
			int missedCols = thisCol - currentCol - 1;
			for (int i = 0; i < missedCols; i++) {
				output.append(',');
			}
			currentCol = thisCol;

			// Number or string?
			try {
				Double.parseDouble(formattedValue);
				output.append(formattedValue);
			} catch (NumberFormatException e) {
				try {
					LocalDate localdate = parseDateString(formattedValue);
					if (localdate.isAfter(LocalDate.now())) {
						localdate = localdate.plusYears(-100);
					}

					DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
					String formattedDate = localdate.format(format);
					output.append('"');
					output.append(formattedDate);
					output.append('"');
				} catch (Exception ex) {
					output.append('"');
					output.append(formattedValue);
					output.append('"');
				}

			}

		}

		LocalDate parseDateString(String dateString) {
			String[] dateFormats = { "M/d/yy", "M/dd/yy", "MM/d/yy", "MM/dd/yy", "MM/dd/yyyy", "M/d/yyyy",
					"M/dd/yyyy" };
			LocalDate localDate = null;
			for (String dateFormat : dateFormats) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
				try {
					localDate = LocalDate.parse(dateString, formatter);
				} catch (Exception e) {
					LOG.warn(e.getCause().getMessage());
				}
			}
			return localDate;

		}

		@Override
		public void headerFooter(String text, boolean isHeader, String tagName) {
			// Skip, no headers or footers in CSV
		}

		@Override
		public void endRow() {
			output.append('\r');
			output.append('\n');

		}

	}

	///////////////////////////////////////

	private OPCPackage xlsxPackage;

	/**
	 * Number of columns to read starting with leftmost
	 */
	private int minColumns;

	private StringBuilder output = new StringBuilder();
	/**
	 * Destination for data
	 */
	private FileOutputStream fos;

	/**
	 * Creates a new XLSX -> CSV converter
	 *
	 * @param pkg
	 *            The XLSX package to process
	 * @param output
	 *            The PrintStream to output the CSV to
	 * @param minColumns
	 *            The minimum number of columns to output, or -1 for no minimum
	 */
	public XLSX2CSV(File inputFile, File outputFile) throws InvalidFormatException, IOException {
		if (!inputFile.exists()) {
			LOG.warn("Not found or not a file: " + inputFile.getPath());
			return;
		}
		OPCPackage p = OPCPackage.open(inputFile.getPath(), PackageAccess.READ);
		this.xlsxPackage = p;
		this.fos = new FileOutputStream(outputFile);
		this.minColumns = 1;
	}

	/**
	 * Parses and shows the content of one sheet using the specified styles and
	 * shared-strings tables.
	 *
	 * @param styles
	 * @param strings
	 * @param sheetInputStream
	 */
	public void processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, SheetContentsHandler sheetHandler,
			InputStream sheetInputStream) throws IOException, ParserConfigurationException, SAXException {
		DataFormatter formatter = new DataFormatter();
		InputSource sheetSource = new InputSource(sheetInputStream);
		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = saxFactory.newSAXParser();
			XMLReader sheetParser = saxParser.getXMLReader();
			ContentHandler handler = new XSSFSheetXMLHandler(styles, strings, sheetHandler, formatter, false);
			sheetParser.setContentHandler(handler);
			sheetParser.parse(sheetSource);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
		}
	}

	/**
	 * Initiates the processing of the XLS workbook file to CSV.
	 *
	 * @throws IOException
	 * @throws OpenXML4JException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void process() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
		ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
		XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
		StylesTable styles = xssfReader.getStylesTable();
		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		while (iter.hasNext()) {
			InputStream stream = iter.next();
			processSheet(styles, strings, new SheetToCSV(), stream);
			stream.close();
		}

		this.fos.write(this.output.toString().getBytes());
		this.fos.close();
		this.xlsxPackage.close();

	}

	public static void xls(File inputFile, File outputFile) throws InvalidFormatException, IOException {
		// For storing data into CSV files

		if (!inputFile.exists()) {
			LOG.error("Not found or not a file: " + inputFile.getPath());
			return;
		}

		// The package open is instantaneous, as it should be.
		XLSX2CSV xlsx2csv = new XLSX2CSV(inputFile, outputFile);
		try {
			xlsx2csv.process();
		} catch (OpenXML4JException | ParserConfigurationException | SAXException e) {
			LOG.warn(e.getCause().getMessage());
		}

	}
}