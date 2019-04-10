/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BookEntity;
import entity.FineEntity;
import entity.LendingEntity;
import entity.MemberEntity;
import entity.ReservationEntity;
import entity.StaffEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @PersistenceContext(unitName = "librarydb2New-ejbPU")
    private EntityManager entityManager;

    public LibraryOperationController() {

    }

    @Override
    public StaffEntity staffLogin(String username, String password) throws InvalidLoginException {
        try {
            StaffEntity retrievedStaffEntity = staffEntityControllerLocal.staffLogin(username, password);
            return retrievedStaffEntity;
        } catch (InvalidLoginException ex) {
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
            List<ReservationEntity> reservations = reservationControllerLocal.retrieveAllUnfulfilledReservationsByBookId(bookId);
            if (!reservations.isEmpty()) {
                reservationControllerLocal.checkIfMemberOnReserveList(reservations, identityNumber);
                reservationControllerLocal.fulfillReservation(reservations.get(0));
            }

            newLendingEntity.setMember(memberEntity);
            newLendingEntity.setBook(bookEntity);
            Date duedate = lendingEntityControllerLocal.generateDueDate(date);
            newLendingEntity.setLendDate(date);
            newLendingEntity.setDueDate(duedate);
            newLendingEntity.setHasReturned(false);
            newLendingEntity = lendingEntityControllerLocal.createNewLending(newLendingEntity);

            return newLendingEntity;

        } catch (BookNotFoundException
                | MemberNotFoundException
                | BookIsOnLoanException
                | MemberHasFinesException
                | MaxLoansExceeded
                | MemberNotAtTopOfReserveList ex) {
            throw ex;
        }
    }

    @Override
    public LendingEntity doExtendBook(String identityNumber, Long bookId) throws LendingNotFoundException, BookHasBeenReservedException, MemberHasFinesException, BookIsAlreadyOverdueException {

//        If the book is already overdue
//        o Member has unpaid fines
//        o The book is reserved by another member    
        try {
            //retrieve LEnding Entity
            LendingEntity currentLendingEntity = lendingEntityControllerLocal.retrieveLendingByBookId(bookId);

            //Compare due date with current date
            Date dueDate = currentLendingEntity.getDueDate();
            lendingEntityControllerLocal.checkIsBookOverdue(dueDate);

            //check for fines
            fineControllerLocal.checkIfMemberHasFines(identityNumber);
            //check for reserve
            List<ReservationEntity> reservations = reservationControllerLocal.retrieveAllReservationsByBookId(bookId);
            if (!reservations.isEmpty()) {
                throw new BookHasBeenReservedException("Book has been reserved by another member.\n");
            }
            //extend book
            Long lendId = currentLendingEntity.getLendId();
            LendingEntity updatedLendingEntity = lendingEntityControllerLocal.extendBook(lendId);
            //return lending entity
            return updatedLendingEntity;

        } catch (BookIsAlreadyOverdueException
                | LendingNotFoundException
                | MemberHasFinesException ex) {
            throw ex;
        }
    }

    @Override
    public void viewReservations() {
        System.out.print("Enter Book ID>\n");
        Scanner scanner = new Scanner(System.in);
        Long bookId = scanner.nextLong();

        try {
            BookEntity bookEntity = bookEntityControllerLocal.retrieveBookByBookId(bookId);

            List<ReservationEntity> reservationEntities = reservationControllerLocal.retrieveAllReservationsByBookId(bookId);
            if (!reservationEntities.isEmpty()) {
                for (ReservationEntity reservationEntity : reservationEntities) {
                    Long reservationId = reservationEntity.getReservationId();
                    Long memberId = reservationEntity.getMember().getMemberId();
                    System.out.println(reservationId + "\t|" + memberId);
                }
            }
            System.out.println();
        } catch (BookNotFoundException ex) {
            System.out.println("Book cannot be found!");
        }

        System.out.println();

    }

    @Override
    public void deleteReservation(Long bookId, String identityNumber) throws MemberNotFoundException, ReservationNotFoundException {
        try {
            MemberEntity memberEntity = memberEntityControllerLocal.retrieveMemberByIdentityNumber(identityNumber);
            ReservationEntity reservationToDelete = reservationControllerLocal.retrieveReservationOfMember(bookId, memberEntity.getMemberId());
            reservationControllerLocal.deleteReservation(reservationToDelete);

        } catch (MemberNotFoundException | ReservationNotFoundException ex) {
            throw ex;
        }
    }

    @Override
    public void doReturnBook(Long bookId, Long memberId) throws LendingNotFoundException, MemberNotFoundException {
        try {
            LendingEntity currentLendingEntity = lendingEntityControllerLocal.retrieveLendingByBookId(bookId);
            currentLendingEntity = lendingEntityControllerLocal.returnLending(currentLendingEntity.getLendId());
            Date currentDate = new Date();
            Date dueDate = currentLendingEntity.getDueDate();

            if (currentDate.after(dueDate)) {
                MemberEntity currentMemberEntity = memberEntityControllerLocal.retrieveMemberByMemberId(memberId);
                long diff = currentDate.getTime() - dueDate.getTime();
                int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                System.out.println("\n\n days=" + days + " \n\n");

                FineEntity newFineEntity = new FineEntity();
                newFineEntity.setMemberEntity(currentMemberEntity);
                newFineEntity.setAmount(days);
                newFineEntity.setHasPaid(false);

                newFineEntity = fineControllerLocal.createFine(newFineEntity);
            }
        } catch (LendingNotFoundException
                | MemberNotFoundException ex) {
            throw ex;
        }
    }

    @Override
    public List<Object[]> searchBookToReserve(String titleToSearch) {

        Query query = entityManager.createQuery(
                "SELECT b.bookId, b.title, l1.hasReturned as hasReturned, l1.dueDate as dueDate \n"
                + "FROM BookEntity b, LendingEntity l1 \n"
                + "WHERE b.title LIKE :inTitleToSearch \n"
                + "and l1.book.bookId = b.bookId\n"
                + "and l1.hasReturned = false"
        );
        titleToSearch = "%" + titleToSearch + "%";
        query.setParameter("inTitleToSearch", titleToSearch);
        return query.getResultList();
    }

    @Override
    public List<Object[]> searchBook(String titleToSearch) {
        Query query = entityManager.createQuery(
                "SELECT b.bookId, b.title, b.title\n"
                + "FROM BookEntity b \n"
                + "WHERE b.title LIKE :inTitleToSearch \n"
        );

        titleToSearch = "%" + titleToSearch + "%";
        query.setParameter("inTitleToSearch", titleToSearch);
        List<Object[]> results = query.getResultList();

        for (Object[] result : results) {
            //if book is on  loan. If yes, get due date, 
            //if no, check for reservations. If no reservations, append currently available
            Long bookId = (Long) result[0];
            try {

                lendingEntityControllerLocal.checkIsBookLent(bookId);
                reservationControllerLocal.checkIfBookHasReservations(bookId);
                result[2] = "Currently Available";
            } catch (BookIsOnLoanException ex) {
                //retrieve lending and get due date
                try {
                    LendingEntity currentLending = lendingEntityControllerLocal.retrieveLendingByBookId(bookId);
                    Date dueDate = currentLending.getDueDate();
                    SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                    result[2] = "Due on " + dt1.format(dueDate);
                } catch (LendingNotFoundException ex1) {
                }

            } catch (BookHasBeenReservedException ex) {
                // has been reserved
                result[2] = "Reserved";
            }

        }

        return results;

    }

    public void persist(Object object) {
        entityManager.persist(object);
    }

    @Override
    public void doReserveBook(MemberEntity currentMember, Long bookId) throws
            BookIsAlreadyLoanedByMemberException,
            BookIsAvailableForLoanException,
            MultipleReservationException,
            MemberHasFinesException,
            BookNotFoundException {

        try {
            BookEntity currentBook = bookEntityControllerLocal.retrieveBookByBookId(bookId);
            String identityNumber = currentMember.getIdentityNumber();
//         Member cannot reserve books that are currently available in the library without any reservations.
//        • Member cannot reserve books currently loaned by him/her.
            LendingEntity currentLendingEntity = checkBookIsUnavailableToBorrowAndNotBorrowedByMember(bookId, identityNumber);

//        • Member cannot make multiple reservations on the same book.
            reservationControllerLocal.checkForMultipleReservationsOnSameBook(identityNumber, bookId);

//        • Members with unpaid fines cannot reserve books.
            fineControllerLocal.checkIfMemberHasFines(identityNumber);

            ReservationEntity newReservationEntity = new ReservationEntity();
            newReservationEntity.setAvailability(currentLendingEntity.getDueDate());
            newReservationEntity.setMember(currentMember);
            newReservationEntity.setBook(currentBook);
            newReservationEntity.setHasFulfilled(false);

            reservationControllerLocal.createReservation(newReservationEntity);

        } catch (BookIsAvailableForLoanException
                | BookIsAlreadyLoanedByMemberException
                | MultipleReservationException
                | MemberHasFinesException
                | BookNotFoundException ex) {
            throw ex;
        }

    }

    private LendingEntity checkBookIsUnavailableToBorrowAndNotBorrowedByMember(Long bookId, String identityNumber) throws BookIsAvailableForLoanException, BookIsAlreadyLoanedByMemberException, BookIsAvailableForLoanException {
        //check that there are no lendingentities for l.book.bookId = bookId that has not been returned.
        // retrieve current lending for book that has not been returned.

        try {
            LendingEntity currentLendingEntity = lendingEntityControllerLocal.retrieveLendingByBookId(bookId);
            if (currentLendingEntity == null) { //If book is currently not on loan

                //check that there are existing unfulfilled reservations
                List<ReservationEntity> reservations = reservationControllerLocal.retrieveAllUnfulfilledReservationsByBookId(bookId);
                if (reservations.isEmpty()) {
                    throw new BookIsAvailableForLoanException("Member cannot reserve books that are currently available in the library\n");
                }
            } else { // if book is currently on loan
                if (currentLendingEntity.getMember().getIdentityNumber().equals(identityNumber)) { // if mmeber has already borrowed the same book
                    throw new BookIsAlreadyLoanedByMemberException("You cannot make reservations for books that are loaned to you.\n");
                }
            }
            return currentLendingEntity;
        } catch (LendingNotFoundException ex) {
            throw new BookIsAvailableForLoanException("Member cannot reserve books that are currently available in the library.\n");
        }

    }

    @Override
    public void setFines(Integer amount, MemberEntity currentMember) {
        FineEntity newFineEntity = new FineEntity();
        newFineEntity.setMemberEntity(currentMember);
        newFineEntity.setAmount(amount);
        newFineEntity.setHasPaid(false);
        fineControllerLocal.createFine(newFineEntity);
    }

}
