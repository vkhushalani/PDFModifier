package com.ngahr.PDFModifier.controller;
import org.springframework.web.bind.annotation.RestController;

import com.ngahr.PDFModifier.Extension.GetPhraseAndLocation;
import com.ngahr.PDFModifier.model.WritePDF;
import com.ngahr.PDFModifier.services.WritePDFService;

import java.awt.print.PrinterException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/PDFModifier")
public class PDFModifier {
	
	Logger logger = LoggerFactory.getLogger(PDFModifier.class);
	 @Autowired
	 WritePDFService writePDFService;
	 
	@GetMapping("/get/{filename}")
	public ResponseEntity<?> modifyPDF(@PathVariable("filename") String fName,HttpServletResponse response) throws InvalidPasswordException, IOException, PrinterException{
		
		ByteArrayOutputStream out = null;
		
		float fontSize = 12;
		PDFont font = PDType1Font.HELVETICA_BOLD;
		
		InputStream is = getClass().getResourceAsStream("/Files/"+fName+".pdf");
		
		PDDocument document  = PDDocument.load(is);
		
		PDPageTree pages = document.getPages();
		PDPage page;
		List<WritePDF> items;
		//loop the pages 
		for (int i=0;i< document.getNumberOfPages();i++)
		{	
			page = pages.get(i);
			items = writePDFService.findByPageNoOfDoc(i+1,fName);
			
			
			// loop for all the pdf form labels per page
			for(WritePDF item :items){
				float xOffset = 0;
				float yOffset = 0;
				
				// search the coordinates of the identifier and than accordingly determine coordinates of the new text 
				List<Float[]> coordinates = getWordLocation(item,document);
				//Calculating the xOffset and yOffset using item xShift and yShift Values
				if(coordinates.size() != 0){
				if(item.getRelativeDir().equalsIgnoreCase("R")){
				xOffset = coordinates.get(item.getSearchLabel().length())[0] + item.getXShift();
				yOffset = coordinates.get(item.getSearchLabel().length())[1] + item.getYShift();
				}
				else
				{ logger.debug("coordinates else");
					
					xOffset = coordinates.get(0)[0] + item.getXShift();
					yOffset = coordinates.get(0)[1] + item.getYShift();
					logger.debug("X label:"+ xOffset);
					logger.debug("Y label:"+ yOffset);
				}
				yOffset = page.getMediaBox().getHeight() - yOffset;
				}	
				PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
				contentStream.setFont(font, fontSize);
				logger.debug("X write:"+xOffset);
				logger.debug("Y write:"+yOffset);
				//Writing text on the pdf 
				contentStream.beginText();
				contentStream.newLineAtOffset(xOffset,yOffset); // coordinates of the new text
				contentStream.showText(item.getVariableValue()); // currently writing variable later change to the value of variable from object
				contentStream.endText();
				contentStream.close(); 
				}
			//end loop 
			
				
		
		}
		//end loop
		
		out = new ByteArrayOutputStream();
        document.save(out);
        document.close();
        
        byte[] data = out.toByteArray();
        is = new ByteArrayInputStream(data);
		
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename='"+fName+".pdf'");
        
        IOUtils.copy(is, response.getOutputStream());
        IOUtils.closeQuietly(is);
		

		return ResponseEntity.ok().body("Success");
	 }

	private List<Float[]> getWordLocation(WritePDF item, PDDocument file) throws IOException {
		PDDocument document = null;
		List<TextPosition> locations = null;
		try {
            
			PDFTextStripper stripper = new GetPhraseAndLocation();
            document = file;
            stripper.setSortByPosition(true);
            stripper.setStartPage(item.getPageNo());
            stripper.setEndPage(item.getPageNo());
 
            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);
            locations = ((GetPhraseAndLocation) stripper).getPositions();
        }
        finally {
        	
            
        }
		String searchString = item.getSearchLabel();
		List<Float[]> coordinates = new ArrayList<Float[]>();
		int j=0;
		if(locations !=null){
        for (TextPosition text : locations)
        	{	
        		String sLetter = Character.toString(searchString.charAt(j));
        		if(text.getUnicode().equals(sLetter))
        		{ 
        			j = j+1;
        			
        			Float [] cc = new Float[2] ;
    				cc[0] = text.getXDirAdj();
    				cc[1] = text.getYDirAdj();
  
    				coordinates.add(cc);
        			
    				if(j == searchString.length())
        			{	
        				break;
        			}
        		}
        		else
        		{coordinates.clear();
        			j =0;
        		}
        	}}
		return coordinates;
        
	}


}
