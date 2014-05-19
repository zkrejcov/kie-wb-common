package org.kie.workbench.common.services.refactoring.backend.server.query.standard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;

import org.apache.lucene.search.Query;
import org.drools.workbench.models.datamodel.util.PortablePreconditions;
import org.kie.workbench.common.services.refactoring.backend.server.query.NamedQuery;
import org.kie.workbench.common.services.refactoring.backend.server.query.QueryBuilder;
import org.kie.workbench.common.services.refactoring.model.index.terms.IndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.ParentRuleIndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.RuleIndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.valueterms.ValueIndexTerm;

@ApplicationScoped
public class FindParentRulesQuery implements NamedQuery {

    @Override
    public String getName() {
        return "FindParentRulesQuery";
    }

    @Override
    public Set<IndexTerm> getTerms() {
        return new HashSet<IndexTerm>() {{
            add( new ParentRuleIndexTerm() );
        }};
    }

    @Override
    public Set<IndexTerm> getResultTerms() {
        return new HashSet<IndexTerm>() {{
            add( new RuleIndexTerm() );
            add( new ParentRuleIndexTerm() );
        }};
    }

    @Override
    public Query toQuery( final Set<ValueIndexTerm> terms,
                          final boolean useWildcards ) {
        PortablePreconditions.checkNotNull( "terms",
                                            terms );
        if ( terms.size() != 1 ) {
            throw new IllegalArgumentException( "Required term has not been provided. Require '" + ParentRuleIndexTerm.TERM + "'." );
        }
        final Map<String, ValueIndexTerm> normalizedTerms = normalizeTerms( terms );
        final ValueIndexTerm parentRuleTerm = normalizedTerms.get( ParentRuleIndexTerm.TERM );
        if ( parentRuleTerm == null ) {
            throw new IllegalArgumentException( "Required term has not been provided. Require '" + ParentRuleIndexTerm.TERM + "'." );
        }

        final QueryBuilder builder = new QueryBuilder();
        if ( useWildcards ) {
            builder.useWildcards();
        }
        builder.addTerm( parentRuleTerm );
        return builder.build();
    }

    private Map<String, ValueIndexTerm> normalizeTerms( final Set<ValueIndexTerm> terms ) {
        final Map<String, ValueIndexTerm> normalizedTerms = new HashMap<String, ValueIndexTerm>();
        for ( ValueIndexTerm term : terms ) {
            normalizedTerms.put( term.getTerm(),
                                 term );
        }
        return normalizedTerms;
    }

}
