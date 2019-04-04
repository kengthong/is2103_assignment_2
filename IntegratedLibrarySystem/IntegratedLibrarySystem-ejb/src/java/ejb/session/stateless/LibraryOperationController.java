/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import entity.ReservationEntity;
import entity.StaffEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import util.exception.BookIsOnLoanException;
import util.exception.BookNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.MaxLoansExceeded;
import util.exception.MemberHasFinesException;
import util.exception.MemberNotAtTopOfReserveList;
import util.exception.MemberNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Local(LibraryOperationControllerLocal.class)
@Remote(LibraryOperationControllerRemote.class)
@Stateless
public class LibraryOperationController implements LibraryOperationControllerRemote, LibraryOperationControllerLocal {

    @EJB
    private LendingEntityControllerLocal lendingEntityControllerLocal;

    @EJB
    private ReservationControllerLocal reservationControllerLocal;

    @EJB
    private FineControllerLocal fineControllerLocal;

    @EJB
    private BookEntityControllerLocal bookEntityControllerLocal;

    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;

    @EJB
    private MemberEntityControllerLocal memberEntityControllerLocal;

    public LibraryOperationController() {

    }

    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginException{
        try{
            StaffEntity retrievedStaffEntity = staffEntityControllerLocal.staffLogin(username, password);
            return retrievedStaffEntity;
        } catch (InvalidLoginException ex){
            throw ex;
        }
    }

    @Override
    public LendingEntity doLendBook(String identityNumber, Long bookId) throws BookNotFoundException, MemberNotFoundException, BookIsOnLoanException, MemberHasFinesException, MaxLoansExceeded, MemberNotAtTopOfReserveList {

        Date date = new Date();

        try {
            BookEntity bookEntity = bookEntityControllerLocal.retrieveBookByBookId(bookId);
            MemberEntity memberEntity = memberEntityControllerLocal.retrieveMemberByIdentityNumber(identityNumber);
            LendingEntity newLendingEntity = new LendingEntity();

            lendingEntityControllerLocal.checkIsBookLent(bookId);
            fineControllerLocal.checkIfMemberHasFines(identityNumber);
            lendingEntityControllerLocal.checkIfMemberExceedsMaxLoans(identityNumber);
            List<ReservationEntity> reservations = reservationControllerLocal.retrieveAllReservationsByBookId(bookId);
            if (!reservations.isEmpty()) {
                lendingEntityControllerLocal.checkIfMemberOnReserveList(identityNumber);
            }

            newLendingEntity.setMember(memberEntity);
            newLendingEntity.setBook(bookEntity);
            Date duedate = lendingEntityControllerLocal.generateDueDate(date);
            newLendingEntity.setLendDate(date);
            newLendingEntity.setDueDate(duedate);
            newLendingEntity.setHasReturned(false);
            newLendingEntity = lendingEntityControllerLocal.createNewLending(newLendingEntity);
            
            return newLendingEntity;
            
        } catch(
                BookNotFoundException | 
                MemberNotFoundException | 
                BookIsOnLoanException | 
                MemberHasFinesException |
                MaxLoansExceeded |
                MemberNotAtTopOfReserveList ex
            ) {
            throw ex;
        }
    }
    
    

}
