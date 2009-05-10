/*******************************************************************************
 * Copyright (c) 2009 Gorkem Ercan.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.pragmatikyazilim.codescanner.internal;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.media.Buffer;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.control.FormatControl;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.protocol.DataSource;
import javax.media.util.BufferToImage;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;

import com.pragmatikyazilim.codescanner.Activator;

public class CameraControl {

	private Player player;
	private FrameGrabbingControl frameGrabber;
	private VideoFormat format;
	private Composite viewFinder;

	public CameraControl(){
		super();
	}
	/**
	 * Initializes the JMF player.
	 * 
	 * @throws CoreException
	 */
	public void initializePlayer( ) throws CoreException{
		MediaLocator locator = new MediaLocator("vfw://0");
		DataSource source;
		try {
			source = Manager.createDataSource(locator);
			player = Manager.createRealizedPlayer(source);
			frameGrabber = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
			FormatControl formatControl = (FormatControl)player.getControl ( "javax.media.control.FormatControl" );
			format = (VideoFormat)formatControl.getFormat();
			
		} catch (Exception e) {
			CoreException ce = new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error initializing the video capture device", e));
			throw ce ;
		} 
	}
	/**
	 * Call initializePlayer first
	 * 
	 * @param parent
	 * @return
	 */
	public Composite initializeViewFinder( Composite parent ){
		Assert.isNotNull(player, "Can not initialize view finder without a player");
		destroyViewFinder();
		viewFinder = new Composite(parent, SWT.EMBEDDED);
		Component component = player.getVisualComponent();
		Frame frame = SWT_AWT.new_Frame(viewFinder);
		frame.add(component);
		parent.layout(true);
		
		player.start();
		return viewFinder;
	}
	public void destroyViewFinder() {
		if(viewFinder != null && !viewFinder.isDisposed()) viewFinder.dispose();
	}
	
	
	public void destroyPlayer(){
		player.stop();
		player.deallocate();
		player=null;
	}
	
	public BufferedImage getFrame(){
		Buffer buffer =  frameGrabber.grabFrame();
		BufferToImage boi = new BufferToImage((VideoFormat)buffer.getFormat());
		Image image = boi.createImage(buffer);
		BufferedImage bi = new BufferedImage(format.getSize().width, format.getSize().height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bi.createGraphics();
        g2.drawImage ( image, null, null );
		return bi;
	}

}
