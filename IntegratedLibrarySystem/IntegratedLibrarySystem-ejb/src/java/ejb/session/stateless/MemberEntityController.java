/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MemberEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.MemberNotFoundException;


/**
 *
 * @author hiixdayah
 */

@Stateless
@Local(MemberEntityControllerLocal.class)
@Remote(MemberEntityControllerRemote.class)
public class MemberEntityController implements MemberEntityControllerRemote, MemberEntityControllerLocal {

    @PersistenceContext(unitName = "librarydb2-ejbPU")
    private javax.persistence.EntityManager entityManager;
    
    public MemberEntityController()
    {
    }
    
    @Override
    public MemberEntity createNewMember(MemberEntity newMemberEntity)  {
        entityManager.persist(newMemberEntity);
        entityManager.flush();
        
        return newMemberEntity;
    }
    
    @Override
    public MemberEntity retrieveMemberByIdentityNumber(String identityNumber) throws MemberNotFoundException {
        
        Query query = entityManager.createQuery("SELECT m FROM MemberEntity m WHERE m.identityNumber = :inIdentityNumber");
        query.setParameter("inIdentityNumber", identityNumber);
        
        try
        {
            return (MemberEntity)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
             throw new MemberNotFoundException("Member Identity Number  " + identityNumber + " does not exist!");

        }
    }
    
    @Override
    public MemberEntity retrieveMemberByMemberId(Long memberId) throws MemberNotFoundException
    {
        MemberEntity memberEntity = entityManager.find(MemberEntity.class, memberId);
        
        if(memberEntity != null)
        {
            return memberEntity;
        }
        else
        {
            throw new MemberNotFoundException("Member ID " + memberId + " does not exist!");
        }
    }
    
    @Override
    public void updateMember(MemberEntity memberEntity) {
        entityManager.merge(memberEntity);
    }
    
    @Override
    public void deleteMember(Long memberId) throws MemberNotFoundException
    {
        MemberEntity memberEntityToRemove = retrieveMemberByMemberId(memberId);
        entityManager.remove(memberEntityToRemove);
    }
    
    
}
