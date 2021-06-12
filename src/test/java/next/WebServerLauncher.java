package next;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class WebServerLauncher {
    private static final Logger logger = LoggerFactory.getLogger(WebServerLauncher.class);

    public static void main(String[] args) throws Exception {
        String webappDirLocation = "./webapp/";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        final Connector connector = tomcat.getConnector();
        connector.setURIEncoding("UTF-8");

        tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
        logger.info("configuring app with basedir: {}", new File("./" + webappDirLocation).getAbsolutePath());


        tomcat.start();
        tomcat.getServer().await();
    }
}
