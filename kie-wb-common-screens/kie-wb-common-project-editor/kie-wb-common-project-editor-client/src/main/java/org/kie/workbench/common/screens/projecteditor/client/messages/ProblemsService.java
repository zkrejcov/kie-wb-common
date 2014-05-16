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

package org.kie.workbench.common.screens.projecteditor.client.messages;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import org.guvnor.common.services.project.builder.model.BuildMessage;
import org.guvnor.common.services.project.builder.model.BuildResults;
import org.guvnor.common.services.project.builder.model.IncrementalBuildResults;
import org.guvnor.common.services.shared.events.PublishBatchMessagesEvent;
import org.guvnor.common.services.shared.events.PublishMessagesEvent;
import org.guvnor.common.services.shared.events.SystemMessage;
import org.guvnor.common.services.shared.events.UnpublishMessagesEvent;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.workbench.PanelManager;
import org.uberfire.client.workbench.events.PerspectiveChange;
import org.uberfire.rpc.SessionInfo;
import org.uberfire.security.Identity;

/**
 * Service for Message Console, the Console is a screen that shows compile time errors.
 * This listens to Messages and if the Console is not open it opens it.
 */
@ApplicationScoped
public class ProblemsService {

    private final PlaceManager placeManager;
    private final PanelManager panelManager;
    private final ListDataProvider<BuildMessage> dataProviderOLD = new ListDataProvider<BuildMessage>();

    private final ListDataProvider<ProblemsServiceRow> dataProvider = new ListDataProvider<ProblemsServiceRow>( );

    private List<String> allowedPerspectives = new ArrayList<String>();

    private String currentPerspective;

    @Inject
    private SessionInfo sessionInfo;

    @Inject
    private Identity identity;

    @Inject
    public ProblemsService( final PlaceManager placeManager,
                            final PanelManager panelManager ) {
        this.placeManager = placeManager;
        this.panelManager = panelManager;
    }

    @PostConstruct
    protected void init() {
        allowedPerspectives.add("org.kie.workbench.drools.client.perspectives.DroolsAuthoringPerspective");
        allowedPerspectives.add("org.kie.workbench.client.perspectives.DroolsAuthoringPerspective");
    }

    public void publishMessages( final @Observes PublishMessagesEvent publishEvent ) {
        publishMessages( publishEvent.getSessionId(), publishEvent.getUserId(), publishEvent.getPlace(), publishEvent.getMessagesToPublish() );
        if (publishEvent.isShowSystemConsole() && allowedPerspectives.contains(currentPerspective)) {
            placeManager.goTo( "org.kie.guvnor.Problems" );
        }
    }

    public void unpublishMessages( final @Observes UnpublishMessagesEvent unpublishEvent ) {
        unpublishMessages( unpublishEvent.getSessionId(), unpublishEvent.getUserId(), unpublishEvent.getMessageType(), unpublishEvent.getMessagesToUnpublish() );
        if (unpublishEvent.isShowSystemConsole() && allowedPerspectives.contains(currentPerspective)) {
            placeManager.goTo( "org.kie.guvnor.Problems" );
        }
    }

    public void publishBatchMessages( final @Observes PublishBatchMessagesEvent publishBatchEvent ) {
        if ( publishBatchEvent.isCleanExisting() ) {
            unpublishMessages( publishBatchEvent.getSessionId(), publishBatchEvent.getUserId(), publishBatchEvent.getMessageType(), publishBatchEvent.getMessagesToUnpublish() );
        } else {
            //only remove provided messages
            removeRowsByMessage( publishBatchEvent.getMessagesToUnpublish() );
        }
        publishMessages( publishBatchEvent.getSessionId(), publishBatchEvent.getUserId(), publishBatchEvent.getPlace(), publishBatchEvent.getMessagesToPublish() );
        if (publishBatchEvent.isShowSystemConsole() && allowedPerspectives.contains(currentPerspective)) {
            placeManager.goTo( "org.kie.guvnor.Problems" );
        }
    }

    public void addBuildMessages( final @Observes BuildResults results ) {
        List<BuildMessage> list = dataProviderOLD.getList();
        list.clear();
        for ( BuildMessage buildMessage : results.getMessages() ) {
            list.add( buildMessage );
        }
        if (allowedPerspectives.contains(currentPerspective)) {
            placeManager.goTo( "org.kie.guvnor.Problems" );
        }
    }

