package org.kie.workbench.common.services.backend.validation;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ValidationServiceImplTest {

    @Mock
    private org.uberfire.ext.editor.commons.service.ValidationService validationService;

    @InjectMocks
    private ValidationServiceImpl service;

    @Test
    public void verifyValidationCalled() {
        String input = "some text";
        service.isFileNameValid(input);
        verify(validationService).isFileNameValid(input);
    }
}
