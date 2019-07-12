package de.creativemd.launcher.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.OS;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.browser.CefMessageRouter;
import org.cef.browser.CefMessageRouter.CefMessageRouterConfig;
import org.cef.browser.CefRequestContext;
import org.cef.handler.CefLoadHandlerAdapter;

import de.creativemd.launcher.frame.BrowserFrame;
import de.creativemd.launcher.frame.MainFrame;
import de.creativemd.launcher.module.InternetModule;
import de.creativemd.launcher.module.LauncherServiceModule;
import de.creativemd.launcher.utils.Logger;

public class Core {
	
	public static final String PROGRAM_NAME = "CMDLauncher";
	public static final String PROGRAM_VERSION = "3.0.0";
	
	public static final Logger MAIN_LOG = new Logger(true);
	
	public static MainFrame MAIN_FRAME = null;
	public static CefApp CEF_APP = null;
	
	public static final Path PROGRAM_PATH = Paths.get("").toAbsolutePath();
	public static final Path INSTANCES_PATH = PROGRAM_PATH.resolve("instances");
	public static final Path ASSETS_PATH = PROGRAM_PATH.resolve("assets");
	public static final Path STYLE_PATH = ASSETS_PATH.resolve("style");
	public static final Path DOWNLOADS_PATH = PROGRAM_PATH.resolve("downloads");
	public static final Path TEMP_PATH = DOWNLOADS_PATH.resolve("temp");
	public static final Path HTML_PATH = PROGRAM_PATH.resolve("html");
	public static final Path MINECRAFT_PATH = Paths.get(System.getenv("APPDATA"), ".minecraft");
	
	public static final InternetModule INTERNET = new InternetModule();
	public static final LauncherServiceModule SERVICE = new LauncherServiceModule();
	
	public static Language LANG = new Language("en");
	public static Style STYLE = new Style("default", STYLE_PATH.resolve("default"));
	
	public static CopyOnWriteArrayList<BrowserFrame> openFrames = new CopyOnWriteArrayList<>();
	
	public static boolean terminated = false;
	public static boolean closeLauncher = false;
	
	public static void main(String[] args) {
		MAIN_LOG.log("Init Core");
		MAIN_LOG.log("Launching %s v%s ...", PROGRAM_NAME, PROGRAM_VERSION);
		
		if (args.length > 0)
			MAIN_LOG.log("Arguments detected %s", (Object) args);
		
		try {
			loadCEFApp(args);
		} catch (Exception e) {
			MAIN_LOG.logError("Something went wrong during loading Chromium");
			e.printStackTrace();
			MAIN_LOG.logError("Application will termiante!");
			return;
		}
		MAIN_LOG.log("Loaded Chromium successfully");
		
		LANG.loadLanguage();
		
		createMainFrame();
	}
	
	private static CefClient client_;
	private static boolean transparentPaintingEnabled;
	private static boolean osrEnabled;
	private static CefRequestContext requestContext;
	
	private static void loadCEFApp(String[] args) throws Exception {
		// OSR mode is enabled by default on Linux.
		// and disabled by default on Windows and Mac OS X.
		boolean osrEnabled = OS.isLinux();
		for (String arg : args) {
			arg = arg.toLowerCase();
			if (!OS.isLinux() && arg.equals("--off-screen-rendering-enabled"))
				osrEnabled = true;
			else if (arg.equals("--transparent-painting-enabled"))
				transparentPaintingEnabled = true;
		}
		
		if (CefApp.getState() != CefApp.CefAppState.INITIALIZED) {
			CefSettings settings = new CefSettings();
			settings.windowless_rendering_enabled = osrEnabled;
			settings.background_color = settings.new ColorType(100, 255, 242, 211);
			
			MAIN_LOG.log("Starting Chromium ...");
			CEF_APP = CefApp.getInstance(args, settings);
			MAIN_LOG.log(CEF_APP.getVersion().toString());
			if (osrEnabled)
				MAIN_LOG.log("Offscreen rendering is enabled");
		}
		
		client_ = CEF_APP.createClient();
		
		/* DownloadDialog downloadDialog = new DownloadDialog(this);
		 * client_.addContextMenuHandler(new ContextMenuHandler(this));
		 * client_.addDownloadHandler(downloadDialog);
		 * client_.addDragHandler(new DragHandler());
		 * client_.addJSDialogHandler(new JSDialogHandler());
		 * client_.addKeyboardHandler(new KeyboardHandler());
		 * //client_.addRequestHandler(new RequestHandler(this)); */
		//http://magpcss.org/ceforum/viewtopic.php?f=17&t=12317
		CefMessageRouterConfig screenInteraction = new CefMessageRouterConfig("screenControl", "noScreenControl");
		CefMessageRouter msgRouter = CefMessageRouter.create(screenInteraction);
		msgRouter.addHandler(new ScreenMessageHandler(), true);
		client_.addMessageRouter(msgRouter);
		client_.addLoadHandler(new CefLoadHandlerAdapter() {
			@Override
			public void onLoadingStateChange(CefBrowser browser, boolean isLoading, boolean canGoBack, boolean canGoForward) {
				
				MAIN_FRAME.surface.onLoadingStateChange(browser, isLoading, canGoBack, canGoForward);
			}
			
			@Override
			public void onLoadError(CefBrowser browser, CefFrame frame, ErrorCode errorCode, String errorText, String failedUrl) {
				if (errorCode != ErrorCode.ERR_NONE && errorCode != ErrorCode.ERR_ABORTED) {
					browser.stopLoad();
				}
			}
		});
	}
	
	public static BrowserFrame getFrame(CefBrowser browser) {
		for (BrowserFrame frame : openFrames) {
			if (frame.getBrowser() == browser)
				return frame;
		}
		return null;
	}
	
	public static CefBrowser createBrowser(String url) {
		return client_.createBrowser(url, osrEnabled, transparentPaintingEnabled, requestContext);
	}
	
	private static void createMainFrame() {
		MAIN_FRAME = new MainFrame(Startup.getStartupScreen());
		MAIN_FRAME.setSize(800, 600);
		MAIN_FRAME.setVisible(true);
	}
}
