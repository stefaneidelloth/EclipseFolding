/*
 * Created on Sep 8, 2004
 */
package com.cb.eclipse.folding.java.preferences;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.java.user.UserDefinedEntry;
import com.cb.eclipse.folding.java.user.UserDefinedSettings;
import com.cb.eclipse.folding.java.user.UserDefinedSettingsEvent;
import com.cb.eclipse.folding.java.user.UserDefinedSettingsListener;
import com.cb.eclipse.folding.preferences.PreferencesTab;
import com.cb.eclipse.folding.util.GridDataFactory;
import com.cb.eclipse.folding.util.SWTUtil;
import com.cb.eclipse.folding.util.StringUtil;

/**
 * The UserDefinedTab handles all of the preferences for user-defined folding
 * regions.
 * 
 * @author R.J. Lorimer
 */
public class UserDefinedTab implements PreferencesTab {

	private static final String[] COLUMNS = {"FOLD", "COLLAPSE", "NAME" };
	
	// {{ header stuff
	private PreferencesMediator mediator;
	private TableViewer viewer;
		
	public UserDefinedTab(PreferencesMediator med) {
		mediator = med;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cb.eclipse.folding.preferences.PreferencesTab#getTabControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control getTabControl(Composite parent) {

		Composite inner = new Composite(parent, SWT.NONE);
		inner.setLayoutData(new GridData(GridData.FILL_BOTH));
		inner.setLayout(new GridLayout(1, false));
		createGeneralSettings(inner);
		Group group = new Group(inner, SWT.NONE);

		group.setLayoutData(new GridData(GridData.FILL_BOTH));

		group.setText(FoldingPlugin.getMessage("userdefined.typesettings.title"));

		GridLayout layout = new GridLayout(1, true);
		layout.verticalSpacing = 5;
		layout.marginWidth = 5;
		group.setLayout(layout);
		
		
		createTypeAdder(group);
		createTypeSettings(group);
		
		

		return inner;

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cb.eclipse.folding.preferences.PreferencesTab#getTabName()
	 */
	public String getTabName() {
		return FoldingPlugin.getMessage("userdefined.tab.title");
	}
	
	// }}

	

	private void createGeneralSettings(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayout(new GridLayout(4, false));
		group.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));

		group.setText(FoldingPlugin.getMessage("userdefined.general.title"));

		Label startTagSummary = new Label(group, SWT.NONE);

		startTagSummary.setText(FoldingPlugin.getMessage("user.defined.start"));

		Text startUserDefine = new Text(group, SWT.BORDER | SWT.SINGLE);
		startUserDefine.setLayoutData(GridDataFactory.getMinimumWidthData());
		mediator.setStartUserDefineField(startUserDefine);

		Label endTagSummary = new Label(group, SWT.NONE);
		endTagSummary.setText(FoldingPlugin.getMessage("user.defined.end"));

