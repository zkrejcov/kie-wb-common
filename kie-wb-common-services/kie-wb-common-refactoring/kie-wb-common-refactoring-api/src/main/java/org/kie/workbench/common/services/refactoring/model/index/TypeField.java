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
package org.kie.workbench.common.services.refactoring.model.index;

import java.util.ArrayList;
import java.util.List;

import org.uberfire.commons.data.Pair;
import org.uberfire.commons.validation.PortablePreconditions;

public class TypeField implements IndexElementsGenerator {

    private String name;
    private String fullyQualifiedClassName;
    private String parentClassFullQualifiedClassName;

    public TypeField( final String name,
                      final String fullyQualifiedClassName,
                      final String parentClassFullQualifiedClassName ) {
        this.name = PortablePreconditions.checkNotNull( "name",
                                                        name );
        this.fullyQualifiedClassName = PortablePreconditions.checkNotNull( "fullyQualifiedClassName",
                                                                           fullyQualifiedClassName );
        this.parentClassFullQualifiedClassName = PortablePreconditions.checkNotNull( "parentClassFullQualifiedClassName",
                                                                                     parentClassFullQualifiedClassName );
    }

    @Override
    public List<Pair<String, String>> toIndexElements() {
        final List<Pair<String, String>> indexElements = new ArrayList<Pair<String, String>>();
        indexElements.add( new Pair<String, String>( IndexableElements.FIELD_TYPE_NAME.toString(),
                                                     name ) );
        indexElements.add( new Pair<String, String>( IndexableElements.FIELD_TYPE_FULLY_QUALIFIED_CLASS_NAME.toString(),
                                                     fullyQualifiedClassName ) );
        indexElements.add( new Pair<String, String>( IndexableElements.TYPE_NAME.toString() + ":" + parentClassFullQualifiedClassName + ":" + IndexableElements.FIELD_TYPE_NAME.toString(),
                                                     name ) );
        return indexElements;
    }

}
