package com.octest.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.octest.dao.DaoFactory;
import com.octest.dao.VilleDAO;

@WebServlet("/Primary")
@MultipartConfig
public class PrimaryPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	VilleDAO villeDao;

	public void init() throws ServletException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		villeDao = daoFactory.getVilleDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int page = 1;
		String pageParam = request.getParameter("page");
		if (pageParam != null && !pageParam.isEmpty()) {
			page = Integer.parseInt(pageParam);
		}

		ArrayList<String[]> allData = villeDao.getAllData();

		int totalPages = allData.size() / 50;
		if (totalPages % 50 != 0) {
			totalPages++;
		}

		if (page > totalPages)
			page = totalPages;
		else if (page < 1)
			page = 1;

		int startIdx = (page - 1) * 50;
		int endIdx = Math.min(startIdx + 50, allData.size());
		List<String[]> dataPage = allData.subList(startIdx, endIdx);

		request.setAttribute("page", page);
		request.setAttribute("dataPage", dataPage);
		request.setAttribute("totalPages", totalPages);

		this.getServletContext().getRequestDispatcher("/WEB-INF/primaryJ.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/WEB-INF/primaryJ.jsp").forward(request, response);
	}
}