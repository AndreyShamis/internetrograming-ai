/*
 * This class provide tools for intelectual work with URL voting
 * and open the template in the editor.
 */
//=============================================================================
//=============================================================================
//=============================================================================
/**
 *  URL voting class
 * @author Andrey Shamis & Ilia Gaysinsky
 */

public class URLclass {
    private String URLname;         //  URL
    private double points;          //  All points
    private int voteCount;          //  Number of votes
//=============================================================================
    /**
     * Constructor function
     * @param URL the url name of this url
     */
    public URLclass(String URL){
        URLname     =   URL;        //  set the url
        voteCount   =   0;          //  set voting counter
        points      =   0;          //  set initializing points
    }
//=============================================================================
    /**
     * Function wich sets the new points of voting for this url
     * @param newValue the value
     * @return true if success or false on fail
     */
    public boolean SetPoints(int newValue){
        if(points < 0 || points > 10){
            return(false);          //  bad value
        }else{
            //  manipulation with previous resuls
            points = (points*voteCount + newValue)/++voteCount;
        }
        return(true);       //  return true if success
    }
//=============================================================================
    /**
     * Function wicg return the URL
     * @return the url
     */
    public String GetURLName(){
        return(URLname);    //  return the url
    }
//=============================================================================
    /**
     * Function wich return the curent value of voting reults
     * @return the voting results
     */
    public double getPoints(){
        return(points);     //  return the voting results
    }

}
//=============================================================================
//=============================================================================
//=============================================================================