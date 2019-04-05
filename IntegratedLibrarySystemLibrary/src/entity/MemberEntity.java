/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author sing jie
 */
@Entity
public class MemberEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(length = 9, nullable = false)
    private String identityNumber;
    @Column(length = 32, nullable = false)
    private String securityCode;
    @Column(length = 32, nullable = false)
    private String firstName;
    @Column(length = 32, nullable = false)
    private String lastName;
    @Column(length = 6, nullable = false)
    private String gender;
    @Column(nullable = false)
    private Integer age;
    @Column(length = 8, nullable = false)
    private String phone;
    @Column(length = 60, nullable = false)
    private String address;
    @OneToMany(mappedBy="memberEntity",fetch = FetchType.LAZY)
    private List<LendingEntity> lendings;
    @OneToMany(mappedBy = "memberEntity", fetch = FetchType.LAZY) 
    private List<FineEntity> fines;
    @OneToMany (mappedBy = "memberEntity", fetch = FetchType.LAZY) 
    private List<ReservationEntity> reservations ; 
  
       public MemberEntity() {
    }

    public MemberEntity(String identityNumber, String securityCode, String firstName, String lastName, String gender, Integer age, String phone, String address) {
        this.identityNumber = identityNumber;
        this.securityCode = securityCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.address = address;
    }
    
    
    
    
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }
    
    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<LendingEntity> getLendings() {
        return lendings;
    }

    public void setLendings(List<LendingEntity> lendings) {
        this.lendings = lendings;
    }
    
    public List<FineEntity> getFines() {
        return fines;
    }

    public void setFines(List<FineEntity> fines) {
        this.fines = fines;
    }

    public List<ReservationEntity> getReservations() {
        return reservations;
    }
    
    public void setReservations(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (memberId != null ? memberId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MemberEntity)) {
            return false;
        }
        MemberEntity other = (MemberEntity) object;
        if ((this.memberId == null && other.memberId != null) || (this.memberId != null && !this.memberId.equals(other.memberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.MemberEntity[ id=" + memberId + " ]";
    }

    
    
}
