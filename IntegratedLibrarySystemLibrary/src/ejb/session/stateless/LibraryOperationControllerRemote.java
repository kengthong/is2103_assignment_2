/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LendingEntity;
import entity.StaffEntity;
import javax.ejb.Remote;
import util.exception.BookIsAlreadyOverdueException;
import util.exception.BookIsOnLoanException;
import util.exception.BookNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.MaxLoansExceeded;
import util.exception.MemberHasFinesException;
import util.exception.MemberNotAtTopOfReserveList;
import util.exception.MemberNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Remote
public interface LibraryOperationControllerRemote {

    StaffEntity staffLogin(String username, String password) throws InvalidLoginException;

    LendingEntity doLendBook(String identityNumber, Long bookId) throws BookNotFoundException, MemberNotFoundException, BookIsOnLoanException, MemberHasFinesException, MaxLoansExceeded, MemberNotAtTopOfReserveList;

    void viewReservations();

    void deleteReservation(Long bookId, String identityNumber) throws MemberNotFoundException, ReservationNotFoundException ;

    LendingEntity doExtendBook(String identityNumber, Long bookId) throws MemberNotAtTopOfReserveList, BookIsAlreadyOverdueException, MemberHasFinesException;
    
}
