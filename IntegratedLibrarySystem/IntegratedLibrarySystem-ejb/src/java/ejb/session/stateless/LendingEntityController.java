/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookEntity;
import entity.LendingEntity;
import entity.ReservationEntity;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.LendingNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Stateless
@Local(LendingEntityControllerLocal.class)
@Remote(LendingEntityControllerRemote.class)
public class LendingEntityController implements LendingEntityControllerRemote, LendingEntityControllerLocal {

    @PersistenceContext(unitName = "librarydb2New-ejbPU")
    private EntityManager entityManager;

    
    
    public LendingEntityController() {
    }

    @Override
    public boolean checkIsBookLent(Long bookId) { //return true if book is not available 
        Query query = entityManager.createQuery("SELECT l FROM LendingEntity l WHERE l.book.bookId = :inBookId AND l.hasReturned = false "); //book has not been returned 
        query.setParameter("inBookId", bookId);

        if (query.getResultList().isEmpty()) {
            return false; //book is available  
        } else {
            return true;
        }
    }
    
    
    @Override
    public boolean checkForReservations(Long bookId) { //do we remove the member from the list after they have borrowed 
        System.out.println("Here");
        return true;
//        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r WHERE r.book.bookId = :inBookId") ; 
//        query.setParameter("inBookId", bookId) ; 
//        
//        if (query.getResultList().isEmpty()) {
//            return false ; //there are no reservations 
//        } else {
//            return true ; 
//        }
    }
    
    

    @Override
    public int checkNumBooksLoaned(String identityNumber) {
        Query query = entityManager.createQuery("SELECT l FROM LendingEntity l WHERE l.memberEntity.identityNumber = :inIdentityNumber AND l.hasReturned = false"); 
        query.setParameter("inIdentityNumber", identityNumber);
        return query.getResultList().size();
    }
    
    @Override
    public boolean checkIfMemberOnReserveList(String identityNumber) {
        Query query = entityManager.createQuery("SELECT b FROM BookEntity b WHERE b.reservations.identityNumber = :inIdentityNumber AND  ") ; 
        query.setParameter("inIdentityNumber", identityNumber) ; 
        
        BookEntity book = (BookEntity)query.getResultList() ; 
        List<ReservationEntity> reserveList = book.getReservations() ;
        if (book.getReservations().get(0).getMember().getIdentityNumber().equals(identityNumber) )  {
            return true ; //member is at the top of reservation list 
        } else {
            return false ; 
        }
        
    } 

    @Override
    public Date generateDueDate(Date date) {
        Calendar duedate = Calendar.getInstance();
        duedate.setTime(date);
        duedate.add(Calendar.DATE, 14);
        return duedate.getTime();
//        String returnDate = duedate.get(Calendar.YEAR) + "-" + duedate.get(Calendar.MONTH) + "-" + duedate.get(Calendar.DATE) ; 
    }

    @Override
    public List<LendingEntity> retrieveBooksLoanedByMember(String identityNumber) { //need to iterate through the list 
//        try {
//            Query query = entityManager.createQuery("SELECT l FROM LendingEntity l WHERE l.member.identityNumber = :inIdentityNumber ORDER BY l.book.bookId ASC");
//            query.setParameter("inIdentityNumber", identityNumber);
//        } catch (//No result)
//                 {
//            //return empty list
//            return new List<LendingEntity>();
//        }
//        return query.getResultList();
        
        return null;
    }

    @Override
    public LendingEntity retrieveLendingByBookId(Long bookId) {
        Query query = entityManager.createQuery("SELECT l FROM LendingEntity WHERE l.book.bookId = :inBookId");
        query.setParameter("inBookId", bookId);
        return (LendingEntity) query.getSingleResult();
    }

    @Override
    public void setBookAvailable(String identityNumber, Long returnBookId) {
        Query query = entityManager.createQuery("SELECT l FROM LendingEntity l WHERE l.member.identityNumber = :inIdentityNumber AND l.book.bookId = :inBookId"); //AND l.hasReturned == false
        query.setParameter("inIdentityNumber", identityNumber);
        query.setParameter("inBookId", returnBookId);
        LendingEntity lendingEntity = (LendingEntity) query.getSingleResult();
        lendingEntity.setHasReturned(true);
        updateLendingEntity(lendingEntity);
    }

    @Override
    public LendingEntity createNewLending(LendingEntity newLendingEntity) {
        System.out.println("here at create new lending");
        try
        {
            entityManager.persist(newLendingEntity);
            entityManager.flush();
        }
        catch(Exception ex)
        {
            System.out.println("Exception =" + ex.getMessage());
        }

        return newLendingEntity;
    }

    @Override
    public LendingEntity retrieveLendingByLendingId(Long lendId) throws LendingNotFoundException {
        LendingEntity lendingEntity = entityManager.find(LendingEntity.class, lendId);
        if (lendingEntity != null) {
            return lendingEntity;
        } else {
            throw new LendingNotFoundException("Lending with lendingId " + lendId + "does not exist!");
        }
    }

    @Override
    public void updateLendingEntity(LendingEntity lendingEntity) {
        entityManager.merge(lendingEntity);
    }

    @Override
    public void deleteLendingEntity(Long lendId) throws LendingNotFoundException {

        LendingEntity lendingEntityToRemove = retrieveLendingByLendingId(lendId);
        entityManager.remove(lendingEntityToRemove);
    }

    public void persist(Object object) {
        entityManager.persist(object);
    }

    @Override
    public boolean test(Long bookId) {
        System.out.println("Test " + bookId);
        return true;
    }




}
