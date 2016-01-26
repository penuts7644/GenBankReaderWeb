/*
 * Copyright (c) 2015 Wout van Helvoirt [wout.van.helvoirt@gmail.com].
 * All rights reserved.
 */
package nl.bioinf.wvanhelvoirt.servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.bioinf.wvanhelvoirt.genbankreader.GenBankFeatures;
import nl.bioinf.wvanhelvoirt.genbankreader.GeneBankFileParser;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Wout van Helvoirt
 */
@WebServlet(name = "GenBankReaderUploadServlet", urlPatterns = {"/readgbk.do"})
public class GenBankReaderUploadServlet extends HttpServlet {

    /**
     * Set max request (upload file) size at 10 mb.
     */
    private static final int MAX_REQUEST_SIZE = 1024 * 1024; // 10 MB

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

        /* Check if there is a file upload request. If not, return to index with an error message. */
        if (!ServletFileUpload.isMultipartContent(request)) {
            request.setAttribute("error", "Please do not alter the HTML code.");
            getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
        }

        /* Create a factory for the disk-based file items. Sets the directory used to temporarily store files that are
           larger than the configured size threshold. We use temporary directory for java */
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        /* Create a new file upload handler and set an overall request size. */
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        /* Try to parse the request. */
        try {
            List<FileItem> items = upload.parseRequest(request);
            FileItem inputFile = items.get(0);

            /* If there is an open session, close it and create a new one. 10 minutes web xml */
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            session = request.getSession(true);
            GenBankFeatures sessionParsedGBK = (GenBankFeatures) session.getAttribute("gbkObj");

            /* If no session object, create temp file and use temp file for creating reader opbject.
               Else continue to page */
            if (sessionParsedGBK == null) {

                try {
                    File tempInput = File.createTempFile("upload", inputFile.getName());
                    inputFile.write(tempInput.getAbsoluteFile());

                    try {
                        GeneBankFileParser infile = new GeneBankFileParser(tempInput.getAbsolutePath());
                        GenBankFeatures gbkObj = infile.ParseGenBankContent();

                        /* Delete temp file when reader object created. */
                        tempInput.delete();

                        session.setAttribute("gbkObj", gbkObj);
                        session.setAttribute("inputFile", inputFile.getName());
                        getServletContext().getRequestDispatcher("/jsp/content.jsp").forward(request, response);

                    /* Catch any ocurring errors from file validation. */
                    } catch (FileNotFoundException e) {
                        tempInput.delete();
                        request.setAttribute("error", e.getMessage());
                        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
                    }

                /* Catch any ocurring errors from temp file writing. */
                } catch (Exception e) {
                    request.setAttribute("error", e.getMessage());
                    getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
                }
            } else {
                getServletContext().getRequestDispatcher("/jsp/content.jsp").forward(request, response);
            }

        /* Catch any ocurring errors from file upload. */
        } catch (FileUploadException e) {
            request.setAttribute("error", e.getMessage());
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

        /* When entering an url in the adress bar of the browser, get session object. */
        HttpSession session = request.getSession();
        GenBankFeatures sessionParsedGBK = (GenBankFeatures) session.getAttribute("gbkObj");

        /* If no session found, remove all sessions and redirect to home. */
        if (sessionParsedGBK == null) {
            session.invalidate();
            getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
        } else {
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
        return "This Servlet uploads the GenBank file and creates a GenBankFeatures Object "
                + "for the Option servlet to use.";
    }// </editor-fold>

}
