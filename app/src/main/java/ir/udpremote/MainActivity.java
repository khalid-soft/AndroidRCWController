package ir.udpremote;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.cacheColorHint;
import static android.R.attr.data;

public class MainActivity extends AppCompatActivity implements Button.OnTouchListener {

    static String serverIP = "192.168.4.1";
    static String serverPort = "10000";

    DatagramSocket udpSocket;

    @BindView(R.id.btnL1) Button btnL1;
    @BindView(R.id.btnL2) Button btnL2;
    @BindView(R.id.btnR1) Button btnR1;
    @BindView(R.id.btnR2) Button btnR2;

    @BindView(R.id.btnA) Button btnA;
    @BindView(R.id.btnB) Button btnB;
    @BindView(R.id.btnX) Button btnX;
    @BindView(R.id.btnY) Button btnY;

    @BindView(R.id.btnSelect) Button btnSelect;
    @BindView(R.id.btnStart) Button btnStart;
    @BindView(R.id.btnOn) Button btnOn;

    @BindView(R.id.btnUp) Button btnUp;
    @BindView(R.id.btnLeft) Button btnLeft;
    @BindView(R.id.btnRight) Button btnRight;
    @BindView(R.id.btnDown) Button btnDown;

    @BindView(R.id.txtIPPort) TextView txtIPPort;
    @BindView(R.id.txtResult) TextView txtResult;
    @BindView(R.id.txtListen) TextView txtListen;

    DialogPlus changePortAndIPDialog;
    EditText ip1, ip2, ip3, ip4, port;

    Map<String, BufferAbs> Buffer = new HashMap<>();

