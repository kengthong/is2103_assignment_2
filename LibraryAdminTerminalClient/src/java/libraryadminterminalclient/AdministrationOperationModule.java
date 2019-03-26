/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libraryadminterminalclient;

import ejb.session.stateless.BookEntityControllerRemote;
import ejb.session.stateless.MemberEntityControllerRemote;
import ejb.session.stateless.StaffEntityControllerRemote;
import entity.MemberEntity;
import entity.StaffEntity;
import java.util.Scanner;
import util.exception.MemberNotFoundException;


/**
 *
 * @author hiixdayah
 */
public class AdministrationOperationModule {
    
    private BookEntityControllerRemote bookEntityControllerRemote;
    private MemberEntityControllerRemote memberEntityControllerRemote;
    private StaffEntityControllerRemote staffEntityControllerRemote;
    private StaffEntity currentStaffEntity;
        

  public AdministrationOperationModule() {  
  }
  
  public AdministrationOperationModule(MemberEntityControllerRemote memberEntityControllerRemote, BookEntityControllerRemote bookEntityControllerRemote, StaffEntityControllerRemote staffEntityControllerRemote, StaffEntity currentStaffEntity) {  
      this() ;
      this.memberEntityControllerRemote = memberEntityControllerRemote;
      this.bookEntityControllerRemote = bookEntityControllerRemote;
      this.staffEntityControllerRemote = staffEntityControllerRemote;
      this.currentStaffEntity = currentStaffEntity;
  }
  
