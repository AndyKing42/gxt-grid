package org.greatlogic.gxtgrid.client;

import java.util.ArrayList;
import java.util.TreeSet;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent.CancelEditHandler;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.StartEditHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

public class GXTGrid implements EntryPoint {
//--------------------------------------------------------------------------------------------------
private ListStore<Pet>   _listStore;
private boolean          _petWasSelected;
private TreeSet<Integer> _selectedPetIdSet;
//--------------------------------------------------------------------------------------------------
@Override
public void onModuleLoad() {
  _selectedPetIdSet = new TreeSet<>();
  _listStore = new ListStore<>(new ModelKeyProvider<Pet>() {
    @Override
    public String getKey(final Pet pet) {
      return Integer.toString(pet.getPetId());
    }
  });
  final CellSelectionModel<Pet> sm = new CellSelectionModel<>();
  final ArrayList<ColumnConfig<Pet, ?>> ccList = new ArrayList<>();
  final ValueProvider<Pet, Boolean> selectValueProvider;
  selectValueProvider = new ValueProvider<GXTGrid.Pet, Boolean>() {
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
  final ColumnConfig<Pet, Boolean> cc0 = new ColumnConfig<>(selectValueProvider, 23, "");
  final CheckBoxCell checkBoxCell = new CheckBoxCell() {
    @Override
    protected void onClick(final XElement parent, final NativeEvent event) {
      super.onClick(parent, event);
      final Pet pet = sm.getSelectedItem();
      if (!_selectedPetIdSet.remove(pet.getPetId())) {
        _selectedPetIdSet.add(pet.getPetId());
      }
    }
  };
  cc0.setCell(checkBoxCell);
  cc0.setFixed(true);
  cc0.setHideable(false);
  cc0.setMenuDisabled(true);
  cc0.setResizable(false);
  cc0.setSortable(false);
  ccList.add(cc0);
  final ColumnConfig<Pet, String> cc1;
  cc1 = new ColumnConfig<>(Pet.getPetNameValueProvider(), 100, "Name");
  ccList.add(cc1);
  final ColumnModel<Pet> columnModel = new ColumnModel<>(ccList);
  final Grid<Pet> grid = new Grid<>(_listStore, columnModel);
  grid.setSelectionModel(sm);
  grid.setView(new GridView<Pet>());
  final GridRowEditing<Pet> gre = new GridRowEditing<>(grid);
  gre.addStartEditHandler(new StartEditHandler<GXTGrid.Pet>() {
    @Override
    public void onStartEdit(final StartEditEvent<Pet> event) {
      _petWasSelected = _selectedPetIdSet.contains(sm.getSelectedItem().getPetId());
    }
  });
  gre.addCancelEditHandler(new CancelEditHandler<GXTGrid.Pet>() {
    @Override
    public void onCancelEdit(final CancelEditEvent<Pet> event) {
      if (_petWasSelected) {
        _selectedPetIdSet.add(sm.getSelectedItem().getPetId());
      }
      else {
        _selectedPetIdSet.remove(sm.getSelectedItem().getPetId());
      }
    }
  });
  final Field<Boolean> checkBox = new CheckBox();
  checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
    @Override
    public void onValueChange(final ValueChangeEvent<Boolean> event) {
      final Pet pet = sm.getSelectedItem();
      if (!_selectedPetIdSet.remove(pet.getPetId())) {
        _selectedPetIdSet.add(pet.getPetId());
      }
    }
  });
  gre.addEditor(cc0, checkBox);
  gre.addEditor(cc1, new TextField());
  _listStore.add(new Pet(1, "Lassie"));
  _listStore.add(new Pet(2, "Scooby"));
  _listStore.add(new Pet(3, "Snoopy"));
  final ContentPanel contentPanel = new ContentPanel();
  contentPanel.add(grid);
  RootLayoutPanel.get().add(contentPanel);
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