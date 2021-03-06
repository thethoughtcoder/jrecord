/*  -------------------------------------------------------------------------
 *
 *            Sub-Project: JRecord CodeGen
 *    
 *    Sub-Project purpose: Generate Java - JRecord source code 
 *                        to read/write cobol data files.
 *    
 *                 Author: Bruce Martin
 *    
 *                License: GPL 3 or later
 *                
 *    Copyright (c) 2016, Bruce Martin, All Rights Reserved.
 *   
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU General Public License
 *    as published by the Free Software Foundation; either
 *    version 3.0 of the License, or (at your option) any later version.
 *   
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 * ------------------------------------------------------------------------ */
      
package net.sf.JRecord.cg.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import net.sf.JRecord.Common.FieldDetail;
import net.sf.JRecord.ExternalRecordSelection.ExternalFieldSelection;
import net.sf.JRecord.ExternalRecordSelection.ExternalGroupSelection;
import net.sf.JRecord.ExternalRecordSelection.ExternalSelection;
import net.sf.JRecord.Option.Options;
import net.sf.JRecord.cg.common.CCode;
import net.sf.JRecord.cgen.defc.IRecordDetail4gen;


/**
 * Class to describe one record type in a file for use in code generation
 * @author Bruce Martin
 *
 */
public class RecordDef extends JavaDetails {
	private final IRecordDetail4gen record;
	
	private final ArrayList<FieldDef> fields = new ArrayList<FieldDef>();
	
	private final String RecordSelectionStr, recordPositionOptionStr;
	private final List<ArrayDetails> arrayDetailsList = new ArrayList<ArrayDetails>();
	private final List<List<ArrayDetails>> arraySameSize = new ArrayList<List<ArrayDetails>>();
	private final List<String> importList;

	/**
	 * Class to describe one record type in a file for use in code generation
	 * 
	 * @param record standard Record Description
	 */
	public RecordDef(IRecordDetail4gen record) {
		super(record.getRecordName()); 
		this.record = record;
		
		int fieldCount = record.getFieldCount();
		HashMap<String, Integer> fieldsUsed = new HashMap<String, Integer>(fieldCount * 3);
		FieldDetail field;
		String fldName, lcFldName;
		ArrayElement ai;
		HashMap<String, ArrayDetails> arrayMap = new HashMap<String, ArrayDetails>();
		FieldDef fieldDef;
		ArrayDetails ad = null;
		TreeSet<String> importSet = new TreeSet<String>();

		
		fields.ensureCapacity(fieldCount);
		
		for (int i = 0; i < record.getFieldCount(); i++) {
			field = record.getField(i);
			fldName = field.getName();
			lcFldName = field.getName().toLowerCase();
			if (! "filler".equals(lcFldName)){
				Integer num = fieldsUsed.get(lcFldName);
				if (num == null) {
					num = Integer.valueOf(1);
				} else {
					fldName = fldName + num; 
					num = num + 1;
				}
				fieldsUsed.put(lcFldName, num);
				
				ai = null;
				if (fldName.indexOf('(') > 0) {
					ai = ArrayElement.newArrayItem(fldName);
					fieldDef = new FieldDef(fldName, field, ai);
					
					if (arrayMap.containsKey(ai.arrayName)) {
						ad = arrayMap.get(ai.arrayName);
						ad.addDetails(ai, fieldDef);
					} else {
						ad = new ArrayDetails(ai, fieldDef);
						arrayMap.put(ai.arrayName, ad);
						arrayDetailsList.add(ad);
					}
				} else {
					fieldDef = new FieldDef(fldName, field, ai);
				}
				
				fields.add(fieldDef);
				
				String jType = fieldDef.getJavaType();
				if ("BigDecimal".equals(jType)) {
					importSet.add("java.math.BigDecimal");
				} else if ("BigInteger".equals(jType)) {
					importSet.add("java.math.BigInteger");
				}
			}
		}
		
		importList = new ArrayList<String>(importSet);
		
		StringBuilder b = new StringBuilder(30);
		expandSel(b, record.getRecordSelection().getRecSel(), 0);
		RecordSelectionStr = b.toString();
		String s = "null";
		if (record.getRecordPositionOption() == Options.RP_FIRST_RECORD_IN_FILE) {
			s = "Options.RP_FIRST_RECORD_IN_FILE"; 
		} else if (record.getRecordPositionOption() == Options.RP_MIDDLE_RECORDS) {
			s = "Options.RP_MIDDLE_RECORDS"; 
		} else if (record.getRecordPositionOption() == Options.RP_LAST_RECORD_IN_FILE) {
			s = "Options.RP_LAST_RECORD_IN_FILE"; 
		}
		this.recordPositionOptionStr = s;
		
		if (arrayDetailsList.size() > 0) {
		    List<ArrayDetails> currList = new ArrayList<ArrayDetails>();
			currList.add(arrayDetailsList.get(0));
			for (int i = 1; i < arrayDetailsList.size(); i++) {
				if (! currList.get(0).sizesEqual(arrayDetailsList.get(i))) {
					arraySameSize.add(currList);
					currList = new ArrayList<ArrayDetails>();
				}
				currList.add(arrayDetailsList.get(i));
			}
			arraySameSize.add(currList);
		}
	}

