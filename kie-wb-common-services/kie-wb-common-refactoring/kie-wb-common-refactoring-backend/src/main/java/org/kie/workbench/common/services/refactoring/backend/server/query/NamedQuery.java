package org.kie.workbench.common.services.refactoring.backend.server.query;

import java.util.Set;

import org.apache.lucene.search.Query;
import org.kie.workbench.common.services.refactoring.backend.server.query.response.ResponseBuilder;
import org.kie.workbench.common.services.refactoring.model.index.terms.IndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.valueterms.ValueIndexTerm;

public interface NamedQuery {

    String getName();

    Set<IndexTerm> getTerms();

    Query toQuery( final Set<ValueIndexTerm> terms,
                   final boolean useWildcards );

    ResponseBuilder getResponseBuilder();

}
