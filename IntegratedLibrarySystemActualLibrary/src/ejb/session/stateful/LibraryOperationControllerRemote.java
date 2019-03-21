/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import entity.BookEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import entity.StaffEntity;
import javax.ejb.Remote;
import util.exception.BookNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author Hidayah
 */

@Remote
public interface LibraryOperationControllerRemote {
    
   
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginException ; 
    public StaffEntity createNewStaff(StaffEntity newStaffEntity)  ;
    public StaffEntity retrieveStaffByUsername(String userName) throws StaffNotFoundException ; 
    public BookEntity createNewBook(BookEntity newBookEntity) ;
    public MemberEntity createNewMember(MemberEntity newMemberEntity) ;
    public MemberEntity retrieveMemberByIdentityNumber(String identityNumber) throws MemberNotFoundException ; 
    public BookEntity retrieveBookByBookId(Long id) throws BookNotFoundException ;
    public boolean checkLentBook(Long bookId) throws BookNotFoundException ; 
    public LendingEntity createNewLending(LendingEntity newLendingEntity) ; 
   








}
