package de.fzi.cep.sepa.client.declarer;

import de.fzi.cep.sepa.model.impl.graph.SecDescription;
import de.fzi.cep.sepa.model.impl.graph.SecInvocation;

public interface SemanticEventConsumerDeclarer extends InvocableDeclarer<SecDescription, SecInvocation> {

	public boolean isVisualizable();
	
	public String getHtml(SecInvocation graph);
}
