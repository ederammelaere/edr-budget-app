package org.edr.util.jpa;

public interface IdentifiableVersioned {

	Long getId();
	void setId(Long id);
	
	Integer getVersion();
	void setVersion(Integer version);
	
}
