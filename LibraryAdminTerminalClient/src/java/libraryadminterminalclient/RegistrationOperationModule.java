/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryadminterminalclient;

import ejb.session.stateless.MemberEntityControllerRemote;
import entity.MemberEntity;
import entity.StaffEntity;
import java.util.Scanner;
import util.exception.MemberNotFoundException;

/**
 *
 * @author hiixdayah
 */
public class RegistrationOperationModule {

    private MemberEntityControllerRemote memberEntityControllerRemote;
    private StaffEntity currentStaffEntity;

    public RegistrationOperationModule() {
    }

    public RegistrationOperationModule(MemberEntityControllerRemote memberEntityControllerRemote, StaffEntity currentStaffEntity) {
        this();
        this.memberEntityControllerRemote = memberEntityControllerRemote;
        this.currentStaffEntity = currentStaffEntity;
    }

    public void menuRegistrationOperation() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** ILS :: Registration Operation ***\n");
            System.out.println("1: Register New Member");
            System.out.println("2: Back\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doRegisterMember();
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 2) {
                break;
            }
        }
    }

    private void doRegisterMember() {
        Scanner scanner = new Scanner(System.in);
        MemberEntity newMemberEntity = new MemberEntity();

        System.out.println("*** ILS :: Library Operation :: Register Member ***\n");
        System.out.print("Enter Identity Number> ");
        newMemberEntity.setIdentityNumber(scanner.nextLine().trim());
        System.out.print("Enter Security Code> ");
        newMemberEntity.setSecurityCode(scanner.nextLine().trim());
        System.out.print("Enter First Name> ");
        newMemberEntity.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newMemberEntity.setLastName(scanner.nextLine().trim());
        System.out.print("Enter Gender> ");
        newMemberEntity.setGender(scanner.nextLine().trim());
        System.out.print("Enter Age> ");
        newMemberEntity.setAge(scanner.nextInt());
        scanner.nextLine();
        System.out.print("Enter Phone> ");
        newMemberEntity.setPhone(scanner.nextLine().trim());
        System.out.print("Enter Address>");
        newMemberEntity.setAddress(scanner.nextLine());

        try {
            memberEntityControllerRemote.retrieveMemberByIdentityNumber(newMemberEntity.getIdentityNumber());
            System.out.println("Member has already been registered previously and cannot register again!\n");
        } catch (MemberNotFoundException ex) {
            memberEntityControllerRemote.createNewMember(newMemberEntity);
            System.out.println("Member has been registered successfully!\n");
        }
    }

}
