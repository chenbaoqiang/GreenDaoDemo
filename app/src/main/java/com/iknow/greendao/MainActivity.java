package com.iknow.greendao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.iknow.greendao.bean.Message;
import com.iknow.greendao.db.GreenDaoManager;


import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button delete;
    private Button add;
    private Button update;
    private Button query;
    private MessageDBHelper messageDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GreenDaoManager.getInstance().init(this.getApplication(),"fenno.db",true,"1234");
        messageDBHelper = new MessageDBHelper(this.getApplication());
        initView();


    }

    private void initView() {
        add = findViewById(R.id.bt_add);
        delete = findViewById(R.id.bt_delete);
        update = findViewById(R.id.bt_update);
        query = findViewById(R.id.bt_query);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        update.setOnClickListener(this);
        query.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_add) {
            Message message = new Message();
            message.setContent("1");
            message.setUri("15510461830");
            messageDBHelper.insertMessage(message);
        }else if (v.getId() == R.id.bt_query) {

            messageDBHelper.query();

        }

    }


}
