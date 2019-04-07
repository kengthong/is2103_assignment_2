/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MemberEntity;
import entity.ReservationEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.MemberNotAtTopOfReserveList;
import util.exception.MultipleReservationException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author hiixdayah
 */
@Stateless
@Local(ReservationControllerLocal.class)
@Remote(ReservationControllerRemote.class)
public class ReservationController implements ReservationControllerRemote, ReservationControllerLocal {

    @PersistenceContext(unitName = "librarydb2New-ejbPU")
    private EntityManager entityManager;

    public ReservationController() {
    }

    @Override
    public boolean checkForReservation(Long bookId) {
        return false;
    }

    @Override
    public List<ReservationEntity> retrieveAllReservations() {
        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r");

        return query.getResultList();
    }

    @Override
    public void deleteReservation(ReservationEntity bookToRemove) throws ReservationNotFoundException {
        entityManager.remove(bookToRemove);
    }

    public void persist(Object object) {
        entityManager.persist(object);
    }

    @Override
    public List<ReservationEntity> retrieveAllReservationsByBookId(Long bookId) {
        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r WHERE r.book.bookId = :inBookId");
        query.setParameter("inBookId", bookId);
        return query.getResultList();
    }

    @Override
    public List<ReservationEntity> retrieveReservationsByIsbn(String isbn) {
        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r WHERE r.book.isbn = :inIsbn");
        query.setParameter("inIsbn", isbn);
        return query.getResultList();
    }

    @Override
    public List<ReservationEntity> retrieveReservationsByMember(Long memberId) {
        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r WHERE r.memberEntity.memberId = :inMemberId");
        query.setParameter("inMemberId", memberId);
        return query.getResultList();
    }

    @Override
    public ReservationEntity retrieveReservationOfMember(Long bookId, Long memberId) {
        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r WHERE r.memberEntity.memberId = :inMemberId AND r.book.bookId = :inBookId");
        query.setParameter("inMemberId", memberId);
        query.setParameter("inBookId", bookId);
        return (ReservationEntity) query.getSingleResult();
    }

    @Override
    public List<ReservationEntity> retrieveAllUnfulfilledReservationsByBookId(Long bookId) {
        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r WHERE r.book.bookId = :inBookId and r.hasFulfilled = false");
        query.setParameter("inBookId", bookId);

        return query.getResultList();
    }

    @Override
    public void checkForMultipleReservationsOnSameBook(String identityNumber, Long bookId) throws MultipleReservationException {
        Query query = entityManager.createQuery("SELECT r FROM ReservationEntity r WHERE r.book.bookId = :inBookId and r.hasFulfilled = false and r.memberEntity.identityNumber = :inIdentityNumber");
        query.setParameter("inBookId", bookId);
        query.setParameter("inIdentityNumber", identityNumber);

        if (!query.getResultList().isEmpty()) {
            throw new MultipleReservationException("Member has already made the same reservation previously");
        }
    }

    @Override
    public ReservationEntity createReservation(ReservationEntity newReservationEntity) {
        entityManager.persist(newReservationEntity);
        entityManager.flush();
        
        return newReservationEntity;
    }
   
    @Override
    public void fulfillReservation(ReservationEntity currentReservationEntity) {
        currentReservationEntity.setHasFulfilled(true);
        entityManager.merge(currentReservationEntity);
        entityManager.flush();
    }


    @Override
    public void checkIfMemberOnReserveList(List<ReservationEntity> reservations, String identityNumber) throws MemberNotAtTopOfReserveList {
        
        if (!reservations.get(0).getMember().getIdentityNumber().equals(identityNumber)) {
            throw new MemberNotAtTopOfReserveList("Book has been reserved by another member");
        }
    }
}
