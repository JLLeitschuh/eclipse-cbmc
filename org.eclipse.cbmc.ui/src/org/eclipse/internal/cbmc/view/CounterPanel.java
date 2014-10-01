package org.eclipse.internal.cbmc.view;

import org.eclipse.cbmc.CbmcPackage;
import org.eclipse.cbmc.Results;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.osgi.framework.FrameworkUtil;

/**
 * A panel with counters for the number of Runs, Errors and Failures.
 */
public class CounterPanel extends Composite {

	protected Text fNumberOfSuccess;
	protected Text fNumberOfFailures;
	protected Text fNumberOfErrors;
	protected Text fNumberOfRuns;
	protected int runsCount;
	private int totalCount;
	private final Image fSuccessIcon = ImageDescriptor.createFromURL(FileLocator.find(FrameworkUtil.getBundle(this.getClass()), new Path("icons/success_ovr.gif"), null)).createImage(); //$NON-NLS-1$
	private final Image fFailureIcon = ImageDescriptor.createFromURL(FileLocator.find(FrameworkUtil.getBundle(this.getClass()), new Path("icons/failure_ovr.gif"), null)).createImage(); //$NON-NLS-1$
	private final Image fErrorIcon = ImageDescriptor.createFromURL(FileLocator.find(FrameworkUtil.getBundle(this.getClass()), new Path("icons/error_ovr.gif"), null)).createImage(); //$NON-NLS-1$
	private DataBindingContext bindingContext;

	public CounterPanel(Composite parent) {
		super(parent, SWT.WRAP);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);

		Composite leftComposite = new Composite(this, SWT.NONE);
		GridLayout leftLayout = new GridLayout();
		leftLayout.numColumns = 3;
		leftLayout.makeColumnsEqualWidth = false;
		leftLayout.marginWidth = 0;
		leftComposite.setLayout(leftLayout);
		leftComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_FILL));
		runsCount = 0;
		totalCount = 0;
		fNumberOfRuns = createLabel(leftComposite, Messages.CounterPanel_label_runs, null, " 0/0  "); //$NON-NLS-1$

		Composite rightComposite = new Composite(this, SWT.NONE);
		GridLayout rightLayout = new GridLayout();
		rightLayout.numColumns = 3;
		rightLayout.makeColumnsEqualWidth = false;
		rightLayout.marginWidth = 0;
		rightComposite.setLayout(rightLayout);
		rightComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.HORIZONTAL_ALIGN_FILL));
		rightComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));

		fNumberOfSuccess = createLabel(rightComposite, Messages.CounterPanel_label_success, fSuccessIcon, " 0 "); //$NON-NLS-1$
		fNumberOfFailures = createLabel(rightComposite, Messages.CounterPanel_label_failures, fFailureIcon, " 0 "); //$NON-NLS-1$
		fNumberOfErrors = createLabel(rightComposite, Messages.CounterPanel_label_errors, fErrorIcon, " 0 "); //$NON-NLS-1$
	}

	private void disposeIcons() {
		if (fSuccessIcon != null)
			fSuccessIcon.dispose();
		if (fFailureIcon != null)
			fFailureIcon.dispose();
		if (fFailureIcon != null)
			fFailureIcon.dispose();
		if (fErrorIcon != null)
			fErrorIcon.dispose();
	}

	private Text createLabel(Composite composite, String name, Image image, String init) {
		Label label = new Label(composite, SWT.NONE);
		if (image != null) {
			image.setBackground(label.getBackground());
			label.setImage(image);
		}
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		label = new Label(composite, SWT.NONE);
		label.setText(name);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		Text value = new Text(composite, SWT.READ_ONLY);
		value.setText(init);
		value.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		value.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
		return value;
	}

	public void setRunValue() {
		String runString = Messages.format(Messages.CounterPanel_runcount, new String[] {Integer.toString(runsCount), Integer.toString(totalCount)});
		fNumberOfRuns.setText(runString);
	}

	public void bind(final Results results) {
		if (bindingContext != null)
			bindingContext.dispose();

		UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setConverter(new IConverter() {

			@Override
			public Object getToType() {
				return String.class;
			}

			@Override
			public Object getFromType() {
				return Integer.class;
			}

			@Override
			public Object convert(Object fromObject) {
				return ((Integer) fromObject).toString();
			}
		});
		bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(fNumberOfSuccess), EMFProperties.value(CbmcPackage.Literals.RESULTS__SUCCEEDED_COUNT).observe(results), null, strategy);
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(fNumberOfFailures), EMFProperties.value(CbmcPackage.Literals.RESULTS__FAILED_COUNT).observe(results), null, strategy);
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(fNumberOfErrors), EMFProperties.value(CbmcPackage.Literals.RESULTS__ERROR_COUNT).observe(results), null, strategy);
		UpdateValueStrategy strategy2 = new UpdateValueStrategy();
		strategy2.setConverter(new IConverter() {

			@Override
			public Object getToType() {
				return String.class;
			}

			@Override
			public Object getFromType() {
				return Integer.class;
			}

			@Override
			public Object convert(Object fromObject) {
				return Messages.format(Messages.CounterPanel_runcount, new String[] {Integer.toString(((Integer) fromObject)), Integer.toString(results.getProperties().size())});
			}
		});
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(fNumberOfRuns), EMFProperties.value(CbmcPackage.Literals.RESULTS__RUN_COUNT).observe(results), null, strategy2);

	}

	public void dispose() {
		if (bindingContext != null)
			bindingContext.dispose();
		disposeIcons();
	}
}