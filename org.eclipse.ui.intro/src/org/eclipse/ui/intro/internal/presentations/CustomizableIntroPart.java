/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.intro.internal.presentations;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.intro.*;
import org.eclipse.ui.intro.internal.*;
import org.eclipse.ui.intro.internal.extensions.*;
import org.eclipse.ui.intro.internal.model.*;
import org.eclipse.ui.intro.internal.parts.*;
import org.eclipse.ui.intro.internal.util.*;
import org.eclipse.ui.part.intro.*;

/**
 * 
 *  
 */
public class CustomizableIntroPart extends IntroPart {

	private IntroModelRoot model;
	private IIntroSite introSite;
	private IntroPartPresentation presentation;
	private StandbyPart standbyPart;
	private Composite container;

	/**
	 *  
	 */
	public CustomizableIntroPart() {
		// model can not be loaded here because the configElement of this part
		// is still not loaded here.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.temp.IIntroPart#init(org.eclipse.ui.internal.temp.IIntroSite)
	 */
	public void init(IIntroSite site) throws PartInitException {
		this.introSite = site;

		// load the correct model based in the current Intro Part id. Set the
		// IntroPartId in the manager class.
		String introId = getConfigurationElement().getAttribute("id");
		ExtensionPointManager extensionPointManager =
			IntroPlugin.getDefault().getExtensionPointManager();
		extensionPointManager.setIntroId(introId);
		model = extensionPointManager.getCurrentModel();

		if (model != null && model.hasValidConfig()) {
			// we have a valid config contribution, get presentation.
			presentation = model.getPresentation();
			if (presentation != null)
				presentation.init(this);
			standbyPart = new StandbyPart(model, site);
		}

		// REVISIT: make sure this is handled better.
		//			throw new PartInitException(
		//				"Could not find a valid configuration for Intro Part: "
		//					+ ExtensionPointManager.getLogString(
		//						getConfigurationElement()));

		if (model == null || !model.hasValidConfig())
			DialogUtil.displayErrorMessage(
				site.getShell(),
				"Could not find a valid configuration for Intro Part: "
					+ ExtensionPointManager.getLogString(
						getConfigurationElement())
					+ "\nCheck Log View for details.",
				null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.temp.IIntroPart#getIntroSite()
	 */
	public IIntroSite getIntroSite() {
		return introSite;
	}

	/**
	 * Creates the UI based on how the InroPart has been configured.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {

		container = new Composite(parent, SWT.NULL);
		StackLayout layout = new StackLayout();
		layout.marginHeight = 0;
		layout.marginHeight = 0;
		container.setLayout(layout);

		if (model != null && model.hasValidConfig()) {
			presentation.createPartControl(container);
			standbyPart.createPartControl(container);
			standbyStateChanged(false);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IIntroPart#standbyStateChanged(boolean)
	 */
	public void standbyStateChanged(boolean standby) {
		// do this only if there is a valid config.
		if (model != null && model.hasValidConfig()) {
			if (standby)
				standbyPart.setInput(null);
			setTopControl(
				standby ? getStandbyControl() : getPresentationControl());
			if (!standby)
				presentation.setFocus();
		}

	}

	private void setTopControl(Control c) {
		StackLayout layout = (StackLayout) container.getLayout();
		layout.topControl = c;
		container.layout();
	}

	private Control getPresentationControl() {
		return container.getChildren()[0];
	}

	private Control getStandbyControl() {
		return container.getChildren()[1];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		if (presentation != null)
			presentation.setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		super.dispose();
		if (presentation != null)
			// call dispose on presentation.
			presentation.dispose();
		// call dispose on the standby part
		if (standbyPart != null)
			standbyPart.dispose();
		// clear all loaded models since we are disposing of the Intro Part.
		IntroPlugin.getDefault().getExtensionPointManager().clear();
	}

	public void setStandbyInput(Object input) {
		standbyPart.setInput(input);
	}

}
