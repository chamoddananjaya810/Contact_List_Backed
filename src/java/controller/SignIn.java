/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Contact;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Chamod
 */
@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject ContactJson = gson.fromJson(request.getReader(), JsonObject.class);

        JsonObject jsonResponse = new JsonObject();

        String email = ContactJson.get("email").getAsString();
        String password = ContactJson.get("password").getAsString();
        System.out.println(email);
        System.out.println(password);

        if (email.isEmpty()) {

            jsonResponse.addProperty("message", "email  can not be empty !");

        } else if (!Util.isEmailValid(email)) {
            jsonResponse.addProperty("message", "please enter the valid email !");

        } else if (password.isEmpty()) {
            jsonResponse.addProperty("message", "password  can not be empty !");
        } else if (!Util.isPasswordValid(password)) {
            jsonResponse.addProperty("message", "please enter the valid password!");
        } else {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria criteria = s.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", email));
            criteria.add(Restrictions.eq("password", password));

            if (criteria.list().isEmpty()) {
                jsonResponse.addProperty("message", " Invalid Credetials!");

            } else {
                jsonResponse.addProperty("success", true);
            }
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(jsonResponse));
    }

}
