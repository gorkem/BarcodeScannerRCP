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
