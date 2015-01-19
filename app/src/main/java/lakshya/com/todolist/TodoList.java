package lakshya.com.todolist;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodoList extends Activity {
    private static final String TODO_LIST_FILE = "TODO_LIST_FILE";

    private EditText mEnteredWords;
    private ListView mListView;
    private TodoListAdapater mTodoAdapter;
    private ArrayList<Todo> mTodos;
    private Handler mUIThreadHandler;
    private ExecutorService mSingleThreadExecutor;
    private TodoDbOpenHelper mTodoDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        mTodoDbOpenHelper = new TodoDbOpenHelper(this);
        mEnteredWords = (EditText)findViewById(R.id.et_words);
        Button submitButton = (Button)findViewById(R.id.btnSubmit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });

        mTodos = new ArrayList<Todo>();

        mTodoAdapter = new TodoListAdapater(mTodos, this);

        mListView = (ListView)findViewById(R.id.lv_displaylist);

        mListView.setAdapter(mTodoAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TodoList.this, EditTodo.class);
                intent.putExtra(EditTodo.EXTRA_TODO, mTodos.get(position));
                intent.putExtra(EditTodo.EXTRA_POSITION, position);
                startActivityForResult(intent, 1);

            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Todo deletedTodo = mTodos.remove(position);
                deleteListItem(deletedTodo);
                mTodoAdapter.notifyDataSetChanged();
                return true;
            }
        });

        mSingleThreadExecutor= Executors.newSingleThreadExecutor();
        mUIThreadHandler = new Handler();
        readAll();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK || data==null) {
            return;
        } else {
            Todo editedTodo = (Todo) data.getSerializableExtra(EditTodo.EXTRA_TODO);

            int editedTodoPosition = data.getIntExtra(EditTodo.EXTRA_POSITION, -1);
            if(editedTodoPosition!=-1) {
                Todo todoTemp = mTodos.get(editedTodoPosition);
                todoTemp.setTitle(editedTodo.getTitle());
                todoTemp.setTargetDate(editedTodo.getTargetDate());
                update(todoTemp);
                reorder();
            }
//
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }

    private void onSubmit() {
        if(!TextUtils.isEmpty(mEnteredWords.getText())) {
            String enteredWord = mEnteredWords.getText().toString();
            Todo todo = new Todo(enteredWord, -1, System.currentTimeMillis());
            mTodos.add(todo);
            add(todo);
            mEnteredWords.setText(null);
            reorder();
        }

    }

    private void readAll() {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTodos.addAll(mTodoDbOpenHelper.readAll());
                notifyAdapter();
            }
        });
    }

    private void add(final Todo todo) {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTodoDbOpenHelper.add(todo);
            }
        });
    }

    private void update(final Todo todo) {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTodoDbOpenHelper.update(todo);
            }
        });
    }

    private void deleteListItem(final Todo todo) {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTodoDbOpenHelper.delete(todo);
            }
        });
    }

    private void notifyAdapter()
    {
        mUIThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                mTodoAdapter.notifyDataSetChanged();
            }
        });
    }

   private void reorder(){
       Collections.sort(mTodos,new Comparator<Todo>() {
           @Override
           public int compare(Todo lhs, Todo rhs) {
                if (lhs.getTargetDate()==-1 && rhs.getTargetDate()==-1) {
                    if(lhs.getCreationDate()<rhs.getCreationDate()) {
                        return -1;
                    }else {
                        return 1;
                    }
                }
                if(lhs.getTargetDate()==-1){
                    return -1;
                }
               if (rhs.getTargetDate()==-1){
                   return 1;
               }
               if(lhs.getTargetDate()<rhs.getTargetDate()){
                   return -1;
               }else if(lhs.getTargetDate()>rhs.getTargetDate()){
                   return 1;
               }
               return 0;
           }
       });
       notifyAdapter();
   }
}
