/**
 * 
 */
/*  -------------------------------------------------------------------------
 *
 *                Project: JRecord
 *    
 *    Sub-Project purpose: Provide support for reading Cobol-Data files 
 *                        using a Cobol Copybook in Java.
 *                         Support for reading Fixed Width / Binary / Csv files
 *                        using a Xml schema.
 *                         General Fixed Width / Csv file processing in Java.
 *    
 *                 Author: Bruce Martin
 *    
 *                License: LGPL 2.1 or latter
 *                
 *    Copyright (c) 2016, Bruce Martin, All Rights Reserved.
 *   
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *   
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 * ------------------------------------------------------------------------ */

package net.sf.JRecord.zExamples.cobol.toCsv.test;

import net.sf.JRecord.zExamples.cobol.toCsv.Cobol2Csv;

/**
 * @author Bruce Martin
 *
 */
public class TestCobol2Csv02_1_UTF16 {

	/**
	 * @param args
	 */
	public static void main(String[] a) {

		String inputFileName = TestCobol2Csv02_1_UTF16.class.getResource("DTAR020.bin").getFile();
		String[] args= {
				"-I", inputFileName, 
				"-O", ExampleConstants.TEMP_DIR + "DTAR020_02_UTF016.csv", 
				"-C", TestCobol2Csv02_1_UTF16.class.getResource("DTAR020.cbl").getFile(), 
				"-Q", "\"",                /* Quote           */
				"-FS", "Fixed_Length",     /* File Structure  */
				"-IF", "CP273",            /* Character set   */
				"-OC", "UTF-16",           /* Output Character set  */
				"-D", ","                 /* Field Seperator */ 
		}; /* Field Seperator will default to \t */
		
		Cobol2Csv.main(args); 
	}

}
