/*
 * Created on Aug 20, 2004
 */
package com.cb.eclipse.folding.java.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.preferences.PreferencesTab;

/**
 * TODO Javadoc.
 * @author R.J. Lorimer
 */
public class GeneralTab implements PreferencesTab {

	// {{ Constructor Junk
	private PreferencesMediator mediator;
	public GeneralTab(PreferencesMediator mediator) {
		this.mediator = mediator;
	}
	
	public String getTabName() {
		return FoldingPlugin.getMessage("general.tab.title");
	}
	// }} End Constructor Junk
	
	public Control getTabControl(Composite parent) {
		Composite inner = new Composite(parent, SWT.NONE);
		inner.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(1, true);
		layout.verticalSpacing = 5;
		layout.marginWidth = 5;
		inner.setLayout(layout);
		
		Label msg = new Label(inner, SWT.NONE);
		msg.setText(FoldingPlugin.getMessage("general.table.title"));

				
		Table table = createTable(inner);
		
		return inner;
	}
	
	private Table createTable(Composite parent) {
		Table table = new Table(parent, SWT.MULTI | SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText(FoldingPlugin.getMessage("fold.title"));

		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText(FoldingPlugin.getMessage("collapse.title"));

	
		
		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setText(FoldingPlugin.getMessage("region.description"));

		createTableItems(table, "topLevelType");
		createTableItems(table, "methods");
		createTableItems(table, "constructors");
		createTableItems(table, "getterSetter");
		createTableItems(table, "main_methods");
		createTableItems(table, "innerType");
		createTableItems(table, "static_initializers");
		createTableItems(table, "imports");

		createTableItems(table, "header");
		createTableItems(table, "comment_blocks");
		createTableItems(table, "javadocs");
		createTableItems(table, "line.comments");
		createTableItems(table, "userDefined");
		
		

		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumn(i).pack();
		}
		table.setSize(table.computeSize(SWT.DEFAULT, 300));

		return table;

	}
	
	private void createTableItems(Table table, String key) {

		TableItem item = new TableItem(table, SWT.NONE);
		String labelText = FoldingPlugin.getMessage(key);

		TableEditor foldEditor = new TableEditor(table);
		final Button foldCheckBox = new Button(table, SWT.CHECK);
		foldCheckBox.pack();
		foldEditor.minimumWidth = foldCheckBox.getSize().x;
		foldEditor.horizontalAlignment = SWT.CENTER;
		foldEditor.setEditor(foldCheckBox, item, 0);

		TableEditor collapseEditor = new TableEditor(table);
		final Button initialCheckBox = new Button(table, SWT.CHECK);
		initialCheckBox.pack();
		collapseEditor.minimumWidth = initialCheckBox.getSize().x;
		collapseEditor.horizontalAlignment = SWT.CENTER;
		collapseEditor.setEditor(initialCheckBox, item, 1);

		item.setText(2, labelText);

		foldCheckBox.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				initialCheckBox.setEnabled(foldCheckBox.getSelection());
			}

		});
		
		mediator.addFoldCheckBox(key, foldCheckBox);
		mediator.addInitialCollapseCheckBox(key, initialCheckBox);

	}
	
}
