
package org.inf.uth.eydokia;

import java.sql.Connection;
import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 * Web application lifecycle listener.
 *
 * @author Nilos
 */
public class ServletListener implements ServletContextListener 
{
    @Resource(name = "jdbc/eydokia")
    private DataSource mDataSource;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) 
    {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) 
    {
    }
}
