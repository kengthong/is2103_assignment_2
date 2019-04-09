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

/**
 *
 * @author sing jie
 */
@Entity
public class BookEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    @Column(length = 60, nullable = false)
    private String title;
    @Column(length = 6, nullable = false)
    private String isbn;
    @Column(precision = 4, nullable = false)
    private Integer publishedYear;
    //@OneToMany(mappedBy="book", fetch = FetchType.LAZY)
    //private List<LendingEntity> lendings;
    //@OneToMany(mappedBy="book")
    //private List<ReservationEntity> reservations;
    
        public BookEntity() {
    }
    
    public BookEntity(String title, String isbn, Integer publishedYear) {
        this();
        
        this.title = title;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

//    public List<LendingEntity> getLendings() {
//        return lendings;
//    }
//
//    public void setLendings(List<LendingEntity> lendings) {
//        this.lendings = lendings;
//    }
//    
//    public List<ReservationEntity> getReservations() {
//        return reservations;
//    }
//
//    public void setReservations(List<ReservationEntity> reservations) {
//        this.reservations = reservations;
//    }
//    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookId != null ? bookId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BookEntity)) {
            return false;
        }
        BookEntity other = (BookEntity) object;
        if ((this.bookId == null && other.bookId != null) || (this.bookId != null && !this.bookId.equals(other.bookId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BookEntity[ id=" + bookId + " ]";
    }

}
