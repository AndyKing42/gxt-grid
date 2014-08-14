package org.greatlogic.gxtgrid.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class GXTGrid implements EntryPoint {
//--------------------------------------------------------------------------------------------------
@Override
public void onModuleLoad() {
  final PersonDetailsWidget personDetailsWidget = new PersonDetailsWidget();
  RootLayoutPanel.get().add(personDetailsWidget);
}
//--------------------------------------------------------------------------------------------------
}