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
        Todo todo = mTodos.get(position);

        TextView title = (TextView)convertView.findViewById(R.id.todo_title);
        title.setText(todo.getTitle());

        TextView dueDate = (TextView) convertView.findViewById(R.id.due_date);
        if(todo.getTargetDate()!=-1) {
            dueDate.setVisibility(View.VISIBLE);
            dueDate.setText(todo.getFormattedDate());
        }
        else {
            dueDate.setVisibility(View.GONE);
            dueDate.setText(null);
        }
        return convertView;
    }
}
