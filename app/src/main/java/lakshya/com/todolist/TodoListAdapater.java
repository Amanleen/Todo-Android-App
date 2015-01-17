package lakshya.com.todolist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class TodoListAdapater extends BaseAdapter {

    List<Todo> mTodos;

    public TodoListAdapater() {
        mTodos=new ArrayList<Todo>();
    }

    @Override
    public int getCount() {
        return mTodos.size();
    }

    @Override
    public Todo getItem(int position) {
        return mTodos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTodos.get(position).getmId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
