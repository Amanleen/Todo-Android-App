package lakshya.com.todolist;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Amanleen on 1/14/15.
 */
public class Todo implements Serializable{

    private String mTitle;
    private long mTargetDate;
    private long mId;
    private long mCreationDate;

    public Todo(String mTitle, long mTargetDate, long creationDate) {
        this.mTitle = mTitle;
        this.mTargetDate = mTargetDate;
        this.mCreationDate = creationDate;
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

    public long getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(long mCreationDate) {
        this.mCreationDate = mCreationDate;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public String getFormattedDate()
    {
        Date date = new Date(mTargetDate);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM");
        return sdf.format(date);
    }
}
