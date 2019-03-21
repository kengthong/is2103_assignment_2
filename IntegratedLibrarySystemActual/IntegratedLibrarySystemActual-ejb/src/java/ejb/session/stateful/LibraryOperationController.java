/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import dao.BookEntityManager;
import dao.LendingEntityManager;
import dao.MemberEntityManager;
import dao.StaffEntityManager;
import entity.BookEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import entity.StaffEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.BookNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.MemberNotFoundException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author Hidayah
 */
@Stateful
@Local(LibraryOperationControllerLocal.class)
@Remote(LibraryOperationControllerRemote.class)

public class LibraryOperationController implements LibraryOperationControllerRemote, LibraryOperationControllerLocal {
    
    @PersistenceContext(unitName = "IntegratedLibrarySystemActual-ejbPU")
    private EntityManager entityManager;
        
    StaffEntityManager staffEntityManager = new StaffEntityManager();
    BookEntityManager bookEntityManager = new BookEntityManager();
    MemberEntityManager memberEntityManager = new MemberEntityManager();
    LendingEntityManager lendingEntityManager = new LendingEntityManager();
    LibraryOperationControllerRemote libraryOperationControllerRemote;   

   
    
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
        
        staffEntityManager.createStaff(newStaffEntity, entityManager) ;
        
        return newStaffEntity ; 
    }
    
    @Override
    public BookEntity createNewBook(BookEntity newBookEntity)  {
        
        bookEntityManager.createBook(newBookEntity, entityManager) ;
        
        return newBookEntity ; 
    }
    
    @Override
    public MemberEntity createNewMember(MemberEntity newMemberEntity)  {
        
        memberEntityManager.createBook(newMemberEntity, entityManager) ;
        
        return newMemberEntity ; 
    }
    
    @Override 
    public LendingEntity createNewLending(LendingEntity newLendingEntity) {
        lendingEntityManager.createLending(newLendingEntity, entityManager) ; 
        return newLendingEntity ; 
    }
    
    @Override 
    public StaffEntity retrieveStaffByUsername(String userName) throws StaffNotFoundException 
    { 
      return staffEntityManager.retrieveStaff(userName, entityManager) ; 
 
    }
    
    @Override 
    public MemberEntity retrieveMemberByIdentityNumber(String identityNumber) throws MemberNotFoundException {
        return memberEntityManager.retrieveMember(identityNumber, entityManager) ; 
    } 
    
    @Override 
    public BookEntity retrieveBookByBookId(Long id) throws BookNotFoundException {
        return bookEntityManager.retrieveBook(id, entityManager) ; 
    } 
 
    @Override
    public boolean checkLentBook(Long bookId) throws BookNotFoundException {
       return lendingEntityManager.isBookLent(bookId, entityManager) ;
   
    }
    
   
    
}


       
