package kr.co.hoon.a180523sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // 데이터베이스 사용 클래스 생성
    class WordDBHelper extends SQLiteOpenHelper {
        public WordDBHelper(Context context){
            //데이터베이스 파일 생성
            //이 파일은 애플리케이션의 데이터 영역에 생성
            super(context, "EngWord.db", null, 1);
        }
        // 파일이 만들어지고 난 후 호출되는 메소드
        // 테이블을 생성
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table dic(_id integer primary key autoincrement, eng text, han text);");
        }

        // SQLite가 업데이트되었을 때 호출되는 메소드
        // 테이블을 삭제하고 다시 생성
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists dic;");
            onCreate(db);
        }
    }

    WordDBHelper dbHelper ;
    TextView display;

    // Activity가 생성되고 난 후 가장 먼저 호출되는 메소드
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 인스턴스 변수의 초기화
        dbHelper = new WordDBHelper(this);
        display = (TextView)findViewById(R.id.display);

        // 데이터 삽입
        Button insert = (Button)findViewById(R.id.insert);
        insert.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 데이터베이스 열기
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // SQL 없이 삽입
                ContentValues value = new ContentValues();
                value.put("eng", "orange");
                value.put("han", "오렌지");
                db.insert("dic", null, value);
                // SQL로 삽입
                db.execSQL("insert into dic values(null,'tree', '나무');");
                display.setText("데이터가 삽입되었습니다.");
                db.close();
            }
        });

        // 데이터 읽기
        Button select = (Button)findViewById(R.id.select);
        select.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // DB 열기
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                // 조회하는 SQL
                Cursor cursor = db.rawQuery("select * from dic", null);
                String result = "";
                while(cursor.moveToNext()){
                    String eng = cursor.getString(1);
                    String han = cursor.getString(2);
                    result += eng + ":" + han + "\n";
                }
                display.setText(result);
                cursor.close();
                db.close();
            }
        });

        // 데이터 수정
        Button update = (Button)findViewById(R.id.update);
        update.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // DB 열기
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                ContentValues row = new ContentValues();
                row.put("han", "감귤");
                db.update("dic", row, "eng='orange'", null);
                display.setText("수정되었습니다.");
                db.close();
            }
        });

        // 데이터 삭제
        Button delete = (Button)findViewById(R.id.delete);
        delete.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // DB 열기
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                db.delete("dic", "eng='orange'", null);
                display.setText("삭제되었습니다.");
                db.close();
            }
        });
    }
}