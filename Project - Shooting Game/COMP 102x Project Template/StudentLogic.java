import java.util.Random;

import comp102x.ColorImage;
import comp102x.assignment.GameLogic;
import comp102x.assignment.GameRecord;
import comp102x.assignment.Goal;

public class StudentLogic implements GameLogic{

    /**
     * Generate an intermediate football image for the shooting animation.
     * 
     * @param   depthImages     an array of football images of different depths
     * @param   initialStep     the initial step of the shooting animation
     * @param   currentStep     the current step of the shooting animation
     * @param   finalStep       the final step of the shooting animation
     * @param   initialScale    the initial scale of the football
     * @param   finalScale      the final scale of the football
     * @param   initialX        the initial x position of the football
     * @param   finalX          the final x position of the football
     * @param   initialY        the initial y position of the football
     * @param   finalY          the final y position of the football
     * @return  the selected depth image
     */ 
    public ColorImage generateIntermediateFootballImage(ColorImage[] depthImages, int initialStep, int currentStep, int finalStep, double initialScale, double finalScale, int initialX, int finalX, int initialY, int finalY) {
        // write your code after this line
        double ratio = (currentStep - (initialStep * 1.0)) / (finalStep - initialStep);
        double imageIndex = (depthImages.length - 1) * ratio;
        double xPosition = initialX + ((finalX - initialX) * ratio);
        double yPosition = initialY + ((finalY - initialY) * ratio);
        double scale = initialScale + ((finalScale - initialScale) * ratio);

        ColorImage image = depthImages[(int) imageIndex];
        image.setX((int) xPosition);
        image.setY((int) yPosition);
        image.setScale(scale);

        return image;
    }

    /**
     * Update the positions of the goals
     * 
     * @param     a 2D array of Goal objects that represents the goals displayed on the main
     * game screen
     * 
     * The order of preference for moving (and switching) the movable goals is:
     * 1. Vertically up one row
     * 2. Vertically down one row
     * 3. Horizontally left one column
     * 4. Horizontally right one column
     * 5. Diagonally up one row and to the right column
     * 6. Diagonally up one row and to the left column
     * 7. Diagonally down one row and to the right column
     * 8. Diagonally down one row and to the left column
     */ 
    public void updateGoalPositions(Goal[][] goals) {
        // write your code after this line
        //a 2D array, track, is created to keep track of the goals that are being moved
        //to new locations so that we don't move them back again to their original position
        String[][] track = new String[goals.length][goals[0].length];

        for (int i = 0; i < goals.length; i++) {
            for (int j = 0; j < goals[0].length; j++) {
                track[i][j] = "unmoved";
            }
        }

        for (int i = 0; i < goals.length; i++) {
            for (int j = 0; j < goals[0].length; j++) {
                if (goals[i][j].isHit() && goals[i][j].getType() == 2 && track[i][j].equals("unmoved")){
                    if (i-1 >= 0 && goals[i-1][j].getType() == 2) {
                        Goal temp = goals[i][j];
                        goals[i][j] = goals[i-1][j];
                        goals[i-1][j] = temp;
                        track[i][j] = "moved";
                        track[i-1][j] = "moved";
                    }
                    else if (i+1 < goals.length && goals[i+1][j].getType() == 2) {
                        Goal temp = goals[i][j];
                        goals[i][j] = goals[i+1][j];
                        goals[i+1][j] = temp;
                        track[i][j] = "moved";
                        track[i+1][j] = "moved";
                    }
                    else if (j-1 >= 0 && goals[i][j-1].getType() == 2) {
                        Goal temp = goals[i][j];
                        goals[i][j] = goals[i][j-1];
                        goals[i][j-1] = temp;
                        track[i][j] = "moved";
                        track[i][j-1] = "moved";
                    }
                    else if (j+1 < goals[0].length && goals[i][j+1].getType() == 2) {
                        Goal temp = goals[i][j];
                        goals[i][j] = goals[i][j+1];
                        goals[i][j+1] = temp;
                        track[i][j] = "moved";
                        track[i][j+1] = "moved";
                    }
                    else if (i-1 >= 0 && j+1 < goals[0].length && goals[i-1][j+1].getType() == 2) {
                        Goal temp = goals[i][j];
                        goals[i][j] = goals[i-1][j+1];
                        goals[i-1][j+1] = temp;
                        track[i][j] = "moved";
                        track[i-1][j+1] = "moved";
                    }
                    else if (i-1 >= 0 && j-1 >= 0 && goals[i-1][j-1].getType() == 2) {
                        Goal temp = goals[i][j];
                        goals[i][j] = goals[i-1][j-1];
                        goals[i-1][j-1] = temp;
                        track[i][j] = "moved";
                        track[i-1][j-1] = "moved";
                    }
                    else if (i+1 >= 0 && j+1 < goals[0].length && goals[i+1][j+1].getType() == 2) {
                        Goal temp = goals[i][j];
                        goals[i][j] = goals[i+1][j+1];
                        goals[i+1][j+1] = temp;
                        track[i][j] = "moved";
                        track[i+1][j+1] = "moved";
                    }
                    else if (i+1 >= 0 && j-1 >= 0 && goals[i+1][j-1].getType() == 2) {
                        Goal temp = goals[i][j];
                        goals[i][j] = goals[i+1][j-1];
                        goals[i+1][j-1] = temp;
                        track[i][j] = "moved";
                        track[i+1][j-1] = "moved";
                    }
                }
            }
        }
    }

