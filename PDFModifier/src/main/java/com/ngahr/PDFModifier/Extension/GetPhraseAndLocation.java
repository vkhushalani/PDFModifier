package com.ngahr.PDFModifier.Extension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetPhraseAndLocation extends PDFTextStripper{
	private  List<TextPosition> positions = new ArrayList<TextPosition>(); 
	Logger logger = LoggerFactory.getLogger(GetPhraseAndLocation.class);
	public GetPhraseAndLocation() throws IOException {
	logger.debug("calling exteded class");	
	}
 
	
	@Override
	  protected void writeString(String str, List<TextPosition> textPositions) throws IOException {
		
			for(TextPosition pos : textPositions)
			{
				positions.add(pos);
			}
		
	    }
	

	public List<TextPosition> getPositions() {
		return positions;
	}

	public void setPositions(List<TextPosition> positions) {
		this.positions = positions;
	}
	

	

}
