/*
 * Copyright (c) 2015 Wout van Helvoirt [wout.van.helvoirt@gmail.com].
 * All rights reserved.
 */
package nl.bioinf.wvanhelvoirt.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.bioinf.wvanhelvoirt.genbankreader.GenBankFeatures;

/**
 *
 * @author Wout van Helvoirt
 */
@WebServlet(name = "GenBankReaderOptionServlet", urlPatterns = {"/searchgbk.do"})
public class GenBankReaderOptionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        /* Create empty ouput array. */
        ArrayList<String> outputMessage = new ArrayList<String>();

        /* Get the session and attributes. */
        HttpSession session = request.getSession();
        GenBankFeatures sessionParsedGBK = (GenBankFeatures) session.getAttribute("gbkObj");
        String inputFile = (String) session.getAttribute("inputFile");

        /* If there is a session, get user selected option and search GenBank file. */
        if (sessionParsedGBK != null) {
            switch (request.getParameter("selectedOption")) {
                case "Summary": outputMessage = sessionParsedGBK.getSummary(inputFile); break;
                case "Fetch Gene(s)": outputMessage = sessionParsedGBK.fetchGene(request.getParameter("optionInputValue")); break;
                case "Fetch CDS(s)": outputMessage = sessionParsedGBK.fetchCds(request.getParameter("optionInputValue")); break;
                case "Fetch Features": outputMessage = sessionParsedGBK.fetchFeatures(request.getParameter("optionInputValue")); break;
                case "Find Site(s)": outputMessage = sessionParsedGBK.findSites(request.getParameter("optionInputValue")); break;
                default: outputMessage.add("The selected option is not yet available."); break;
            }

            /* Set attributes and show results, else session timeout, return to home. */
            session.setAttribute("selectedOption", request.getParameter("selectedOption"));
            session.setAttribute("optionInputValue", request.getParameter("optionInputValue"));
            session.setAttribute("message", outputMessage);
            getServletContext().getRequestDispatcher("/jsp/content.jsp").forward(request, response);
        } else {
            session.invalidate();
            request.setAttribute("error", "Sorry, but your session has been expired. Don't worry, you can upload another GenBank file if you like.");
            getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

        /* When entering an url in the adress bar of the browser, get session object and strings. */
        HttpSession session = request.getSession();
        GenBankFeatures sessionParsedGBK = (GenBankFeatures) session.getAttribute("gbkObj");
        String inputFile = (String) session.getAttribute("inputFile");
        String selectedOption = (String) session.getAttribute("selectedOption");
        String optionInputValue = (String) session.getAttribute("optionInputValue");

        /* If no session gbk object, remove all other session items and return to home. */
        if (sessionParsedGBK == null) {
            session.invalidate();
            getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
        } else {

            /* Else, get info from session object and go to result page. */
            ArrayList<String> outputMessage = new ArrayList<String>();
            switch (selectedOption) {
                case "Summary": outputMessage = sessionParsedGBK.getSummary(inputFile); break;
                case "Fetch Gene(s)": outputMessage = sessionParsedGBK.fetchGene(optionInputValue); break;
                case "Fetch CDS(s)": outputMessage = sessionParsedGBK.fetchCds(optionInputValue); break;
                case "Fetch Features": outputMessage = sessionParsedGBK.fetchFeatures(optionInputValue); break;
                case "Find Site(s)": outputMessage = sessionParsedGBK.findSites(optionInputValue); break;
                default: outputMessage.add("Have you been sniffing around in the html code?"); break;
            }

            request.setAttribute("message", outputMessage);
            getServletContext().getRequestDispatcher("/jsp/content.jsp").forward(request, response);
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This Servlet checks if there is a GenBankFeatures Object available "
                + "and uses it to get the correct info based on the users choice.";
    }// </editor-fold>

}
