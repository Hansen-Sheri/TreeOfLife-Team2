/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package byui.cit260.treeOfLife.view;
import byui.cit260.treeOfLife.control.GameControl;
import byui.cit260.treeOfLife.control.MapControl;
import byui.cit260.treeOfLife.control.MapControl.SceneType;
import byui.cit260.treeOfLife.control.QuestionControl;
import byui.cit260.treeOfLife.model.Location;
import byui.cit260.treeOfLife.model.Map;
import byui.cit260.treeOfLife.model.QuestionArray;
import byui.cit260.treeOfLife.model.Scene;
import java.awt.Point;
import byui.cit260.treeOfLife.model.Character;
import byui.cit260.treeOfLife.model.Question;
import citbyui.cit260.treeOfLife.exceptions.MapControlException;
import java.util.logging.Level;
import java.util.logging.Logger;

import treeoflife.TreeOfLife;
/**
 *
 * @author sherihansen
 */
public class LevelView extends View {
    private Point currentCoordinates;

    public LevelView() {
//        super("\n"
//  
//            +"\n========================================"
//            +"\n| Level Menu                             |"
//            +"\n========================================"
//            //+"\nR - Rest at Temple" Potential variance in game - faith dwindles with questions asked, can be restored here. What would be the consequence of not having faith?
//            +"\nA - Answer Level Questions"
//            +"\nM - Go back to Map"
//            +"\nG - Go to Game Menu"
////            +"\nQ - Return to Main Menu" 
//            +"\n========================================");
//   
//    }
    
     super("=================================================================");
     
        currentCoordinates = TreeOfLife.getCurrentGame().getCharacter().getCoordinates();
        Location location =  TreeOfLife.getCurrentGame().getMap().getLocations()[currentCoordinates.x][currentCoordinates.y];
        String description = location.getScene().getMapSymbol();
        Scene levelName = location.getScene();
     
this.console.println("\n================================================================="
            + "\n\t\t\t "+ description 
            +"\n================================================================="
           
            +"\nA - Answer Level Questions"
            +"\nM - Go back to Map"
            +"\nG - Go to Game Menu"
//            +"\nQ - Return to Main Menu" 
           );
   
    }
           

@Override
    public boolean doAction(Object obj) {
        String value = (String) obj;
        value = value.toUpperCase(); //convert to all upper case
        char choice = value.charAt(0); //get first character entered
             switch (choice){
          
            case 'A': // answer levelQuestions//              
              
                     this.answerLevelQuestions(); 
            
               return true;
//                break;
            case 'M': // to to Armor Shop Menu
                this.displayMapView();
                break;
            case 'G': // go to level view
                this.displayGameMenu(); 
                break;
//            case 'Q': // go to the Main Menu
//                this.returnToMainMenu();
//                break;
            default:
            ErrorView.display("LevelView", "\n*** Invalid Level Menu selection *** Try again");
                  break;
}
        return true;
        

    }

    private void answerLevelQuestions() {
        
        QuestionArray levelQuestion = new QuestionArray();
        //get first (in group of 3 level questions)
//            QuestionArray.setNumSetsOfLevelQuestionsAsked(3);
           
            //check answer
            int setOfQuestionsAlreadyAsked = QuestionArray.getNumSetsOfLevelQuestionsAsked();
               if(setOfQuestionsAlreadyAsked >= 3){
                   this.console.println("You may only answer 9 questions per level. "
                           + "You will be returned to the map");
                  try {
                 //continue to next level
                 
                 this.continueToNextLevel();
             } catch (MapControlException ex) {
                  ErrorView.display(this.getClass().getName(), "Error reading input: "+ ex.getMessage());
             }
                   
               }else{
//                    int numQuestionsAnswered = levelQuestion.getNumLevelQuestionsAnswered();
                    //loop 3 times - while numLevelQuestionAnswere <=3 continue
//                      
                    for(int x = 0; x < 3; x++){
                        //ask a question

                        Question question =  levelQuestion.getNextLevelQuestion();

                        String nextQuestion = question.getQuestion();
                         this.console.println(nextQuestion);
                         //get input
                        String response =  this.getInput();
                        String answerToQuestion = question.getAnswerToLevelQuestion();
                        //convert answer and check to upperCase
                        String userResponse = response.toUpperCase();
                        String answer = answerToQuestion.toUpperCase();
                        //check answer
                        if(userResponse.equals(answer)){
                            int points = 30;
                           this.console.println("Correct!  You just earned "+ points + " faith points!");
                           int currentFaith = TreeOfLife.getCurrentGame().getProgressMeter().getFaithStat();
                           TreeOfLife.getCurrentGame().getProgressMeter().setFaithStat(currentFaith + points);
                        }
                        else {
                            this.console.println("Good try.  But the correct answer was " + answerToQuestion);

                      }
//                      
                    int increment = levelQuestion.getNumLevelQuestionsAnswered() + 1;
                    levelQuestion.setNumLevelQuestionsAnswered(increment);
               
//                    this.console.println("numb of Questions answered is " + numQuestionsAnswered);
                    
                //assign points
               //increment and set  number level Questions asked
               }
                    int incrementSetQuestions = setOfQuestionsAlreadyAsked + 1;
                     QuestionArray.setNumSetsOfLevelQuestionsAsked(incrementSetQuestions);
              //once set of three has been asked, unblock mantle and temple sites
                    Location[][] locations = TreeOfLife.getCurrentGame().getMap().getLocations();
                     locations[1][0].setBlocked(false); //unblock temple
                     locations[2][0].setBlocked(false); //unblock mantle
            
            //set numLevelQuestions to 0 after while loop ends so can restart on next loop if needed
            levelQuestion.setNumLevelQuestionsAnswered(0);
            this.wantMoreLevelQuestions();
            
               }
        }

