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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.NumberCell;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.TextMetrics;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent.ColumnWidthChangeHandler;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.HeaderContextMenuEvent;
import com.sencha.gxt.widget.core.client.event.HeaderContextMenuEvent.HeaderContextMenuHandler;
import com.sencha.gxt.widget.core.client.event.RefreshEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.BigDecimalField;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class PetGridWidget implements IsWidget {
//--------------------------------------------------------------------------------------------------
private final HashSet<ColumnConfig<Pet, ?>>   _checkBoxSet;
private TreeMap<String, ColumnConfig<Pet, ?>> _columnConfigMap;
private ContentPanel                          _contentPanel;
private Grid<Pet>                             _grid;
protected ListStore<Pet>                      _petStore;
private GridSelectionModel<Pet>               _selectionModel;
//--------------------------------------------------------------------------------------------------
protected PetGridWidget() {
  super();
  _petStore = new ListStore<Pet>(new ModelKeyProvider<Pet>() {
    @Override
    public String getKey(final Pet pet) {
      return Integer.toString(pet.getPetId());
    }
  });
  _checkBoxSet = new HashSet<ColumnConfig<Pet, ?>>();
  createContentPanel();
  createGrid();
  _contentPanel.add(_grid);
}
//--------------------------------------------------------------------------------------------------
private void addHeaderContextMenuHandler() {
  final HeaderContextMenuHandler headerContextMenuHandler = new HeaderContextMenuHandler() {
    @Override
    public void onHeaderContextMenu(final HeaderContextMenuEvent headerContextMenuEvent) {
      MenuItem menuItem = new MenuItem("Size To Fit");
      menuItem.addSelectionHandler(new SelectionHandler<Item>() {
        @Override
        public void onSelection(final SelectionEvent<Item> selectionEvent) {
          resizeColumnToFit(headerContextMenuEvent.getColumnIndex());
          _grid.getView().refresh(true);
        }
      });
      headerContextMenuEvent.getMenu().add(menuItem);
      menuItem = new MenuItem("Size All To Fit");
      menuItem.addSelectionHandler(new SelectionHandler<Item>() {
        @Override
        public void onSelection(final SelectionEvent<Item> selectionEvent) {
          final int startIndex = _selectionModel instanceof CheckBoxSelectionModel ? 1 : 0;
          for (int columnIndex = startIndex; columnIndex < _grid.getColumnModel().getColumnCount(); ++columnIndex) {
            resizeColumnToFit(columnIndex);
          }
          _grid.getView().refresh(true);
        }
      });
      headerContextMenuEvent.getMenu().add(menuItem);
    }
  };
  _grid.addHeaderContextMenuHandler(headerContextMenuHandler);
}
//--------------------------------------------------------------------------------------------------
@Override
public Widget asWidget() {
  return _contentPanel;
}
//--------------------------------------------------------------------------------------------------
private void centerCheckBox(final ColumnConfig<Pet, ?> columnConfig) {
  final int leftPadding = (columnConfig.getWidth() - 12) / 2;
  final String styles = "padding: 3px 0px 0px " + leftPadding + "px;";
  final SafeStyles textStyles = SafeStylesUtils.fromTrustedString(styles);
  columnConfig.setColumnTextStyle(textStyles);
}
//--------------------------------------------------------------------------------------------------
private void createCheckBoxSelectionModel() {
  final IdentityValueProvider<Pet> identityValueProvider;
  identityValueProvider = new IdentityValueProvider<Pet>();
  _selectionModel = new CheckBoxSelectionModel<Pet>(identityValueProvider) {
    @Override
    protected void onRefresh(final RefreshEvent event) {
      if (isSelectAllChecked()) {
        selectAll();
      }
      super.onRefresh(event);
    }
  };
}
//--------------------------------------------------------------------------------------------------
private ColumnConfig<Pet, BigDecimal> createColumnConfigBigDecimal(final ValueProvider<Pet, BigDecimal> valueProvider,
                                                                   final int width,
                                                                   final String title,
                                                                   final HorizontalAlignmentConstant horizontalAlignment,
                                                                   final boolean currency) {
  final ColumnConfig<Pet, BigDecimal> result;
  result = new ColumnConfig<Pet, BigDecimal>(valueProvider, width, title);
  result.setHorizontalAlignment(horizontalAlignment);
  NumberFormat numberFormat;
  if (currency) {
    numberFormat = NumberFormat.getSimpleCurrencyFormat();
  }
  else {
    numberFormat = NumberFormat.getDecimalFormat();
  }
  result.setCell(new NumberCell<BigDecimal>(numberFormat));
  return result;
}
//--------------------------------------------------------------------------------------------------
private ColumnConfig<Pet, Boolean> createColumnConfigBoolean(final ValueProvider<Pet, Boolean> valueProvider,
                                                             final int width, final String title) {
  final ColumnConfig<Pet, Boolean> result;
  result = new ColumnConfig<Pet, Boolean>(valueProvider, width, title);
  result.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
  result.setCell(new CheckBoxCell());
  result.setSortable(false);
  _checkBoxSet.add(result);
  return result;
}
//--------------------------------------------------------------------------------------------------
private ColumnConfig<Pet, Date> createColumnConfigDateTime(final ValueProvider<Pet, Date> valueProvider,
                                                           final int width, final String title,
                                                           final String dateTimeFormat) {
  final ColumnConfig<Pet, Date> result;
  result = new ColumnConfig<Pet, Date>(valueProvider, width, title);
  result.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
  final DateCell dateCell = new DateCell(DateTimeFormat.getFormat(dateTimeFormat));
  result.setCell(dateCell);
  return result;
}
//--------------------------------------------------------------------------------------------------
//private ColumnConfig<Pet, String> createColumnConfigForeignKey(final GLGridColumnDef gridColumnDef,
//                                                               final IGLColumn column) {
//  final ColumnConfig<Pet, String> result;
//  final ValueProvider<Pet, String> valueProvider = new GLForeignKeyValueProvider(column);
//  result = new ColumnConfig<Pet, String>(valueProvider, gridColumnDef.getWidth(), //
//                                         column.getTitle());
//  result.setHorizontalAlignment(gridColumnDef.getHorizontalAlignment());
//  result.setCell(new TextCell());
//  return result;
//}
//--------------------------------------------------------------------------------------------------
private ColumnConfig<Pet, Integer> createColumnConfigInteger(final ValueProvider<Pet, Integer> valueProvider,
                                                             final int width,
                                                             final String title,
                                                             final HorizontalAlignmentConstant horizontalAlignment) {
  final ColumnConfig<Pet, Integer> result;
  result = new ColumnConfig<Pet, Integer>(valueProvider, width, title);
  result.setHorizontalAlignment(horizontalAlignment);
  result.setCell(new NumberCell<Integer>());
  return result;
}
//--------------------------------------------------------------------------------------------------
private ColumnConfig<Pet, String> createColumnConfigString(final ValueProvider<Pet, String> valueProvider,
                                                           final int width,
                                                           final String title,
                                                           final HorizontalAlignmentConstant horizontalAlignment) {
  final ColumnConfig<Pet, String> result;
  result = new ColumnConfig<Pet, String>(valueProvider, width, title);
  result.setHorizontalAlignment(horizontalAlignment);
  result.setCell(new TextCell());
  return result;
}
//--------------------------------------------------------------------------------------------------
private ColumnModel<Pet> createColumnModel() {
  ColumnModel<Pet> result;
  final ArrayList<ColumnConfig<Pet, ?>> columnConfigList = new ArrayList<ColumnConfig<Pet, ?>>();
  if (_selectionModel instanceof CheckBoxSelectionModel) {
    columnConfigList.add(((CheckBoxSelectionModel<Pet>)_selectionModel).getColumn());
  }
  ColumnConfig<Pet, ?> columnConfig;
  columnConfig = createColumnConfigString(Pet.getPetNameValueProvider(), 100, "Name", //
                                          HasHorizontalAlignment.ALIGN_LEFT);
  columnConfigList.add(columnConfig);
  //  _gridColumnDefList.add(new GLGridColumnDef(Pet.PetTypeId, ELookupListKey.PetTypes));
  columnConfig = createColumnConfigString(Pet.getSexValueProvider(), 40, "Sex", //
                                          HasHorizontalAlignment.ALIGN_CENTER);
  columnConfigList.add(columnConfig);
  columnConfig = createColumnConfigDateTime(Pet.getIntakeDateValueProvider(), 100, "Intake Date", //
                                            "dd MMM yyyy hh:mm a");
  columnConfigList.add(columnConfig);
  columnConfig = createColumnConfigBoolean(Pet.getTrainedFlagValueProvider(), 80, "Trained?");
  columnConfigList.add(columnConfig);
  columnConfig = createColumnConfigBigDecimal(Pet.getAdoptionFeeValueProvider(), 100, //
                                              "Adoption Fee", HasHorizontalAlignment.ALIGN_RIGHT, //
                                              true);
  columnConfigList.add(columnConfig);
  columnConfig = createColumnConfigDateTime(Pet.getFosterDateValueProvider(), 100, "Foster Date", //
                                            "dd MMM yyyy");
  columnConfigList.add(columnConfig);
  _columnConfigMap = new TreeMap<String, ColumnConfig<Pet, ?>>();
  for (final ColumnConfig<Pet, ?> petColumnConfig : columnConfigList) {
    _columnConfigMap.put(petColumnConfig.getPath(), petColumnConfig);
  }
  result = new ColumnModel<Pet>(columnConfigList);
  result.addColumnWidthChangeHandler(new ColumnWidthChangeHandler() {
    @Override
    public void onColumnWidthChange(final ColumnWidthChangeEvent event) {
      final ColumnConfig<Pet, ?> petColumnConfig = columnConfigList.get(event.getIndex());
      if (_checkBoxSet.contains(petColumnConfig)) {
        centerCheckBox(petColumnConfig);
        _grid.getView().refresh(true);
      }
    }
  });
  for (final ColumnConfig<Pet, ?> checkBoxColumnConfig : _checkBoxSet) {
    centerCheckBox(checkBoxColumnConfig);
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
private void createContentPanel() {
  _contentPanel = new ContentPanel();
  _contentPanel.setHeaderVisible(false);
  _contentPanel.setButtonAlign(BoxLayoutPack.START);
  _contentPanel.addButton(new TextButton("Reset", new SelectHandler() {
    @Override
    public void onSelect(final SelectEvent event) {
      _petStore.rejectChanges();
    }
  }));
  _contentPanel.addButton(new TextButton("Save", new SelectHandler() {
    @Override
    public void onSelect(final SelectEvent event) {
      _petStore.commitChanges();
    }
  }));
  _contentPanel.addButton(createContentPanelDeleteButton());
  createContentPanelDeleteButton();
}
//--------------------------------------------------------------------------------------------------
private TextButton createContentPanelDeleteButton() {
  return new TextButton("Delete Selected", new SelectHandler() {
    @Override
    public void onSelect(final SelectEvent selectEvent) {
      final List<Pet> petList = _selectionModel.getSelectedItems();
      if (petList.size() == 0) {
        final AlertMessageBox messageBox;
        messageBox = new AlertMessageBox("Delete Rows", "You haven't selected any rows to delete");
        messageBox.show();
        return;
      }
      final String rowMessage;
      if (petList.size() == 1) {
        rowMessage = "this row";
      }
      else {
        rowMessage = "these " + petList.size() + " rows";
      }
      final ConfirmMessageBox messageBox;
      messageBox = new ConfirmMessageBox("Delete Rows", //
                                         "Are you sure you want to delete " + rowMessage + "?");
      messageBox.addDialogHideHandler(new DialogHideHandler() {
        @Override
        public void onDialogHide(final DialogHideEvent hideEvent) {
          if (hideEvent.getHideButton() == PredefinedButton.YES) {
            for (final Pet pet : petList) {
              _petStore.remove(pet);
            }
          }
        }
      });
      messageBox.show();
    }
  });
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private void createEditors() {
  final GridEditing<Pet> gridEditing = new GridInlineEditing<Pet>(_grid);
  gridEditing.addEditor((ColumnConfig<Pet, String>)(_columnConfigMap.get("Pet.PetName")),
                        new TextField());
  gridEditing.addEditor((ColumnConfig<Pet, String>)(_columnConfigMap.get("Pet.Sex")),
                        new TextField());
  DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
  DateTimePropertyEditor propertyEditor = new DateTimePropertyEditor(dateTimeFormat);
  DateField dateField = new DateField(propertyEditor);
  dateField.setClearValueOnParseError(false);
  gridEditing.addEditor((ColumnConfig<Pet, Date>)(_columnConfigMap.get("Pet.IntakeDate")),
                        dateField);
  gridEditing.addEditor((ColumnConfig<Pet, BigDecimal>)(_columnConfigMap.get("Pet.AdoptionFee")),
                        new BigDecimalField());
  dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yyyy");
  propertyEditor = new DateTimePropertyEditor(dateTimeFormat);
  dateField = new DateField(propertyEditor);
  dateField.setClearValueOnParseError(false);
  gridEditing.addEditor((ColumnConfig<Pet, Date>)(_columnConfigMap.get("Pet.FosterDate")),
                        dateField);
}
//--------------------------------------------------------------------------------------------------
//@SuppressWarnings("unchecked")
//private void createEditorsForeignKeyCombobox(final GridEditing<Pet> gridEditing,
//                                             final GLGridColumnDef gridColumnDef) {
//  final IGLColumn column = gridColumnDef.getColumn();
//  final IGLLookupListStoreKey lookupListStoreKey = gridColumnDef.getLookupListStoreKey();
//  final ListStore<Pet> lookupListStore = getLookupListStore(lookupListStoreKey);
//  if (lookupListStore == null) {
//    GLUtil.info(10, "Lookup list store not found for column:" + column + " key" +
//                    lookupListStoreKey);
//    return;
//  }
//  final LabelProvider<Pet> labelProvider = new LabelProvider<Pet>() {
//    @Override
//    public String getLabel(final Pet record) {
//      try {
//        return record.asString(column.getParentDisplayColumn());
//      }
//      catch (final GLInvalidFieldOrColumnException e) {
//        return "???";
//      }
//    }
//  };
//  final ComboBox<Pet> comboBox = new ComboBox<Pet>(lookupListStore, labelProvider);
//  final Converter<String, Pet> converter = new Converter<String, Pet>() {
//    @Override
//    public Pet convertModelValue(final String displayValue) {
//      return getRecordForLookupValue(lookupListStoreKey, displayValue);
//    }
//    @Override
//    public String convertFieldValue(final Pet record) {
//      return "Cat";
//    }
//  };
//  gridEditing.addEditor((ColumnConfig<Pet, String>)gridColumnDef.getColumnConfig(), converter,
//                        comboBox);
//}
//--------------------------------------------------------------------------------------------------
private void createGrid() {
  createCheckBoxSelectionModel();
  final ColumnModel<Pet> columnModel = createColumnModel();
  _grid = new Grid<Pet>(_petStore, columnModel);
  _grid.addRowClickHandler(new RowClickEvent.RowClickHandler() {
    @Override
    public void onRowClick(final RowClickEvent event) {
      final Collection<Store<Pet>.Record> records = _petStore.getModifiedRecords();
      if (records.size() > 0) {
        _grid.setBorders(false);
      }
    }
  });
  _grid.setBorders(true);
  _grid.setColumnReordering(true);
  _grid.setLoadMask(true);
  _grid.setSelectionModel(_selectionModel);
  _grid.setView(createGridView());
  addHeaderContextMenuHandler();
  createEditors();
}
//--------------------------------------------------------------------------------------------------
private GridView<Pet> createGridView() {
  final GridView<Pet> result = new GridView<Pet>();
  result.setColumnLines(true);
  result.setEmptyText("There are no results to display");
  result.setForceFit(false);
  result.setStripeRows(true);
  return result;
}
//--------------------------------------------------------------------------------------------------
public ListStore<Pet> getListStore() {
  return _petStore;
}
//--------------------------------------------------------------------------------------------------
private void resizeColumnToFit(final int columnIndex) {
  final ColumnConfig<Pet, ?> columnConfig = _grid.getColumnModel().getColumn(columnIndex);
  final TextMetrics textMetrics = TextMetrics.get();
  textMetrics.bind(_grid.getView().getHeader().getAppearance().styles().head());
  int maxWidth = textMetrics.getWidth(columnConfig.getHeader().asString()) + 15; // extra is for the dropdown arrow
  if (_petStore.size() > 0) {
    textMetrics.bind(_grid.getView().getCell(1, 1));
    for (final Pet pet : _petStore.getAll()) {
      final Object value = columnConfig.getValueProvider().getValue(pet);
      if (value != null) {
        String valueAsString;
        if (columnConfig.getCell() instanceof DateCell) {
          final DateCell dateCell = (DateCell)columnConfig.getCell();
          final SafeHtmlBuilder sb = new SafeHtmlBuilder();
          dateCell.render(null, (Date)value, sb);
          valueAsString = sb.toSafeHtml().asString();
        }
        else {
          valueAsString = value.toString();
        }
        final int width = textMetrics.getWidth(valueAsString) + 12;
        maxWidth = width > maxWidth ? width : maxWidth;
      }
    }
    for (final Store<Pet>.Record record : _petStore.getModifiedRecords()) {
      final int width = textMetrics.getWidth(record.getValue(columnConfig.getValueProvider()) //
                                                   .toString()) + 12;
      maxWidth = width > maxWidth ? width : maxWidth;
    }
  }
  columnConfig.setWidth(maxWidth);
  if (_checkBoxSet.contains(columnConfig)) {
    centerCheckBox(columnConfig);
  }
}
//--------------------------------------------------------------------------------------------------
}