    /**
     * Compare the record of the current game play with those of previous game plays and update the
     * highscore records
     * 
     * @param   highScoreRecords     represents the 1D array of the GameRecords of PREVIOUS game plays
     * @param   name                 name of the current game play
     * @param   level                level of the current game play
     * @param   score                score of the current game play
     * @return  a 1D array of GameRecords after processing the record of the current game play
     */ 
    public GameRecord[] updateHighScoreRecords(GameRecord[] highScoreRecords, String name, int level, int score) {
        // write your code after this line
        //If there are no previous game play records, return a new GameRecord array of size 1
        //containing the current record
        if (highScoreRecords.length == 0) {
            GameRecord[] oneScoreRecord = new GameRecord[1];
            oneScoreRecord[0] = new GameRecord(name, level, score);
            return oneScoreRecord;
            //highScoreRecords[0] = new GameRecord(name, level, score);
        }
        else {

            boolean playerNameExists = false;
            int existingPlayerIndex = -1;
            int recordLength = highScoreRecords.length;

            //check to see if the player name exists in the highScoreRecords array
            for (int i = 0; i < recordLength; i++) {
                if (highScoreRecords[i].getName().equals(name)) {
                    playerNameExists = true;
                    existingPlayerIndex = i;
                    break;
                }
            }

            //If the player's name doesn't exist in the previous records and there are less than 10
            //previous records, return a new GameRecord array containing all the previous records and the new record
            if (!playerNameExists && recordLength < 10) {
                GameRecord[] manyScoreRecords = new GameRecord[recordLength+1];
                manyScoreRecords[recordLength] = new GameRecord(name, level, score);
                highScoreRecords = manyScoreRecords;
            }

            //If the player's name doesn't exist in the previous records and there are 10 previous
            //records, return a new GameRecord array containing the best 10 records. A record is 
            //better than the other one if it has a higher score, or the two records have the same
            //score, but it has a higher level
            else if (!playerNameExists && recordLength == 10) {
                for (int i = recordLength-1; i >= 0; i--) {
                    GameRecord current = highScoreRecords[i];
                    if (current.getScore() < score) {
                        highScoreRecords[i] = new GameRecord(name, level, score);
                        break;
                    }
                    else if (current.getScore() == score) {
                        if (current.getLevel() < level) {
                            highScoreRecords[i] = new GameRecord(name, level, score);
                            break;
                        }
                    }
                }
            }

            //If the player's name exists in the previous records and the current record is better
            //than the previous record, return a new GameRecord array containing all the previous
            //records, but with the score and level of the player updated to those of the current
            //game play
            else if (playerNameExists) {
                GameRecord player = highScoreRecords[existingPlayerIndex];
                if (player.getScore() < score) {
                    player.setScore(score);
                    player.setLevel(level);
                }
                else if (player.getScore() == score) {
                    if (player.getLevel() < level) {
                        player.setScore(score);
                        player.setLevel(level);
                    }
                }
            }
        }

        //In all of the cases listed above, the records in the returned GameRecord array should be
        //sorted first by score, and then by level in descending order
        //sort the 1D highScoreRecords array (first by score, and then by level in descending order)
        for (int i = 0; i < highScoreRecords.length; i++) {
            int otherIndex = -1;
            GameRecord current1 = highScoreRecords[i];
            for (int j = i+1; j < highScoreRecords.length; j++) {
                GameRecord current2 = highScoreRecords[j];
                if (current2.getScore() > current1.getScore()) {
                    current1 = highScoreRecords[j];
                    otherIndex = j;
                }
                else if (current2.getScore() == current1.getScore()) {
                    if (current2.getLevel() > current1.getLevel()) {
                        current1 = highScoreRecords[j];
                        otherIndex = j;
                    }
                }
            }
            if (otherIndex != -1) {
                GameRecord temp = highScoreRecords[i];
                highScoreRecords[i] = current1;
                highScoreRecords[otherIndex] = temp;
            }
        }
        return highScoreRecords;
    }
}
