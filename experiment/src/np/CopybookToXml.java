package np;

import java.io.IOException;
import java.io.InputStream;

import org.w3c.dom.Document;

import net.sf.cb2xml.def.Cb2xmlConstants;
import net.sf.cb2xml.sablecc.lexer.LexerException;
import net.sf.cb2xml.sablecc.parser.ParserException;

public class CopybookToXml {
	
	public static final String COPYBOOK_DIALECT = "copybookDialect";
	
	public static Document convertToXMLDOM(InputStream copybookInputStream) {
		int defaultCopybookDialect = Cb2xmlConstants.USE_STANDARD_COLUMNS;
		
		Document result = null;
		try {
			result = net.sf.cb2xml.Cb2Xml2.convertToXMLDOM(copybookInputStream, null, false, defaultCopybookDialect);
		} catch (ParserException | LexerException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
