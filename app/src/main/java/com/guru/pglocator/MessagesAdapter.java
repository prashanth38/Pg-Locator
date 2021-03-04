package com.guru.pglocator;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.guru.pglocator.models.Messages;
import com.guru.pglocator.utils.Store;
import com.guru.pglocator.utils.ToastHelper;

import java.util.HashMap;
import java.util.List;

class MessagesAdapter  extends BaseAdapter {
    Context context;
    List<Messages> messages;
    HashMap map;
    DatabaseReference mdatabaseref;

    public MessagesAdapter(MessageList messageList, List<Messages> messageViewList) {
        this.messages=messageViewList;
        this.context=messageList;
        map= Store.getUserDetails(context);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_message_row,null);
        final Messages msg = messages.get(position);


        if(map.get("type").toString().equalsIgnoreCase(msg.getType().toString().trim())) {
           /* TextView tvhostelname = view.findViewById(R.id.tvhostelname);
            TextView tvmsg = view.findViewById(R.id.tvmsg);

            tvhostelname.setText(msg.getHostelname());
            tvmsg.setText(msg.getMessage());*/
            TextView tvhostelname = view.findViewById(R.id.tvhostelname);
            TextView tvmsg = view.findViewById(R.id.tvmsg);
            Button btmsg=view.findViewById(R.id.btmsg);
            LinearLayout ll=view.findViewById(R.id.layout);
tvhostelname.setVisibility(View.INVISIBLE);tvmsg.setVisibility(View.INVISIBLE);btmsg.setVisibility(View.INVISIBLE);ll.setVisibility(View.INVISIBLE);
view=new Space(context);

        }else{
            if(map.get("type").toString().equalsIgnoreCase("user")){
                TextView tvhostelname = view.findViewById(R.id.tvhostelname);
                TextView tvmsg = view.findViewById(R.id.tvmsg);
                Button btmsg=view.findViewById(R.id.btmsg);

                tvhostelname.setText(msg.getHostelname());
                tvmsg.setText(msg.getMessage());
                btmsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastHelper.toastMsg(context,"message");
                        showDialogUpdate(context, msg);
                    }
                });


            }else {
                TextView tvhostelname = view.findViewById(R.id.tvhostelname);
                TextView tvmsg = view.findViewById(R.id.tvmsg);
                Button btmsg=view.findViewById(R.id.btmsg);

                tvhostelname.setText(msg.getUsername());
                tvmsg.setText(msg.getMessage());
                btmsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastHelper.toastMsg(context,"message");
                        showDialogUpdate(context, msg);
                    }
                });
            }
        }

        return view;
    }

    public void showDialogUpdate(final Context context, final Messages msg) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_send_message);
        dialog.setTitle("Message");
        final EditText etmessage=dialog.findViewById(R.id.etmessage);
        Button btsubmit=dialog.findViewById(R.id.btsubmit);
        Button btcancel=dialog.findViewById(R.id.btcancel);

        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messages=etmessage.getText().toString();
             //   String pgkey=pgDetails.getKey();


                mdatabaseref= FirebaseDatabase.getInstance().getReference("messages");

               String messageid=mdatabaseref.push().getKey();
                Messages messages1=new Messages(map.get("username").toString(),msg.getOwnerid(),messages,msg.getHostelname());
                messages1.setUsername(map.get("name").toString().trim());
                messages1.setType(map.get("type").toString().trim());
                mdatabaseref.child(messageid).setValue(messages1);
                //addMessageChangeListener();

            }
        });

        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.5);
        dialog.getWindow().setLayout(width, height);


        dialog.show();




    }
}
