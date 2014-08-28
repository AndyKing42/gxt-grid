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
import java.util.ArrayList;
import java.util.TreeSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

public class PersonDetailsWidget extends Composite {
//--------------------------------------------------------------------------------------------------
//@UiField
//CardLayoutContainer                cardLayoutContainer;
@UiField
ContentPanel                       petGridPanel;

private Grid<Pet>                  _petGrid;
private ListStore<Pet>             _petListStore;
private ColumnConfig<Pet, String>  _petNameColumnConfig;
private ColumnConfig<Pet, Boolean> _selectColumnConfig;
private TreeSet<Integer>           _selectedPetIdSet;
//==================================================================================================
interface PersonDetailsWidgetUiBinder extends UiBinder<Widget, PersonDetailsWidget> { //
}
//==================================================================================================
public PersonDetailsWidget() {
  final PersonDetailsWidgetUiBinder uiBinder = GWT.create(PersonDetailsWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
}
//--------------------------------------------------------------------------------------------------
private void createGrid() {
  _selectedPetIdSet = new TreeSet<>();
  final CellSelectionModel<Pet> selectionModel = new CellSelectionModel<>();
  _petGrid = new Grid<Pet>(createListStore(), createGridColumnModel(selectionModel));
  _petGrid.setSelectionModel(selectionModel);
  _petGrid.setView(new GridView<Pet>());
  _petGrid.getView().setColumnLines(true);
  _petGrid.getView().setStripeRows(true);
  final GridRowEditing<Pet> gridRowEditing = new GridRowEditing<>(_petGrid);
  gridRowEditing.getButtonBar().add(createGridDeleteButton(selectionModel, gridRowEditing));
  final Field<Boolean> checkBox = new CheckBox();
  checkBox.setEnabled(false);
  gridRowEditing.addEditor(_selectColumnConfig, checkBox);
  gridRowEditing.addEditor(_petNameColumnConfig, new TextField());
}
//--------------------------------------------------------------------------------------------------
private ColumnModel<Pet> createGridColumnModel(final CellSelectionModel<Pet> selectionModel) {
  final ArrayList<ColumnConfig<Pet, ?>> columnConfigList = new ArrayList<>();
  final ValueProvider<Pet, Boolean> selectValueProvider;
  selectValueProvider = new ValueProvider<Pet, Boolean>() {
    @Override
    public String getPath() {
      return "SelectCheckBox";
    }
    @Override
    public Boolean getValue(final Pet pet) {
      return _selectedPetIdSet.contains(pet.getPetId());
    }
    @Override
    public void setValue(final Pet pet, final Boolean selected) { //
    }
  };
  _selectColumnConfig = new ColumnConfig<>(selectValueProvider, 23, "");
  final CheckBoxCell checkBoxCell = new CheckBoxCell() {
    @Override
    protected void onClick(final XElement parent, final NativeEvent event) {
      super.onClick(parent, event);
      final Pet pet = selectionModel.getSelectedItem();
      if (!_selectedPetIdSet.remove(pet.getPetId())) {
        _selectedPetIdSet.add(pet.getPetId());
      }
    }
  };
  _selectColumnConfig.setCell(checkBoxCell);
  _selectColumnConfig.setFixed(true);
  _selectColumnConfig.setHideable(false);
  _selectColumnConfig.setMenuDisabled(true);
  _selectColumnConfig.setResizable(false);
  _selectColumnConfig.setSortable(false);
  columnConfigList.add(_selectColumnConfig);
  _petNameColumnConfig = new ColumnConfig<>(Pet.getPetNameValueProvider(), 100, "Name");
  columnConfigList.add(_petNameColumnConfig);
  return new ColumnModel<Pet>(columnConfigList);
}
//--------------------------------------------------------------------------------------------------
private TextButton createGridDeleteButton(final CellSelectionModel<Pet> selectionModel,
                                          final GridRowEditing<Pet> gridRowEditing) {
  final TextButton result = new TextButton("Delete");
  result.addSelectHandler(new SelectEvent.SelectHandler() {
    @Override
    public void onSelect(final SelectEvent event) {
      final Pet pet = selectionModel.getSelectedItem();
      final ConfirmMessageBox messageBox;
      messageBox = new ConfirmMessageBox("Delete Row", //
                                         "Are you sure you want to delete " + pet._petName + "?");
      messageBox.addDialogHideHandler(new DialogHideHandler() {
        @Override
        public void onDialogHide(final DialogHideEvent hideEvent) {
          if (hideEvent.getHideButton() == PredefinedButton.YES) {
            _petListStore.remove(pet);
            gridRowEditing.cancelEditing();
          }
        }
      });
      messageBox.show();
    }
  });
  return result;
}
//--------------------------------------------------------------------------------------------------
private ListStore<Pet> createListStore() {
  _petListStore = new ListStore<>(new ModelKeyProvider<Pet>() {
    @Override
    public String getKey(final Pet pet) {
      return Integer.toString(pet.getPetId());
    }
  });
  _petListStore.add(new Pet(1, "Lassie"));
  _petListStore.add(new Pet(2, "Otto"));
  _petListStore.add(new Pet(3, "Scooby"));
  _petListStore.add(new Pet(4, "Scrappy"));
  _petListStore.add(new Pet(5, "Snert"));
  _petListStore.add(new Pet(6, "Snoopy"));
  return _petListStore;
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"petsButton"})
public void onPetsButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  createGrid();
  petGridPanel.setWidget(_petGrid);
  //  cardLayoutContainer.setActiveWidget(petGridPanel);
}
//==================================================================================================
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
//==================================================================================================
}