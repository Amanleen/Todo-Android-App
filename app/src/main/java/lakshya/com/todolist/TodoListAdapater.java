package lakshya.com.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TodoListAdapater extends BaseAdapter {

    List<Todo> mTodos;
    Context mContext;

    public TodoListAdapater(ArrayList<Todo> todos, Context context) {
        mTodos = todos;
        mContext = context;
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
        return mTodos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.list_view_row_item, parent, false);
        }
        TextView listItem = (TextView)convertView.findViewById(R.id.todo_title);
        Todo todo = mTodos.get(position);
        listItem.setText(todo.getTitle());
        return convertView;
    }
}
