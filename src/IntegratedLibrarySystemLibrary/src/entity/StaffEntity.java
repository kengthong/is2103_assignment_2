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
import util.security.CryptographicHelper;

/**
 *
 * @author sing jie
 */
@Entity
public class StaffEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;
    @Column(length = 32, nullable = false)
    private String firstName;
    @Column(length = 32, nullable = false)
    private String lastName;
    @Column(length = 32, nullable = false, unique = true)
    private String userName;
    @Column(length = 32, nullable = false)
    private String password;
    @Column(nullable = false, length = 32)
    private String salt;
    
    public StaffEntity() {
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
    }
    
    public StaffEntity(String firstName, String lastName, String userName, String password) {
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
//        this.password = password;
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);        
        setPassword(password);
        
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password != null)
        {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.getSalt()));
        }
        else
        {
            this.password = null;
        }
    } 
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffId != null ? staffId.hashCode() : 0);
        return hash;
    }
    
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StaffEntity)) {
            return false;
        }
        StaffEntity other = (StaffEntity) object;
        if ((this.staffId == null && other.staffId != null) || (this.staffId != null && !this.staffId.equals(other.staffId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StaffEntity[ id=" + staffId + " ]";
    }
    
}
