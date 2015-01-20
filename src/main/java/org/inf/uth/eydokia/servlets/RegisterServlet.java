
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
import static org.inf.uth.eydokia.servlets.LoginServlet.COOKIE_DELIM;
import static org.inf.uth.eydokia.servlets.LoginServlet.COOKIE_NAME_USER;
import static org.inf.uth.eydokia.servlets.LoginServlet.login;
import static org.inf.uth.eydokia.jooq.tables.User.USER;
import org.inf.uth.eydokia.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 *
 * @author Nilos
 */
public class RegisterServlet extends HttpServlet 
{
    private static final int UNAME_MIN = 4;
    private static final int UNAME_MAX = 16;
    
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
        request.getRequestDispatcher("/register_or_login.jsp")
                .forward(request, response);
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
        String username =   request.getParameter("username");
        String email =      request.getParameter("email");
        String password =   request.getParameter("password");
        String _password =  request.getParameter("_password");
        String fullName =  request.getParameter("full_name");
        String phone =      request.getParameter("phone");
        String extraInfo =  request.getParameter("extra_info");
        
        try (Connection conn = mDataSource.getConnection())
        {
            if (!password.equals(_password))
            {
                throw new Exception("Passwords don't match.");
            }
            
            if (!password.matches(String.format("^([A-Za-z0-9]{%d,%d})$", 
                                    UNAME_MIN, UNAME_MAX)))
            {
                throw new Exception(
                    String.format("Password must be between %d and %d charcters "
                            + "and contain only letter and digits.", 
                            UNAME_MIN, UNAME_MAX));
            }
            
            if (!username.matches(String.format("^([A-Za-z0-9]{%d,%d})$", 
                                    UNAME_MIN, UNAME_MAX)))
            {
                throw new Exception(
                    String.format("Username must be between %d and %d charcters "
                            + "and contain only letter and digits.", 
                            UNAME_MIN, UNAME_MAX));
            }
            
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            
            UserRecord user = create.newRecord(USER);            
            
            UserRecord registered = create.selectFrom(USER)
                                                .where(USER.USERNAME.eq(username))
                                        .fetchOne();
            if (registered != null)
            {
                throw new Exception("Username already exists.");
            }
            user.setUsername(username);
            
            registered = create.selectFrom(USER)
                                    .where(USER.EMAIL.eq(email))
                                .fetchOne();
            if (registered != null)
            {
                throw new Exception("Email already registered with the user " 
                                    + registered.getUsername());
            }
            user.setEmail(email);
            
            user.setRoleId(2);      // registered
            user.setPassword(password);
            
            if (fullName == null || fullName.isEmpty())
            {
                throw new Exception("Please fill you full name");
            }
            user.setFullName(fullName);
            user.setPhone(phone);
            user.setExtraInfo(extraInfo);
            
            user.store();
            request.getSession().setAttribute("user", user);
            request.getRequestDispatcher("/calendar").forward(request, response);
        }
        catch (Exception e) 
        {
            request.setAttribute("errorMsg", e.getMessage());
            request.getRequestDispatcher("/register_or_login.jsp")
                    .forward(request, response);
        }
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
