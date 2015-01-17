package lakshya.com.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import lakshya.com.todolist.R;

public class EditTodo extends Activity {

    static String EXTRA_TODO = "EXTRA_TODO";
    static String EXTRA_POSITION = "EXTRA_POSITION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        final EditText todoTitle = (EditText)findViewById(R.id.todo_title);

        Intent intent = getIntent();
        final Todo todo = (Todo)intent.getSerializableExtra(EXTRA_TODO);
        final int position = (int)intent.getIntExtra(EXTRA_POSITION,-1);

        todoTitle.setText(todo.getmTitle());
        todoTitle.setSelection((todo.getmTitle()).length());

        Button saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(todoTitle.getText())) {
                    String newTitle = todoTitle.getText().toString();
                    todo.setmTitle(newTitle);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(EXTRA_TODO, todo);
                    resultIntent.putExtra(EXTRA_POSITION,position);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
                else
                {
                    Toast.makeText(EditTodo.this, "Todo title cannot be empty", Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