    public void initializeBuffer() {
        Buffer.put("UP", new BufferAbs(
                "0, 1, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 0, (byte) 1, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));

        Buffer.put("DOWN", new BufferAbs(
                "0, 2, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 0, (byte) 2, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));

        Buffer.put("RIGHT", new BufferAbs(
                "0, 4, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 0, (byte) 4, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));

        Buffer.put("LEFT", new BufferAbs(
                "0, 8, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 0, (byte) 8, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));

        Buffer.put("L1", new BufferAbs(
                "2, 0, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 2, (byte) 0, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));

        Buffer.put("L2", new BufferAbs(
                "4, 0, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 4, (byte) 0, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));

        Buffer.put("R1", new BufferAbs(
                "8, 0, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 8, (byte) 0, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));

        Buffer.put("R2", new BufferAbs(
                "16, 0, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 16, (byte) 0, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));

        Buffer.put("Y", new BufferAbs(
                "0, 16, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 0, (byte) 16, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));

        Buffer.put("A", new BufferAbs(
                "0, 32, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 0, (byte) 32, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));

        Buffer.put("B", new BufferAbs(
                "0, 64, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 0, (byte) 64, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));

        Buffer.put("X", new BufferAbs(
                "1, 0, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 1, (byte) 0, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));

        Buffer.put("START", new BufferAbs(
                "0, 3, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 0, (byte) 3, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));

        Buffer.put("SELECT", new BufferAbs(
                "0, 12, 128, 128, 128, 128, 0, 0, 0, 1",
                new byte[]{(byte) 0, (byte) 12, (byte) 128, (byte) 128, (byte) 128, (byte) 128, 0, 0, 0, 1}
        ));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeBuffer();

        try {
            udpSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        btnL1.setOnTouchListener(this);
        btnL2.setOnTouchListener(this);
        btnR1.setOnTouchListener(this);
        btnR2.setOnTouchListener(this);
        btnA.setOnTouchListener(this);
        btnB.setOnTouchListener(this);
        btnX.setOnTouchListener(this);
        btnY.setOnTouchListener(this);
        btnSelect.setOnTouchListener(this);
        btnStart.setOnTouchListener(this);
        btnOn.setOnTouchListener(this);
        btnUp.setOnTouchListener(this);
        btnLeft.setOnTouchListener(this);
        btnRight.setOnTouchListener(this);
        btnDown.setOnTouchListener(this);

        txtIPPort.setText(serverIP + ":" + serverPort);

        changePortAndIPDialog = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.picker_ip_port))
                .setCancelable(true)
                .setExpanded(false)
                .setGravity(Gravity.BOTTOM)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOverlayBackgroundResource(android.R.color.transparent)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.confirm) {
                            serverIP = ip1.getText().toString() + "." + ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString();
                            serverPort = port.getText().toString();

                            udpListener receiveSocket = new udpListener();
                            receiveSocket.execute("");

                            SharedPreferences sPref = getSharedPreferences("cache", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sPref.edit();
                            editor.putString("ip1", ip1.getText().toString());
                            editor.putString("ip2", ip2.getText().toString());
                            editor.putString("ip3", ip3.getText().toString());
                            editor.putString("ip4", ip4.getText().toString());
                            editor.putString("port", port.getText().toString());
                            editor.apply();

                            dialog.dismiss();
                        }
                    }
                })
                .create();
        changePortAndIPDialog.show();

        ip1 = (EditText) changePortAndIPDialog.findViewById(R.id.ip1);
        ip2 = (EditText) changePortAndIPDialog.findViewById(R.id.ip2);
        ip3 = (EditText) changePortAndIPDialog.findViewById(R.id.ip3);
        ip4 = (EditText) changePortAndIPDialog.findViewById(R.id.ip4);
        port = (EditText) changePortAndIPDialog.findViewById(R.id.port);

        SharedPreferences sPref = getSharedPreferences("cache", Context.MODE_PRIVATE);
        ip1.setText(sPref.getString("ip1", "192"));
        ip2.setText(sPref.getString("ip2", "168"));
        ip3.setText(sPref.getString("ip3", "4"));
        ip4.setText(sPref.getString("ip4", "1"));
        port.setText(sPref.getString("port", "10000"));
    }

    private void sendPacket(final String key) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName(serverIP);
                    byte[] buffer = Buffer.get(key).getBuffer();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddr, Integer.parseInt(serverPort));
                    udpSocket.send(packet);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtResult.setText("Send " + Buffer.get(key).getStr());
                        }
                    });
                } catch (SocketException e) {
                    Log.e("Udp:", "Socket Error:", e);
                } catch (IOException e) {
                    Log.e("Udp Send:", "IO Error:", e);
                }
            }
        }).start();
    }

    private class udpListener extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... f_url) {
            try {
                byte buffer[] = new byte[2000];

                DatagramPacket p = new DatagramPacket(buffer, buffer.length);
                try {
                    DatagramSocket ds = new DatagramSocket(Integer.parseInt(serverPort) + 1);

                    while (true) {
                        ds.receive(p);
                        final String recievedText = new String(buffer, 0, p.getLength());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtListen.setText(recievedText);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        LinearLayout arrows = (LinearLayout) findViewById(R.id.img_arrows);
        int action = event.getAction();

        switch (v.getId()) {
            case R.id.btnOn:
                changePortAndIPDialog.show();
                break;
            case R.id.btnUp:
                buttonClicked(arrows, "UP", action, R.mipmap.ic_btn_arrows, R.mipmap.ic_btn_arrows_up_pressed);
                break;
            case R.id.btnDown:
                buttonClicked(arrows, "DOWN", action, R.mipmap.ic_btn_arrows, R.mipmap.ic_btn_arrows_down_pressed);
                break;
            case R.id.btnRight:
                buttonClicked(arrows, "RIGHT", action, R.mipmap.ic_btn_arrows, R.mipmap.ic_btn_arrows_right_pressed);
                break;
            case R.id.btnLeft:
                buttonClicked(arrows, "LEFT", action, R.mipmap.ic_btn_arrows, R.mipmap.ic_btn_arrows_left_pressed);
                break;
            case R.id.btnY:
                buttonClicked(v, "Y", action, R.mipmap.ic_btn_y, R.mipmap.ic_btn_y_pressed);
                break;
            case R.id.btnA:
                buttonClicked(v, "A", action, R.mipmap.ic_btn_a, R.mipmap.ic_btn_a_pressed);
                break;
            case R.id.btnB:
                buttonClicked(v, "B", action, R.mipmap.ic_btn_b, R.mipmap.ic_btn_b_pressed);
                break;
            case R.id.btnX:
                buttonClicked(v, "X", action, R.mipmap.ic_btn_x, R.mipmap.ic_btn_x_pressed);
                break;
            case R.id.btnStart:
                sendPacket("START");
                break;
            case R.id.btnSelect:
                sendPacket("SELECT");
                break;
            case R.id.btnL1:
                buttonClicked(v, "L1", action, R.mipmap.ic_btn_l1, R.mipmap.ic_btn_l1_pressed);
                break;
            case R.id.btnL2:
                buttonClicked(v, "L2", action, R.mipmap.ic_btn_l2, R.mipmap.ic_btn_l2_pressed);
                break;
            case R.id.btnR1:
                buttonClicked(v, "R1", action, R.mipmap.ic_btn_r1, R.mipmap.ic_btn_r1_pressed);
                break;
            case R.id.btnR2:
                buttonClicked(v, "R2", action, R.mipmap.ic_btn_r2, R.mipmap.ic_btn_r2_pressed);
                break;
        }
        return true;
    }

    void buttonClicked(View v, String packet, int action, int defaultImage, int pressedImage) {
        if (action == MotionEvent.ACTION_DOWN) {
            v.setBackground(ContextCompat.getDrawable(getBaseContext(), pressedImage));
            sendPacket(packet);
        } else if (action == MotionEvent.ACTION_UP) {
            v.setBackground(ContextCompat.getDrawable(getBaseContext(), defaultImage));
        }
    }
}
