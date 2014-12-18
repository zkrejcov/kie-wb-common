package org.kie.workbench.common.screens.datamodeller.backend.server;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.kie.workbench.common.screens.datamodeller.model.DataModelTO;
import org.kie.workbench.common.screens.datamodeller.model.DataObjectTO;
import org.kie.workbench.common.services.shared.project.KieProject;
import org.uberfire.backend.vfs.Path;

import static org.junit.Assert.*;

public class DataModelerInnerTypesTest extends DataModelerServiceBaseTest {


    @Test
    public void testDataModelerService() throws Exception {

        final URL packageUrl = this.getClass().getResource( "/TestInnerTypes" );
        final org.uberfire.java.nio.file.Path nioPackagePath = fs.getPath( packageUrl.toURI() );
        final Path packagePath = paths.convert( nioPackagePath );

        KieProject project = projectService.resolveProject( packagePath );

        DataModelTO dataModelTO = dataModelService.loadModel( project );


        int i = 0;

        /*
        Map<String, DataObjectTO> objectsMap = new HashMap<String, DataObjectTO>();

        assertNotNull( dataModelTO );

        assertEquals( dataModelOriginalTO.getDataObjects().size(), dataModelTO.getDataObjects().size() );

        for ( DataObjectTO dataObjectTO : dataModelTO.getDataObjects() ) {
            objectsMap.put( dataObjectTO.getClassName(), dataObjectTO );
        }

        for ( DataObjectTO dataObjectTO : dataModelOriginalTO.getDataObjects() ) {
            DataModelerAssert.assertEqualsDataObject( dataObjectTO, objectsMap.get( dataObjectTO.getClassName() ) );
        }
        */

    }

}
