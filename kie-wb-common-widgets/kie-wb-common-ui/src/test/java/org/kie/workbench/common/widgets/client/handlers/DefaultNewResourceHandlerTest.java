/*
 * Copyright 2015 JBoss Inc
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

package org.kie.workbench.common.widgets.client.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.guvnor.common.services.project.context.ProjectContext;
import org.guvnor.common.services.project.model.Package;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.kie.workbench.common.services.shared.validation.ValidationService;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.uberfire.ext.editor.commons.client.validation.ValidatorWithReasonCallback;
import org.uberfire.workbench.type.AnyResourceTypeDefinition;
import org.uberfire.workbench.type.ResourceTypeDefinition;

@RunWith(GwtMockitoTestRunner.class) // needed for GWT.create(..) calls
public class DefaultNewResourceHandlerTest {

    private final String mockFileName = "filename";

    @Mock
    private PackageListBox packagesListBox;
    @Mock
    private ProjectContext context; // needed so that 'validationServiceCaller' is properly injected
    @Mock
    private Caller<KieProjectService> projectService; // needed so that 'validationServiceCaller' is properly injected
    @Mock(name = "validationService")
    private Caller<ValidationService> validationServiceCaller;
    @Mock
    private ValidationService validationService;
    @Captor
    private ArgumentCaptor<RemoteCallback> callbackCaptor;

    @InjectMocks
    private DumbDefaultNewResourceHandler handler = new DumbDefaultNewResourceHandler();

    @Before
    public void setup() {
        when(packagesListBox.getSelectedPackage()).thenReturn(new Package());
        when(validationServiceCaller.call(callbackCaptor.capture())).thenReturn(validationService);
    }

    @Test
    public void verifyValidationCalled() {
        handler.validate(mockFileName, mock(ValidatorWithReasonCallback.class));
        verify(validationService).isFileNameValid(mockFileName);
    }

    /**
     * A very dumb extension of {@link DefaultNewResourceHandler} - just so that we can get to the
     * {@link DefaultNewResourceHandler#validate(String, ValidatorWithReasonCallback) validate(..)}
     * method implementation easily.
     */
    private class DumbDefaultNewResourceHandler extends DefaultNewResourceHandler {

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public IsWidget getIcon() {
            return null;
        }

        @Override
        public ResourceTypeDefinition getResourceType() {
            return new AnyResourceTypeDefinition();
        }

        @Override
        public void create(Package pkg, String baseFileName, NewResourcePresenter presenter) {
        }
    }
}
