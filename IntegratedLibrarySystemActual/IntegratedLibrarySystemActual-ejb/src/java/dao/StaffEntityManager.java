package dao;

import entity.StaffEntity;
import javax.persistence.EntityManager;
import util.exception.StaffNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;


/**
 *
 * @author Hidayah
 */
public class StaffEntityManager {
    
    
    public StaffEntityManager() {
    }
    
    public StaffEntity createStaff(StaffEntity newStaffEntity, EntityManager entityManager) {
        entityManager.persist(newStaffEntity) ;
        entityManager.flush() ; 
        
        return newStaffEntity ; 
        
    }
    
   
    public StaffEntity retrieveStaff(String userName, EntityManager entityManager) throws StaffNotFoundException
    {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s WHERE s.userName = :inUsername");
        query.setParameter("inUsername", userName);
        
        try
        {
            return (StaffEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new StaffNotFoundException("Staff Username " + userName + " does not exist!");
        }
    }

}
