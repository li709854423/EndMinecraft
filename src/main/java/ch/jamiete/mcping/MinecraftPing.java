package ch.jamiete.mcping;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MinecraftPing {

    public String getPing(final String hostname) throws IOException {
        return this.getPing(new MinecraftPingOptions().setHostname(hostname));
    }

    public String getPing(final MinecraftPingOptions options) {
        try {
            MinecraftPingUtil.validate(options.getHostname(), "Hostname cannot be null.");
            MinecraftPingUtil.validate(options.getPort(), "Port cannot be null.");
            Socket socket;
            if (options.getProxy() == null) {

                socket = new Socket();
            } else {
                socket = new Socket(options.getProxy());
            }
            socket.connect(new InetSocketAddress(options.getHostname(), options.getPort()), options.getTimeout());

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

            //< Status response

            MinecraftPingUtil.readVarInt(in); // Size
            int id = MinecraftPingUtil.readVarInt(in);

            MinecraftPingUtil.io(id == -1, "Server prematurely ended stream.");
            MinecraftPingUtil.io(id != MinecraftPingUtil.PACKET_STATUSREQUEST, "Server returned invalid packet.");

            int length = MinecraftPingUtil.readVarInt(in);
            MinecraftPingUtil.io(length == -1, "Server prematurely ended stream.");
            MinecraftPingUtil.io(length == 0, "Server returned unexpected value.");

            byte[] data = new byte[length];
            in.readFully(data);
            String json = new String(data, options.getCharset());
            try {
                socket.setSoTimeout(options.getTimeout());
                //> Ping
                out.writeByte(0x09); // Size of packet
                out.writeByte(MinecraftPingUtil.PACKET_PING);
                out.writeLong(System.currentTimeMillis());

                //< Ping

                MinecraftPingUtil.readVarInt(in); // Size
                id = MinecraftPingUtil.readVarInt(in);
                MinecraftPingUtil.io(id == -1, "Server prematurely ended stream.");
                MinecraftPingUtil.io(id != MinecraftPingUtil.PACKET_PING, "Server returned invalid packet.");
            } catch (Exception e) {
                System.out.println("服务器禁ping！！！");
            }
            // Close

            handshake.close();
            handshake_bytes.close();
            out.close();
            in.close();
            socket.close();

            return json;
        }catch (Exception ignored) {

        }
        return null;
    }

}