    private void displayMapView() {
      GameMenuView gameMenu = new GameMenuView();
        gameMenu.displayMap();
    }

    private void displayGameMenu() {
        GameMenuView gameMenu = new GameMenuView();
        gameMenu.display();     
    }

    private void wantMoreLevelQuestions() {
        //ask for user input
    this.console.println("Would you like to (A)answer another set of questions or (C)continue to the next level?");
    //get user input
    String answer = this.levelQuestionInput();
    //do action
    this.doActionLevelQuestion(answer);
    }

    private String levelQuestionInput() {
        boolean valid = false; //indicates if the input has been recieved
        String response = "";
        try {
            while (!valid) { //while a valid name has not been retrieved
                  response = this.keyboard.readLine();
            break; //out of the (exit) the repitition
            }

        } catch (Exception e) { //program said it was IOException
             ErrorView.display(this.getClass().getName(),"Error reading input: " + e.getMessage());
        }
        return response; // return the name

    }
    
    private void doActionLevelQuestion(String answer){
         answer = answer.toUpperCase();
        char choice = answer.charAt(0);
            switch(choice){
                case 'A': // answer another set of questions
                    this.answerLevelQuestions();
                    break;
                case 'C': {
             try {
                 //continue to next level
                 
                 this.continueToNextLevel();
             } catch (MapControlException ex) {
                  ErrorView.display(this.getClass().getName(), "Error reading input: "+ ex.getMessage());
             }
         }
                    break;
                default:
                    break;
            }
    }

    private void continueToNextLevel() throws MapControlException {
        //get currentLevel of player & move them to next level
        MapControl.SceneType currentLevel = TreeOfLife.getCurrentGame().getProgressMeter().getCurrentLevel();
        Character character = TreeOfLife.getCurrentGame().getCharacter();
       Point currentCoordinates = character.getCoordinates();
       Location[][] locations = TreeOfLife.getCurrentGame().getMap().getLocations();
       MapView map = new MapView();
       switch(currentLevel){
            case levelOne:
                //unblock next level and show it on map
                locations[1][2].setBlocked(false);
               locations[1][2].setVisited(true);
                TreeOfLife.getCurrentGame().getProgressMeter().setCurrentLevel(SceneType.levelTwo);
                GameMenuView.displayMap();
                this.display();
//                map.display();
                
               break;
            case levelTwo:
                locations[1][1].setBlocked(false);
                 locations[1][1].setVisited(true);
                 TreeOfLife.getCurrentGame().getProgressMeter().setCurrentLevel(SceneType.levelThree);
                 GameMenuView.displayMap();
                 map.display();
               break;
            case levelThree:
                locations[2][1].setBlocked(false);
                 locations[2][1].setVisited(true);
                 TreeOfLife.getCurrentGame().getProgressMeter().setCurrentLevel(SceneType.levelFour);
                 GameMenuView.displayMap();
                 map.display();
                break;
            case levelFour:
                locations[2][2].setBlocked(false);
                 locations[2][2].setVisited(true); 
                 TreeOfLife.getCurrentGame().getProgressMeter().setCurrentLevel(SceneType.levelFive);
                 GameMenuView.displayMap();
                   map.display();
                
                break;
            case levelFive:
                 locations[currentCoordinates.x][currentCoordinates.y].setBlocked(true);
                 
                this.console.println("Congratulations!!! You have completed the Tree of Life.");
                
                //This will take you to the end game sequence to the JudgementBarView
                JudgementBarView judgeBar= new JudgementBarView(); 
                judgeBar.display();
                //unblock endgame view after calculation has been done (so only 1 area is unblocked - the one they achieve
                                    
                break;
            default:
               
                break;
       }
    }
}
