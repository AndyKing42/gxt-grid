package org.greatlogic.gxtgrid.client;

import java.util.ArrayList;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.StartEditHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.sencha.gxt.widget.core.client.info.DefaultInfoConfig;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.info.InfoConfig;

public class GXTGrid implements EntryPoint {
//--------------------------------------------------------------------------------------------------
@Override
public void onModuleLoad() {
  final ListStore<Pet> listStore = new ListStore<>(new ModelKeyProvider<Pet>() {
    @Override
    public String getKey(final Pet pet) {
      return Integer.toString(pet.getPetId());
    }
  });
  final IdentityValueProvider<Pet> ivp = new IdentityValueProvider<>();
  final CheckBoxSelectionModel<Pet> sm = new CheckBoxSelectionModel<>(ivp);
  final ArrayList<ColumnConfig<Pet, ?>> ccList = new ArrayList<>();
  ccList.add(sm.getColumn());
  final ColumnConfig<Pet, String> cc1;
  cc1 = new ColumnConfig<>(Pet.getPetNameValueProvider(), 100, "Name");
  ccList.add(cc1);
  final ColumnModel<Pet> columnModel = new ColumnModel<>(ccList);
  final Grid<Pet> grid = new Grid<>(listStore, columnModel);
  grid.setSelectionModel(sm);
  grid.setView(new GridView<Pet>());
  final GridRowEditing<Pet> gre = new GridRowEditing<>(grid);
  gre.addStartEditHandler(new StartEditHandler<GXTGrid.Pet>() {
    @Override
    public void onStartEdit(final StartEditEvent<Pet> event) {
      popup(30, "Start-ButtonBar:" + gre.getButtonBar().getElement());
      Element element = gre.getButtonBar().getElement();
      boolean widthFound = false;
      while (!widthFound && element != null) {
        element = element.getParentElement();
        if (element != null) {
          final String style = element.getAttribute("style");
          widthFound = style.contains("width");
        }
      }
      if (element != null) {
        element = element.getFirstChildElement();
        if (element != null) {
          element = element.getFirstChildElement();
          if (element != null) {
            element = element.getFirstChildElement();
            if (element != null) {
              element = element.getFirstChildElement();
              if (element != null) {
                element = element.getNextSiblingElement();
                if (element != null) {
                  element = element.getFirstChildElement();
                  if (element != null) {
                    element = element.getFirstChildElement();
                    String style = element.getAttribute("style");
                    final int widthIndex = style.indexOf("width: 17px");
                    if (widthIndex > 0) {
                      style = style.substring(0, widthIndex + 7) + "2" + //
                              style.substring(widthIndex + 9);
                      element.setAttribute("style", style);
                      element.setInnerHTML("");
                      //        <div class="gwt-Label GMFEK3RP5 GMFEK3ROJ GMFEK3RIK" style="margin: 0px;
                      //width: 17px; left: 3px; top: 1px;">
                      //org.greatlogic.gxtgrid.client.GXTGrid$Pet@5883969f</div>
                      //0123456789
                      popup(30, "Start-element:" + element);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  });
  gre.addEditor(cc1, new TextField());
  listStore.add(new Pet(1, "Lassie"));
  listStore.add(new Pet(2, "Scooby"));
  listStore.add(new Pet(3, "Snoopy"));
  final ContentPanel contentPanel = new ContentPanel();
  contentPanel.add(grid);
  RootLayoutPanel.get().add(contentPanel);
}
//--------------------------------------------------------------------------------------------------
private void popup(final int seconds, final String message) {
  final InfoConfig infoConfig = new DefaultInfoConfig("", message);
  infoConfig.setDisplay(seconds * 1000);
  final Info info = new Info();
  info.show(infoConfig);
}
//--------------------------------------------------------------------------------------------------
private static class Pet {
private final int _petId;
private String    _petName;
public static ValueProvider<Pet, String> getPetNameValueProvider() {
  return new ValueProvider<Pet, String>() {
    @Override
    public String getPath() {
      return "Pet.PetName";
    }
    @Override
    public String getValue(final Pet pet) {
      return pet._petName;
    }
    @Override
    public void setValue(final Pet pet, final String value) {
      pet._petName = value;
    }
  };
}
public Pet(final int petId, final String petName) {
  _petId = petId;
  _petName = petName;
}
public int getPetId() {
  return _petId;
}
}
//--------------------------------------------------------------------------------------------------
}