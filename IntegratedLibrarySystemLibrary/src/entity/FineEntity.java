/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 *
 * @author sing jie
 */
@Entity
public class FineEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fineId;
    //@OneToOne(optional = false)
    //private MemberEntity identityNumber;
    @ManyToOne
    @JoinColumn (name="memberId")
    private MemberEntity memberEntity;
    @Column(scale = 2, nullable = false)
    private double amount;
    @Column(nullable = false)
    private boolean hasPaid;

    public FineEntity() {
    }

    public FineEntity(MemberEntity memberEntity, double amount, boolean hasPaid) {
        this.memberEntity = memberEntity;
        this.amount = amount;
        this.hasPaid = hasPaid;
    }
    
    public Long getFineId() {
        return fineId;
    }

    public void setFineId(Long id) {
        this.fineId = fineId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean getHasPaid() {
        return hasPaid;
    }

    public void setHasPaid(boolean hasPaid) {
        this.hasPaid = hasPaid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fineId != null ? fineId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FineEntity)) {
            return false;
        }
        FineEntity other = (FineEntity) object;
        if ((this.fineId == null && other.fineId != null) || (this.fineId != null && !this.fineId.equals(other.fineId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FineEntity[ fineId=" + fineId + " ]";
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public void setMemberEntity(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }
    
}