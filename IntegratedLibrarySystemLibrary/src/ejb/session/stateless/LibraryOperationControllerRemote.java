/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LendingEntity;
import entity.MemberEntity;
import entity.StaffEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.BookHasBeenReservedException;
import util.exception.BookIsAlreadyLoanedByMemberException;
import util.exception.BookIsAlreadyOverdueException;
import util.exception.BookIsAvailableForLoanException;
import util.exception.BookIsOnLoanException;
import util.exception.BookNotFoundException;
import util.exception.InvalidLoginException;
import util.exception.LendingNotFoundException;
import util.exception.MaxLoansExceeded;
import util.exception.MemberHasFinesException;
import util.exception.MemberNotAtTopOfReserveList;
import util.exception.MemberNotFoundException;
import util.exception.MultipleReservationException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Remote
public interface LibraryOperationControllerRemote {

    StaffEntity staffLogin(String username, String password) throws InvalidLoginException;

    LendingEntity doLendBook(String identityNumber, Long bookId) throws BookNotFoundException, MemberNotFoundException, BookIsOnLoanException, MemberHasFinesException, MaxLoansExceeded, MemberNotAtTopOfReserveList;

    LendingEntity doExtendBook(String identityNumber, Long bookId) throws MemberNotAtTopOfReserveList, MemberHasFinesException, BookIsAlreadyOverdueException;

    void viewReservations();

    void deleteReservation(Long bookId, String identityNumber) throws MemberNotFoundException, ReservationNotFoundException ;

    LendingEntity doExtendBook(String identityNumber, Long bookId) throws LendingNotFoundException, BookHasBeenReservedException, MemberHasFinesException, BookIsAlreadyOverdueException;

    void doReturnBook(Long bookId, Long memberId) throws LendingNotFoundException, MemberNotFoundException;

    List<Object[]> searchBookToReserve(String titleToSearch);

    void doReserveBook(MemberEntity currentMember, Long bookId) throws BookNotFoundException, BookIsAlreadyLoanedByMemberException,BookIsAvailableForLoanException, MultipleReservationException, MemberHasFinesException;

    List<Object[]> searchBook(String titleToSearch);
}
