/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginException;
import util.exception.StaffNotFoundException;


/**
 *
 * @author hiixdayah
 */

@Stateless
@Local(StaffEntityControllerLocal.class)
@Remote(StaffEntityControllerRemote.class)
public class StaffEntityController implements StaffEntityControllerRemote, StaffEntityControllerLocal {

    @PersistenceContext(unitName = "librarydb2-ejbPU")
    private javax.persistence.EntityManager entityManager;
    
    public StaffEntityController()
    {
    }
    
    
    
        
    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginException
    {
        try
        {
            StaffEntity staffEntity = retrieveStaffByUsername(username);
            
            if(staffEntity.getPassword().equals(password))
            {
                return staffEntity;
            }
            else
            {
                throw new InvalidLoginException("Username does not exist or invalid password!");
            }
        }
        catch(StaffNotFoundException ex)
        {
            throw new InvalidLoginException("Username does not exist or invalid password!");
        }
    }
    
    @Override
    public StaffEntity createNewStaff(StaffEntity newStaffEntity)  {
        entityManager.persist(newStaffEntity);
        entityManager.flush();
        
        return newStaffEntity;
    }
    
    @Override 
    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException {
        StaffEntity staffEntity = entityManager.find(StaffEntity.class, username) ; 
        if (staffEntity != null) {
            return staffEntity ; 
        } else {
            throw new StaffNotFoundException("Staff Username" + username + "does not exist!") ; 
        }
    }
    
    @Override
    public StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException
    {
        StaffEntity staffEntity = entityManager.find(StaffEntity.class, staffId);
        
        if(staffEntity != null)
        {
            return staffEntity;
        }
        else
        {
            throw new StaffNotFoundException("Staff ID " + staffId + " does not exist!");
        }
    }
    
    @Override
    public void updateStaff(StaffEntity staffEntity) {
        entityManager.merge(staffEntity);
    }
    
    @Override
    public void deleteStaff(Long staffId) throws StaffNotFoundException
    {
        StaffEntity staffEntityToRemove = retrieveStaffByStaffId(staffId);
        entityManager.remove(staffEntityToRemove);
    }
    
    @Override
    public List<StaffEntity> retrieveAllStaff()
    {
        Query query = entityManager.createQuery("SELECT s FROM StaffEntity s");
        
        return query.getResultList();
    }
    
    
}
