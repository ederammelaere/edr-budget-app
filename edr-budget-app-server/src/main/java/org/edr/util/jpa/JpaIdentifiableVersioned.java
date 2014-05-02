package org.edr.util.jpa;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.proxy.HibernateProxyHelper;

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

	@Override
	public boolean equals(Object other) {
		// Test op null
		if (other == null) {
			return false;
		}

		// Vergelijk pointers
		if (this == other) {
			return true;
		}

		// Vergelijken class
		Class<?> thisClass = HibernateProxyHelper.getClassWithoutInitializingProxy(this);
		Class<?> otherClass = HibernateProxyHelper.getClassWithoutInitializingProxy(other);
		if (!thisClass.equals(otherClass)) {
			return false;
		}

		// Vergelijken id
		IdentifiableVersioned objIV = (IdentifiableVersioned) other;
		if (objIV.getId() == null) {
			return false;
		} else {
			return objIV.getId().equals(this.id);
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1515, 12797).append(id).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(id).append(version).toString();
	}

}
