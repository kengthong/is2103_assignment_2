/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.BookEntityControllerLocal;
import ejb.session.stateless.MemberEntityControllerLocal;
import ejb.session.stateless.StaffEntityControllerLocal;
import entity.BookEntity;
import entity.MemberEntity;
import entity.StaffEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exception.StaffNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Singleton
@LocalBean
@Startup 
public class InitSessionBean {

    @EJB
    private BookEntityControllerLocal bookEntityControllerLocal;

    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;

    @EJB
    private MemberEntityControllerLocal memberEntityControllerLocal;
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public InitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {

                
        try {
//            staffEntityControllerLocal.retrieveStaffByUsername("manager");
            staffEntityControllerLocal.retrieveStaffByStaffId((long) 1);
        } catch (StaffNotFoundException ex) {
            initializeData();
        }
    }

    private void initializeData() {

        staffEntityControllerLocal.createNewStaff(new StaffEntity("Linda", "Chua", "manager", "password"));
        staffEntityControllerLocal.createNewStaff(new StaffEntity("Barbara", "Durham", "assistant", "password"));

        bookEntityControllerLocal.createNewBook(new BookEntity("The Lord of the Rings", "S18018", 1954));
        bookEntityControllerLocal.createNewBook(new BookEntity("Le Petit Prince", "S64921", 1943));
        bookEntityControllerLocal.createNewBook(new BookEntity("Harry Potter and the Philosopher's Stone", "S38101", 1997));
        bookEntityControllerLocal.createNewBook(new BookEntity("The Hobbit", "S19527", 1937));
        bookEntityControllerLocal.createNewBook(new BookEntity("And Then There Were None", "S63288", 1939));
        bookEntityControllerLocal.createNewBook(new BookEntity("Dream of the Red Chamber", "S32187", 1791));
        bookEntityControllerLocal.createNewBook(new BookEntity("The Lion, the Witch and the Wardrobe", "S74569", 1950));

        memberEntityControllerLocal.createNewMember(new MemberEntity("S7483027A" , "password", "Tony", "Teo", "Male", 44, "87297373", "11 Tampines Ave 3"));
        memberEntityControllerLocal.createNewMember(new MemberEntity("S8381028X" , "password2", "Wendy", "Tan", "Female", 35, "97502837", "15 Computing Dr"));
        memberEntityControllerLocal.createNewMember(new MemberEntity("test" , "pass", "firstname", "lastname", "Female", 22, "2222222", "15 222 Dr"));
    }
}