    public void addIncrementalBuildMessages( final @Observes IncrementalBuildResults results ) {
        final List<BuildMessage> addedMessages = results.getAddedMessages();
        final List<BuildMessage> removedMessages = results.getRemovedMessages();

        List<BuildMessage> list = dataProviderOLD.getList();
        for ( BuildMessage buildMessage : removedMessages ) {
            list.remove( buildMessage );
        }
        for ( BuildMessage buildMessage : addedMessages ) {
            list.add( buildMessage );
        }
        if ( allowedPerspectives.contains(currentPerspective) ) {
            placeManager.goTo( "org.kie.guvnor.Problems" );
        }
    }

    public void addDataDisplay( HasData<ProblemsServiceRow> display ) {
        dataProvider.addDataDisplay( display );
    }

    public void onPerspectiveChange(@Observes PerspectiveChange perspectiveChange) {
        currentPerspective = perspectiveChange.getIdentifier();
    }

    private void publishMessages( String sessionId, String userId, PublishMessagesEvent.Place place, List<SystemMessage> messages ) {
        List<ProblemsServiceRow> list = dataProvider.getList();
        List<SystemMessage> newMessages = filterMessages( sessionId, userId, null, messages );
        List<ProblemsServiceRow> newRows = new ArrayList<ProblemsServiceRow>();

        int index = ( place != null && place == PublishMessagesEvent.Place.TOP ) ? 0 : ( list != null && list.size() > 0 ? list.size() : 0 );

        for ( SystemMessage systemMessage : newMessages ) {
            newRows.add( new ProblemsServiceRow( sessionId, userId, systemMessage ) );
        }

        list.addAll( index, newRows );
    }

    private void unpublishMessages( String sessionId, String userId, String messageType, List<SystemMessage> messages ) {

        String currentSessionId = sessionInfo != null ? sessionInfo.getId() : null;
        String currentUserId = identity != null ? identity.getName() : null;

        List<ProblemsServiceRow> rowsToDelete = new ArrayList<ProblemsServiceRow>();
        for ( ProblemsServiceRow row : dataProvider.getList() ) {
            if ( sessionId == null && userId == null ) {
                //delete messages for all users and sessions
                if ( messageType == null || messageType.equals( row.getMessageType() ) ) {
                    rowsToDelete.add( row );
                }
            } else if ( sessionId != null ) {
                //messages for a given session, no matter what the user have, sessions are unique.
                if ( sessionId.equals( currentSessionId ) && ( messageType == null || messageType.equals( row.getMessageType() ) ) ) {
                    rowsToDelete.add( row );
                }

            } else {
                //messages for a user.
                if ( userId.equals( currentUserId ) && ( messageType == null || messageType.equals( row.getMessageType() ) ) ) {
                    rowsToDelete.add( row );
                }
            }
        }

        dataProvider.getList().removeAll( rowsToDelete );
        removeRowsByMessage( messages );
    }

    private void removeRowsByMessage(List<SystemMessage> messages) {
        List<ProblemsServiceRow> rowsToDelete = new ArrayList<ProblemsServiceRow>( );
        if (messages != null) {
            for (ProblemsServiceRow row : dataProvider.getList()) {
                if (messages.contains( row.getMessage() )) rowsToDelete.add( row );
            }
            dataProvider.getList().removeAll( rowsToDelete );
        }
    }

    private List<SystemMessage> filterMessages( String sessionId, String userId, String messageType, List<SystemMessage> messages ) {
        List<SystemMessage> result = new ArrayList<SystemMessage>();

        String currentSessionId = sessionInfo != null ? sessionInfo.getId() : null;
        String currentUserId = identity != null ? identity.getName() : null;

        if ( messages != null ) {
            for ( SystemMessage message : messages ) {
                if ( sessionId == null && userId == null ) {
                    //messages for all users, all sessions.
                    if ( messageType == null || messageType.equals( message.getMessageType() ) ) {
                        result.add( message );
                    }

                } else if ( sessionId != null ) {
                    //messages for a given session, no matter what the user have, sessions are unique.
                    if ( sessionId.equals( currentSessionId ) && ( messageType == null || messageType.equals( message.getMessageType() ) ) ) {
                        result.add( message );
                    }

                } else {
                    //messages for a user.
                    if ( userId.equals( currentUserId ) && ( messageType == null || messageType.equals( message.getMessageType() ) ) ) {
                        result.add( message );
                    }
                }
            }
        }
        return result;
    }
}