	public void expandSel(StringBuilder b, ExternalSelection sel, int level) {
		char[] indent = new char[level * 3 + 5];
		Arrays.fill(indent, ' ');
		
		b.append(indent);
		if (sel == null) {
			
		} else if (sel instanceof ExternalGroupSelection) {
			@SuppressWarnings("rawtypes")
			ExternalGroupSelection eg = (ExternalGroupSelection) sel;
			char sep = '(';
			
			if (eg.getType() == ExternalSelection.TYPE_AND) {
				b.append("ExternalGroupSelection.newAnd\n");
			} else {
				b.append("ExternalGroupSelection.newOr\n");
			}
			for (int i = 0; i < eg.getElementCount(); i++) {
				expandSel(b.append(sep), eg.get(i), level+1);
				sep = ',';
			}
			b.append(indent).append("   )\n");
		} else if (sel instanceof ExternalFieldSelection) {
			ExternalFieldSelection ef = (ExternalFieldSelection) sel;
			b.append("new ExternalFieldSelection(\"" 
						+ ef.getFieldName() + "\", \"" + ef.getFieldValue()+ "\", \"" + ef.getOperator() + "\")\n");
		} else {
			throw new RuntimeException("Invalid Record Selection class: " + sel.getClass().getName());
		}
	}
	
	
	/**
	 * @return the record
	 */
	public final IRecordDetail4gen getRecord() {
		return record;
	}

	/**
	 * @return the fields
	 */
	public final List<FieldDef> getFields() {
		return fields;
	}
	
	/**
	 * @return the fields
	 */
	public final List<FieldDef> getFields(int limit) {
		if (fields.size() <= limit) {
			return fields;
		}
		ArrayList<FieldDef> r = new ArrayList<FieldDef>(limit);
		for (int i = 0; i < limit; i++) {
			r.add(fields.get(i));
		}
		return r;
	}

	
	public String getJRecordRecordType() {
		return CCode.getRecordTypeName(record.getRecordType());
	}

	/**
	 * @return the recordSelectionStr
	 */
	public final String getRecordSelectionStr() {
		return RecordSelectionStr;
	}

	/**
	 * @return the recordPositionOptionStr
	 */
	public final String getRecordPositionOptionStr() {
		return recordPositionOptionStr;
	}

	/**
	 * @return the arrayDetailsList
	 */
	public final List<ArrayDetails> getArrayDetailsList() {
		return arrayDetailsList;
	}

	/**
	 * @return the arraySameSize
	 */
	public final List<List<ArrayDetails>> getArraySameSize() {
		return arraySameSize;
	}

	/**
	 * @return the importList
	 */
	public final List<String> getImportList() {
		return importList;
	}
}
