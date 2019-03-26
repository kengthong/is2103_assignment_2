/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MemberEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Stateless
public class MemberEntityController implements MemberEntityControllerRemote, MemberEntityControllerLocal {

    @PersistenceContext(unitName = "librarydb2-ejbPU")
    private EntityManager em;

    
    
    @Override
    public MemberEntity doMemberLogin(String username, String password) throws InvalidLoginException {
        try
        {
            MemberEntity memberEntity = retrieveMemberByUsername(username);
            
            if(memberEntity.getPassword().equals(password))
            {
                return memberEntity;
            }
            else
            {
                throw new InvalidLoginException("Username does not exist or invalid password!");
            }
        }
        catch(MemberNotFoundException ex)
        {
            throw new InvalidLoginException("Username does not exist or invalid password!");
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public MemberEntity retrieveMemberByUsername(String username) throws MemberNotFoundException {
        Query query = em.createQuery("SELECT m FROM MemberEntity m WHERE m.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (MemberEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new MemberNotFoundException("Staff Username " + username + " does not exist!");
        }
    }
    
}
