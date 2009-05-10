/*******************************************************************************
 * Copyright (c) 2009 Gorkem Ercan.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.pragmatikyazilim.codescanner;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;

import com.pragmatikyazilim.codescanner.internal.BarcodeListener;
import com.pragmatikyazilim.codescanner.internal.BarcodeScanner;
import com.pragmatikyazilim.codescanner.internal.CameraControl;
import com.pragmatikyazilim.codescanner.internal.ScanResult;

public class View extends ViewPart {
	public static final String ID = "com.pragmatikyazilim.codescanner.view";
	private BarcodeScanner scanner;
	private Composite parent;



	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(final Composite parent) {
		this.parent=parent;
		parent.setLayout( new FillLayout());
		CameraControl camera = startCamera(parent);
		startBarcodeScanning(camera);
	}
	private void startBarcodeScanning(CameraControl camera) {
		if (scanner == null ) scanner = new BarcodeScanner(camera);
		scanner.startScanning(new BarcodeListener(){
			@Override
			public void barcodeResolved(final ScanResult result) {
				getViewSite().getShell().getDisplay().syncExec( new Runnable(){
					@Override
					public void run() {
						resultRecieved(result);
					}
				});
				
				
			}
		});
	}
	private CameraControl startCamera(final Composite parent) {
		CameraControl camera = Activator.getDefault().getCameraControl();
		try {
			camera.initializePlayer();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		camera.initializeViewFinder(parent);
		return camera;
	}
	
	private void resultRecieved(ScanResult result){
		CameraControl camera = Activator.getDefault().getCameraControl();
		camera.destroyViewFinder();
		Display display = parent.getDisplay();
		display.beep();
		Clipboard cb = new Clipboard(display);
		TextTransfer tt= TextTransfer.getInstance();
		cb.setContents(new Object[]{result.getResultText()}, new Transfer[]{tt});
		cb.dispose();
		Browser browser = new Browser(parent, SWT.NONE);
		browser.setUrl("http://www.google.com/books?vid=isbn"+result.getResultText());
		parent.getShell().setMaximized(true);
		
//		final Label label = new Label(parent, SWT.NONE);
//		label.setText("Result: "+result.getResultText());
//		final Button button = new Button(parent, SWT.PUSH);
//		button.setText("Scan Another");
		parent.layout(true);
//		button.addListener(SWT.Selection, new Listener(){
//		
//			@Override
//			public void handleEvent(Event event) {
//				label.dispose();
//				button.dispose();
//				CameraControl camera = Activator.getDefault().getCameraControl();
//				camera.initializeViewFinder(parent);
//				startBarcodeScanning(camera);
//			}
//		});
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		
	}
}