package com.ngahr.PDFModifier.services;

import java.util.List;

import com.ngahr.PDFModifier.model.WritePDF;

public interface WritePDFService {
	
	public List<WritePDF> findAll();
	public WritePDF update(WritePDF pdf);
	public WritePDF create(WritePDF pdf);
	public WritePDF findById(Long id);
	public List<WritePDF> findByPageNoOfDoc(Integer pageNo, String docName);
	public void deleteById(Long id);
	public void deleteByObject(WritePDF pdf);
	

}
