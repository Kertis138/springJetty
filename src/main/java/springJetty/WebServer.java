package springJetty;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Example WebServer class which sets up an embedded Jetty appropriately
 * whether running in an IDE or in "production" mode in a shaded jar.
 * 
 * @author steve liles
 */
public class WebServer
{
	// TODO: You should configure this appropriately for your environment
	private static final String LOG_PATH = "./var/logs/access/yyyy_mm_dd.request.log";
	
	private static final String WEB_XML = "WEB-INF/web.xml";

    private int localPort;
    
    private Server server;
    private int port;
    private String bindInterface;
    
    public WebServer(int aPort)
    {
        this(aPort, null);
    }
    
    public WebServer(int aPort, String aBindInterface)
    {
        port = aPort;
        bindInterface = aBindInterface;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void start() throws Exception
    {
        server = new Server(createThreadPool());

        NetworkTrafficServerConnector connector = createConnector();
        server.addConnector(connector);

        server.setHandler(createHandlers());
        server.setStopAtShutdown(true);

        server.start();
        localPort = connector.getLocalPort();
    }
    
    public void join() throws InterruptedException
    {
        server.join();
    }
    
    public void stop() throws Exception
    {        
        server.stop();
    }
    
    private ThreadPool createThreadPool()
    {
    	// TODO: You should configure these appropriately
    	// for your environment - this is an example only
        QueuedThreadPool _threadPool = new QueuedThreadPool();
        _threadPool.setMinThreads(10);
        _threadPool.setMaxThreads(100);
        return _threadPool;
    }
    
    private NetworkTrafficServerConnector createConnector()
    {
        NetworkTrafficServerConnector _connector = new NetworkTrafficServerConnector(server);
        _connector.setPort(port);
        _connector.setHost(bindInterface);
        return _connector;
    }
    
    private HandlerCollection createHandlers()
    {                
        WebAppContext _ctx = new WebAppContext();
        _ctx.setContextPath("/");
        _ctx.setWar(getWarUrl());

        
        List<Handler> _handlers = new ArrayList<Handler>();
        
        _handlers.add(_ctx);
        
        HandlerList _contexts = new HandlerList();
        _contexts.setHandlers(_handlers.toArray(new Handler[0]));
        
        RequestLogHandler _log = new RequestLogHandler();
        _log.setRequestLog(createRequestLog());
        
        HandlerCollection _result = new HandlerCollection();
        _result.setHandlers(new Handler[] {_contexts, _log});
        
        return _result;
    }
    
    private RequestLog createRequestLog()
    {
        NCSARequestLog _log = new NCSARequestLog();
        
    	File _logPath = new File(LOG_PATH);
        _logPath.getParentFile().mkdirs();
                
        _log.setFilename(_logPath.getPath());
        _log.setRetainDays(90);
        _log.setExtended(false);
        _log.setAppend(true);
        _log.setLogTimeZone("GMT");
        _log.setLogLatency(true);
        return _log;
    }

    private String getWarUrl()
    {
        URL resourceURL = Thread.currentThread().getContextClassLoader().getResource(WEB_XML);
        String _urlStr = resourceURL.toString();
        return _urlStr.substring(0, _urlStr.length() - WEB_XML.length());
    }
}
