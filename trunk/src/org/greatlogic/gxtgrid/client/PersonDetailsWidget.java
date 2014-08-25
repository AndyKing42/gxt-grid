package org.greatlogic.gxtgrid.client;
/*
 * Copyright 2006-2014 Andy King (GreatLogic.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

public class PersonDetailsWidget extends Composite {
//--------------------------------------------------------------------------------------------------
@UiField
VBoxLayoutContainer     cardButtonsContainer;
@UiField
CardLayoutContainer     cardLayoutContainer;
@UiField
HBoxLayoutContainer     hblc1;
@UiField
VerticalLayoutContainer mainVLC;
//==================================================================================================
interface PersonDetailsWidgetUiBinder extends UiBinder<Widget, PersonDetailsWidget> { //
}
//==================================================================================================
public PersonDetailsWidget() {
  final PersonDetailsWidgetUiBinder uiBinder = GWT.create(PersonDetailsWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"saveChangesButton"})
public void onSaveButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  final int h1 = cardButtonsContainer.getOffsetHeight();
  final int h2 = cardButtonsContainer.getOffsetWidth();
  final int height = cardLayoutContainer.getOffsetHeight();
  final int width = cardLayoutContainer.getOffsetWidth();
  if (height < 0 || width < 0) {
    mainVLC = null;
  }
}
//--------------------------------------------------------------------------------------------------
}