  public void menuAdministrationOperation() {
    Scanner scanner = new Scanner(System.in) ; 
    Integer response = 0 ; 
    
     while(true) {
            System.out.println("*** ILS :: Administration Operation ***\n");
            System.out.println("1: Member Management");
            System.out.println("2: Book Management");
            System.out.println("3: Staff Management");
            System.out.println("4: Back\n");
            response = 0;  
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();
                 

                if(response == 1)
                {
                    doManageMember() ;  
                } else if (response == 2) {
                    doManageBook() ;  
                } else if (response == 3) {
                    doManageStaff() ; 
                } else if (response == 4) {
                    break ; 
                } else {
                    System.out.println("Invalid option, please try again!\n") ; 
                }
            }
               if(response == 4)
            {
                break;
            }
        }
    
  }
  
  private void doManageMember() {
    Scanner scanner = new Scanner(System.in) ; 
    Integer response = 0 ; 
    
     while(true) {
            System.out.println("*** ILS :: Administration Operation :: Member Management ***\n");
            System.out.println("1: Add Member");
            System.out.println("2: View Member Details");
            System.out.println("3: Update Member");
            System.out.println("4: Delete Member");
            System.out.println("5: View All Members");
            System.out.println("6: Back\n");
            response = 0;  
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateNewMember();  
                } else if (response == 2) {
                    doViewMemberDetails();  
                } else if (response == 3) {
                    doUpdateMember() ;  
                } else if (response == 4) {
                    doDeleteMember() ;  
                } else if (response == 5) {
                    doViewAllMembers(); 
                } else if (response == 6) {
                    break ; 
                } else {
                    System.out.println("Invalid option, please try again!\n") ; 
                }
            }
               if(response == 6)
            {
                break;
            }
        }
      
  }
  
   private void doCreateNewMember() throws MemberNotFoundException {
     Scanner scanner = new Scanner (System.in) ; 
    MemberEntity newMemberEntity = new MemberEntity() ; 
    
    System.out.println("*** ILS :: Administration Operation :: Member Management :: Add Member ***\n") ; 
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
    scanner.nextLine() ; 
    System.out.print("Enter Phone> ");
    newMemberEntity.setPhone(scanner.nextLine().trim());
    System.out.print("Enter Address>") ; 
    newMemberEntity.setAddress(scanner.nextLine());
    

   try { 
        memberEntityControllerRemote.retrieveMemberByIdentityNumber(newMemberEntity.getIdentityNumber()) ;
        System.out.println("Member has already been registered previously and cannot register again!\n") ; 
    } catch (MemberNotFoundException ex) { 
        memberEntityControllerRemote.createNewMember(newMemberEntity); 
    System.out.println("Member has been registered successfully!\n") ; 
    }   
    }
   
       private void doViewMemberDetails() {
           System.out.println("*** ILS :: Administration Operation :: Member Management :: View Member Details ***\n");
           System.out.print("Enter Member ID> ");
           Scanner scanner = new Scanner (System.in) ; 
           String identityNumber = scanner.nextLine() ;
        
        try
        {
            MemberEntity memberEntity = memberEntityControllerRemote.retrieveMemberByIdentityNumber(identityNumber); 
            System.out.printf("%9s%20s%20s%15s%20s%20s%d%20s\n", "Member ID", "Security Code", "First Name", "Last Name", "Gender", "Age", "Phone", "Address");
            System.out.printf("%9s%20s%20s%15s%20s%20s%d%20s\n", memberEntity.getMemberId().toString(), memberEntity.getSecurityCode(), memberEntity.getFirstName(), memberEntity.getLastName(), memberEntity.getGender().toString(), memberEntity.getAge().toString() , memberEntity.getPhone(), memberEntity.getAddress() );   
           
       } catch (MemberNotFoundException ex) {
        System.out.println("Member cannot be found!\n") ; 
       }
        
       }
       
       
        private void doUpdateMember(MemberEntity memberEntity) {
    
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** ILS :: Administration Operation :: Member Management :: Update Member ***\n");
        
        System.out.print("Enter Identity Number (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            memberEntity.setIdentityNumber(input);
        }
        
        System.out.print("Enter Security Code (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            memberEntity.setSecurityCode(input);
        }
        
        System.out.print("Enter First Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            memberEntity.setFirstName(input);
        }
                
        System.out.print("Enter Last Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            memberEntity.setLastName(input);
        }
        
        System.out.print("Enter Gender (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            memberEntity.setGender(input);
        }
        
         System.out.print("Enter Age (blank if no change)> ");
        input = scanner.nextInt();
        if(input.length() > 0)
        {
            memberEntity.setAge(input);
        }
        scanner.nextLine() ; 
        
        System.out.print("Enter Phone (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            memberEntity.setPhone(input);
        }
        
        System.out.print("Enter Address (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            memberEntity.setAddress(input);
        }
        
        
        memberEntityControllerRemote.updateMember(memberEntity);
        System.out.println("Member updated successfully!\n");
    }
        
        
     private void doDeleteMember(MemberEntity memberEntity) { 
          Scanner scanner = new Scanner(System.in);        
          String input;
        
        System.out.println("*** ILS :: Administration Operation :: Member Management :: Delete Member ***\n");
        System.out.printf("Confirm Delete Member %s %s (Member ID: %d) (Enter 'Y' to Delete)> ", memberEntity.getFirstName(), memberEntity.getLastName(), memberEntity.getMemberId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
            try 
            {
                memberEntityControllerRemote.deleteMember(memberEntity.getMemberId());
                System.out.println("Staff deleted successfully!\n");
            } 
            catch (MemberNotFoundException ex) 
            {
                System.out.println("An error has occurred while deleting member: " + ex.getMessage() + "\n");
            }            
        }
        else
        {
            System.out.println("Member NOT deleted!\n");
        }
     } 
        
        
        
      
       
       
       
   
   
  
  private void doManageBook() {
    Scanner scanner = new Scanner(System.in) ; 
    Integer response = 0 ; 
    
     while(true) {
            System.out.println("*** ILS :: Administration Operation :: Member Management ***\n");
            System.out.println("1: Add Book");
            System.out.println("2: View Book Details");
            System.out.println("3: Update Book");
            System.out.println("4: Delete Book");
            System.out.println("5: View All Books");
            System.out.println("6: Back\n");
            response = 0;  
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateNewBook();  
                } else if (response == 2) {
                    doViewBookDetails();  
                } else if (response == 3) {
                    doUpdateBook() ;  
                } else if (response == 4) {
                    doDeleteBook() ;  
                } else if (response == 5) {
                    doViewAllBooks(); 
                } else if (response == 6) {
                    break ; 
                } else {
                    System.out.println("Invalid option, please try again!\n") ; 
                }
            }
               if(response == 6)
            {
                break;
            }
        }
      
  }
  
  private void doManageStaff()() {
    Scanner scanner = new Scanner(System.in) ; 
    Integer response = 0 ; 
    
     while(true) {
            System.out.println("*** ILS :: Administration Operation :: Member Management ***\n");
            System.out.println("1: Add Staff");
            System.out.println("2: View Staff Details");
            System.out.println("3: Update Staff");
            System.out.println("4: Delete Staff");
            System.out.println("5: View All Staff");
            System.out.println("6: Back\n");
            response = 0;  
            
            while(response < 1 || response > 6)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateNewBook();  
                } else if (response == 2) {
                    doViewStaffDetails();  
                } else if (response == 3) {
                    doUpdateStaff() ;  
                } else if (response == 4) {
                    doDeleteStaff() ;  
                } else if (response == 5) {
                    doViewAllStaff(); 
                } else if (response == 6) {
                    break ; 
                } else {
                    System.out.println("Invalid option, please try again!\n") ; 
                }
            }
               if(response == 6)
            {
                break;
            }
        }
      
  }
  
  
    
    
      
}
