/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.cheatsheets;

import org.eclipse.ui.cheatsheets.ICheatSheetManager;

/**
 * Event in the life cycle of a cheat sheet.
 * <p>
 * Events over the life time of a running cheat sheet
 * follow this pattern:
 * <pre>
 * opened { started | restored } { restarted | end_reached }* closed
 * </pre> 
 * </p>
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
 * 
 * @see CheatSheetListener
 * @since 3.0
 */
public interface ICheatSheetEvent {
	
	/**
	 * Event type constant (value {@value}) indicating that
	 * the cheat sheet was opened. This is the first event 
     * in the life of a running cheat sheet.
	 */
	public static final int CHEATSHEET_OPENED = 0;
	
	/**
	 * Event type constant (value {@value}) indicating that
	 * the cheat sheet was closed. This is the last event 
     * in the life of a running cheat sheet.
	 */
	public static final int CHEATSHEET_CLOSED = 1;
	
	/**
	 * Event type constant (value {@value}) indicating that
	 * the cheat sheet was started.
	 */
	public static final int CHEATSHEET_STARTED = 2;
	
	/**
	 * Event type constant (value {@value}) indicating that
	 * the cheat sheet was restarted.
	 */
	public static final int CHEATSHEET_RESTARTED = 3;
	
	/**
	 * Event type constant (value {@value}) indicating that
	 * the cheat sheet has been completed.
	 * TODO (lorne) - rename CHEATSHEET_COMPLETED ?
	 */
	public static final int CHEATSHEET_END_REACHED = 4;

	/**
	 * Event type constant (value {@value}) indicating that
	 * the cheat sheet has been restored.
	 */
	public static final int CHEATSHEET_RESTORED = 5;

	/**
	 * Returns the type of this cheat sheet event.
	 * 
	 * @return the event type code; one of the event type constants
	 * declared on this class
	 */
	public int getEventType();
	
	/**
	 * Returns the id of the cheat sheet that generated this event.
	 * 
	 * @return the cheat sheet id
	 */
	public String getCheatSheetID();
	
	/**
	 * Returns the cheat sheet manager responsible for executing
	 * the cheat sheet.
	 * 
	 * @return the cheat sheet manager
	 */
	public ICheatSheetManager getCheatSheetManager();
}