		Text endUserDefine = new Text(group, SWT.BORDER | SWT.SINGLE);
		endUserDefine.setLayoutData(GridDataFactory.getMinimumWidthData());
		mediator.setEndUserDefineField(endUserDefine);

	}

	private void createTypeAdder(Composite parent) {
		
		Shell shell = parent.getShell();
		Composite group = new Composite(parent, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(4, false);
		layout.horizontalSpacing=3;
		layout.marginWidth=0;
		layout.marginHeight=0;
		group.setLayout(layout);
		
		Label label = new Label(group, SWT.NONE);
		label.setText("Name");
		Text name = new Text(group, SWT.BORDER | SWT.SINGLE);
		
		Button collapse = new Button(group, SWT.CHECK);
		collapse.setText("Initially Collapse");
		
		Button button = new Button(group, SWT.NONE);
		button.setText("Add");
		button.addListener(SWT.Selection, new AddEntryHandler(name, collapse, shell));
		
		
	}

	private void createTypeSettings(Composite group) {
		
		final Table aTable = createTable(group);
		
		Button delete = new Button(group, SWT.NONE);
		delete.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		delete.setText(FoldingPlugin.getMessage("userdefined.delete"));
		delete.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				UserDefinedEntry removed = (UserDefinedEntry)((IStructuredSelection)viewer.getSelection()).getFirstElement();
				FoldingPlugin.getJavaDomain().getUserDefinedSettings().deleteType(removed.getName());
			}

		});
		
	}

	private Table createTable(Composite parent) {

		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
		SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		Table table = new Table(parent, style);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		
		
		TableColumn column = new TableColumn(table, SWT.CENTER, 0);
		column.setText(FoldingPlugin.getMessage("userdefined.fold.title"));
		
		TableColumn column2 = new TableColumn(table, SWT.CENTER, 1);
		column2.setText(FoldingPlugin.getMessage("userdefined.collapse.title"));
		
		TableColumn column3 = new TableColumn(table, SWT.CENTER, 2);
		column3.setText(FoldingPlugin.getMessage("userdefined.region.name"));
		
		column.pack();
		column2.pack();
		column3.pack();
		
		viewer = new TableViewer(table);
		
		CellEditor[] editors = new CellEditor[3];
		editors[0] = new CheckboxCellEditor(table);
		editors[1] = new CheckboxCellEditor(table);
		editors[2] = new TextCellEditor(table);
								
		viewer.setColumnProperties(COLUMNS);
		viewer.setUseHashlookup(true);
		viewer.setCellEditors(editors);
		viewer.setCellModifier(new UserDefinedEntryModifier());
		viewer.setSorter(new UserDefinedEntrySorter(2));
		
		viewer.setContentProvider(new UserDefinedEntryContentProvider());
		viewer.setLabelProvider(new UserDefinedEntryLabelProvider());
		
		viewer.setInput(FoldingPlugin.getJavaDomain().getUserDefinedSettings());
		
		
		//table.setSize(table.computeSize(SWT.DEFAULT, 300));


		return table;
	}
	

	private class AddEntryHandler implements Listener {
		private Shell propertiesDialog;
		private Text nameInput;
		private Button collapseInput;
		private UserDefinedSettings settings;
		
		AddEntryHandler(Text name, Button collapse, Shell parent) {
			nameInput = name;
			collapseInput = collapse;
			propertiesDialog = parent;
			settings = FoldingPlugin.getJavaDomain().getUserDefinedSettings();
		}
		public void handleEvent(Event event) {
			
			String userInput = nameInput.getText();
			
			String errorMessage = validate(userInput);
			if(errorMessage != null) {
				
				MessageDialog.openError(propertiesDialog, "Invalid Name", errorMessage);
			}
			else {
				boolean collapseIt = collapseInput.getSelection();
				settings.addType(userInput, true, collapseIt);
			}
		}
		
		private String validate(String userInput) {
			// TODO apply constants.
			String msg;
			if(userInput == null || userInput.length() == 0 || !characterCheck(userInput)) {
				msg = "The name must not be blank and must only contain alpha-numeric characters.";
			}
			else if(settings.containsEntry(userInput)) {
				msg = "The name " + userInput + " is already a defined folding region.";
			}
			else {
				msg = null;
			}
			return msg;
		}
		
		private boolean characterCheck(String userInput) {
			return StringUtil.isAlphaNumeric(userInput);
		}
	}

	private class UserDefinedEntryContentProvider implements IStructuredContentProvider, UserDefinedSettingsListener {
		
		UserDefinedEntryContentProvider() {
			
		}
		
		
		public Object[] getElements(Object inputElement) {
			UserDefinedSettings settings = FoldingPlugin.getJavaDomain().getUserDefinedSettings();
			
			return settings.getEntries().toArray();
			
		}
		
		public void dispose() {

		}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if(oldInput != null) 
				((UserDefinedSettings)oldInput).removeListener(this);
			if(newInput != null)
				((UserDefinedSettings)newInput).addListener(this);
		}
		
		
		public void entryAdded(UserDefinedSettingsEvent evt) {
			viewer.add(evt.getEntry());
		}
		
		
		public void entryDeleted(UserDefinedSettingsEvent evt) {
			viewer.remove(evt.getEntry());
		}
		
		public void entryUpdated(UserDefinedSettingsEvent evt) {
			
			viewer.update(evt.getEntry(), null);
		}
	}

	private class UserDefinedEntryLabelProvider extends LabelProvider implements ITableLabelProvider {
	
		public String getColumnText(Object element, int columnIndex) {
			switch(columnIndex) {
				case 2:
					return ((UserDefinedEntry)element).getName();
				default:
					return null;
			}
		}
		
		public Image getColumnImage(Object element, int columnIndex) {
			UserDefinedEntry entry = (UserDefinedEntry)element;
			switch(columnIndex) {
				case 0:
					return FoldingPlugin.getImages().getCheckBoxImage(entry.isFold());
				case 1:
					return FoldingPlugin.getImages().getCheckBoxImage(entry.isCollapse());
				default:
					return null;
			}
		}
		
	}
	
	private class UserDefinedEntryModifier implements ICellModifier {
		
		public boolean canModify(Object element, String property) {
			
			return !(COLUMNS[2].equals(property));
		}
		
		public Object getValue(Object element, String property) {
			
			UserDefinedEntry entry = (UserDefinedEntry)element;
			if(COLUMNS[0].equals(property)) {
				return Boolean.valueOf(entry.isFold());				
			}
			else if (COLUMNS[1].equals(property)){
				return Boolean.valueOf(entry.isCollapse());
			}
			else {
				return entry.getName();
			}
		}
		
		public void modify(Object element, String property, Object value) {
			
			TableItem item = (TableItem)element;
			UserDefinedEntry entry = (UserDefinedEntry)item.getData();
			if(COLUMNS[0].equals(property)) {
				entry.setFold(((Boolean)value).booleanValue());
				item.setChecked(entry.isFold());
			}
			else {
				entry.setCollapse(((Boolean)value).booleanValue());
				item.setChecked(entry.isCollapse());
			}
			FoldingPlugin.getJavaDomain().getUserDefinedSettings().entryChanged(entry);
		}
	}
	
	private class UserDefinedEntrySorter extends ViewerSorter {
		
		private int criteria;
		public UserDefinedEntrySorter(int aCriteria) {
			criteria = aCriteria;
		}
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public int compare(Viewer viewer, Object e1, Object e2) {
			UserDefinedEntry a = (UserDefinedEntry)e1;
			UserDefinedEntry b = (UserDefinedEntry)e2;
			switch(criteria) {
				case 0:
					return boolCompare(a.isFold(), b.isFold());
				case 1:
					return boolCompare(a.isCollapse(), b.isCollapse());
				default:
					return a.getName().compareTo(b.getName());
			}
		}
		
		private int boolCompare(boolean a, boolean b) {
			int val;
			if(a == b) val = 0;
			else if(a && !b) val = 1;
			else val = -1;
			
			return val;
		}
	}
}