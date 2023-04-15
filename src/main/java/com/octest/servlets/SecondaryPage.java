package com.octest.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.octest.dao.VilleDAO;
import com.octest.dao.VilleDAOImpl;

@WebServlet("/Secondary")
@MultipartConfig
public class SecondaryPage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    VilleDAO villeDao;

    public void init() throws ServletException {
        villeDao = new VilleDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	List<String[]> allData = villeDao.getAllData();
    	
        request.setAttribute("allData", allData);
        this.getServletContext().getRequestDispatcher("/WEB-INF/secondaryJ.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codeCommune1 = request.getParameter("codeCommune1");
        String codeCommune2 = request.getParameter("codeCommune2");
                
        double distance = villeDao.getDistanceVille(codeCommune1, codeCommune2);
        request.setAttribute("distance", distance);
        doGet(request, response);
    }
}
