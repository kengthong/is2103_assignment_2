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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long lendId;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date lendDate;    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private MemberEntity member;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private BookEntity book;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dueDate;
    @Column(nullable = false)
    private boolean status;
    

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
        return member;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
