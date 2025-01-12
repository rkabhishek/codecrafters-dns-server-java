import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Main {
  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

     try (DatagramSocket serverSocket = new DatagramSocket(2053)) {
       while (true) {
         final byte[] buf = new byte[512];
         final DatagramPacket packet = new DatagramPacket(buf, buf.length);
         serverSocket.receive(packet);
         System.out.println("Received data");

         final byte[] bufResponse = new byte[512];
         DNSHeader header = new DNSHeader();
         header.setId((short)1234);
         header.setQR((short)1);

         serializeHeader(header, bufResponse);
         System.out.println(Arrays.toString(bufResponse));
         final DatagramPacket packetResponse = new DatagramPacket(bufResponse, bufResponse.length, packet.getSocketAddress());
         serverSocket.send(packetResponse);
       }
     } catch (IOException e) {
         System.out.println("IOException: " + e.getMessage());
     }
  }

    public static void serializeHeader(DNSHeader header, byte[] bufResponse) {
        ByteBuffer buffer = ByteBuffer.wrap(bufResponse).order(ByteOrder.BIG_ENDIAN);

        buffer.putShort(header.getId());             // ID
        buffer.putShort(header.getFlag());          // Flags
        buffer.putShort(header.getQdCount());      // Number of Questions
        buffer.putShort(header.getAnCount());      // Number of Answer RRs
        buffer.putShort(header.getNsCount());   // Number of Authority RRs
        buffer.putShort(header.getArCount());  // Number of Additional RRs
    }
}





class DNSHeader {
    short id;
    short qdCount;
    short anCount;
    short nsCount;
    short arCount;
    short flags;

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public short getQdCount() {
        return qdCount;
    }

    public void setQdCount(short qdCount) {
        this.qdCount = qdCount;
    }

    public short getAnCount() {
        return anCount;
    }

    public void setAnCount(short anCount) {
        this.anCount = anCount;
    }

    public short getNsCount() {
        return nsCount;
    }

    public void setNsCount(short nsCount) {
        this.nsCount = nsCount;
    }

    public short getArCount() {
        return arCount;
    }

    public void setArCount(short arCount) {
        this.arCount = arCount;
    }

    public short getQR() {
        return (short) ((flags & (1 << 15)) >> 15);
    }
    public void setQR(short qr) {
        if (qr != 0 && qr != 1) {
            throw new IllegalArgumentException("QR must be 0 or 1");
        }

        if (qr == 1) {
            flags = (short) (flags | (1 << 15));
        } else {
            flags = (short) (flags & ~(1 << 15));
        }
    }

    public short getOpcode() {
        return (short) ((flags >> 11) & (0b1111));
    }

    public void setOpcode(short opcode) {
        if (opcode < 0 || opcode > 15) {
            throw new IllegalArgumentException("Opcode must be between 0 and 15");
        }

        flags = (short) (flags & ~(0b1111 << 11));
        flags = (short) (flags | (opcode << 11));
    }

    public short getRcode() {
        return (short) (flags & 0b1111);
    }

    public void setRcode(short rcode) {
        if (rcode < 0 || rcode > 15) {
            throw new IllegalArgumentException("Rcode must be between 0 and 15");
        }

        flags = (short) (flags & ~0b1111);
        flags = (short) (flags | rcode);

    }

    public int getZ() {
        return flags & (0b111 << 4);
    }

    public void setZ(int z) {
        if (z < 0 || z > 7) {
            throw new IllegalArgumentException("Z must be between 0 and 7");
        }

        flags = (short) (flags & ~(0b111 << 4));
        flags = (short) (flags | (z << 4));
    }


    public int getAA() {
        return (flags & (1 << 10)) >> 10;
    }

    public void setAA(int aa) {
        if (aa != 0 && aa != 1) {
            throw new IllegalArgumentException("AA must be 0 or 1");
        }

        if (aa == 1) {
            flags = (short) (flags | (1 << 10));
        } else {
            flags = (short) (flags & ~(1 << 10));
        }
    }

    public int getTC() {
        return (flags & (1 << 9)) >> 9;
    }

    public void setTC(int tc) {
        if (tc != 0 && tc != 1) {
            throw new IllegalArgumentException("TC must be 0 or 1");
        }

        if (tc == 1) {
            flags = (short) (flags | (1 << 9));
        } else {
            flags = (short) (flags & ~(1 << 9));
        }
    }

    public int getRD() {
        return (flags & (1 << 8)) >> 8;
    }

    public void setRD(int rd) {
        if (rd != 0 && rd != 1) {
            throw new IllegalArgumentException("RD must be 0 or 1");
        }

        if (rd == 1) {
            flags = (short) (flags | (1 << 8));
        } else {
            flags = (short) (flags & ~(1 << 8));
        }
    }

    public int getRA() {
        return (flags & (1 << 7)) >> 7;
    }

    public void setRA(int ra) {
        if (ra != 0 && ra != 1) {
            throw new IllegalArgumentException("RA must be 0 or 1");
        }

        if (ra == 1) {
            flags = (short) (flags | (1 << 7));
        } else {
            flags = (short) (flags & ~(1 << 7));
        }
    }

    public short getFlag() {
        return flags;
    }

}
