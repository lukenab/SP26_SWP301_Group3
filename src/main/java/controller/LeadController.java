package controller;


import dao.LeadDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Lead;

/**
 *
 * @author LienNTK
 */

@WebServlet(name = "LeadController", urlPatterns = {"/lead"})

public class LeadController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         String action = request.getParameter("action") != null ? request.getParameter("action") : "";
        switch (action) {
//            case "add":
//                doGetAdd(request, response);
//                break;

//            case "edit":
//                doGetUpdate(request, response);
//                break;

//                 case "delete":
//                     doGetDelete(request,response);
//                     break;

            default:
                doGetRead(request, response);
                break;

        }
    }
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         String action = request.getParameter("action") != null ? request.getParameter("action") : "";
//
        switch (action) {
//            case "add":
//                doPostAdd(request, response);
//                break;
////                
////                
//            case "edit":
//                doPostUpdate(request, response);
//                break;
                 
//                 case "delete":
//                     doPostDelete(request, response);
//                     break;
                
                 
        }
    }

   



    

    private void doGetRead(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LeadDAO leadDAO = new LeadDAO();
        String status = request.getParameter("status");
        List<Lead> lead = leadDAO.getAllLeads(status);

        request.setAttribute("lead", lead);
        request.getRequestDispatcher("sales/lead-dashboard.jsp").forward(request, response);
    }

    
    
    
}
