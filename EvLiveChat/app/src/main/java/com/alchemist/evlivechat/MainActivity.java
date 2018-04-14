package com.alchemist.evlivechat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  private Socket socket;
  private EditText e1,e2;
  private Button b1;
  private TextView t1,typing_text,disconnectText;
  private RecyclerView listView;
  private ArrayList<Data> list=new ArrayList<>();
  private MessageAdapter messageAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        listView=findViewById(R.id.listMsg);
        messageAdapter=new MessageAdapter(getApplicationContext(),list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
         listView.setLayoutManager(linearLayoutManager);

        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(messageAdapter);

                messageAdapter.notifyDataSetChanged();;
                listView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });

        e1=findViewById(R.id.handle);
        e2=findViewById(R.id.message);
        typing_text=findViewById(R.id.typing);
        //disconnectText=findViewById(R.id.disconnectText);
        //t1=findViewById(R.id.das);
        b1=findViewById(R.id.send);
        try {
            socket= IO.socket("http://192.168.1.4:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               socket.emit("typing",e1.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        socket.connect();
        socket.on("chat-message",handleIncomingMessageEvent);
        socket.on("typing",handleTypingEvent);

       // socket.on("leave",handleDisconnectPerson);

    }

    private void sendMessage() {
        String textMessage=e2.getText().toString().trim();
        e2.setText("");

        //addMessage(textMessage);
        JSONObject objData = new JSONObject();
        try {
            objData.put("message",textMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            objData.put("handle",e1.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("chat-message",objData);
        typing_text.setText("");
    }

    private Emitter.Listener handleTypingEvent=new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data=(JSONObject) args[0];
                    String person=null;
                    try {
                        person=data.getString("person").toString();
                        typing_text.setText(person +" is typing text....");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
//    private Emitter.Listener handleDisconnectPerson=new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data=(JSONObject) args[0];
//
//                    try {
//                        String disconnect_person=data.getString("person").toString();
//                        disconnectText.setText(disconnect_person +" has leave the chat");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//    };


    private Emitter.Listener handleIncomingMessageEvent=new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
          runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  JSONObject data=(JSONObject) args[0];
                  String msg = null;
                  String name=null;
                  try {
                      msg=data.getString("message").toString();
                      name=data.getString("handle").toString();
                      Data data1=new Data(msg,name);
                      Log.d("msg",msg);
                      typing_text.setText("");
                     //t1.setText(msg);
                      list.add(data1);

                      messageAdapter.notifyDataSetChanged();

                  } catch (JSONException e) {
                      e.printStackTrace();
                  }




              }
          });
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.emit("disconnect",e1.getText().toString());
        socket.disconnect();
        socket.off("chat-message", handleIncomingMessageEvent);

    }
}
