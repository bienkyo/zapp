package vn.com.z11.z11app.RestAPI;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kienlv58 on 12/30/16.
 */
public interface onEventListenter {
    public void eventCheck(int next);
    public void eventNext(ArrayList<HashMap<Integer, HashMap<Integer,Boolean>>> user_answer);
}
