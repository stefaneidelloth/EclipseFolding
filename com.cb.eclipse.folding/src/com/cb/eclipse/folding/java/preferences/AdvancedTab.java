/*
 * Created on Aug 20, 2004
 */
package com.cb.eclipse.folding.java.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.cb.eclipse.folding.FoldingPlugin;
import com.cb.eclipse.folding.preferences.PreferencesTab;
import com.cb.eclipse.folding.util.GridDataFactory;

/**
 * TODO Javadoc.
 * @author R.J. Lorimer
 */
public class AdvancedTab implements PreferencesTab {

		
	private PreferencesMediator mediator;
	public AdvancedTab(PreferencesMediator mediator) {
		this.mediator = mediator;
	}
	
	
	public String getTabName() {
		return FoldingPlugin.getMessage("advanced.tab.title");
	}
	
	
	
	/**
	 * @return
	 */
	private GridLayout getTwoColumnLayout(int margin) {
		GridLayout layout = new GridLayout(2, true);
		layout.verticalSpacing = 5;
		layout.marginWidth = margin;
		
		layout.makeColumnsEqualWidth=false;
		return layout;
	}
	
	
	public Control getTabControl(Composite parent) {
		
		
		Composite advancedPanel = new Composite(parent, SWT.NONE);
		advancedPanel.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		GridLayout layout = new GridLayout(1, true);
		
		advancedPanel.setLayout(layout);
		
			
		createTweakingPanel(advancedPanel);
		
			
		
		
		createFoldIconPanel(advancedPanel);
		
		return advancedPanel;
		
	}


	
	/**
	 * @param advancedPanel
	 */
	private void createTweakingPanel(Composite advancedPanel) {
		
		Group group = new Group(advancedPanel, SWT.NONE);
		group.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL));
		group.setLayout(getTwoColumnLayout(5));
		group.setText(FoldingPlugin.getMessage("line.control.title"));
		
		Label minLinesLabel = new Label(group, SWT.NONE);
		minLinesLabel.setText(FoldingPlugin.getMessage("minSize.title"));
			
		Text minimumLines = new Text(group, SWT.BORDER | SWT.SINGLE);
		minimumLines.setLayoutData(GridDataFactory.getMinimumWidthData());
		mediator.setMinimumLinesField(minimumLines);		
		
		Label label = new Label(group, SWT.NONE);
		label.setText(FoldingPlugin.getMessage("last.line.title"));
		label.setLayoutData(GridDataFactory.getTwoColumnData(0));
			
		Button lastLinesMethods = new Button(group, SWT.CHECK);
		lastLinesMethods.setLayoutData(GridDataFactory.getTwoColumnData(5));
				
		lastLinesMethods.setText(FoldingPlugin.getMessage("last.line.methods"));
		mediator.setMethodLastLine(lastLinesMethods);
		
		Button lastLinesControls = new Button(group, SWT.CHECK);
		lastLinesControls.setLayoutData(GridDataFactory.getTwoColumnData(5));
		lastLinesControls.setText(FoldingPlugin.getMessage("last.line.controls"));
		mediator.setControlLastLine(lastLinesControls);
		
		Button lastLinesTypes = new Button(group, SWT.CHECK);
		lastLinesTypes.setLayoutData(GridDataFactory.getTwoColumnData(5));
		lastLinesTypes.setText(FoldingPlugin.getMessage("last.line.types"));
		mediator.setTypeLastLine(lastLinesTypes);
		
		Button lastLinesComments = new Button(group, SWT.CHECK);
		mediator.setCommentLastLine(lastLinesComments);
		lastLinesComments.setLayoutData(GridDataFactory.getTwoColumnData(5));
		lastLinesComments.setText(FoldingPlugin.getMessage("last.line.comments"));
		mediator.setCommentLastLine(lastLinesComments);
		
		
	}
	
	private void createFoldIconPanel(Composite advancedPanel) {
		
		
		Group group = new Group(advancedPanel, SWT.NONE);
		group.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL));
		group.setText(FoldingPlugin.getMessage("folding.icons.title"));
		group.setLayout(new GridLayout(1, false));
				
		Combo combo = new Combo (group, SWT.READ_ONLY);
		combo.setItems (FoldingPlugin.getImages().getAvailableFoldingKeys());
		mediator.setImageSelector(combo);
		
		
		
	}
}
