package de.creativemd.launcher.frame;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.cef.CefApp;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefLifeSpanHandlerAdapter;

import de.creativemd.launcher.core.Core;
import de.creativemd.launcher.screen.Screen;

public class BrowserFrame extends JFrame {
	
	public Screen surface;
	
	private static final long serialVersionUID = 8968473594447736431L;
	
	private boolean isClosed_ = false;
	private CefBrowser browser_ = null;
	private static int browserCount_ = 0;
	
	public BrowserFrame(String title, Screen face) {
		super(title);
		this.surface = face;
		this.surface.init(this);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (browser_ == null) {
					isClosed_ = true;
					dispose();
					return;
				}
				
				boolean isClosed = isClosed_;
				
				if (isClosed)
					browser_.setCloseAllowed();
				
				browser_.close(isClosed);
				if (!isClosed_)
					isClosed_ = true;
				if (isClosed)
					dispose();
			}
		});
		
		setBrowser(Core.createBrowser(surface.build()));
		
		Core.openFrames.add(this);
		
		surface.load(browser_);
		
		getContentPane().add(createContentPanel(), BorderLayout.CENTER);
	}
	
	public void loadSurface(Screen surface) {
		this.surface = surface;
		getBrowser().loadURL(surface.build());
	}
	
	private JPanel createContentPanel() {
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(getBrowser().getUIComponent(), BorderLayout.CENTER);
		return contentPanel;
	}
	
	public void setBrowser(CefBrowser browser) {
		if (browser_ == null)
			browser_ = browser;
		
		browser_.getClient().removeLifeSpanHandler();
		browser_.getClient().addLifeSpanHandler(new CefLifeSpanHandlerAdapter() {
			@Override
			public void onAfterCreated(CefBrowser browser) {
				browserCount_++;
			}
			
			@Override
			public boolean doClose(CefBrowser browser) {
				return browser.doClose();
			}
			
			@Override
			public void onBeforeClose(CefBrowser browser) {
				if (--browserCount_ == 0) {
					CefApp.getInstance().dispose();
				}
				Core.openFrames.remove(BrowserFrame.this);
			}
		});
	}
	
	public CefBrowser getBrowser() {
		return browser_;
	}
}
