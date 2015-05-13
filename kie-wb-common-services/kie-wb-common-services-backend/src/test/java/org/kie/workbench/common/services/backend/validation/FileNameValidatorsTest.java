package org.kie.workbench.common.services.backend.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// yeah, I'm kinda lazy...and this is still only an example kind of thing
public class FileNameValidatorsTest {

    private final String[] names = {"`", "\"", "?", "*", "/", "\\", "<", ">", "|", ":", "\n", "\r", "\t", "\0", "\f"};
    private final String[] emptyNames = {"", " "};

    protected JavaFileNameValidator validatorJava = new JavaFileNameValidator();
    protected PackageNameValidator validatorPackage = new PackageNameValidator();
    protected ProjectNameValidator validatorProject = new ProjectNameValidator();

    // just to verify that the validation doesn't simply return false for everything
    @Test
    public void verifyValidFilenamePasses() {
        assertTrue(validatorJava.isValid("someVALIDname.java"));
        assertTrue(validatorPackage.isValid("someVALIDname"));
        assertTrue(validatorProject.isValid("some VALID name"));
    }

    @Test
    public void testInvalidChars() {
        for (String filename : names) {
            doTestFor(filename);
            doTestFor("prefix" + filename);
            doTestFor(filename + "suffix");
            doTestFor("prefix" + filename + "suffix");
        }
    }

    @Test
    public void testEmptyFilename() {
        for (String filename : emptyNames) {
            doTestFor(filename);
        }
    }

    private void doTestFor(String filename) {
        String desc = "tested filename: '" + filename + "' with ";
        assertNotNull(desc + validatorJava.getClass().getSimpleName(), validatorJava.isValid(filename));
        assertFalse(desc + validatorJava.getClass().getSimpleName(), validatorJava.isValid(filename));
        assertNotNull(desc + validatorPackage.getClass().getSimpleName(), validatorPackage.isValid(filename));
        assertFalse(desc + validatorPackage.getClass().getSimpleName(), validatorPackage.isValid(filename));
        assertNotNull(desc + validatorProject.getClass().getSimpleName(), validatorProject.isValid(filename));
        assertFalse(desc + validatorProject.getClass().getSimpleName(), validatorProject.isValid(filename));
    }
}
