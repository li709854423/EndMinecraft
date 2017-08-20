package ch.jamiete.mcping;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MinecraftPingWithOutPing {

    public void getPing(final MinecraftPingOptions options) throws Exception {
        MinecraftPingUtil.validate(options.getHostname(), "Hostname cannot be null.");
        MinecraftPingUtil.validate(options.getPort(), "Port cannot be null.");
        Socket socket;
        if (options.getProxy() == null) {

            socket = new Socket();
        } else {
            socket = new Socket(options.getProxy());
        }
        try {
            socket.connect(new InetSocketAddress(options.getHostname(), options.getPort()), options.getTimeout());
        }catch (Exception e) {

        }

        final DataInputStream in = new DataInputStream(socket.getInputStream());
        final DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        //> Handshake

        ByteArrayOutputStream handshake_bytes = new ByteArrayOutputStream();
        DataOutputStream handshake = new DataOutputStream(handshake_bytes);

        handshake.writeByte(MinecraftPingUtil.PACKET_HANDSHAKE);
        MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.PROTOCOL_VERSION);
        MinecraftPingUtil.writeVarInt(handshake, options.getHostname().length());
        handshake.writeBytes(options.getHostname());
        handshake.writeShort(options.getPort());
        MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.STATUS_HANDSHAKE);

        MinecraftPingUtil.writeVarInt(out, handshake_bytes.size());
        out.write(handshake_bytes.toByteArray());

        //> Status request

        out.writeByte(0x01); // Size of packet
        out.writeByte(MinecraftPingUtil.PACKET_STATUSREQUEST);

        handshake.close();
        handshake_bytes.close();
        out.close();
        in.close();
        socket.close();
    }

}
