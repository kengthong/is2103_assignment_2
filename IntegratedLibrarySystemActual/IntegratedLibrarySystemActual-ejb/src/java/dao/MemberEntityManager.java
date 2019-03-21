package dao;

import entity.MemberEntity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import util.exception.MemberNotFoundException;

/**
 *
 * @author Hidayah
 */
public class MemberEntityManager {

    
    public MemberEntityManager() {
    }
    
    public MemberEntity createBook(MemberEntity newMemberEntity, EntityManager entityManager) {
        entityManager.persist(newMemberEntity) ;
        entityManager.flush() ; 
        
        return newMemberEntity ; 
        
    }
    
    
    public MemberEntity retrieveMember(String identityNumber, EntityManager entityManager) throws MemberNotFoundException {
     
        Query query = entityManager.createQuery("SELECT m FROM MemberEntity m WHERE m.identityNumber = :inidentityNumber");
        query.setParameter("inidentityNumber", identityNumber);

        
        try
        {
             System.out.println("Member has already been registered!") ; 
             return (MemberEntity) query.getSingleResult() ;
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new MemberNotFoundException("Member Identity Number  " + identityNumber + " does not exist!");
        }

    }
    
    }



