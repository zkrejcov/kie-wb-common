package org.kie.workbench.common.services.refactoring.backend.server.query.standard;

import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;

import org.apache.lucene.search.Query;
import org.drools.workbench.models.datamodel.util.PortablePreconditions;
import org.kie.workbench.common.services.refactoring.backend.server.query.NamedQuery;
import org.kie.workbench.common.services.refactoring.backend.server.query.QueryBuilder;
import org.kie.workbench.common.services.refactoring.model.index.terms.IndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.RuleIndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.valueterms.ValueIndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.valueterms.ValueRuleIndexTerm;

@ApplicationScoped
public class FindRulesQuery implements NamedQuery {

    @Override
    public String getName() {
        return "FindRulesQuery";
    }

    @Override
    public Set<IndexTerm> getTerms() {
        return new HashSet<IndexTerm>() {{
            add( new RuleIndexTerm() );
        }};
    }

    @Override
    public Set<IndexTerm> getResultTerms() {
        return new HashSet<IndexTerm>() {{
            add( new RuleIndexTerm() );
        }};
    }

    @Override
    public Query toQuery( final Set<ValueIndexTerm> terms,
                          final boolean useWildcards ) {
        PortablePreconditions.checkNotNull( "terms",
                                            terms );
        if ( terms.size() > 1 ) {
            throw new IllegalArgumentException( "terms should only contain one item" );
        }
        final ValueIndexTerm vit = terms.iterator().next();
        if ( !( vit instanceof ValueRuleIndexTerm ) ) {
            throw new IllegalArgumentException( "term is not an instance of ValueRuleIndexTerm" );
        }
        final QueryBuilder builder = new QueryBuilder();
        if ( useWildcards ) {
            builder.useWildcards();
        }
        builder.addTerm( vit );
        return builder.build();
    }

}
