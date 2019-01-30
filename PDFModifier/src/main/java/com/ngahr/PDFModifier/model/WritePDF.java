package com.ngahr.PDFModifier.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table
@NamedQueries({
    @NamedQuery(name = "WritePDF.findAll", query = "SELECT wp FROM WritePDF wp"),
    @NamedQuery(name = "WritePDF.findByPageNoOfDoc", query = "SELECT wp FROM WritePDF wp WHERE wp.pageNo = :pageNo AND wp.docName = :dname"),
})
public class WritePDF {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column
	private String variableValue;
	
	@Column
	private Integer pageNo; 
	
	@Column
	private String docName;
	
	@Column
	private String searchLabel;
	
	@Column
	private Float XShift;
	
	@Column
	private Float YShift;
	
	@Column
	private Integer searchLabelIndex;
	
	@Column
	private String relativeDir;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVariableValue() {
		return variableValue;
	}

	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getSearchLabel() {
		return searchLabel;
	}

	public void setSearchLabel(String searchLabel) {
		this.searchLabel = searchLabel;
	}

	public Float getXShift() {
		return XShift;
	}

	public void setXShift(Float xShift) {
		XShift = xShift;
	}

	public Float getYShift() {
		return YShift;
	}

	public void setYShift(Float yShift) {
		YShift = yShift;
	}

	public Integer getSearchLabelIndex() {
		return searchLabelIndex;
	}

	public void setSearchLabelIndex(Integer searchLabelIndex) {
		this.searchLabelIndex = searchLabelIndex;
	}

	public String getRelativeDir() {
		return relativeDir;
	}

	public void setRelativeDir(String relativeDir) {
		this.relativeDir = relativeDir;
	}
	

}
