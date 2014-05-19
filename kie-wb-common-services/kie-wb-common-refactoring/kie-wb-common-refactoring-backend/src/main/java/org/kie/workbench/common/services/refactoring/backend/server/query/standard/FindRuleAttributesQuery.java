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
import org.kie.workbench.common.services.refactoring.model.index.terms.RuleAttributeIndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.RuleAttributeValueIndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.valueterms.ValueIndexTerm;

@ApplicationScoped
public class FindRuleAttributesQuery implements NamedQuery {

    @Override
    public String getName() {
        return "FindRuleAttributesQuery";
    }

    @Override
    public Set<IndexTerm> getTerms() {
        return new HashSet<IndexTerm>() {{
            add( new RuleAttributeIndexTerm() );
            add( new RuleAttributeValueIndexTerm() );
        }};
    }

    @Override
    public Set<IndexTerm> getResultTerms() {
        return new HashSet<IndexTerm>() {{
            add( new RuleAttributeIndexTerm() );
            add( new RuleAttributeValueIndexTerm() );
        }};
    }

    @Override
    public Query toQuery( final Set<ValueIndexTerm> terms,
                          final boolean useWildcards ) {
        PortablePreconditions.checkNotNull( "terms",
                                            terms );
        if ( terms.size() != 2 ) {
            throw new IllegalArgumentException( "Required terms have not been provided. Require '" + RuleAttributeIndexTerm.TERM + "' and '" + RuleAttributeValueIndexTerm.TERM + "'." );
        }
        final Map<String, ValueIndexTerm> normalizedTerms = normalizeTerms( terms );
        final ValueIndexTerm ruleAttributeTerm = normalizedTerms.get( RuleAttributeIndexTerm.TERM );
        final ValueIndexTerm ruleAttributeValueTerm = normalizedTerms.get( RuleAttributeValueIndexTerm.TERM );
        if ( ruleAttributeTerm == null || ruleAttributeValueTerm == null ) {
            throw new IllegalArgumentException( "Required terms have not been provided. Require '" + RuleAttributeIndexTerm.TERM + "' and '" + RuleAttributeValueIndexTerm.TERM + "'." );
        }

        final QueryBuilder builder = new QueryBuilder();
        if ( useWildcards ) {
            builder.useWildcards();
        }
        builder.addTerm( ruleAttributeTerm ).addTerm( ruleAttributeValueTerm );
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
