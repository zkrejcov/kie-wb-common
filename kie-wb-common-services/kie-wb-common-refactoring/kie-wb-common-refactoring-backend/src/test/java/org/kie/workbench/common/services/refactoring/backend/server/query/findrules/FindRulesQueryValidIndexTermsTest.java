package org.kie.workbench.common.services.refactoring.backend.server.query.findrules;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.inject.Instance;

import org.apache.lucene.analysis.Analyzer;
import org.junit.Test;
import org.kie.workbench.common.services.refactoring.backend.server.BaseIndexingTest;
import org.kie.workbench.common.services.refactoring.backend.server.TestIndexer;
import org.kie.workbench.common.services.refactoring.backend.server.drl.TestDrlFileIndexer;
import org.kie.workbench.common.services.refactoring.backend.server.drl.TestDrlFileTypeDefinition;
import org.kie.workbench.common.services.refactoring.backend.server.indexing.RuleAttributeNameAnalyzer;
import org.kie.workbench.common.services.refactoring.backend.server.query.NamedQuery;
import org.kie.workbench.common.services.refactoring.backend.server.query.RefactoringQueryServiceImpl;
import org.kie.workbench.common.services.refactoring.backend.server.query.standard.FindRulesQuery;
import org.kie.workbench.common.services.refactoring.model.index.terms.RuleAttributeIndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.RuleIndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.valueterms.ValueIndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.valueterms.ValueRuleIndexTerm;
import org.kie.workbench.common.services.refactoring.model.query.RefactoringPageRequest;
import org.kie.workbench.common.services.refactoring.model.query.RefactoringPageRow;
import org.kie.workbench.common.services.refactoring.service.RefactoringQueryService;
import org.uberfire.java.nio.file.Path;
import org.uberfire.paging.PageResponse;

import static org.apache.lucene.util.Version.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FindRulesQueryValidIndexTermsTest extends BaseIndexingTest<TestDrlFileTypeDefinition> {

    private Set<NamedQuery> queries = new HashSet<NamedQuery>() {{
        add( new FindRulesQuery() );
    }};

    @Test
    public void testFindRulesQueryValidIndexTerms() throws IOException, InterruptedException {
        final Instance<NamedQuery> namedQueriesProducer = mock( Instance.class );
        when( namedQueriesProducer.iterator() ).thenReturn( queries.iterator() );

        final RefactoringQueryService service = new RefactoringQueryServiceImpl( getConfig(),
                                                                                 ioService(),
                                                                                 namedQueriesProducer );

        //Don't ask, but we need to write a single file first in order for indexing to work
        final Path basePath = getDirectoryPath().resolveSibling( "someNewOtherPath" );
        ioService().write( basePath.resolve( "dummy" ),
                           "<none>" );

        //Add test files
        final Path path1 = basePath.resolve( "drl1.drl" );
        final String drl1 = loadText( "drl1.drl" );
        ioService().write( path1,
                           drl1 );

        Thread.sleep( 5000 ); //wait for events to be consumed from jgit -> (notify changes -> watcher -> index) -> lucene index

        {
            final RefactoringPageRequest request = new RefactoringPageRequest( "FindRulesQuery",
                                                                               new HashSet<ValueIndexTerm>() {{
                                                                                   add( new ValueRuleIndexTerm( "myRule" ) );
                                                                               }},
                                                                               0,
                                                                               10 );

            try {
                final PageResponse<RefactoringPageRow> response = service.query( request );
                assertNotNull( response );
                assertEquals( 1,
                              response.getPageRowList().size() );
                assertResponseContains( response.getPageRowList(),
                                        path1 );
                final RefactoringPageRow row = response.getPageRowList().get( 0 );
                assertEquals( 1,
                              row.getTerms().size() );
                assertEquals( "myRule",
                              row.getTerms().get( RuleIndexTerm.TERM ) );

            } catch ( IllegalArgumentException e ) {
                fail();
            }
        }

    }

    @Override
    protected TestIndexer getIndexer() {
        return new TestDrlFileIndexer();
    }

    @Override
    public Map<String, Analyzer> getAnalyzers() {
        return new HashMap<String, Analyzer>() {{
            put( RuleAttributeIndexTerm.TERM,
                 new RuleAttributeNameAnalyzer( LUCENE_40 ) );
        }};
    }

    @Override
    protected TestDrlFileTypeDefinition getResourceTypeDefinition() {
        return new TestDrlFileTypeDefinition();
    }

    @Override
    protected String getRepositoryName() {
        return this.getClass().getSimpleName();
    }

    private void assertResponseContains( final List<RefactoringPageRow> rows,
                                         final Path path ) {
        for ( RefactoringPageRow row : rows ) {
            final String rowFileName = row.getPath().getFileName();
            final String fileName = path.getFileName().toString();
            if ( rowFileName.endsWith( fileName ) ) {
                return;
            }
        }
        fail( "Response does not contain expected Path '" + path.toUri().toString() );
    }

}
