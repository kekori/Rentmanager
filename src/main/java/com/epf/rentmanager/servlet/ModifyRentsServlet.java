package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.exception.ValideException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/rents/modify")
public class ModifyRentsServlet extends HttpServlet {

    @Autowired
    private ClientService clientService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private ReservationService reservationService;
    @Override


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try{
            long id = Long.parseLong(request.getParameter("id"));
            Reservation reservation = reservationService.findById(id);
            request.setAttribute("reservation", reservation);
            request.setAttribute("allClients", clientService.findAll());
            request.setAttribute("allVehicles", vehicleService.findAll());
        }
        catch (ServiceException e){
            throw new ServletException();
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/modify.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Reservation reservation = null;
        try {
            long id = Long.parseLong(request.getParameter("id"));
            long client_id = Long.parseLong(request.getParameter("client_id"));
            long vehicle_id = Long.parseLong(request.getParameter("vehicle_id"));
            LocalDate beginning = LocalDate.parse(request.getParameter("beginning"));
            LocalDate ending = LocalDate.parse(request.getParameter("ending"));

            reservation = new Reservation(id, clientService.findById(client_id), vehicleService.findById(vehicle_id), beginning, ending);

            if (reservationService.valideReserv(reservation)
                && reservationService.valideSept(reservation)) { reservationService.modify(reservation); }

            request.setAttribute("allReservations", reservationService.findAll());
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/list.jsp").forward(request, response);
        } catch (ValideException e) {
            e.printStackTrace();
            request.setAttribute("reservation", reservation);

            try {
                request.setAttribute("allClients", clientService.findAll());
                request.setAttribute("allVehicles", vehicleService.findAll());
            } catch (ServiceException ex) { throw new ServletException(ex); }

            request.setAttribute("errorMessage", e.getMessage());
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/modify.jsp").forward(request, response);
        } catch (ServiceException e) {
            throw new ServletException(e);
        }
    }


    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
}