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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Chamod
 */
@WebServlet(name = "SaveContact", urlPatterns = {"/SaveContact"})
@MultipartConfig
public class SaveContact extends HttpServlet {

    private static final String UPLOAD_PATH = "contact_image";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonResponse = new JsonObject();
        
        
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String mobile = request.getParameter("mobile");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        Part filePart = request.getPart("profileImage");

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Criteria criteria = s.createCriteria(Contact.class);
        criteria.add(Restrictions.eq("mobile", mobile));
        if (!criteria.list().isEmpty()) {
            jsonResponse.addProperty("message", "Invalid Contact!");

        } else {

            String appPath = getServletContext().getRealPath("");
            String newPath = appPath.replace(
                    "build" + File.separator + "web",
                    "web" + File.separator + UPLOAD_PATH + File.separator + firstName
            );

            File uploadDir = new File(newPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_contact.png";
            File profile = new File(uploadDir, fileName);
            String relativePath = UPLOAD_PATH + "/" + firstName + "/" + fileName;
// Check if a file was actually uploaded before copying
            if (filePart != null && filePart.getSize() > 0) {
                Files.copy(filePart.getInputStream(), profile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            Contact c = new Contact();
            c.setFirstName(firstName);
            c.setLast_name(lastName);
            c.setMobile(mobile);
            c.setEmail(email);
            c.setAddress(address);
            c.setImage_path(relativePath);
            s.beginTransaction();
            s.save(c);
            s.getTransaction().commit();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            out.print("{\"success\": true}");
            out.flush();

        }

    }

}
