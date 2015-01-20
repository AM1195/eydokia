
package org.inf.uth.eydokia.servlets;

import java.io.IOException;
import java.sql.Connection;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import static org.inf.uth.eydokia.jooq.Tables.USER;
import org.inf.uth.eydokia.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 *
 * @author Nilos
 */
public class LoginServlet extends HttpServlet 
{
    public final static String COOKIE_DELIM = "_";
    public final static String COOKIE_NAME_USER = "eydokia_logged_user";
    
    @Resource(name = "jdbc/eydokia")
    private DataSource mDataSource;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        request.getRequestDispatcher("/register_or_login.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try (Connection conn = mDataSource.getConnection())
        {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            
            UserRecord user = login(create,
                                    request.getParameter("username"), 
                                    request.getParameter("password"));            
            String msg = "";
            
            if (user != null)
            {
                request.getSession().setAttribute("user", user);   
                if (request.getParameter("remember") != null)
                {
                    Cookie userCookie = new Cookie(COOKIE_NAME_USER, 
                            String.format("%s%s%s", 
                                    user.getUsername(),
                                    COOKIE_DELIM,
                                    user.getPassword()));
                    
                    userCookie.setMaxAge(60 * 60 * 24 * 30);    // about a month
                    
                    response.addCookie(userCookie);
                }
            }
            else
            {
                msg = "Wrong username or password";
            }
            
            request.setAttribute("errorMsg", msg);
            request.getRequestDispatcher("/register_or_login.jsp")
                    .forward(request, response);
        }
        catch (Exception e)
        {
            request.setAttribute("errorMsg", e.getMessage());
            request.getRequestDispatcher("/register_or_login.jsp")
                    .forward(request, response);
        }        
    }
    
    public static UserRecord login(DSLContext create, String username, String password)
    {
        return create.selectFrom(USER)
                    .where(USER.USERNAME.eq(username))
                    .and(USER.PASSWORD.eq(password))
                .fetchOne();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
