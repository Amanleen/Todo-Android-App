package lakshya.com.todolist;

import java.io.Serializable;

/**
 * Created by Amanleen on 1/14/15.
 */
public class Todo implements Serializable{

   private String mTitle;

    @Override
    public String toString() {
        return mTitle;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    private int mId;

    public Todo(int mId, String mTitle) {
        this.mId = mId;
        this.mTitle = mTitle;
    }



    public String getmTitle() {

        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
