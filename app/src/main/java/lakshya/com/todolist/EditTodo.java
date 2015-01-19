package lakshya.com.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditTodo extends Activity {

    static String EXTRA_TODO = "EXTRA_TODO";
    static String EXTRA_POSITION = "EXTRA_POSITION";

    private EditText mTodoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_edit_todo);
        mTodoTitle = (EditText)findViewById(R.id.todo_title);

        Intent intent = getIntent();
        final Todo todo = (Todo)intent.getSerializableExtra(EXTRA_TODO);

        mTodoTitle.setText(todo.getTitle());
        mTodoTitle.setSelection((todo.getTitle()).length());

        Button saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndFinish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            saveAndFinish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndFinish()
    {
        final int position = (int)getIntent().getIntExtra(EXTRA_POSITION, -1);
        final Todo todo = (Todo)getIntent().getSerializableExtra(EXTRA_TODO);
        if(!TextUtils.isEmpty(mTodoTitle.getText())) {
            String newTitle = mTodoTitle.getText().toString();
            todo.setTitle(newTitle);
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
}
