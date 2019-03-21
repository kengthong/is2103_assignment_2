package dao;

import entity.LendingEntity;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Hidayah
 */
public class LendingEntityManager {
    
    
    public LendingEntityManager() {  
    }   
    
    public LendingEntity createLending(LendingEntity newLendingEntity, EntityManager entityManager) {
        entityManager.persist(newLendingEntity) ;
        entityManager.flush() ; 
        
        return newLendingEntity ; 
        
    }
    
    public boolean isBookLent(Long bookId, EntityManager entityManager) {
        
        Query query = entityManager.createQuery("SELECT l FROM LendingEntity l WHERE l.book.bookId = :inBookId") ; 
        query.setParameter("inBookId",bookId) ; 

        if ( query.getResultList().isEmpty() ) {
            return false ;
        } else {
            return true ;
        }
        
    }
        
  
    
}
