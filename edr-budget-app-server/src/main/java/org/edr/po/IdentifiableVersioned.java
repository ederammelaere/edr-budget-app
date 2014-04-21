package org.edr.po;

public interface IdentifiableVersioned {

	Long getId();
	void setId(Long id);
	
	Integer getVersion();
	void setVersion(Integer version);
	
}
