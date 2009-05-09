package com.pragmatikyazilim.codescanner.internal;

import java.util.EventListener;

public interface BarcodeListener extends EventListener {

	public void barcodeResolved( ScanResult result );
}
