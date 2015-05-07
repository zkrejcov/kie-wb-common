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

import java.util.List;
import javax.enterprise.context.ApplicationScoped;

import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.base.InlineLabel;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.kie.workbench.common.widgets.client.resources.i18n.NewItemPopupConstants;
import org.uberfire.commons.data.Pair;
import org.uberfire.ext.widgets.common.client.common.popups.BaseModal;
import org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterOKCancelButtons;

@ApplicationScoped
public class NewResourceView extends BaseModal implements NewResourcePresenter.View {

    interface NewResourceViewBinder
            extends
            UiBinder<Widget, NewResourceView> {

    }

    private static NewResourceViewBinder uiBinder = GWT.create( NewResourceViewBinder.class );

    private ModalFooterOKCancelButtons footer;

    @UiField
    ControlGroup fileNameGroup;

    @UiField
    InlineLabel fileTypeLabel;

    @UiField
    TextBox fileNameTextBox;

    @UiField
    HelpInline fileNameHelpInline;

    @UiField
    ControlGroup handlerExtensionsGroup;

    @UiField
    VerticalPanel handlerExtensions;

    public NewResourceView() {
        add( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public void initModalFooterButtons( Command okCommand, Command cancelCommand ) {
        footer = new ModalFooterOKCancelButtons( okCommand,
                                                 cancelCommand );
        footer.enableOkButton( true );
        add( footer );
    }

    @Override
    public void show( final NewResourceHandler handler ) {
        //Clear previous resource name
        fileNameTextBox.setText( "" );
        fileNameGroup.setType( ControlGroupType.NONE );
        fileNameHelpInline.setText( "" );
        setActiveHandler(handler);
        setTitle( NewItemPopupConstants.INSTANCE.popupTitle() + " " + handler.getDescription() );
        super.show();
    }

    private void setActiveHandler( final NewResourceHandler handler ) {
        final List<Pair<String, ? extends IsWidget>> extensions = handler.getExtensions();
        final boolean showExtensions = !( extensions == null || extensions.isEmpty() );
        fileTypeLabel.setText( handler.getDescription() );

        handlerExtensions.clear();
        handlerExtensionsGroup.getElement().getStyle().setDisplay( showExtensions ? Style.Display.BLOCK : Style.Display.NONE );
        if ( showExtensions ) {
            for ( Pair<String, ? extends IsWidget> extension : extensions ) {
                final ControlGroup cg = new ControlGroup();
                cg.add( extension.getK2() );
                handlerExtensions.add( cg );
            }
        }
    }

    @Override
    public String getFileNameText() {
        return fileNameTextBox.getText();
    }

    @Override
    public void showError( String errorMessage ) {
        fileNameGroup.setType( ControlGroupType.ERROR );
        if ( errorMessage != null ) {
            fileNameHelpInline.setText( errorMessage );
        }
    }

    @Override
    public void showMissingFilenameError() {
        showError( NewItemPopupConstants.INSTANCE.fileNameIsMandatory() );
    }

    @Override
    public void resetControlGroupType() {
        fileNameGroup.setType( ControlGroupType.NONE );
    }
}
