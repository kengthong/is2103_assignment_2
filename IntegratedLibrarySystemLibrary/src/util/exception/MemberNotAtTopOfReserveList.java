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
public class MemberNotAtTopOfReserveList extends Exception {

    public MemberNotAtTopOfReserveList() {
    }

    
    public MemberNotAtTopOfReserveList(String msg) {
        super(msg);
    }
}
