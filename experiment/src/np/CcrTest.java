package np;

import net.sf.JRecord.Common.Constants;
import net.sf.JRecord.Details.AbstractLine;
import net.sf.JRecord.External.CopybookLoader;
import net.sf.JRecord.IO.AbstractLineReader;
import net.sf.JRecord.IO.CobolIoProvider;
import net.sf.JRecord.Numeric.Convert;

public final class CcrTest {

	public static void main(String[] args) {
        CobolIoProvider ioProvider = CobolIoProvider.getInstance();
        AbstractLine line;
        int lineNum = 0;
        try {
                AbstractLineReader reader  = ioProvider.getLineReader(Constants.IO_FIXED_LENGTH_RECORDS,
                                Convert.FMT_MAINFRAME,
                                CopybookLoader.SPLIT_NONE,
                                "/home/dev/now/ccres/users/CPY-CCREST4P.txt",
                                "/home/dev/now/ccres/ZK000.ZK3ALMTO.CUSTME.RESULT.D1520901.dat");
                while ((line = reader.read()) != null) {
                        lineNum += 1;
                        String recType = line.getFieldValue("PRRS-RECORD-TYPE").asString();
                        int date = line.getFieldValue("PRRS-RUN-DATE").asInt();
                        System.out.println(recType + " " + date);
                        if (lineNum > 3) break;
                }
                System.out.println("Num of lines: " + lineNum);
        } catch (Exception e) {
                e.printStackTrace();
        }
	}
}

