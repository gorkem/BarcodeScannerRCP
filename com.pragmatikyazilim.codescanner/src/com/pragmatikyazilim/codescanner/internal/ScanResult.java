/*******************************************************************************
 * Copyright (c) 2009 Gorkem Ercan.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.pragmatikyazilim.codescanner.internal;

public class ScanResult {
	private String resultText;
	
	public ScanResult(String resultText ){
		this.setResultText(resultText);
	}

	private void setResultText(String resultText) {
		this.resultText = resultText;
	}

	public String getResultText() {
		return resultText;
	}
	
	
}
