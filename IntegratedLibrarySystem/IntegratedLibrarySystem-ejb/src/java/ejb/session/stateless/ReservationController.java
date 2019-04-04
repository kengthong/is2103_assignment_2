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
    public void deleteReservation(Long reservationId) throws ReservationNotFoundException {
//        ReservationEntity reservationEntityToRemove = retrieveReservationByReservationId(reservationId);
//        entityManager.remove(reservationEntityToRemove);
    }

    public void persist(Object object) {
        entityManager.persist(object);
    }

    @Override
    public List<ReservationEntity> retrieveAllReservationsByBookId(Long bookId) {
        Query query = entityManager.createQuery("SELECT r from ReservationEntity r WHERE r.book.bookId = :inBookId");
        query.setParameter("inBookId", bookId);
        return query.getResultList();
    }

    
}
