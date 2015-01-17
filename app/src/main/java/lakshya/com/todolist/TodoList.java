package lakshya.com.todolist;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class TodoList extends Activity {

    private EditText mEnteredWords;
    private ListView mListView;
    private ArrayAdapter<Todo> mTodoAdapter;
    private ArrayList<Todo> mTodos;
    private static final String TODO_LIST_FILE = "TODO_LIST_FILE";
    private Handler mUIThreadHandler;
    private ExecutorService mSingleThreadExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        mEnteredWords = (EditText)findViewById(R.id.et_words);
        Button submitButton = (Button)findViewById(R.id.btnSubmit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onSubmit();
            }
        });


        mTodos = new ArrayList<Todo>();

        mTodoAdapter = new ArrayAdapter<Todo>(this, android.R.layout.simple_list_item_1, mTodos);

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
        if(resultCode!=RESULT_OK || data==null)
        {
            return;
        }
        else
        {
            Todo editedTodo = (Todo) data.getSerializableExtra(EditTodo.EXTRA_TODO);

            int editedTodoPosition = data.getIntExtra(EditTodo.EXTRA_POSITION, -1);
            if(editedTodoPosition!=-1)
            {
                Todo todoTemp = mTodos.get(editedTodoPosition);
                todoTemp.setmTitle(editedTodo.getmTitle());
                mTodoAdapter.notifyDataSetChanged();
                update(todoTemp);
            }
//
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }

    private void onSubmit()
    {
        if(!TextUtils.isEmpty(mEnteredWords.getText()))
        {
            String enteredWord = mEnteredWords.getText().toString();
            Todo todo = new Todo(mTodos.size(), enteredWord);
            mTodoAdapter.add(todo);
            writeAll();
            mEnteredWords.setText(null);

        }

    }

    private void readAll()
    {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                readTodoListFile();
            }
        });
    }

    private void writeAll()
    {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                saveTodoListToFile();
            }
        });
    }

    private void update(Todo todo)
    {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                saveTodoListToFile();
            }
        });
    }

    private void deleteListItem(Todo todo)
    {
        mSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                saveTodoListToFile();
            }
        });
    }
    private synchronized void readTodoListFile()
    {
        try {
            FileInputStream fIn = this.openFileInput(TODO_LIST_FILE);
            ObjectInputStream oInStream = new ObjectInputStream(fIn);
            mTodos.addAll((ArrayList<Todo>) oInStream.readObject());
            mUIThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTodoAdapter.notifyDataSetChanged();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private synchronized void saveTodoListToFile()
    {
        try {
            FileOutputStream fOut = this.openFileOutput(TODO_LIST_FILE, MODE_PRIVATE);
            ObjectOutputStream oOutStream = new ObjectOutputStream(fOut);
            oOutStream.writeObject(mTodos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
