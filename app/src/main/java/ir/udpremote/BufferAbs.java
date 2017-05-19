package ir.udpremote;

public class BufferAbs {
    private byte[] Buffer;
    private String Str;

    public BufferAbs(String str, byte[] buffer) {
        Buffer = buffer;
        Str = str;
    }

    public byte[] getBuffer() {
        return Buffer;
    }

    public void setBuffer(byte[] buffer) {
        Buffer = buffer;
    }

    public String getStr() {
        return Str;
    }

    public void setStr(String str) {
        Str = str;
    }
}
