/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MemberEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Local
public interface MemberEntityControllerLocal {

    MemberEntity doMemberLogin(String username, String password) throws InvalidLoginException;

    
    public MemberEntity createNewMember(MemberEntity newMemberEntity) ; 
    public MemberEntity retrieveMemberByIdentityNumber(String identityNumber) throws MemberNotFoundException ; 
    public void updateMember(MemberEntity memberEntity) ; 
    public MemberEntity retrieveMemberByMemberId(Long memberId) throws MemberNotFoundException ; 
    public void deleteMember(Long memberId) throws MemberNotFoundException ; 
    public List<MemberEntity> retrieveAllMembers() ; 





    
}
