package us.universalpvp.schematic.nms;

/**
 * Created by avigh on 8/20/2016.
 */
public class Schematic {

    private byte[] blocks;
    private byte[] data;
    private short width;
    private short length;
    private short height;
    private String name;

    public Schematic(String name, byte[] blocks, byte[] data, short width, short length, short height) {
        this.blocks = blocks;
        this.data = data;
        this.width = width;
        this.length = length;
        this.height = height;
        this.name = name;
    }

    public byte[] getBlocks() {
        return blocks;
    }

    public byte[] getData() {
        return data;
    }

    public short getWidth() {
        return width;
    }

    public short getLength() {
        return length;
    }

    public short getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }
}
