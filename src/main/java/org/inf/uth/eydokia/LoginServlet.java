
package org.inf.uth.eydokia;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import static org.inf.uth.eydokia.jooq.Tables.USER;
import org.inf.uth.eydokia.jooq.tables.daos.UserDao;
import org.inf.uth.eydokia.jooq.tables.pojos.User;
import org.inf.uth.eydokia.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 *
 * @author Nilos
 */
public class LoginServlet extends HttpServlet {

    @Resource(name = "jdbc/eydokia")
    private DataSource mDataSource;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String userName = "root";
        String password = "root";
        String url = "jdbc:mysql://localhost:3306/eydokia";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Connection is the only JDBC resource that we need
        // PreparedStatement and ResultSet are handled by jOOQ, internally
        try (Connection conn = DriverManager.getConnection(url, userName, password)) 
        {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            
            UserRecord user = create.newRecord(USER);
            user.setUsername(userName + (System.currentTimeMillis() % 100));
            user.setEmail("a@" + (System.currentTimeMillis() % 100) + ".gr");
            user.setPassword("123");
            user.store();
            
            List<String> names = create.selectFrom(USER).fetch(USER.USERNAME);
            for (String n : names)
                response.getWriter().print(n + "<br />");
            
            response.getWriter().println("<br />-----");

        } // For the sake of this tutorial, let's keep exception handling simple
        catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            throws ServletException, IOException {
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
            
            UserRecord user = create.selectFrom(USER)
                    .where(USER.USERNAME.eq(request.getParameter("username")))
                    .and(USER.PASSWORD.eq(request.getParameter("password")))
                .fetchOne();
            
            String msg = "";
            
            if (user != null)
            {
                request.getSession().setAttribute("user", user);                
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
