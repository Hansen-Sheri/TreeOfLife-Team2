/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treeoflife;

import byui.cit260.treeOfLife.model.Game;
import byui.cit260.treeOfLife.model.Players;

/**
 *
 * @author Chuck
 */
public class TreeOfLife {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Players playerOne = new Players();
        
        playerOne.setName("Fred Flinstone");
        
        String playerInfo = playerOne.toString();
        
        System.out.println(playerInfo);
        
        Game game1 = new Game();
        
        game1.setId(1);
        
        String gameID = game1.toString();
        System.out.println(gameID);
    }
    
}
