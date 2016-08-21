package us.universalpvp.schematic.nms;

import com.flowpowered.nbt.*;
import com.flowpowered.nbt.stream.NBTInputStream;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.Chunk;
import net.minecraft.server.v1_10_R1.IBlockData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by avigh on 8/20/2016.
 */
public class SchematicLoader {

    public void pasteSchematic(Location loc, World w, Schematic schematic) {
        byte[] blocks = schematic.getBlocks(),
                blockData = schematic.getData();

        short length = schematic.getLength(),
                width = schematic.getWidth(),
                height = schematic.getHeight();

        Queue<Block> queue = new ConcurrentLinkedQueue<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    Block block = new Location(w, x, y, z).getBlock();

                    queue.add(block);

                    setBlockFast(w, x, y, z, block.getTypeId(), block.getData());
                }
            }
        }
    }

    public void createSchematic(String name, World w, CuboidSelection selection) throws IOException {
        File file = new File(name + ".schematic");
    }


    public Schematic loadSchematic(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        NBTInputStream nbtStream = new NBTInputStream(inputStream);

        CompoundTag schematicTag = (CompoundTag) nbtStream.readTag();
        if (!schematicTag.getName().equals("Schematic")) {
            nbtStream.close();
            throw new IllegalArgumentException("Tag \"Schematic\" does not exist or is not first");
        }

        Map<String, Tag> schematic = (Map) schematicTag.getValue();

        if (!schematic.containsKey("Blocks")) {
            nbtStream.close();
            throw new IllegalArgumentException("Schematic file is missing a \"Blocks\" tag");
        }

        short width = getChildTag(schematic, "Width", ShortTag.class).getValue();
        short length = getChildTag(schematic, "Length", ShortTag.class).getValue();
        short height = getChildTag(schematic, "Height", ShortTag.class).getValue();

        String materials = getChildTag(schematic, "Materials", StringTag.class).getValue();
        if (!materials.equals("Alpha")) {
            nbtStream.close();
            throw new IllegalArgumentException("Schematic file is not an Alpha schematic");
        }

        byte[] blocks = getChildTag(schematic, "Blocks", ByteArrayTag.class).getValue();
        byte[] blockData = getChildTag(schematic, "Data", ByteArrayTag.class).getValue();

        nbtStream.close();

        return new Schematic(file.getName(), blocks, blockData, width, length, height);
    }

    private static <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws IllegalArgumentException {
        if (!items.containsKey(key)) {
            throw new IllegalArgumentException("Schematic file is missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(key + " tag is not of tag type " + expected.getName());
        }


        return expected.cast(tag);
    }

    public void setBlockFast(World world, int x, int y, int z, int blockId, byte data) {
        net.minecraft.server.v1_10_R1.World w = ((CraftWorld) world).getHandle();
        Chunk chunk = w.getChunkAt(x >> 4, z >> 4);
        a(chunk, new BlockPosition(x, y, z), net.minecraft.server.v1_10_R1.Block.getById(blockId).fromLegacyData(data));

    }

    private IBlockData a(Chunk that, BlockPosition blockposition, IBlockData iblockdata) {
        return that.a(blockposition, iblockdata);
    }
}

