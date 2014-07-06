package org.edr.resource.json;

import java.util.List;

import org.edr.domain.BudgetStaat;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class MixInBudgetStaat implements BudgetStaat {

	@Override
	@JsonIgnore
	public abstract List<BudgetStaat> getChildBudgetStaten();

	@Override
	@JsonIgnore
	public abstract BudgetStaat getParentBudgetStaat();

}
