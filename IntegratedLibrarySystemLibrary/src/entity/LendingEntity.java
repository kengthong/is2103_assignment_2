/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author sing jie
 */
@Entity
public class LendingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lendId;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lendDate;    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    //@JoinColumn(name="memberId", nullable = false)
    private MemberEntity memberEntity;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    //@JoinColumn(name="bookId", nullable = false)
    private BookEntity book;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dueDate;
    @Column(nullable = false)
    private boolean hasReturned;
    
    public LendingEntity() {
        
    }

    public LendingEntity(Date lendDate, MemberEntity memberEntity, BookEntity book, Date dueDate, boolean hasReturned) {
        this.lendDate = lendDate;
        this.memberEntity = memberEntity;
        this.book = book;
        this.dueDate = dueDate;
        this.hasReturned = hasReturned;
    }
    
    

    public Long getLendId() {
        return lendId;
    }

    public void setLendId(Long lendId) {
        this.lendId = lendId;
    }
    
    public Date getLendDate() {
        return lendDate;
    }

    public void setLendDate(Date lendDate) {
        this.lendDate = lendDate;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean getHasReturned() {
        return hasReturned;
    }

    public void setHasReturned(boolean hasReturned) {
        this.hasReturned = hasReturned;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lendId != null ? lendId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LendingEntity)) {
            return false;
        }
        LendingEntity other = (LendingEntity) object;
        if ((this.lendId == null && other.lendId != null) || (this.lendId != null && !this.lendId.equals(other.lendId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.LendingEntity[ id=" + lendId + " ]";
    }
    
}
