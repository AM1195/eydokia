
package org.inf.uth.eydokia.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import static org.inf.uth.eydokia.jooq.tables.Entry.ENTRY;
import static org.inf.uth.eydokia.jooq.tables.Room.ROOM;
import static org.inf.uth.eydokia.jooq.tables.Schedule.SCHEDULE;
import static org.inf.uth.eydokia.jooq.tables.ScheduleType.SCHEDULE_TYPE;
import static org.inf.uth.eydokia.jooq.tables.User.USER;
import org.inf.uth.eydokia.jooq.tables.records.EntryRecord;
import org.inf.uth.eydokia.jooq.tables.records.RoomRecord;
import org.inf.uth.eydokia.jooq.tables.records.ScheduleRecord;
import org.inf.uth.eydokia.jooq.tables.records.ScheduleTypeRecord;
import org.inf.uth.eydokia.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 *
 * @author Nilos
 */
public class ModerateServlet extends HttpServlet 
{
    private final static String STATUS_ACCEPT = "accept";
    private final static String STATUS_REJECT = "reject";
    
    @Resource(name = "jdbc/eydokia")
    private DataSource mDataSource;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException 
    {
        try (Connection conn = mDataSource.getConnection())
        {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            
            UserRecord user = (UserRecord) request.getSession().getAttribute("user");
            int entry_id = Integer.parseInt(request.getParameter("entry_id"));
            EntryRecord entry = create
                                    .fetchOne(ENTRY, ENTRY.ENTRY_ID.eq(entry_id));
            
            switch (request.getParameter("status"))
            {
            case STATUS_ACCEPT:
                entry.setStatus((byte) 1);
                break;
            case STATUS_REJECT:
                String info = request.getParameter("info_text" + entry_id);
                
                if (info == null || info.isEmpty())
                {
                    throw new Exception("Please specify the cause of rejection.");
                }
                
                entry.setInfoText(info);
                entry.setInfoUser(String.format("%s [%s]", user.getFullName(), user.getUsername()));
                entry.setStatus((byte) -1);
                break;
            default:
                throw new Exception("Wrong status param: " + request.getParameter("status"));
            }
            
            entry.update();
            doGet(request, response);
        }
        catch (Exception e)
        {
             request.setAttribute("errorMsg", e.getMessage());
             doGet(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        try (Connection conn = mDataSource.getConnection())
        {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            
            Result entries = create.selectFrom(ENTRY).fetch();
            Map<Integer, RoomRecord> rooms = create.selectFrom(ROOM)
                            .where(ROOM.ROOM_ID.in(entries.getValues(ENTRY.ROOM_ID)))
                        .fetchMap(ROOM.ROOM_ID);
            
            Map<Integer, UserRecord> users = create.selectFrom(USER)
                            .where(USER.USER_ID.in(entries.getValues(ENTRY.USER_ID)))
                        .fetchMap(USER.USER_ID);
            
            Map<Integer, ScheduleRecord> schedules = create.selectFrom(SCHEDULE)
                            .where(SCHEDULE.SCHEDULE_ID.in(entries.getValues(ENTRY.SCHEDULE_ID)))
                        .fetchMap(SCHEDULE.SCHEDULE_ID);
            
            Map<Integer, ScheduleTypeRecord> scheduleTypes = create.selectFrom(SCHEDULE_TYPE)
                                .fetchMap(SCHEDULE_TYPE.SCHEDULE_TYPE_ID);
            
            request.setAttribute("entries", entries);  
            request.setAttribute("rooms", rooms);  
            request.setAttribute("users", users);  
            request.setAttribute("schedules", schedules);  
            request.setAttribute("schedule_types", scheduleTypes);  
            
            request.getRequestDispatcher("/moderate.jsp").forward(request, response);
        }
        catch (Exception e)
        {
            request.setAttribute("errorMsg", e.getMessage());
            request.getRequestDispatcher("/moderate.jsp").forward(request, response);                    
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
