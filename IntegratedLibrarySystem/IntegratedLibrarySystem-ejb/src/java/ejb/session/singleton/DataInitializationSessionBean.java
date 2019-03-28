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
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.StaffNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean  {
    
        @PersistenceContext(unitName = "librarydb2-ejbPU") 
        private EntityManager entityManager;
        
        @EJB
        private StaffEntityControllerLocal staffEntityControllerLocal ; 
        
        @EJB 
        private BookEntityControllerLocal bookEntityControllerLocal ; 
        
        @EJB 
        private MemberEntityControllerLocal memberEntityControllerLocal ; 
        
            
    public DataInitializationSessionBean() {       
    }
    
    @PostConstruct
    public void postConstruct() {
        try {
            staffEntityControllerLocal.retrieveStaffByUsername("manager") ; 
        } catch (StaffNotFoundException ex) {
            initializeData() ; 
        }
    }
    
    private void initializeData() {
        
        staffEntityControllerLocal.createNewStaff(new StaffEntity("Linda", "Chua", "manager", "password")) ; 
        staffEntityControllerLocal.createNewStaff(new StaffEntity("Barbara", "Durham", "assistant", "password")) ; 
        
        bookEntityControllerLocal.createNewBook(new BookEntity("The Lord of the Rings", "S18018", 1954)) ; 
        bookEntityControllerLocal.createNewBook(new BookEntity("Le Petit Prince", "S64921", 1943)) ; 
        bookEntityControllerLocal.createNewBook(new BookEntity("Harry Potter and the Philosopher's Stone", "S38101", 1997)) ; 
        bookEntityControllerLocal.createNewBook(new BookEntity("The Hobbit", "S19527", 1937)) ; 
        bookEntityControllerLocal.createNewBook(new BookEntity("And Then There Were None", "S63288", 1939)) ; 
        bookEntityControllerLocal.createNewBook(new BookEntity("Dream of the Red Chamber", "S32187", 1791)) ; 
        bookEntityControllerLocal.createNewBook(new BookEntity("The Lion, the Witch and the Wardrobe", "S74569", 1950)) ; 
        
        memberEntityControllerLocal.createNewMember(new MemberEntity("Tony", "Teo", "Male", 44, "S7483027A", "87297373", "11 Tampines Ave 3")) ;
        memberEntityControllerLocal.createNewMember(new MemberEntity("Wendy", "Tan", "Female", 35, "S8381028X", "97502837", "15 Computing Dr")) ;
    }
    
    

    

}
