package com.octest.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.octest.dao.VilleDAO;
import com.octest.dao.VilleDAOImpl;

@WebServlet("/Ville")
@MultipartConfig
public class VillePage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	VilleDAO villeDao;
	String villeCode;

	public void init() throws ServletException {
		villeDao = new VilleDAOImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.villeCode = request.getParameter("ville");

		String[] infosVille = villeDao.getData(this.villeCode).get(0);
		
		String[] meteo = villeDao.getMeteo(infosVille[4], infosVille[5]);
		
		request.setAttribute("ville", infosVille);
		request.setAttribute("meteo", meteo);
		
		this.getServletContext().getRequestDispatcher("/WEB-INF/ville.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("boutonModifier")) {
            	
				String nomVille = request.getParameter("nomVille");
				String codePostal = request.getParameter("codePostal");
				String ligne5 = request.getParameter("ligne5");
				String latitude = request.getParameter("latitude");
				String longitude = request.getParameter("longitude");
				String codeCommunal = this.villeCode;
								
				this.villeDao.putData(nomVille, codeCommunal, codePostal, ligne5, latitude, longitude);
				response.sendRedirect(request.getContextPath() + "/Ville?ville=" + this.villeCode);
            }
            else if (action.equals("boutonInhiber")) {
            	this.villeDao.inhibData(this.villeCode);
            	response.sendRedirect(request.getContextPath() + "/Ville?ville=" + this.villeCode);
            }
            else if (action.equals("boutonDelete")) {
            	this.villeDao.deleteData(this.villeCode);
            	response.sendRedirect(request.getContextPath() + "/Primary");
            }
        }
		
	}
}