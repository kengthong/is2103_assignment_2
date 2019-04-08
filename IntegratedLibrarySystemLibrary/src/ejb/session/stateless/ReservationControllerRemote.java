/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.BookHasBeenReservedException;
import util.exception.MemberNotAtTopOfReserveList;
import util.exception.MemberNotFoundException;
import util.exception.MultipleReservationException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Remote
public interface ReservationControllerRemote {

    public List<ReservationEntity> retrieveAllReservations();

    public boolean checkForReservation(Long bookId);

    public void deleteReservation(ReservationEntity bookToRemove) throws ReservationNotFoundException ;

    List<ReservationEntity> retrieveAllReservationsByBookId(Long bookId);

    List<ReservationEntity> retrieveReservationsByIsbn(String isbn);

    List<ReservationEntity> retrieveReservationsByMember(Long memberId);

    ReservationEntity retrieveReservationOfMember(Long bookId, Long memberId);

    List<ReservationEntity> retrieveAllUnfulfilledReservationsByBookId(Long bookId);

    void checkForMultipleReservationsOnSameBook(String identityNumber, Long bookId) throws MultipleReservationException;

    ReservationEntity createReservation(ReservationEntity newReservationEntity);

    void fulfillReservation(ReservationEntity currentReservationEntity);

    void checkIfMemberOnReserveList(List<ReservationEntity> reservations, String identityNumber) throws MemberNotAtTopOfReserveList;

    void checkIfBookHasReservations(Long bookId) throws BookHasBeenReservedException;

}
