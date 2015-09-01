import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import postTools.DBUtil;
import model.Product;

/**
 * Servlet implementation class AddComment
 */
@WebServlet("/AddToCart")
public class AddToCart extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddToCart() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		long productid = Long.parseLong(request.getParameter("productid"));
		int qty=Integer.parseInt(request.getParameter("quantity"));
		List<Product> cartList;
		int[][] prod_qty = new int[100][1];
		if (session.getAttribute("cartList") == null) {
			cartList = new ArrayList();
		} else {
			cartList = (List<Product>) session.getAttribute("cartList");
			prod_qty=(int[][]) session.getAttribute("prod_qty");
		}
		Product prod = new Product();

		EntityManager em = DBUtil.getEmFactory().createEntityManager();
		String qString = "select p from Product p where p.id = ?1";
		TypedQuery<Product> q = em.createQuery(qString, Product.class);
		q.setParameter(1, productid);
		Boolean hasProd=false;
		try {
			prod = q.getSingleResult();
			for(int i=0;i<cartList.size();i++){
				if(cartList.get(i).getId()==productid){
					hasProd=true;
				}else{
					hasProd=false;
				}
			}
			if(hasProd==true){
				//cartList.add(prod);
				prod_qty[(int) productid][0]+=qty;
				System.out.println(prod_qty[(int) productid][0]);
			}else{
				cartList.add(prod);
				prod_qty[(int) productid][0]+=qty;
				System.out.println(prod_qty[(int) productid][0]);
			}
			
			String alert = "Added to cart!";
			request.setAttribute("alert", alert);
			session.setAttribute("cartList", cartList);
			session.setAttribute("prod_qty", prod_qty);
			getServletContext().getRequestDispatcher("/successful.jsp")
					.forward(request, response);
		} catch (Exception e) {

		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}