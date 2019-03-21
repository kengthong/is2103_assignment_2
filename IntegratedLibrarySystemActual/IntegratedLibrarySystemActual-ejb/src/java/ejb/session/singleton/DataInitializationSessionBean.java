/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateful.LibraryOperationControllerLocal;
import entity.BookEntity;
import entity.MemberEntity;
import entity.StaffEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.StaffNotFoundException;

/**
 *
 * @author Hidayah
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {   
    
    @PersistenceContext(unitName = "IntegratedLibrarySystemActual-ejbPU")
    private EntityManager entityManager;
    
    @EJB
    private LibraryOperationControllerLocal libraryOperationControllerLocal;
    
    
    public DataInitializationSessionBean() {       
    }
    
    @PostConstruct
    public void postConstruct() {
        try {

           libraryOperationControllerLocal.retrieveStaffByUsername("linda") ; 
        }
        catch (StaffNotFoundException ex) {
            initializeData() ; 
        }
    }
    
    
    private void initializeData() {
   
        libraryOperationControllerLocal.createNewStaff(new StaffEntity("Linda", "Chua", "linda", "password")) ;
        libraryOperationControllerLocal.createNewStaff(new StaffEntity("Barbara", "Darham", "barbara", "password")) ;
        
        libraryOperationControllerLocal.createNewBook( new BookEntity("The Lord of the Rings", "S18018", 1954)) ;
        libraryOperationControllerLocal.createNewBook( new BookEntity("Le Petit Prince", "S64921", 1943)) ;
        libraryOperationControllerLocal.createNewBook( new BookEntity("Harry Potter and the Philosopher's Stone", "S38101", 1997)) ;
        libraryOperationControllerLocal.createNewBook( new BookEntity("The Hobbit", "S19527", 1937)) ;
        libraryOperationControllerLocal.createNewBook( new BookEntity("And Then There Were None", "S18018", 1939)) ;
        libraryOperationControllerLocal.createNewBook( new BookEntity("Dream of the Red Chamber", "S32187", 1791)) ;
        libraryOperationControllerLocal.createNewBook( new BookEntity("The Lion, the Witch and the Wardrobe", "S74569", 1950)) ;
       
        
        libraryOperationControllerLocal.createNewMember( new MemberEntity("Tony", "Teo", "Male", 44, "S7483027A", "87297373", "11 Tampines Ave 3")) ;
        libraryOperationControllerLocal.createNewMember( new MemberEntity("Wendy", "Tan", "Female", 35, "S8381028X", "97502837", "15 Computing Dr")) ;
       
         
    }

    }
