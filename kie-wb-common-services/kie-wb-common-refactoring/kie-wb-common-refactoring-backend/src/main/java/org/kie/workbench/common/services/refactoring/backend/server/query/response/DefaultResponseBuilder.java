/*
 * Copyright 2014 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.workbench.common.services.refactoring.backend.server.query.response;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.kie.workbench.common.services.refactoring.model.query.RefactoringPageRow;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.io.IOService;
import org.uberfire.java.nio.file.Path;
import org.uberfire.metadata.model.KObject;
import org.uberfire.paging.PageResponse;

public class DefaultResponseBuilder implements ResponseBuilder {

    @Override
    public PageResponse<RefactoringPageRow> buildResponse( final int pageSize,
                                                           final int startRow,
                                                           final IOService ioService,
                                                           final List<KObject> kObjects ) {
        final List<RefactoringPageRow> result = new ArrayList<RefactoringPageRow>( kObjects.size() );
        for ( final KObject kObject : kObjects ) {
            final Path path = ioService.get( URI.create( kObject.getKey() ) );
            final RefactoringPageRow row = new RefactoringPageRow( Paths.convert( path ) );
            result.add( row );
        }

        final int hits = kObjects.size();
        final PageResponse<RefactoringPageRow> response = new PageResponse<RefactoringPageRow>();
        response.setTotalRowSize( hits );
        response.setPageRowList( result );
        response.setTotalRowSizeExact( true );
        response.setStartRowIndex( startRow );
        response.setLastPage( ( pageSize * startRow + 2 ) >= hits );

        return response;
    }

}
