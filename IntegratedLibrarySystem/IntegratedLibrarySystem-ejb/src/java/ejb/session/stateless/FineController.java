/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FineEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.MemberHasFinesException;

/**
 *
 * @author hiixdayah
 */
@Stateless
@Local(FineControllerLocal.class)
@Remote(FineControllerRemote.class)
public class FineController implements FineControllerRemote, FineControllerLocal {

    @PersistenceContext(unitName = "librarydb2New-ejbPU")
    private EntityManager entityManager;

    

    
    public FineController()
    {
    }
    
    
    
    @Override 
    public void checkIfMemberHasFines(String identityNumber) throws MemberHasFinesException {
        Query query = entityManager.createQuery("SELECT f FROM FineEntity f WHERE f.memberEntity.identityNumber = :inIdentityNumber AND f.hasPaid = false ") ; 
        query.setParameter("inIdentityNumber", identityNumber) ; 
        
        if ( !query.getResultList().isEmpty() ) {
            throw new MemberHasFinesException("Member has unpaid fines and cannot borrow any books!");
        } 
    }
    
    @Override 
    public List<FineEntity> retrieveFinesByMember(String identityNumber) {
        Query query = entityManager.createQuery("SELECT f FROM FineEntity f WHERE f.identityNumber = :inIdentityNumber") ;
        query.setParameter("inIdentityNumber", identityNumber) ; 
        
        return query.getResultList() ;
        

    }
    
    @Override
    public FineEntity retrieveFineByFineId(Long fineId) {
        Query query = entityManager.createQuery("SELECT f FROM FineEntity f WHERE f.fineId = :inFineId") ;
        query.setParameter("inFineId", fineId) ; 
        
        return (FineEntity) query.getSingleResult() ;
        
    }
    
    
    
    @Override 
    public void payFine(Long fineId) {
        
    }

    public void persist(Object object) {
        entityManager.persist(object);
    }

    @Override
    public FineEntity createFine(FineEntity newFineEntity) {
        entityManager.persist(newFineEntity);
        entityManager.flush();
        
        return newFineEntity;
    }
    
}