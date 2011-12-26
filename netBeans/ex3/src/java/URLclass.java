/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Admin
 */
public class URLclass {
    private String URLname;
    private int points;
    
    public URLclass(String URL)
    {
        System.out.println("YES" + URL);
        URLname = URL;
        System.out.println("YES1" + URL);
    }
    public boolean SetPoints(int newValue)
    {
        if(points < 0 || points > 10)
        {
            return(false);
        }else{
            points = newValue;
        }
        return(true);
    }
    public String GetURLName()
    {
        return(URLname);
    }
    public int getPoints()
    {
        return(points);
    }
}
