/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author kengthong
 */
public class BookIsAlreadyLoanedByMemberException extends Exception {

    public BookIsAlreadyLoanedByMemberException() {
    }

    
    public BookIsAlreadyLoanedByMemberException(String msg) {
        super(msg);
    }
}
