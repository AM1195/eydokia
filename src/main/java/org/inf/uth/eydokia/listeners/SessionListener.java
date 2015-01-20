/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inf.uth.eydokia.listeners;

import java.sql.Connection;
import javax.annotation.Resource;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;
import static org.inf.uth.eydokia.jooq.tables.User.USER;
import org.inf.uth.eydokia.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 * Web application lifecycle listener.
 *
 * @author Nilos
 */
public class SessionListener implements HttpSessionListener {
    
    @Resource(name = "jdbc/eydokia")
    private DataSource mDataSource;
        
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        try (Connection conn = mDataSource.getConnection())
        {
            String guestUsername = (String) se.getSession().getServletContext()
                                                .getInitParameter("guest_username");
            
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            
            UserRecord user = create.selectFrom(USER)
                                .where(USER.USERNAME.eq(guestUsername))
                            .fetchOne();
            
            se.getSession().setAttribute("user", user);            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) { }
}
