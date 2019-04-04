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
public class MaxLoansExceeded extends Exception {

    public MaxLoansExceeded() {
    }

    public MaxLoansExceeded(String msg) {
        super(msg);
    }
    
}
