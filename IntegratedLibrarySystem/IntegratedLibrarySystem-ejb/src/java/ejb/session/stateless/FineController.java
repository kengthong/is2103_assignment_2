/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hiixdayah
 */
@Stateless
@Local(FineControllerLocal.class)
@Remote(FineControllerRemote.class)
public class FineController implements FineControllerRemote, FineControllerLocal {


    @PersistenceContext(unitName = "librarydb2-ejbPU")
    private javax.persistence.EntityManager entityManager;
    
    public FineController()
    {
    }
    
    
    public boolean checkForFines(String identityNumber) {
        Query query = entityManager.createQuery("SELECT f FROM FineEntity f WHERE f.identityNumber = :inIdentityNumber AND f.paid == false ") ; 
        query.setParameter("inIdentityNumber", identityNumber) ; 
        
        if ( query.getResultList().isEmpty() ) {
            return false ;
        } else {
            return true ;
        }        
        
    }
    

    
}
