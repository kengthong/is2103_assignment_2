/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LendingEntity;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hiixdayah
 */
@Stateless
@Local(LendingEntityControllerLocal.class)
@Remote(LendingEntityControllerRemote.class)
public class LendingEntityController implements LendingEntityControllerRemote, LendingEntityControllerLocal {

    @PersistenceContext(unitName = "librarydb2-ejbPU")
    private javax.persistence.EntityManager entityManager;
    
    public LendingEntityController()
    {
    }
    
    public boolean checkIsBookLent(Long bookId) {
        Query query = entityManager.createQuery("SELECT l FROM LendingEntity l WHERE l.book.bookId = :inBookId") ; 
        query.setParameter("inBookId",bookId) ; 

        if ( query.getResultList().isEmpty() ) {
            return false ;
        } else {
            return true ;
        } 
    }
    
    public int checkNumBooksLoaned(String identityNumber) {
        Query query = entityManager.createQuery("SELECT l FROM LendingEntity l WHERE l.member.identityNumber = :inIdentityNumber") ;
        query.setParameter("inIdentityNumber", identityNumber) ; 
        return query.getResultList().size() ; 
    }
    
    public String generateDueDate() {
        Calendar duedate = Calendar.getInstance() ; 
        duedate.add(Calendar.DATE, 14) ;
        String returnDate = duedate.get(Calendar.YEAR) + "-" + duedate.get(Calendar.MONTH) + "-" + duedate.get(Calendar.DATE) ; 
        
        return returnDate ; 
             
    }
    
    public List<LendingEntity> retrieveBooksLoanedByMember(String identityNumber) {
        Query query = entityManager.createQuery("SELECT l FROM LendingEntity l WHERE l.member.identityNumber = :inIdentityNumber ORDER BY l.book.bookId ASC") ; 
        query.setParameter("inIdentityNumber", identityNumber) ; 
        return query.getResultList() ; 
    }
    
    public void setBookAvailable(String identityNumber, Long returnBookId) {
        Query query = entityManager.createQuery("SELECT l FROM LendingEntity l WHERE l.member.identityNumber = :inIdentityNumber AND l.book.bookId = :inBookId") ;  
        query.setParameter("inIdentityNumber", identityNumber) ; 
        query.setParameter("inBookId", returnBookId) ;
        query.getSingleResult().setStatus(true) ; 
    }
    
    public void extendDueDate(String identityNumber, Long extendBookId) {
        Query query = entityManager.createQuery("SELECT l FROM LendingEntity l WHERE l.member.identityNumber = :inIdentityNumber AND l.book.bookId = :inBookId") ;  
            query.setParameter("inIdentityNumber", identityNumber) ;
            query.setParameter("inBookId", extendBookId) ;
            query.getSingleResult().; 
            
    }
    

    



            

    
     
}
