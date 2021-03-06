/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package byui.cit260.treeOfLife.view;

import byui.cit260.treeOfLife.model.QuitGame;

/**
 *
 * @author Chuck
 */
public class EndGameBuildingView extends View{

    public EndGameBuildingView() {
        super("\n"
  
            +"\n================================================================="
            +"\n\t\t\tThe Large and Spacious Building                           "
            +"\n================================================================="
            +"\nYou only achieved the Large and Spacious Building. A great deal "
            +"\nmore faith is required on you part."
            +"\n"
            +"\nPlease try again to see if you have what it takes to make it to"
            +"\nthe tree of life!"
            +"\nThanks for playing the Tree Of Life!"
//            +"\nQ - Return to Main Menu" 
            +"\n================================================================="
            +"\n"
            +"\nEnter 'E' to end your game."
            +"\nE - END GAME");
        
        
        //Display Final Message based on end user result
        //Hit Q to end game
    }

    

    @Override
     public boolean doAction(Object obj) {
        String value = (String) obj;
        value = value.toUpperCase(); //convert to all upper case
        char choice = value.charAt(0); //get first character entered
        switch (choice){
            case 'E':// Go to the Map
                this.endGame();
                break;
        
            default:
            ErrorView.display("GameMenuView", "*** Invalid game menu selection *** Try again");
                break;
}
        return true;

    
    }

    private void endGame() {
       QuitGame quitGame = new QuitGame();
       quitGame.display();
        
    }
}
