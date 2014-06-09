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
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.NumberCell;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.TextMetrics;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.LabelProvider;
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
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.IsField;
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

public abstract class PetGridWidget implements IsWidget {
//--------------------------------------------------------------------------------------------------
private final HashSet<ColumnConfig<Pet, ?>> _checkBoxSet;
private ContentPanel                        _contentPanel;
private Grid<Pet>                           _grid;
private final String                        _noRowsMessage;
protected ListStore<Pet>                    _petStore;
private GridSelectionModel<Pet>             _selectionModel;
//--------------------------------------------------------------------------------------------------
protected PetGridWidget(final String headingText, final String noRowsMessage) {
  super();
  _noRowsMessage = noRowsMessage == null ? "There are no results to display" : noRowsMessage;
  _petStore = new ListStore<Pet>(new ModelKeyProvider<Pet>() {
    @Override
    public String getKey(final Pet pet) {
      return Integer.toString(pet.getPetId());
    }
  });
  _checkBoxSet = new HashSet<ColumnConfig<Pet, ?>>();
  loadGridColumnDefList();
  createGridColumnDefMap();
  createContentPanel(headingText);
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
private ColumnConfig<Pet, String> createColumnConfigForeignKey(final GLGridColumnDef gridColumnDef,
                                                               final IGLColumn column) {
  final ColumnConfig<Pet, String> result;
  final ValueProvider<Pet, String> valueProvider = new GLForeignKeyValueProvider(column);
  result = new ColumnConfig<Pet, String>(valueProvider, gridColumnDef.getWidth(), //
                                         column.getTitle());
  result.setHorizontalAlignment(gridColumnDef.getHorizontalAlignment());
  result.setCell(new TextCell());
  return result;
}
//--------------------------------------------------------------------------------------------------
private ColumnConfig<Pet, Integer> createColumnConfigInteger(final GLGridColumnDef gridColumnDef,
                                                             final IGLColumn column) {
  final ColumnConfig<Pet, Integer> result;
  final ValueProvider<Pet, Integer> valueProvider = new GLIntegerValueProvider(column);
  result = new ColumnConfig<Pet, Integer>(valueProvider, gridColumnDef.getWidth(), //
                                          column.getTitle());
  result.setHorizontalAlignment(gridColumnDef.getHorizontalAlignment());
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
  final ArrayList<ColumnConfig<Pet, ?>> columnConfigList;
  columnConfigList = new ArrayList<ColumnConfig<Pet, ?>>();
  if (_selectionModel instanceof CheckBoxSelectionModel) {
    columnConfigList.add(((CheckBoxSelectionModel<Pet>)_selectionModel).getColumn());
  }
  //  _gridColumnDefList.add(new GLGridColumnDef(Pet.PetName));
  //  _gridColumnDefList.add(new GLGridColumnDef(Pet.PetTypeId, ELookupListKey.PetTypes));
  //  _gridColumnDefList.add(new GLGridColumnDef(Pet.Sex));
  //  _gridColumnDefList.add(new GLGridColumnDef(Pet.IntakeDate));
  //  _gridColumnDefList.add(new GLGridColumnDef(Pet.TrainedFlag));
  //  _gridColumnDefList.add(new GLGridColumnDef(Pet.AdoptionFee));
  //  _gridColumnDefList.add(new GLGridColumnDef(Pet.FosterDate));

  columnConfigList.add(createColumnConfigString(Pet.getPetNameValueProvider(), 100, "Name",
                                                HasHorizontalAlignment.ALIGN_LEFT));
  columnConfigList.add(createColumnConfigString(Pet.getSexValueProvider(), 20, "Sex",
                                                HasHorizontalAlignment.ALIGN_CENTER));
  columnConfigList.add(createColumnConfigDateTime(Pet.getIntakeDateValueProvider(), 100,
                                                  "Intake Date", "dd MMM yyyy hh:mm a"));
  columnConfigList.add(createColumnConfigBoolean(Pet.getTrainedFlagValueProvider(), 20, "Trained?"));
  columnConfigList.add(createColumnConfigBigDecimal(Pet.getAdoptionFeeValueProvider(), 80,
                                                    "Adoption Fee",
                                                    HasHorizontalAlignment.ALIGN_RIGHT, true));

  for (final GLGridColumnDef gridColumnDef : _gridColumnDefList) {
    ColumnConfig<Pet, ?> columnConfig = null;
    final IGLColumn column = gridColumnDef.getColumn();
    switch (column.getDataType()) {
      case Boolean:
        columnConfig = createColumnConfigBoolean(gridColumnDef, column);
        break;
      case Currency:
        columnConfig = createColumnConfigBigDecimal(gridColumnDef, column);
        break;
      case Date:
        columnConfig = createColumnConfigDateTime(gridColumnDef, column, "dd MMM yyyy");
        break;
      case DateTime:
        columnConfig = createColumnConfigDateTime(gridColumnDef, column, "dd MMM yyyy hh:mm a");
        break;
      case Decimal:
        columnConfig = createColumnConfigBigDecimal(gridColumnDef, column);
        break;
      case Int:
        if (column.getParentTable() == null) {
          columnConfig = createColumnConfigInteger(gridColumnDef, column);
        }
        else {
          columnConfig = createColumnConfigForeignKey(gridColumnDef, column);
        }
        break;
      case String:
        columnConfig = createColumnConfigString(gridColumnDef, column);
        break;
    }
    if (columnConfig != null) {
      gridColumnDef.setColumnConfig(columnConfig, columnConfigList.size());
      columnConfigList.add(columnConfig);
    }
  }
  result = new ColumnModel<Pet>(columnConfigList);
  result.addColumnWidthChangeHandler(new ColumnWidthChangeHandler() {
    @Override
    public void onColumnWidthChange(final ColumnWidthChangeEvent event) {
      final ColumnConfig<Pet, ?> columnConfig = columnConfigList.get(event.getIndex());
      if (_checkBoxSet.contains(columnConfig)) {
        centerCheckBox(columnConfig);
        _grid.getView().refresh(true);
      }
    }
  });
  for (final ColumnConfig<Pet, ?> columnConfig : _checkBoxSet) {
    centerCheckBox(columnConfig);
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
private void createContentPanel(final String headingText) {
  _contentPanel = new ContentPanel();
  if (GLUtil.isBlank(headingText)) {
    _contentPanel.setHeaderVisible(false);
  }
  else {
    _contentPanel.setHeaderVisible(true);
    _contentPanel.setHeadingText(headingText);
  }
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
      final List<Pet> selectedRecordList = _selectionModel.getSelectedItems();
      if (selectedRecordList.size() == 0) {
        final AlertMessageBox messageBox;
        messageBox = new AlertMessageBox("Delete Rows", "You haven't selected any rows to delete");
        messageBox.show();
        return;
      }
      final String rowMessage;
      if (selectedRecordList.size() == 1) {
        rowMessage = "this row";
      }
      else {
        rowMessage = "these " + selectedRecordList.size() + " rows";
      }
      final ConfirmMessageBox messageBox;
      messageBox = new ConfirmMessageBox("Delete Rows", //
                                         "Are you sure you want to delete " + rowMessage + "?");
      messageBox.addDialogHideHandler(new DialogHideHandler() {
        @Override
        public void onDialogHide(final DialogHideEvent hideEvent) {
          if (hideEvent.getHideButton() == PredefinedButton.YES) {
            final ArrayList<Pet> recordList;
            recordList = new ArrayList<Pet>(selectedRecordList.size());
            for (final Pet record : selectedRecordList) {
              recordList.add(record);
            }
            _petStore.remove(recordList);
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
  for (final GLGridColumnDef gridColumnDef : _gridColumnDefList) {
    final ColumnConfig<Pet, ?> columnConfig = gridColumnDef.getColumnConfig();
    final IGLColumn column = gridColumnDef.getColumn();
    switch (column.getDataType()) {
      case Boolean:
        // no editor is needed - the checkbox can be changed in place
        break;
      case Currency:
        gridEditing.addEditor((ColumnConfig<Pet, BigDecimal>)columnConfig, new BigDecimalField());
        break;
      case Date: {
        final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yyyy");
        final DateTimePropertyEditor propertyEditor = new DateTimePropertyEditor(dateTimeFormat);
        final DateField dateField = new DateField(propertyEditor);
        dateField.setClearValueOnParseError(false);
        gridEditing.addEditor((ColumnConfig<Pet, Date>)columnConfig, dateField);
        final IsField<Date> editor = gridEditing.getEditor(columnConfig);
        if (editor == null) {
          GLUtil.initialize();
        }
        break;
      }
      case DateTime: {
        /*
         * In 3, I'd probably start by making an Editor instance with two sub-editors, one DateField
         * and one TimeField, each using @Path("") to have them bind to the same value.
         * 
         * Or make the new class implement IsField, and use setValue() and getValue() to modify/read
         * both sub-editors.
         * 
         * IsField is what is being used in 3 to replace most MultiField cases - it allows a widget
         * to supply methods that are helpful for most fields, and as it extends LeafValueEditor, it
         * can be used in GWT Editor framework, and subfields will be ignored, leaving the dev to
         * write their own logic for binding the values.
         */
        final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);
        final DateTimePropertyEditor propertyEditor = new DateTimePropertyEditor(dateTimeFormat);
        final DateField dateField = new DateField(propertyEditor);
        dateField.setClearValueOnParseError(false);
        gridEditing.addEditor((ColumnConfig<Pet, Date>)columnConfig, dateField);
        break;
      }
      case Decimal:
        gridEditing.addEditor((ColumnConfig<Pet, BigDecimal>)columnConfig, new BigDecimalField());
        break;
      case Int:
        if (column.getParentTable() == null) {
          gridEditing.addEditor((ColumnConfig<Pet, Integer>)columnConfig, new IntegerField());
        }
        else {
          createEditorsForeignKeyCombobox(gridEditing, gridColumnDef);
        }
        break;
      case String:
        gridEditing.addEditor((ColumnConfig<Pet, String>)columnConfig, new TextField());
        break;
    }
  }
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private void createEditorsForeignKeyCombobox(final GridEditing<Pet> gridEditing,
                                             final GLGridColumnDef gridColumnDef) {
  final IGLColumn column = gridColumnDef.getColumn();
  final IGLLookupListStoreKey lookupListStoreKey = gridColumnDef.getLookupListStoreKey();
  final ListStore<Pet> lookupListStore = getLookupListStore(lookupListStoreKey);
  if (lookupListStore == null) {
    GLUtil.info(10, "Lookup list store not found for column:" + column + " key" +
                    lookupListStoreKey);
    return;
  }
  final LabelProvider<Pet> labelProvider = new LabelProvider<Pet>() {
    @Override
    public String getLabel(final Pet record) {
      try {
        return record.asString(column.getParentDisplayColumn());
      }
      catch (final GLInvalidFieldOrColumnException e) {
        return "???";
      }
    }
  };
  final ComboBox<Pet> comboBox = new ComboBox<Pet>(lookupListStore, labelProvider);
  final Converter<String, Pet> converter = new Converter<String, Pet>() {
    @Override
    public Pet convertModelValue(final String displayValue) {
      return getRecordForLookupValue(lookupListStoreKey, displayValue);
    }
    @Override
    public String convertFieldValue(final Pet record) {
      return "Cat";
    }
  };
  gridEditing.addEditor((ColumnConfig<Pet, String>)gridColumnDef.getColumnConfig(), converter,
                        comboBox);
}
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
private void createGridColumnDefMap() {
  _gridColumnDefMap = new TreeMap<String, GLGridColumnDef>();
  for (final GLGridColumnDef gridColumnDef : _gridColumnDefList) {
    _gridColumnDefMap.put(gridColumnDef.getColumn().toString(), gridColumnDef);
  }
}
//--------------------------------------------------------------------------------------------------
private GridView<Pet> createGridView() {
  final GridView<Pet> result = new GridView<Pet>();
  result.setColumnLines(true);
  result.setEmptyText(_noRowsMessage);
  result.setForceFit(false);
  result.setStripeRows(true);
  return result;
}
//--------------------------------------------------------------------------------------------------
public ListStore<Pet> getListStore() {
  return _petStore;
}
//--------------------------------------------------------------------------------------------------
public ListStore<Pet> getLookupListStore(final IGLLookupListStoreKey lookupListStoreKey) {
  return null;
}
//--------------------------------------------------------------------------------------------------
public Pet getRecordForLookupValue(final IGLLookupListStoreKey lookupListStoreKey,
                                   final String value) {
  return null;
}
//--------------------------------------------------------------------------------------------------
protected abstract void loadGridColumnDefList();
//--------------------------------------------------------------------------------------------------
private void resizeColumnToFit(final int columnIndex) {
  final ColumnConfig<Pet, ?> columnConfig = _grid.getColumnModel().getColumn(columnIndex);
  final GLGridColumnDef gridColumnDef = _gridColumnDefMap.get(columnConfig.getPath());
  final TextMetrics textMetrics = TextMetrics.get();
  textMetrics.bind(_grid.getView().getHeader().getAppearance().styles().head());
  int maxWidth = textMetrics.getWidth(columnConfig.getHeader().asString()) + 15; // extra is for the dropdown arrow
  textMetrics.bind(_grid.getView().getCell(0, 1));
  final IGLColumn column = gridColumnDef.getColumn();
  try {
    for (final Pet record : _petStore.getAll()) {
      int width;
      width = textMetrics.getWidth(record.asString(column));
      maxWidth = width > maxWidth ? width : maxWidth;
    }
  }
  catch (final GLInvalidFieldOrColumnException ifoce) {
    //
  }
  for (final Store<Pet>.Record record : _petStore.getModifiedRecords()) {
    final int width = textMetrics.getWidth(record.getValue(columnConfig.getValueProvider()) //
                                                 .toString());
    maxWidth = width > maxWidth ? width : maxWidth;
  }
  columnConfig.setWidth(maxWidth);
  if (_checkBoxSet.contains(columnConfig)) {
    centerCheckBox(columnConfig);
  }
}
//--------------------------------------------------------------------------------------------------
}