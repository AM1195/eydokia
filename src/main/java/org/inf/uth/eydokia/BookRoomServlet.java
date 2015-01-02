
package org.inf.uth.eydokia;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
public class BookRoomServlet extends HttpServlet 
{
    private final static SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");
    
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
            throws ServletException, IOException {
        try (Connection conn = mDataSource.getConnection())
        {
            initCalendarParams(conn, request);
            
            request.getRequestDispatcher("/book_room.jsp").forward(request, response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
            
            int roomId = Integer.parseInt(request.getParameter("room_id"));
            int scheduleTypeId = Integer.parseInt(request.getParameter("schedule_type_id"));
            Date startDate = new Date(DATE_FMT.parse(request.getParameter("start_date")).getTime());
            Date endDate = new Date(DATE_FMT.parse(request.getParameter("end_date")).getTime());
            Time startTime = Time.valueOf(request.getParameter("start_time") + ":00");
            Time endTime = Time.valueOf(request.getParameter("end_time") + ":00");
            String description = request.getParameter("description");
            
            EntryRecord entry = create.newRecord(ENTRY);
            entry.setUserId((Integer) request.getSession().getAttribute("user"));
            entry.setRoomId(roomId);
            entry.setStartDate(startDate);
            entry.setEndDate(endDate);
            entry.setStartTime(startTime);
            entry.setEndTime(endTime);
            entry.setDescription(description);
            
            ScheduleRecord schedule = create.newRecord(SCHEDULE);
            schedule.setScheduleTypeId(scheduleTypeId);
            schedule.store();
            
            entry.setScheduleId(schedule.getScheduleId());
            entry.store();
            
            initCalendarParams(conn, request);
            
            request.getRequestDispatcher("/book_room.jsp").forward(request, response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void initCalendarParams(Connection conn, HttpServletRequest request) 
            throws SQLException
    {
        DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

        Result<RoomRecord> roomsList = create.selectFrom(ROOM)
                        .where(ROOM.DISABLED.eq(false))
                    .fetch();

        Result<ScheduleTypeRecord> scheduleTypesList = create.selectFrom(SCHEDULE_TYPE)
                            .fetch();

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

        request.setAttribute("rooms_list", roomsList);  
        request.setAttribute("schedule_types_list", scheduleTypesList);   
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
