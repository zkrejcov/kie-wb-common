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

import static org.mockito.Mockito.*;

import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.IsWidget;
import org.guvnor.common.services.project.context.ProjectContext;
import org.guvnor.common.services.project.model.Package;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.uberfire.commons.data.Pair;
import org.uberfire.ext.editor.commons.client.validation.ValidatorWithReasonCallback;
import org.uberfire.workbench.type.ResourceTypeDefinition;

@RunWith(MockitoJUnitRunner.class)
public class NewResourcePresenterTest {

    private static final String ERROR_MESSAGE = "test error message";

    @Mock
    private NewResourcePresenter.View view;
    @Spy
    private NewResourceHandlerMock mockHandler = new NewResourceHandlerMock();
    @Spy
    private NewResourceHandlerMockWithMessage mockHandlerWithMessage = new NewResourceHandlerMockWithMessage();
    @Captor
    ArgumentCaptor<ValidatorWithReasonCallback> validatorCaptor;
    @Captor
    ArgumentCaptor<Command> okCommandCaptor;
    @InjectMocks
    private NewResourcePresenter presenter;

    @Test
    public void errorMessageShownOnFailedValidationWithMessage() {
        doTestFailedValidation(mockHandlerWithMessage, ERROR_MESSAGE);
    }

    @Test
    public void errorMessageShownOnFailedValidationWithoutMessage() {
        doTestFailedValidation(mockHandler, null);
    }

    @Test
    public void testEmptyFileName() {
        doTestTrimmableInput("");
    }

    @Test
    public void testWhitespaceFileName() {
        doTestTrimmableInput("\n\r\t ");
    }

    private void doTestFailedValidation(NewResourceHandler mockHandler, String expectedMessage) {
        doValidationFailTest(mockHandler, "test");
        // check that error is presented
        verify(view).showError(expectedMessage);
        // verify that validation was called
        verify(mockHandler).validate(any(String.class), validatorCaptor.capture());
        verify(mockHandler).validate(any(String.class), eq(validatorCaptor.getValue()));
    }

    private void doTestTrimmableInput(String input) {
        doValidationFailTest(mockHandler, input);
        // check that error is presented
        verify(view).showMissingFilenameError();
    }

    private void doValidationFailTest(NewResourceHandler mockHandler, String mockFilename) {
        // setup
        presenter.show(mockHandler);
        when(view.getFileNameText()).thenReturn(mockFilename);
        // capture ok command
        verify(view).initModalFooterButtons(okCommandCaptor.capture(), any(Command.class));
        // simulate clicking ok
        okCommandCaptor.getValue().execute();
        // check that modal is still present
        verify(view, never()).hide();
        // verify that new resource creation was not requested
        verify(mockHandler, never()).create(any(Package.class), anyString(), any(NewResourcePresenter.class));
    }

    private class NewResourceHandlerMock extends HandlerMock {

        @Override
        public void validate(String baseFileName, ValidatorWithReasonCallback callback) {
            callback.onFailure();
        }
    }

    private class NewResourceHandlerMockWithMessage extends HandlerMock {

        @Override
        public void validate(String baseFileName, ValidatorWithReasonCallback callback) {
            callback.onFailure(ERROR_MESSAGE);
        }
    }

    private class HandlerMock implements NewResourceHandler {

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
            return null;
        }

        @Override
        public void create(Package pkg, String baseFileName, NewResourcePresenter presenter) {
        }

        @Override
        public List<Pair<String, ? extends IsWidget>> getExtensions() {
            return null;
        }

        @Override
        public void validate(String baseFileName, ValidatorWithReasonCallback callback) {
        }

        @Override
        public void acceptContext(ProjectContext context, Callback<Boolean, Void> callback) {
        }

    }
}
