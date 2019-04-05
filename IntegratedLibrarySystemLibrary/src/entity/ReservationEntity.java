/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author sing jie
 */
@Entity
public class ReservationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Column(length = 60, nullable = false)
    private String title;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date availability;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="memberID")
    private MemberEntity memberEntity;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BookEntity book;
    
    //NEED LIST OF RESERVATIONS FOR A BOOK 
 
    
    
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getAvailability() {
        return availability;
    }

    public void setAvailability(Date availability) {
        this.availability = availability;
    }

    public MemberEntity getMember() {
        return memberEntity;
    }

    public void setMember(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }
    
    public BookEntity getBook() {
        return book;
    }
    
    public void setBook(BookEntity book) {
        this.book = book;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReservationEntity)) {
            return false;
        }
        ReservationEntity other = (ReservationEntity) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationEntity[ id=" + reservationId + " ]";
    }
    
}
