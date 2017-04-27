package np;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.JRecord.Common.AbstractFieldValue;
import net.sf.JRecord.Common.Constants;
import net.sf.JRecord.Common.IFieldDetail;
import net.sf.JRecord.Details.AbstractLine;
import net.sf.JRecord.Details.FieldIterator;
import net.sf.JRecord.External.CopybookLoader;
import net.sf.JRecord.IO.AbstractLineReader;
import net.sf.JRecord.IO.CobolIoProvider;
import net.sf.JRecord.Numeric.Convert;

public class IterateFields {
	
	private static final String BASE_DIR = "/home/ray/now/cobolfiles/";

	public static void main(String[] args) {
        CobolIoProvider ioProvider = CobolIoProvider.getInstance();
        AbstractLine line;
        try {
			AbstractLineReader reader  = ioProvider.getLineReader(Constants.IO_VB,
			        Convert.FMT_MAINFRAME,
			        CopybookLoader.SPLIT_NONE,
			        BASE_DIR + "FAL_C0006211.txt",
			        BASE_DIR + "FAL.D15215.TXT");
			
			line = reader.read();
			showAllFields(line);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void showAllFields(AbstractLine line) {
		FieldIterator fieldIterator = line.getFieldIterator(0);
		while (fieldIterator.hasNext()) {
			AbstractFieldValue afv = fieldIterator.next();
			
			IFieldDetail fieldDetail = afv.getFieldDetail();
			System.out.println(String.format("%s\t%s\t%d-%d", 
					fieldDetail.getLookupName(),
					afv.asString(), 
					fieldDetail.getPos(), 
					fieldDetail.getLen()));
		}
	}

	private static void sortFieldsByPos(AbstractLine line) {
		FieldIterator fi = line.getFieldIterator(0);
		SortedMap<Integer, List<AbstractFieldValue>> sorted = new TreeMap<>();
		
		while (fi.hasNext()) {
			AbstractFieldValue afv = fi.next();
			IFieldDetail ifd = afv.getFieldDetail();
			int pos = ifd.getPos();
			
			if (!sorted.containsKey(pos)) {
				List<AbstractFieldValue> ele = new LinkedList<AbstractFieldValue>();
				ele.add(afv);
				sorted.put(pos, ele);
			} else {
				List<AbstractFieldValue> ele = sorted.get(pos);
				ele.add(afv);
			}
		}
	}
}
