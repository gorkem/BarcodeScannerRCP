/*******************************************************************************
 * Copyright (c) 2009 Gorkem Ercan.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.pragmatikyazilim.codescanner.internal;

import com.google.zxing.MonochromeBitmapSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageMonochromeBitmapSource;

public class BarcodeScanner implements Runnable {
	
	CameraControl camera;
	BarcodeListener listener;
	
	
	public BarcodeScanner(CameraControl control){
		camera=control;
	}
	
	public void startScanning( BarcodeListener listener ){
		this.listener = listener; 
		Thread t = new Thread(this);
		t.start();		
	}

	@Override
	public void run() {
		boolean found = false;
		do {

			MonochromeBitmapSource bmsource = new BufferedImageMonochromeBitmapSource(
					camera.getFrame());
			Result result;
			try {
				result = new MultiFormatReader().decode(bmsource);
				listener.barcodeResolved(new ScanResult(result.getText()));
				break;
			} catch (ReaderException re) {
			}
		} while (!found);

	}

}
