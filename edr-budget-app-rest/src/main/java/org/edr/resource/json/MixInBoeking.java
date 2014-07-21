package org.edr.resource.json;

import org.edr.po.Boeking;
import org.edr.po.Journaal;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class MixInBoeking implements Boeking {

	@JsonIgnore
	@Override
	public abstract Journaal getJournaal();

}
