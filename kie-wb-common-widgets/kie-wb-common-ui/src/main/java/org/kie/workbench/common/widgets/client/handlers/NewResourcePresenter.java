/*
 * Copyright 2012 JBoss Inc
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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.IsWidget;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.guvnor.common.services.project.model.Package;
import org.uberfire.commons.validation.PortablePreconditions;
import org.uberfire.ext.editor.commons.client.validation.ValidatorWithReasonCallback;

@ApplicationScoped
public class NewResourcePresenter {

    public interface View
            extends
            IsWidget {

        void show( final NewResourceHandler activeHandler );

        void hide();

        public void showError( String errorMessage );

        public void showMissingFilenameError();

        public String getFileNameText();

        public void resetControlGroupType();

        public void initModalFooterButtons( Command okCommand, Command cancelCommand );

    }

    private View view;

    private NewResourceHandler activeHandler = null;

    @Inject
    public NewResourcePresenter(View viewImpl) {
        this.view = viewImpl;
        Command okCommand = new Command() {
            @Override
            public void execute() {
                onOKButtonClick();
            }
        };
        Command cancelCommand = new Command() {
            @Override
            public void execute() {
                view.hide();
            }
        };
        view.initModalFooterButtons( okCommand,
                                     cancelCommand );
    }

    private void onOKButtonClick() {
        //Generic validation
        final String fileName = view.getFileNameText();
        if ( fileName == null || fileName.trim().isEmpty() ) {
            view.showMissingFilenameError();
            return;
        }

        //Specialized validation
        validate( fileName,
                  new ValidatorWithReasonCallback() {

                      @Override
                      public void onSuccess() {
                          view.resetControlGroupType();
                          makeItem( fileName );
                      }

                      @Override
                      public void onFailure() {
                          view.showError( null );
                      }

                      @Override
                      public void onFailure( final String reason ) {
                          view.showError( reason );
                      }

                  } );
    }

    public void show( final NewResourceHandler handler ) {
        activeHandler = PortablePreconditions.checkNotNull( "handler",
                                                            handler );
        view.show( activeHandler );
    }

    public void validate( final String fileName,
                          final ValidatorWithReasonCallback callback ) {
        if ( activeHandler != null ) {
            activeHandler.validate( fileName,
                                    callback );
        }
    }

    public void makeItem( final String fileName ) {
        if ( activeHandler != null ) {
            Package activePackage = null;
            if ( activeHandler instanceof PackageContextProvider ) {
                activePackage = ( (PackageContextProvider) activeHandler ).getPackage();
            }
            activeHandler.create( activePackage,
                                  fileName,
                                  NewResourcePresenter.this );
        }
    }

    public void complete() {
        view.hide();
    }

}
