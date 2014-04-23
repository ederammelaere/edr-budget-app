package org.edr.po.jpa;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.edr.po.IdentifiableVersioned;

@MappedSuperclass
public class JpaIdentifiableVersioned implements IdentifiableVersioned {

	private Long id;
	private Integer version;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "NEXTKEY")
	@TableGenerator(name = "NEXTKEY", table = "nextkey", pkColumnName = "entityname", allocationSize = 5)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
