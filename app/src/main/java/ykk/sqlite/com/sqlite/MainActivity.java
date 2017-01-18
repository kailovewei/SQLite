package ykk.sqlite.com.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    private Button c_button;
    private Button a_button;
    private Button u_button;
    private Button d_button;
    private Button q_button;
    private EditText editText;
    private MyDatabaseHelper db;
    private Handler handler;
    private  Button clear_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c_button = (Button) findViewById(R.id.create_Id);
        a_button = (Button) findViewById(R.id.add_Id);
        u_button = (Button) findViewById(R.id.uqdate_Id);
        d_button = (Button) findViewById(R.id.delete_Id);
        q_button = (Button) findViewById(R.id.query_Id);
        editText = (EditText) findViewById(R.id.editText_Id);
        clear_button= (Button) findViewById(R.id.clear_button);
        db = new MyDatabaseHelper(this, "BookStore.db", null, 1);
        c_button.setOnClickListener(new CreateListener());
        a_button.setOnClickListener(new AddListener());
        u_button.setOnClickListener(new UpdateListener());
        d_button.setOnClickListener(new DeleteListener());
        q_button.setOnClickListener(new QueryListener());
        clear_button.setOnClickListener(new ClearListener());
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String data = (String) msg.obj;
                   editText.setText(editText.getText()+data);
            }
        };
    }

    class ClearListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            editText.setText(" ");


        }
    }
        //创建数据库监听器
        class CreateListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                db.getWritableDatabase();
            }
        }
        //往数据库中增加数据
        class AddListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                database.insert("Book", null, values);
                values.clear();
                values.put("name", "The Lost Symbol");
                values.put("author", "Dan Brown");
                values.put("pages", 510);
                values.put("price", 19.95);
                database.insert("Book", null, values);
                Toast.makeText(MainActivity.this, "Add data success", Toast.LENGTH_SHORT).show();
            }
        }
        //更新数据库中的数据
        class UpdateListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 12.99);
                database.update("Book", values, "name=?", new String[]{"The Lost Symbol"});
            }
        }
        //删除数据库
        class DeleteListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                SQLiteDatabase database = db.getWritableDatabase();
                database.delete("Book", "pages>?", new String[]{"400"});
            }
        }
        //查询数据库中的数据
        class QueryListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = db.getWritableDatabase();
                Cursor cursor = database.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Message msg = handler.obtainMessage();
                        msg.obj = "name:" + name + "  author:" + author + "  pages:" + pages + "  price:" + price + "     ";
                        handler.sendMessage(msg);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }
    }

