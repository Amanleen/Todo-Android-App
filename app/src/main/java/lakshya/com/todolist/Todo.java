package lakshya.com.todolist;

import java.io.Serializable;

/**
 * Created by Amanleen on 1/14/15.
 */
public class Todo implements Serializable{

    private String mTitle;
    private long mTargetDate;
    private long mId;

    public Todo(String mTitle, long mTargetDate) {
        this.mTitle = mTitle;
        this.mTargetDate = mTargetDate;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public long getTargetDate() {
        return mTargetDate;
    }

    public void setTargetDate(long mTargetDate) {
        this.mTargetDate = mTargetDate;
    }

    @Override
    public String toString() {
        return mTitle;
    }

}
