package np;

import net.sf.JRecord.Common.AbstractFieldValue;
import net.sf.JRecord.Common.Constants;
import net.sf.JRecord.Common.IFieldDetail;
import net.sf.JRecord.Details.AbstractLine;
import net.sf.JRecord.Details.FieldIterator;
import net.sf.JRecord.External.CopybookLoader;
import net.sf.JRecord.IO.AbstractLineReader;
import net.sf.JRecord.IO.CobolIoProvider;
import net.sf.JRecord.Numeric.Convert;

public class RedefineSelectionFal {

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
			
			while ((line = reader.read()) != null) {
				System.out.println(
						line.getFieldValue("FAL-01-RECORD-TYPE")
						.asString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
